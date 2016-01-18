//scalaVersion := "2.11.7" // Plugins are not yet ready for Scala 2.11

addSbtPlugin("com.hanhuy.sbt" % "android-sdk-plugin" % "1.5.13")

addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.5.1")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.7.0")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.3.3")

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
