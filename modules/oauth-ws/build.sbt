name := """oauth-ws"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaWs,
  javaCore,
  "be.objectify" %% "deadbolt-java" % "2.4.3",
  "commons-collections" % "commons-collections" % "3.2.1",
  "commons-configuration" % "commons-configuration" % "1.10"
)

routesGenerator := InjectedRoutesGenerator
