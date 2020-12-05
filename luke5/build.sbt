
name := "Luke 5"

version := "1.0"

val scalaV = "2.13.4"
scalaVersion := scalaV

resolvers += Resolver.sonatypeRepo("public")
resolvers += Resolver.sonatypeRepo("snapshots")

lazy val gui = (project in file(".")).settings(
  scalaVersion := scalaV,
  scalaJSUseMainModuleInitializer in Compile := true,
  scalaJSUseMainModuleInitializer in Test := false,

  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "1.1.0",
  )
).enablePlugins(ScalaJSPlugin)
