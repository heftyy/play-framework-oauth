name := """oauth-console"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaWs,
  javaCore,
  "org.webjars" % "jquery" % "2.1.4",
  "org.webjars" % "bootstrap" % "3.2.0",
  "org.webjars" % "momentjs" % "2.8.3",
  "org.webjars" % "jquery-cookie" % "1.4.1",
  "org.webjars" % "requirejs-text" % "2.0.10-3",
  "org.apache.commons" % "commons-lang3" % "3.4",
  "org.bouncycastle" % "bcprov-jdk15on" % "1.53",
  "org.bouncycastle" % "bcpkix-jdk15on" % "1.53"

)

routesGenerator := InjectedRoutesGenerator
