name := """oauth-server"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaWs,
  javaCore
)

routesGenerator := InjectedRoutesGenerator
