logLevel := Level.Warn

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

dependencyOverrides += "org.scala-sbt" % "sbt" % "0.13.7"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.7")