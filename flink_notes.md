# Stream Processing with Apache Flink

## Chapter 1 - Introduction to Stateful Streaming

### Traditional Data Infrastructures: Transactional and Analytical Data Processing
There are traditional 2 ways of processing data: **Transactional Processing** and **Analytical Processing**.

- Transactional Processing Systems separate data processing (the applications) and data storage (the database).
A problem occurs when multiple applications need to work on the same infrastructure. 
For example if we need to change the schema of a table we'll need to do a lot of planning in advance since fixing the
schema for one application might break the other application. Or what if we need to change the dependencies? \
Then we need to use Microservices. Here we have a lot of small decoupled applications that do one thing well, and communicate using REST apis.

- Analytical Processing Systems. Transactional data is often distributed across several disconnected database systems. 
However, data is more valuable when it can be jointly analyzed. But joining this data can be difficult, since it might have different format, 
schema, might contain duplicates etc. Hence, we need to replicate it in a data warehouse, a dedicated datastore for analytical queries.
To create a data warehouse we copy the data from the databases to it. This process is known as ETL.

### Stateful Stream Processing
All data is created as continuous streams of events. In fact, it is difficult to find examples of finite, complete datasets that are generated all at once. 
Stateful stream processing is an application design pattern for processing unbounded streams of events and is applicable 
to many different use cases in the IT infrastructure of a company.

Stateful apps must have the ability to store and access intermediate data. For example Flink stores the application state locally in memory or in an embedded database.
When an application receives an event it reads the data from, or it writes it to a state.
The local state needs to be protected against failures to avoid data loss in case of application or machine failure. 
Flink guarantees this by periodically writing a consistent checkpoint of the application state to a remote and durable storage. 

Stateful stream processing applications often ingest their incoming events from an event log. An event log stores and distributes event streams.
Due to the append-only property of the log, events are always published to all consumers in exactly the same order. 
There are several event log systems available as open source software, Apache Kafka is the most popular one. 
The event log persists the input events and can replay them in deterministic order. 
In case of a failure, Flink recovers a stateful streaming application by restoring its state from a previous checkpoint 
and resetting the read position on the event log.
The application will replay (and fast forward) the input events from the event log until it reaches the tail of the stream. 
This technique is used to recover from failures but can also be leveraged to update an application, 
fix bugs and repair previously emitted results, migrate an application to a different cluster, 
or perform A/B tests with different application versions.

Three classes of applications that are commonly implemented using stateful stream processing are: 

- event-driven applications, 
- data pipeline applications, and 
- data analytics applications.

#### Event-Driven Applications
Event-Driven applications are stateful streaming applications that ingest event streams and process the events with 
application-specific business logic (trigger sending an alert or an email or write events to an outgoing event stream 
to be consumed by another event-driven application). 
Typical use cases for event-driven applications include: real-time recommendations 
(e.g., for recommending products while customers browse a retailer’s website), pattern detection or complex event processing 
(e.g., for fraud detection in credit card transactions), anomaly detection (e.g., to detect attempts to intrude a computer network).
They are an evolution of microservices and they communicate via event logs instead of REST calls and hold application data 
as local state instead of writing it to and reading it from an external datastore.

Why are they nice?

- Local state access provides very good performance compared to reading and writing queries against remote datastores. 
- Scaling and fault tolerance are handled by the stream processor, and by leveraging an event log as the input source the 
complete input of an application is reliably stored and can be deterministically replayed. 
- Furthermore, Flink can reset the state of an application to a previous savepoint, making it possible to evolve or rescale 
an application without losing its state.

#### Data pipelines
Data from let's say a web store might be written in different places at once. We need to have this data synced.
A traditional approach to synchronize data in different storage systems is periodic ETL jobs. 
An alternative is to use an event log to distribute updates. Consumers of the log incorporate the updates into the affected data stores. 
Depending on the use case, the transferred data may need to be normalized, enriched with external data, or aggregated 
before it is ingested by the target data store. Ingesting, transforming, and inserting data with low latency is another 
common use case for stateful stream processing applications. This type of application is called a data pipeline. 
Data pipelines must be able to process large amounts of data in a short time. A stream processor that operates a data 
pipeline should also feature many source and sink connectors to read data from and write data to various storage systems.


####  Streaming Data analytics 
ETL jobs periodically import data into a datastore and the data is processed by ad-hoc or scheduled queries. 
This is batch processing regardless of whether the architecture is based on a data warehouse or components of the Hadoop ecosystem.
Depending on the scheduling intervals it may take hours or days until a data point is included in a report.
There will always be a delay until an event is processed by a query. While this kind of delay may have been acceptable 
in the past, applications today must be able to collect data in real-time and immediately act on it (e.g., by adjusting 
to changing conditions in a mobile game or by personalizing user experiences for an online retailer).

Instead of waiting to be periodically triggered, a streaming analytics application continuously ingests streams of events 
and updates its result by incorporating the latest events with low latency.

* Advantages:

	- Much shorter time needed for an event to be incorporated into an analytics result.
	- Stream processor takes care of all processing steps (event ingestion, continous computations and updating the results).

* Applications:

	- Monitoring the quality of cellphone networks;
	- Analyzing user behavior in mobile applications;
	- Ad-hoc analysis of live data in consumer technology;


### The Evolution of Open Source Stream Processing

* First generation - Lambda Architecture
Data processing systems could either provide fast or accurate results led to the design of the so-called lambda architecture.
In Lambda we have 2 pipelines Batch and Speed. The Batch part processes batches of data correctly and writes them in a batch table. The speed part processes data in near real time and writes this approximated results in a speed table. Then we drop the corresponding inaccurate results from the speed table.
Some disadvantages:

	- It requires two semantically equivalent implementations of the application logic for two separate processing systems with different APIs. 
	- The results computed by the stream processor are only approximate. 
	- The lambda architecture is hard to set up and maintain.

* Second generation: Provided better failure guarantees and ensured that in case of a failure each input record affects the result exactly once.


* Third generation: Addressed the dependency of results on the timing and order of arriving events.


