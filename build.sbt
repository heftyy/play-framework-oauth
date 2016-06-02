name := """play-oauth"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  cache,
  javaJdbc,
  javaWs,
  "org.mockito" % "mockito-core" % "1.9.5" % "test"
)

pipelineStages := Seq(digest, gzip)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

lazy val commons = (project in file("modules/commons")).enablePlugins(PlayJava)

lazy val oauthBase = (project in file("modules/oauth-base")).enablePlugins(PlayJava).dependsOn(commons).aggregate(commons)

lazy val oauthClient = (project in file("modules/oauth-client")).enablePlugins(PlayJava).dependsOn(oauthBase).aggregate(oauthBase)
lazy val oauthConsole = (project in file("modules/oauth-console")).enablePlugins(PlayJava).dependsOn(oauthBase).aggregate(oauthBase)
lazy val oauthServer = (project in file("modules/oauth-server")).enablePlugins(PlayJava).dependsOn(oauthBase).aggregate(oauthBase)
lazy val oauthWs = (project in file("modules/oauth-ws")).enablePlugins(PlayJava).dependsOn(oauthBase).aggregate(oauthBase)

lazy val root = (project in file(".")).enablePlugins(PlayJava)
  .dependsOn(oauthConsole).aggregate(oauthConsole)
  .dependsOn(oauthServer).aggregate(oauthServer)
  .dependsOn(oauthWs).aggregate(oauthWs)
  .dependsOn(oauthClient).aggregate(oauthClient)
