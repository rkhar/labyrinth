name := "labyrinth"

version := "0.0.1"

scalaVersion := "2.13.0"

lazy val akkaVersion  = "2.5.31"
lazy val circeVersion = "0.13.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  // circe
  "io.circe"          %% "circe-parser"    % circeVersion,
  "io.circe"          %% "circe-generic"   % circeVersion,
  "de.heikoseeberger" %% "akka-http-circe" % "1.29.1",
  // logback
  "ch.qos.logback" % "logback-classic" % "1.1.3" % Runtime,
  // slick
  "com.typesafe.slick" %% "slick"          % "3.3.3",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
  // postgres
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc42",
  //test
  "org.scalatest" %% "scalatest" % "3.1.0" % Test
)

enablePlugins(AkkaGrpcPlugin)
