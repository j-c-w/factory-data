import play.Project._

name := "HelloWorld"

version := "1.0"

playScalaSettings

libraryDependencies += "org.jfree" % "jfreechart" % "1.0.14"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"

libraryDependencies += "org.scala-lang" % "scala-actors" % "2.10.0-M6"//this is for futures etc.

libraryDependencies += cache


publishArtifact in (Compile, packageDoc) := false

publishArtifact in packageDoc := false

sources in (Compile,doc) := Seq.empty