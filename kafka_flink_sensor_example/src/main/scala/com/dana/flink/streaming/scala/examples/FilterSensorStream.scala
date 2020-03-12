package com.dana.flink.streaming.scala.examples

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import play.api.libs.json._
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer}

/**
  * Simple filtering program, that filters sensor messages with failed status.
  *
  * The input is sensor data from Kafka.
  *
  * This example shows how to:
  *  - connect to a Kafka topic.
  *  - write and use transformation functions.
  *  - publish the transformed data to another topic.
  */

object FilterSensorStream {
  implicit val jsonMessageReads: Reads[JsonMessage] = Json.reads[JsonMessage]
  implicit val jsonMessageWrite: Writes[JsonMessage] = Json.writes[JsonMessage]

  def main(args: Array[String]) {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val kafkaProperties = new Properties()
    kafkaProperties.setProperty("bootstrap.servers", "localhost:9092")
    kafkaProperties.setProperty("auto.offset.reset", "earliest")

    val kafkaConsumer = new FlinkKafkaConsumer(
      "sample_sensor",
      new SimpleStringSchema,
      kafkaProperties
    )

    val kafkaStream = env.addSource(kafkaConsumer)

    kafkaStream.print()

    val jsonStream = kafkaStream
      .map(entry => Json.fromJson[JsonMessage](Json.parse(entry))) //JSON to Scala object
      .filter(_.isInstanceOf[JsSuccess[JsonMessage]]) // filter successfully parsed messages
      .map(_.get) //get the result of the successful parsing

    val filteredJsonStream = jsonStream.filter(_.status == "ok")
    filteredJsonStream.print().setParallelism(1)

    val kafkaProducer = new FlinkKafkaProducer[String](
      "filtered_sample",
      new SimpleStringSchema,
      kafkaProperties
    )
    // toJson Scala object to JSON
    filteredJsonStream.map(elem => Json.toJson(elem)).map(Json.stringify(_)).addSink(kafkaProducer)

    env.execute("KafkaExample")
  }
}
