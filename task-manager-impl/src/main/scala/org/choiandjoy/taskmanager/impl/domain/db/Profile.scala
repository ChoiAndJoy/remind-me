// Copyright (c) 2020 Clutch Technologies, LLC. All rights reserved.
package org.choiandjoy.taskmanager.impl.domain.db

import java.util.UUID

import com.github.tminglei.slickpg._

trait Profile
    extends ExPostgresProfile
    with PgArraySupport
    with PgPlayJsonSupport
    with PgDateSupportJoda
    with PgEnumSupport {

  override val api = MyApi

  def pgjson = "jsonb"

  object MyApi
      extends API
      with JodaDateTimeImplicits
      with DateTimeImplicits
      with JsonImplicits
      with ArrayImplicits {

    def enumMapper[X <: Enumeration](sqlName: String, enumr: X) = {
      createEnumJdbcType(sqlName, enumr)
    }

    implicit val uuidListTypeMapper =
      new SimpleArrayJdbcType[UUID]("uuid").to(_.toList)

    implicit val intListTypeMapper =
      new SimpleArrayJdbcType[Int]("int").to(_.toList)

  }
}

object Profile extends Profile
