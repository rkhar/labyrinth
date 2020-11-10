name := "labyrinth"

version := "0.0.1"

scalaVersion := "2.13.1"

lazy val akkaVersion  = "2.5.31"
lazy val circeVersion = "0.13.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  // circe
  "io.circe"          %% "circe-parser"    % circeVersion,
  "io.circe"          %% "circe-generic"   % circeVersion,
  "de.heikoseeberger" %% "akka-http-circe" % "1.29.1",
  // logback
  "ch.qos.logback" % "logback-classic" % "1.1.3" % Runtime
)

enablePlugins(AkkaGrpcPlugin)
