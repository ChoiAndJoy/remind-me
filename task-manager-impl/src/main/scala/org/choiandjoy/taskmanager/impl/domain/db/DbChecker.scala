// Copyright (c) 2020 Clutch Technologies, LLC. All rights reserved.
package org.choiandjoy.taskmanager.impl.domain.db

import scala.concurrent.{ExecutionContext, Future}

class DbChecker extends DBAccess {
  import Profile.api._

  private def pingSql(implicit ec: ExecutionContext): DBIO[String] =
    sql"SELECT 'driver_portal_service'".as[String].map(_.head)

  def check()(implicit ec: ExecutionContext): Future[String] = {
    //TODO ping another table/view?
    // def checkTable[T <: AbstractTable[_]](table: TableQuery[T]): DBIO[Seq[T#TableElementType]] = table.take(1).result
    val action = pingSql
    db.run(action.withPinnedSession)
  }

}
