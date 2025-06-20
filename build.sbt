val scala3Version = "3.6.4"

enablePlugins(CoverallsPlugin)
coverageEnabled := true

lazy val root = project
  .in(file("."))
  .settings(
    name := "se_dominion",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0" % Test,
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.19",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % "test",
    libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0",
    libraryDependencies += "net.codingwell" %% "scala-guice" % "7.0.0"
    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "2.4.0"
    /*libraryDependencies += "org.scalafx" %% "scalafx" % "24.0.0-R35",
    libraryDependencies ++= {
      lazy val osName = System.getProperty("os.name") match {
        case os if os.toLowerCase.contains("mac") => "mac"
        case os if os.toLowerCase.contains("win") => "win"
        case os if os.toLowerCase.contains("linux") => "linux"
        case _ => throw new RuntimeException("Unsupported OS")
      }
      Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
        .map(lib => "org.openjfx" % s"javafx-$lib" % "21" classifier osName)
    }*/
  )
