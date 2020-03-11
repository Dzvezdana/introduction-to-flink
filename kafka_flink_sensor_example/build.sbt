ThisBuild / scalaVersion := "2.12.7"
ThisBuild / organization := "com.dana"

val flinkVersion = "1.7.2"
lazy val hello = (project in file("."))
  .settings(
      name := "kafka_flink_sensor_example", 
      libraryDependencies += "org.apache.flink" %% "flink-scala" % flinkVersion,
      libraryDependencies += "com.typesafe.play" %% "play-json" % "2.8.1",
      libraryDependencies += "org.apache.flink" %% "flink-streaming-scala" % flinkVersion,
      libraryDependencies += "org.apache.flink" %% "flink-connector-kafka" % flinkVersion, 
      test in assembly := {}, 
      mainClass in assembly := Some("com.dana.flink.streaming.scala.examples.FilterSensorStream"),
      assemblyJarName in assembly := "kafka_flink_sensor_example.jar"
  )