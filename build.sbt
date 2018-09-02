
//lazy val commonSettings = Seq(
  organization := "com.nossin"
  scalaVersion := "2.11.8"
  libraryDependencies ++= Seq(
    "org.scalameta" %% "scalameta" % "1.3.0",
    "org.scalatest" %% "scalatest" % "3.0.5" % "test"

  )
  addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M5" cross CrossVersion.full)
  licenses +=("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))
  homepage := Some(url("http://www.nossin.com"))
  //test in assembly := {}
//)

//lazy val silverback = project.in(file("."))
//  .settings(commonSettings: _*)

