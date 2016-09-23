import android.Keys._
import android.Plugin._
import android.protify.Keys._
import com.typesafe.sbt.SbtScalariform._
import sbt.Defaults.testTasks
import sbt.TestFrameworks.Specs2
import sbt.Tests.Argument
import sbt._
import sbt.Keys._

import scalariform.formatter.preferences._
import scoverage.ScoverageSbtPlugin
import org.scalastyle.sbt.ScalastylePlugin._

trait Settings {

  import Dependencies._

  private val functionalTestTag = TestTag.FunctionalTest
  val FunctionalTest = config(functionalTestTag) extend Test describedAs "Runs only functional tests"

  private val unitTestTag = TestTag.UnitTest
  val UnitTest = config(unitTestTag) extend Test describedAs "Runs only unit tests"

  private val disabledTestTag = TestTag.DisabledTest

  private val modulesSettings = scalariformSettings ++ Seq(
    // package
    organization           := "com.talkie",
    version                := "0.1.0-SNAPSHOT",
    versionCode in Android := Some(1),
    versionName in Android := Some("0.1.0-SNAPSHOT"),

    // resolvers
    resolvers ++= commonResolvers,
    offline := true,

    // Java
    javacOptions in Compile ++= Seq(
      "-source", "1.7",
      "-target", "1.7"
    ),

    // Scala
    scalaVersion := scalaVersionUsed,
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-feature",
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-language:postfixOps",
      "-Ywarn-dead-code",
      "-Ywarn-infer-any",
      "-Ywarn-unused-import",
      "-Xfatal-warnings",
      "-Xlint"
    ),

    // Android
    packagingOptions   in Android := PackagingOptions(
      excludes = Seq(
        "META-INF/maven",
        "META-INF/LICENSE",
        "META-INF/LICENSE.txt",
        "META-INF/NOTICE",
        "META-INF/NOTICE.txt",
        "scoverage",
        "scalac-plugin.xml"
      ),
      pickFirsts = Seq(
        "AndroidManifest.xml",
        "R.txt"
      ),
      merges = Seq(
      )
    ),
    platformTarget     in Android := "android-23",
    minSdkVersion      in Android := "23",
    targetSdkVersion   in Android := "23",
    debugIncludesTests in Android := false,
    dexMulti           in Android := true,
    typedResources     in Android := false,

    // Proguard
    proguardOptions in Android ++= Seq(
      // classes that should stay
      "-keep class com.facebook.**",
      "-keep class com.talkie.client.**",
      "-keepattributes *Annotation*",
      "-keep public class * extends android.support.design.widget.CoordinatorLayout.Behavior { *; }",
      "-keep public class * extends android.support.design.widget.ViewOffsetBehavior { *; }"
    ),

    // Libraries
    libraryDependencies ++= (androidDeps ++ mainDeps),
    libraryDependencies ++= testDeps map (_ % "test"),
    publishMavenStyle := false,

    // tests
    fork in Test := true,
    parallelExecution in Test := false,
    testOptions in Test += excludeTags(disabledTestTag),

    // Scala warnings
    ScalariformKeys.preferences := ScalariformKeys.preferences.value
      .setPreference(AlignArguments, true)
      .setPreference(AlignParameters, true)
      .setPreference(AlignSingleLineCaseStatements, true)
      .setPreference(DoubleIndentClassDeclaration, true)
      .setPreference(IndentLocalDefs, false)
      .setPreference(PreserveSpaceBeforeArguments, true),
    scalastyleFailOnError := true
  )

  private def excludeTags(tags: String*) = Argument(Specs2, "exclude", tags.reduce(_ + "," + _))
  private def includeTags(tags: String*) = Argument(Specs2, "include", tags.reduce(_ + "," + _))
  private def sequential = Argument(Specs2, "sequential")

  abstract class Configurator(project: Project, config: Configuration, tag: String) {

    protected def configure = project.
      configs(config).
      settings(inConfig(config)(testTasks): _*).
      settings(testOptions in config := Seq(includeTags(tag))).
      settings(libraryDependencies ++= testDeps map (_ % tag)).
      enablePlugins(ScoverageSbtPlugin)

    protected def configureSequential = configure.
      settings(testOptions in config ++= Seq(sequential)).
      settings(parallelExecution in config := false)
  }
}

object Settings extends Settings {

  implicit class DataConfigurator(project: Project) {

    def setName(newName: String): Project = project.settings(name := newName)

    def setDescription(newDescription: String): Project = project.settings(description := newDescription)
  }

  implicit class ModuleConfigurator(project: Project) {

    def configureModule(asLibrary: Boolean = false, withViews: Boolean = false)
                       (deps : sbt.ProjectReference*): Project =
      (if (deps.isEmpty) project.settings(androidBuild) else project.androidBuildWith(deps:_*))
        .settings(libraryProject := asLibrary)
        .settings(modulesSettings: _*)
        .settings(typedResources := withViews)
        .settings((if (!asLibrary && withViews) protifySettings else Seq.empty): _*)
  }

  implicit class FunctionalTestConfigurator(project: Project)
    extends Configurator(project, FunctionalTest, functionalTestTag) {

    def configureFunctionalTests: Project = configure
  }

  implicit class UnitTestConfigurator(project: Project)
    extends Configurator(project, UnitTest, unitTestTag) {

    def configureUnitTests = configure
  }
}
