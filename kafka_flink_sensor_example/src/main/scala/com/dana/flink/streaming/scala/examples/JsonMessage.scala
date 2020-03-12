package com.dana.flink.streaming.scala.examples

case class JsonMessage(
                        timestamp: String,
                        temperature: Double,
                        status: String,
                        id: Int
                      )
