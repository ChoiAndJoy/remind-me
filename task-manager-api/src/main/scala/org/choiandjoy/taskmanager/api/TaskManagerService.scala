package org.choiandjoy.taskmanager.api

import java.util.UUID

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.broker.kafka.{
  KafkaProperties,
  PartitionKeyStrategy
}
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

object TaskManagerService {
  val TOPIC_NAME = "tasks"
}

/**
  * The remind-me service interface.
  * <p>
  * This describes everything that Lagom needs to know about how to serve and
  * consume the RemindmeService.
  */
trait TaskManagerService extends Service {

  def getTask(id: UUID): ServiceCall[NotUsed, Task]
  def getTasks: ServiceCall[NotUsed, Seq[Task]]
  def createTask: ServiceCall[CreateTask, Done]
  def updateTask(id: UUID): ServiceCall[UpdateTask, Done]
  def deleteTask(id: UUID): ServiceCall[NotUsed, Done]

  //added wrt message api
  def tasksTopic: Topic[KTaskMessage]

  override final def descriptor: Descriptor = {
    import Service._
    // @formatter:off
    named("task-manager-service")
      .withCalls(
        pathCall("/tasks/:id", getTask _),
        pathCall("/tasks", getTasks _),
        namedCall("/tasks", createTask _),
        pathCall("/tasks/:id", updateTask _),
        pathCall("/tasks/:id", deleteTask _)
      )
      .withTopics(
        topic(TaskManagerService.TOPIC_NAME, tasksTopic)
          // Kafka partitions messages, messages within the same partition will
          // be delivered in order, to ensure that all messages for the same user
          // go to the same partition (and hence are delivered in order with respect
          // to that user), we configure a partition key strategy that extracts the
          // name as the partition key.
          .addProperty(
            KafkaProperties.partitionKeyStrategy,
            PartitionKeyStrategy[KTaskMessage](_ => "0")
          )
      )
      .withAutoAcl(true)
    // @formatter:on
  }
}
