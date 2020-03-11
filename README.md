# Introduction to Flink

## Set up Kafka
### Step 1: Download the code
[Download](https://www.apache.org/dyn/closer.cgi?path=/kafka/2.4.0/kafka_2.12-2.4.0.tgz) the 2.4.0 release and un-tar it.

```bash
> tar -xzf kafka_2.12-2.4.0.tgz
> cd kafka_2.12-2.4.0
```

### Step 2: Start the server
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

### Step 3: Create the topics
Let's create a topics named "filtered_sample" and "sample_sensor" with a single partition and only one replica:

```bash
> bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic filtered_sample
> bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic sample_sensor
```

### Step 4: Start the producer
Let's generate some sensor data by starting the producer:

```python
cd data_generator
python kafka_producer.py
```