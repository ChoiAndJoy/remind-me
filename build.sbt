organization in ThisBuild := "org.choiandjoy"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.13.0"

lazy val slickPgVersion = "0.18.0"
lazy val slickVersion = "3.3.2"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.3" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.1.1" % Test
val jsonJoda = "com.typesafe.play" %% "play-json-joda" % "2.7.4"
val h2 = "com.h2database" % "h2" % "1.4.200"
val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"

// Slick3 for DB access
val postgres = "org.postgresql" % "postgresql" % "42.2.5"
val slick = "com.typesafe.slick" %% "slick" % "3.3.2"
val slickHikaricp = "com.typesafe.slick" %% "slick-hikaricp" % slickVersion
// slick-pg joda time mapping
val slickpg = "com.github.tminglei" %% "slick-pg" % slickPgVersion
val slickpgjoda = "com.github.tminglei" %% "slick-pg_joda-time" % slickPgVersion
val slickpgplay = "com.github.tminglei" %% "slick-pg_play-json" % slickPgVersion

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
      scalaTest,

    )
  )
  .settings(lagomForkedTestSettings)
  .dependsOn(`remind-me-api`)

lazy val `task-manager` = (project in file("."))
  .aggregate(`task-manager-api`, `task-manager-impl`)

lazy val `task-manager-api` = (project in file("task-manager-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi,
      slick,
        h2,
        logback,
        jsonJoda,
        postgres,
        slick,
        slickHikaricp,
        slickpg,
        slickpgjoda,
        slickpgplay
    )
  )

lazy val `task-manager-impl` = (project in file("task-manager-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest,
      slick,
      h2,
      logback,
      jsonJoda,
      postgres,
      slick,
      slickHikaricp,
      slickpg,
      slickpgjoda,
      slickpgplay
    )
  )
  .settings(lagomForkedTestSettings)
  .dependsOn(`task-manager-api`)
