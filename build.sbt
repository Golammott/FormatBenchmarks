import pl.project13.scala.sbt.JmhPlugin

//name := "FormatTests"

version := "0.1"

scalaVersion := "2.12.8"

lazy val benchmarks = (project in file("."))
  .settings(
    name := "ParquetBenchmarks",
    libraryDependencies ++= Seq("org.apache.spark" %% "spark-core" % "2.4.3",
      "org.apache.spark" %% "spark-sql" % "2.4.3",
    )
  ).enablePlugins(JmhPlugin)