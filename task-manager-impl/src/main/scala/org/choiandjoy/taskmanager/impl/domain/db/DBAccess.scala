// Copyright (c) 2020 Clutch Technologies, LLC. All rights reserved.
package org.choiandjoy.taskmanager.impl.domain.db

object DBAccess {
  import Profile.api._
  lazy val db = Database.forConfig("db.postgres")
}

trait DBAccess {
  lazy val db = DBAccess.db
}
