This project demonstrates a real-time data pipeline built using Spring Boot, Apache Kafka, and PostgreSQL.

It streams live edit events from Wikimedia’s public EventStream API (https://stream.wikimedia.org/v2/stream/recentchange), produces them to a Kafka topic, and consumes them in another Spring Boot service which stores the data in a PostgreSQL database.

This project shows how to:

Build a Kafka Producer with Spring Boot.

Consume real-time Kafka messages.

Persist messages into PostgreSQL in real-time.

Handle long-lived event streams efficiently.

🏗️ Project Architecture
          +-----------------------------+
          | Wikimedia Event Stream API  |
          | (Real-time JSON changes)    |
          +--------------+--------------+
                         |
                         ▼
              [Spring Boot Kafka Producer]
                         |
                         ▼
                 +----------------+
                 | Kafka Topic    |
                 | wikimedia_recentchange |
                 +----------------+
                         |
                         ▼
              [Spring Boot Kafka Consumer]
                         |
                         ▼
                 +----------------+
                 | PostgreSQL DB  |
                 | wikimedia_data |
                 +----------------+

⚙️ Tech Stack
Component	Technology
Language	Java 21
Framework	Spring Boot 3.x
Messaging System	Apache Kafka
Database	PostgreSQL
Dependency Management	Maven
Streaming Library	okhttp-eventsource 2.5.0
Build Tool	Maven
IDE	IntelliJ / Eclipse


🧩 Kafka Setup (Windows Example)

1️⃣ Download latest version of apache-kafka


2️⃣ Start Kafka Broker

bin\windows\kafka-server-start.bat config\server.properties  (change dir to kafka installtion folder)


3️⃣ Create Topic

bin\windows\kafka-topics.bat --create --topic wikimedia_recentchange --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1


4️⃣ Verify Topic

bin\windows\kafka-topics.bat --list --bootstrap-server localhost:9092

# Understanding the Flow ##

Producer uses LaunchDarkly’s EventSource to connect to Wikimedia’s SSE stream.

Each JSON message is pushed into a Kafka topic (wikimedia_recentchange).

Consumer listens to that topic using @KafkaListener.

Messages are parsed and stored into PostgreSQL in real-time.
