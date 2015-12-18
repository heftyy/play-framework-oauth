name := """oauth-client"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaWs,
  javaCore,
  "commons-net" % "commons-net" % "3.1"
)
