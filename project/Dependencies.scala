import sbt._

trait Dependencies {

  val scalaVersionUsed = "2.11.7"

  // resolvers
  val commonResolvers = Seq(
    Resolver sonatypeRepo "public",
    Resolver typesafeRepo "releases",
    "https://jcenter.bintray.com/" at "https://jcenter.bintray.com/",
    "https://jitpack.io" at "https://jitpack.io"
  )

  // android
  val sdkPrefix = "com.android.support"
  val sdkVersion = "23.1.1"
  val annotations = sdkPrefix % "support-annotations" % sdkVersion
  val appcompat = sdkPrefix % "appcompat-v7" % sdkVersion
  val design = sdkPrefix % "design" % sdkVersion
  val support = sdkPrefix % "support-v4" % sdkVersion
  val recyclerview = sdkPrefix % "recyclerview-v7" % sdkVersion
  val multidex = sdkPrefix % "multidex" % "1.0.1"

  // scala
  val scalaLib = "org.scala-lang" % "scala-library" % scalaVersionUsed
  val scalazCore = "org.scalaz" %% "scalaz-core" % "7.2.1"
  val scalazConcurrent = "org.scalaz" %% "scalaz-concurrent" % "7.2.1"

  // permission requests in services
  val permissionEverywhere = "com.github.kaknazaveshtakipishi" % "PermissionEverywhere" % "1.0.2"

  // serialization
  val playJson = "com.typesafe.play" %% "play-json" % "2.4-2014-08-19-5fd9847" excludeAll(
    ExclusionRule(organization = "org.joda"),
    ExclusionRule(organization = "joda-time"),
    ExclusionRule(organization = "org.spec2")
  )

  // sql
  val sqlDroid = "org.sqldroid" % "sqldroid" % "1.0.3"
  val slick = "com.typesafe.slick" %% "slick" % "2.1.0" excludeAll ExclusionRule(organization = "org.slf4j")

  // projection in slick
  val shapeless = "com.chuusai" %% "shapeless" % "2.2.5"

  // datetime in slick
  val joda = "net.danlew" % "android.joda" % "2.9.1"
  val jodaConvert = "org.joda" % "joda-convert" % "1.8.1"

  // facebook
  val facebookSdk = "com.facebook.android" % "facebook-android-sdk" % "4.8.2"

  // testing
  val mockito    = "org.mockito" % "mockito-core" % "1.10.8"
  val scalatest  = "org.scalatest" %% "scalatest" % "2.2.5"
  val robotest   = "com.geteit" %% "robotest" % "0.12"

  val androidDeps = Seq(annotations, appcompat, design, support, recyclerview, multidex)

  val mainDeps = scalaLib :: List(
    scalazCore,
    scalazConcurrent,
    permissionEverywhere,
    playJson,
    sqlDroid,
    slick,
    shapeless,
    joda,
    jodaConvert,
    facebookSdk
  ) map {
    _.excludeAll(
      ExclusionRule(organization = sdkPrefix)
    )
  }

  val testDeps = Seq(mockito, scalatest, robotest)
}

object Dependencies extends Dependencies {

  implicit class ProjectRoot(project: Project) {

    def root: Project = project in file(".")
  }

  implicit class ProjectFrom(project: Project) {

    private val commonDir = "modules"

    def from(dir: String): Project = project in file(s"$commonDir/$dir")
  }
}
