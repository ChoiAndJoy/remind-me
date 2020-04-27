package org.choiandjoy.taskmanager.api

import java.time.Instant
import java.util.UUID

import org.joda.time.LocalTime
import play.api.libs.json.JodaWrites._
import play.api.libs.json._

import scala.collection.immutable.Seq

case class Task(TaskId: UUID,
                userId: UUID,
                name: String,
                complete: Boolean,
                iterations: Seq[Int],
                dueDate: LocalTime) {
  def this(userId: UUID, name: String) =
    this(UUID.randomUUID(), userId, name, false, Seq.empty, LocalTime.now())
}

object Task {
  implicit val localTimeDefault = new Format[LocalTime] {
    override def reads(json: JsValue) =
      JodaReads.DefaultJodaLocalTimeReads.reads(json)
    override def writes(o: LocalTime): JsValue =
      DefaultJodaLocalTimeWrites.writes(o)
  }
  implicit val taskFmt: OFormat[Task] = Json.format[Task]

  def apply(createTask: CreateTask): Task =
    Task(
      UUID.randomUUID(),
      createTask.userId,
      createTask.name,
      complete = false,
      Seq.empty,
      LocalTime.now()
    )
}

case class CreateTask(userId: UUID, name: String)

object CreateTask {
  implicit val createTaskJson: OFormat[CreateTask] = Json.format[CreateTask]
}

case class UpdateTask(name: String,
                      complete: Boolean,
                      iterations: Seq[Int],
                      dueDate: LocalTime)

object UpdateTask {
  implicit val localTimeDefault = new Format[LocalTime] {
    override def reads(json: JsValue) =
      JodaReads.DefaultJodaLocalTimeReads.reads(json)
    override def writes(o: LocalTime): JsValue =
      DefaultJodaLocalTimeWrites.writes(o)
  }
  implicit val updateTaskJson: OFormat[UpdateTask] = Json.format[UpdateTask]
}

sealed trait KTaskMessage

case class KTaskCreated(TaskId: String,
                        name: String,
                        timestamp: Instant = Instant.now())
    extends KTaskMessage

case class KTaskAdded(TaskId: String,
                      friendId: String,
                      timestamp: Instant = Instant.now())
    extends KTaskMessage

object KTaskMessage {
  implicit val writes = new Writes[KTaskMessage] {
    override def writes(o: KTaskMessage): JsObject = o match {
      case x: KTaskCreated => KTaskCreated.format.writes(x)
      case x: KTaskAdded   => KTaskAdded.format.writes(x)
    }
  }

  implicit val reads = new Reads[KTaskMessage] {
    override def reads(json: JsValue): JsResult[KTaskMessage] =
      if (json.validate(KTaskCreated.format).isSuccess)
        json.validate(KTaskCreated.format)
      else if (json.validate(KTaskAdded.format).isSuccess)
        json.validate(KTaskAdded.format)
      else throw new RuntimeException("no valid format found")
  }
}

object KTaskCreated {
  implicit val format: OFormat[KTaskCreated] = Json.format[KTaskCreated]
}

object KTaskAdded {
  implicit val format: OFormat[KTaskAdded] = Json.format[KTaskAdded]
}
