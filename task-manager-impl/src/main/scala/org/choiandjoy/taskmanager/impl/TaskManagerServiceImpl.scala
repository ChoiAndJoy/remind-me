package org.choiandjoy.taskmanager.impl

import java.util.UUID

import akka.cluster.sharding.typed.scaladsl.ClusterSharding
import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import org.choiandjoy.taskmanager.api.{
  CreateTask,
  KTaskMessage,
  Task,
  TaskManagerService,
  UpdateTask
}

import scala.concurrent.ExecutionContext

/**
  * Implementation of the TaskManagerService.
  */
class TaskManagerServiceImpl(
  clusterSharding: ClusterSharding,
  persistentEntityRegistry: PersistentEntityRegistry
)(implicit ec: ExecutionContext)
    extends TaskManagerService {

  override def getTask(id: UUID): ServiceCall[NotUsed, Task] = {
    request: NotUsed =>
      db.run(taskDato.getTaskById(id))
  }

  override def getTasks: ServiceCall[NotUsed, Seq[Task]] =
    db.run(taskDato.getTasks)

  override def createTask: ServiceCall[CreateTask, Done] = {
    request: CreateTask =>
      val taskId = UUID.randomUUID()
      db.run(taskDato.upsert(CreateTask))
  }

  override def deleteTask(id: UUID): ServiceCall[NotUsed, Done] = {
    request: NotUsed =>
      db.run(taskDato.delete(id))
  }

  override def updateTask(id: UUID): ServiceCall[UpdateTask, Done] = {
    request: UpdateTask =>
      val taskId = UUID.randomUUID()
      db.run(taskDato.upsert(CreateTask))
  }

  override def tasksTopic: Topic[KTaskMessage] = ???
}
