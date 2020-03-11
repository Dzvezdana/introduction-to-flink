ThisBuild / scalaVersion := "2.12.7"
ThisBuild / organization := "com.dana"

val flinkVersion = "1.7.2"
lazy val hello = (project in file("."))
  .settings(
    name := "kafka_flink_wordcount_example",
    libraryDependencies += "org.apache.flink" %% "flink-scala" % flinkVersion,
    libraryDependencies += "org.apache.flink" %% "flink-streaming-scala" % flinkVersion,
    libraryDependencies += "org.apache.flink" %% "flink-connector-kafka" % flinkVersion,
    test in assembly := {},
    mainClass in assembly := Some("com.dana.flink.streaming.scala.word.StreamWordCount"),
    assemblyJarName in assembly := "kafka_flink_wordcount_example.jar"
  )
