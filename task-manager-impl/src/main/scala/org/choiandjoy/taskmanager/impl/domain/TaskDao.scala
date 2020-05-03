package org.choiandjoy.taskmanager.impl.domain

import java.util.UUID

import scala.concurrent.ExecutionContext

class TaskDao {
  import TaskTables._
  import db.Profile.api._

  def getTaskById(taskId: UUID) = {
    tasks
      .filter(_.id === taskId)
      .take(1)
      .result
      .headOption
  }

  def upsertTask(task: TaskRow) = {
    tasks.insertOrUpdate(task)
  }

}
