organization in ThisBuild := "org.choiandjoy"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.13.0"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.3" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.1.1" % Test

lagomCassandraEnabled in ThisBuild := false
lagomKafkaEnabled in ThisBuild := false

lazy val `remind-me` = (project in file("."))
  .aggregate(`remind-me-api`, `remind-me-impl`)

lazy val `remind-me-api` = (project in file("remind-me-api"))
  .settings(libraryDependencies ++= Seq(lagomScaladslApi))

lazy val `remind-me-impl` = (project in file("remind-me-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings)
  .dependsOn(`remind-me-api`)

lazy val `task-manager` = (project in file("."))
  .aggregate(`task-manager-api`, `task-manager-impl`)

lazy val `task-manager-api` = (project in file("task-manager-api"))
  .settings(libraryDependencies ++= Seq(lagomScaladslApi))

lazy val `task-manager-impl` = (project in file("task-manager-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings)
  .dependsOn(`task-manager-api`)
