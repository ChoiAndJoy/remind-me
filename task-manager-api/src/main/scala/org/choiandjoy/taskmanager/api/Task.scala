package org.choiandjoy.taskmanager.api

import java.time.Instant
import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json._

case class Task(taskId: UUID,
                userId: UUID,
                name: String,
                completed: Boolean,
                currentIteration: Int,
                iterations: List[Int],
                dueDate: DateTime,
                nextDueDate: DateTime)
//{
//  def this(userId: UUID, name: String, iterations: Seq[Int]) =
//    this(
//      UUID.randomUUID(),
//      userId,
//      name,
//      false,
//      0,
//      iterations,
//      DateTime.now(),
//      DateTime.now().plusDays(iterations.headOption.getOrElse(0))
//    )
//}

object Task extends DateTimeJsFormat {
  implicit val taskFmt: OFormat[Task] = Json.format[Task]

  def createTask(createTask: CreateTask): Task =
    Task(
      UUID.randomUUID(),
      createTask.userId,
      createTask.name,
      false,
      0,
      createTask.iterations,
      DateTime.now(),
      DateTime.now().plusDays(createTask.iterations.headOption.getOrElse(0))
    )

  def updateTask(taskId: UUID, updateTask: UpdateTask): Task =
    Task(
      taskId,
      updateTask.userId,
      updateTask.name,
      false,
      0,
      updateTask.iterations,
      DateTime.now(),
      DateTime.now().plusDays(updateTask.iterations.headOption.getOrElse(0))
    )
}

case class CreateTask(userId: UUID, name: String, iterations: List[Int])

object CreateTask {
  implicit val createTaskJson: OFormat[CreateTask] = Json.format[CreateTask]
}

case class UpdateTask(userId: UUID,
                      name: String,
                      complete: Boolean,
                      iterations: List[Int],
                      dueDate: DateTime)

object UpdateTask extends DateTimeJsFormat {
  implicit val updateTaskJs: OFormat[UpdateTask] = Json.format[UpdateTask]
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
