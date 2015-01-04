name := "HelloWorld"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  "org.jfree" % "jfreechart" % "1.0.14",
  "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test",
  "org.scala-lang" % "scala-actors" % "2.10.0-M6",//this is for futures etc.
  cache
)

publishArtifact in (Compile, packageDoc) := false

publishArtifact in packageDoc := false

sources in (Compile,doc) := Seq.empty