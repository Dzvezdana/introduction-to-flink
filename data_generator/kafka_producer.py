import datetime
import json
import time


from kafka import KafkaProducer
import numpy


class Message:
    def __init__(self, identifier):
        self.timestamp = datetime.datetime.utcnow()
        self.temperature = numpy.random.uniform(0.0, 45.0)
        self.id = identifier

    def next_point(self):
        return {
            "timestamp": datetime.datetime.utcnow(),
            "temperature": numpy.random.uniform(0.0, 45.0),
            "status": numpy.random.choice(['ok', 'failed'], p = (0.9, 0.1)),
            "id" : self.id
        }

    def __repr__(self):
        return str({
            "timestamp": self.timestamp,
            "temperature": self.temperature,
            "id" : self.id
        })

def myconverter(o):
    if isinstance(o, datetime.datetime):
        return o.__str__()

messages = [Message(i) for i in range(0, 1500)]
producer = KafkaProducer(
    bootstrap_servers = ["localhost: 9092"],
    value_serializer = lambda v: json.dumps(v, default = myconverter).encode('utf-8')
)
while True:
    for x in messages:
        producer.send('sample_sensor', x.next_point())
    time.sleep(2)
