package org.choiandjoy.taskmanager.impl.domain

import java.util.UUID

import org.choiandjoy.taskmanager.api.Task

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

  def getTasks =
    tasks.result

  def upsertTask(task: Task) = {
    tasks.insertOrUpdate(task)
  }

  def deleteTask(id: UUID) = {
    tasks
      .filter(_.id === id)
      .take(1)
      .delete
  }

}
