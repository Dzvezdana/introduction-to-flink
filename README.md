# Introduction to Flink

## Dependencies

* sbt 1.3.8
* Scala 2.12.7
* jdk 1.8

### Set up Kafka
#### Step 1: Download the code
[Download](https://www.apache.org/dyn/closer.cgi?path=/kafka/2.4.0/kafka_2.12-2.4.0.tgz) the 2.4.0 release and un-tar it.

```bash
> tar -xzf kafka_2.12-2.4.0.tgz
> cd kafka_2.12-2.4.0
```

#### Step 2: Start the server
Kafka uses ZooKeeper so you need to first start a ZooKeeper server if you don't already have one. You can use the convenience script packaged with kafka to get a quick-and-dirty single-node ZooKeeper instance.

```bash
> bin/zookeeper-server-start.sh config/zookeeper.properties
[2013-04-22 15:01:37,495] INFO Reading configuration from: config/zookeeper.properties (org.apache.zookeeper.server.quorum.QuorumPeerConfig)
...
```

Now start the Kafka server:
```
> bin/kafka-server-start.sh config/server.properties
[2013-04-22 15:01:47,028] INFO Verifying properties (kafka.utils.VerifiableProperties)
[2013-04-22 15:01:47,051] INFO Property socket.send.buffer.bytes is overridden to 1048576 (kafka.utils.VerifiableProperties)
...
```

## Exercise 1

Let's implement a simple word count program. Go to `kafka_flink_wordcount_example`.
Create a Flink Kafka Consumer that consumes the data produced by our Kafka producer on the `inputword` topic.

Push data into the Kafka topic using:
```bash
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic inputword
```

## Exercise 2

### Step 1: Create the topics
Let's create a topics named "filtered_sample" and "sample_sensor" with a single partition and only one replica:

```bash
> bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic filtered_sample
> bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic sample_sensor
```

### Step 2: Start the producer

Create a virtual environment and install Kafka using:
```python
pip install kafka-python
```

Let's generate some sensor data by starting the producer:

```python
cd data_generator
python kafka_producer.py
python kafka_consumer.py
```
Leave the programm running.

### Step 3: Let's create our Flink application.

Go to `kafka_flink_sensor_example`. Create a Flink Kafka Consumer that consumes the data produced by our Kafka producer on the `sample_sensor` topic. Filter all messages that have `status: failed`. After filtering the messages, create new Flink Kafka producer that will publish the filtered stream on the `filtered_sample` topic.

If you did everything correctly you should see the filtered messages being printed by our kafka python consumer.

To run the programm run:

```scala
sbt build
```

*Hint*: We need to parse a JSON string. You need to create a case class corresponding to the format of the sensor data. Then you can use the [Play framework](https://www.playframework.com/) to parse the JSON strings. While parsing, filter the elements that failed to parse.