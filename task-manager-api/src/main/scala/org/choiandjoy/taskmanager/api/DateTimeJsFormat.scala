package org.choiandjoy.taskmanager.api

import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTime, LocalDate}
import play.api.libs.json._

trait DateTimeJsFormat {
  private val dateTimeFormatPattern =
    DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")

  implicit val dateTimeFormat = new Format[DateTime] {
    override def reads(json: JsValue): JsResult[DateTime] =
      json.validate[String] match {
        case JsSuccess(value, _) =>
          try {
            JsSuccess(dateTimeFormatPattern.parseDateTime(value))
          } catch {
            case ex: IllegalArgumentException => JsError(ex.getMessage)
          }
        case JsError(_) => JsError("expected.string")
      }

    override def writes(o: DateTime): JsValue =
      JsString(dateTimeFormatPattern.print(o))
  }

  private val localDateFormatPattern = DateTimeFormat.forPattern("yyyy-MM-dd")
  implicit val localDateFormat = new Format[LocalDate] {
    override def reads(json: JsValue): JsResult[LocalDate] =
      json.validate[String] match {
        case JsSuccess(value, _) =>
          try {
            JsSuccess(localDateFormatPattern.parseLocalDate(value))
          } catch {
            case ex: IllegalArgumentException => JsError(ex.getMessage)
          }
        case JsError(_) => JsError("expected.string")
      }

    override def writes(o: LocalDate): JsValue =
      JsString(localDateFormatPattern.print(o))
  }

}
