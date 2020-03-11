from kafka import KafkaConsumer

consumer = KafkaConsumer('filtered_sample')
for msg in consumer:
    print(msg)
