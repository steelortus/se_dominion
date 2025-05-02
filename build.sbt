val scala3Version = "3.6.4"

enablePlugins(CoverallsPlugin)

lazy val root = project
  .in(file("."))
  .settings(
    name := "se_dominion",
    version := "0.1.0-SNAPSHOT",

    coverageEnabled := true,

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0" % Test,
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.19",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % "test"
  )
