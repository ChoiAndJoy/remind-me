package org.choiandjoy.taskmanager.impl

import java.util.UUID

import akka.cluster.sharding.typed.scaladsl.ClusterSharding
import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import org.choiandjoy.taskmanager.api.{
  CreateTask,
  KTaskMessage,
  Task,
  TaskManagerService,
  UpdateTask
}
import org.choiandjoy.taskmanager.impl.domain.TaskDao
import org.choiandjoy.taskmanager.impl.domain.db.DBAccess._

import scala.concurrent.ExecutionContext

/**
  * Implementation of the TaskManagerService.
  */
class TaskManagerServiceImpl(clusterSharding: ClusterSharding,
                             persistentEntityRegistry: PersistentEntityRegistry,
                             taskDao: TaskDao)(implicit ec: ExecutionContext)
    extends TaskManagerService {

  override def getTask(id: UUID): ServiceCall[NotUsed, Task] = { _: NotUsed =>
    db.run(taskDao.getTaskById(id)).map {
      case Some(task) => task
      case None       => throw NotFound("Task not found")
    }
  }

  override def getTasks: ServiceCall[NotUsed, Seq[Task]] = { _: NotUsed =>
    db.run(taskDao.getTasks)
  }

  override def createTask: ServiceCall[CreateTask, Done] = {
    request: CreateTask =>
      db.run(taskDao.upsertTask(Task.apply(request))).map(_ => Done)
  }

  override def deleteTask(id: UUID): ServiceCall[NotUsed, Done] = {
    _: NotUsed =>
      db.run(taskDao.deleteTask(id)).map(_ => Done)
  }

  override def updateTask(id: UUID): ServiceCall[UpdateTask, Done] = {
    request: UpdateTask =>
      db.run(taskDao.upsertTask(Task.apply(id, request))).map(_ => Done)
  }

  override def tasksTopic: Topic[KTaskMessage] = ???
}
