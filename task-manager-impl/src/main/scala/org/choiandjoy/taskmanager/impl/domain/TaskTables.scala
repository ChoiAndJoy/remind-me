package org.choiandjoy.taskmanager.impl.domain

import java.util.UUID

import org.choiandjoy.taskmanager.api.Task
import org.joda.time.DateTime

object TaskTables {
  import db.Profile.api._

//  case class TaskRow(taskId: UUID,
//                     userId: UUID,
//                     name: String,
//                     completed: Boolean,
//                     currentIteration: Int,
//                     iterations: Int,
//                     dueDate: DateTime,
//                     nextDueDate: DateTime)

  class TaskTable(tag: Tag) extends Table[Task](tag, "task") {

    def id = column[UUID]("id", O.PrimaryKey)

    def userId = column[UUID]("user_id")

    def name = column[String]("name")

    def completed = column[Boolean]("completed")

    def currentIndexIterations = column[Int]("current_index_iterations")

    def iterations = column[String]("iterations")

    def dueDate = column[DateTime]("due_date")

    def nextDueDate = column[DateTime]("next_due_date")

    def * =
      (
        id,
        userId,
        name,
        completed,
        currentIndexIterations,
        iterations,
        dueDate,
        nextDueDate
      ).mapTo[Task]
  }

  lazy val tasks = TableQuery[TaskTable]
}
