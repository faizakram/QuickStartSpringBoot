# Apache Kafka In-Depth for Microservices

## 1. Introduction to Kafka in Microservices Architecture
Apache Kafka plays a critical role in modern microservice architecture by enabling **event-driven communication** between services. It decouples services, allowing them to publish and consume events asynchronously.

### Key Benefits:
- Decouples service dependencies
- Handles high throughput
- Supports message durability and replay
- Enables real-time streaming and analytics

---

## 2. Kafka Core Concepts Recap

| Component         | Description |
|------------------|-------------|
| Topic            | Named stream of records |
| Partition        | Topic subdivision for parallelism |
| Offset           | Position of a record in a partition |
| Producer         | Sends messages to Kafka topics |
| Consumer         | Reads messages from Kafka topics |
| Broker           | Kafka server storing messages |
| Consumer Group   | Set of consumers sharing the same group id |
| Zookeeper/KRaft  | Manages metadata, replaced by KRaft from Kafka 3.0+ |

---

## 3. Kafka with Microservices (Best Practices)

### Typical Use Cases:
- Communication between Order Service -> Inventory Service
- Audit logs for all microservice operations
- Event Sourcing for user actions
- Streaming analytics pipelines

### Microservice Interaction via Kafka:
```
[Order Service] --(OrderCreated)--> [Kafka Topic: order-events] --(Consumer)--> [Inventory Service]
```

---

## 4. Detailed Microservices Example using Kafka

### Scenario:
**Online Retail System** consisting of:
- **Order Service**
- **Inventory Service**
- **Payment Service**
- **Notification Service**

### Kafka Topics:
- `order-events`
- `inventory-events`
- `payment-events`
- `notification-events`

### Flow:
1. **User places an order** (POST /orders).
2. **Order Service** publishes `OrderPlaced` event to `order-events`.
3. **Inventory Service** listens to `order-events`, updates stock, and publishes `InventoryUpdated` to `inventory-events`.
4. **Payment Service** listens to `inventory-events`, processes payment, and publishes `PaymentProcessed` to `payment-events`.
5. **Notification Service** listens to `payment-events`, sends confirmation email/SMS, and publishes `NotificationSent` to `notification-events`.

---

## 5. Java Spring Boot Kafka Implementation (Full Example)

### Maven Dependency
```xml
<dependency>
  <groupId>org.springframework.kafka</groupId>
  <artifactId>spring-kafka</artifactId>
</dependency>
```

### OrderPlacedEvent.java
```java
public class OrderPlacedEvent {
    private String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private Instant timestamp;
    // Getters and Setters
}
```

### OrderProducer.java
```java
@Service
public class OrderProducer {
    @Autowired
    private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public void sendOrderEvent(OrderPlacedEvent event) {
        kafkaTemplate.send("order-events", event.getOrderId(), event);
    }
}
```

### InventoryConsumer.java
```java
@Service
public class InventoryConsumer {
    @KafkaListener(topics = "order-events", groupId = "inventory-service")
    public void consumeOrder(OrderPlacedEvent event) {
        // Validate and update stock
        // Send inventory event
    }
}
```

### Kafka Config (application.yml)
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: inventory-service
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      properties:
        spring.json.trusted.packages: '*'
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
```

---

## 6. Best Practices for Kafka in Microservices

| Best Practice                      | Description |
|-----------------------------------|-------------|
| Use Avro or Protobuf              | For schema evolution and compact payloads |
| Enable Idempotence                | Avoid duplicate messages in retries |
| Use Dead Letter Topics            | Handle poison messages gracefully |
| Externalize Configs               | Use application.yml/properties |
| Monitor Offsets and Lag           | Ensure consumers are not falling behind |
| Use Kafka Streams if needed       | For real-time processing and joins |
| Use Correlation IDs               | For tracing requests end-to-end |

---

## 7. Tools for Debugging & Monitoring
- **Kafdrop**: UI to inspect topics
- **Kafka Manager / Control Center**: Broker, partition, lag metrics
- **Confluent CLI**: Easy integration and management
- **Grafana + Prometheus**: Kafka JVM and broker metrics

---

## 8. Future Considerations
- Transition from Zookeeper to **KRaft**
- Implement **Exactly-Once Semantics (EOS)**
- Adopt **Kafka Connect** for data ingestion
- Integrate **Kafka Streams** for event processing logic
- Add **schema validation** using Schema Registry

---

## 9. Summary
Kafka is the backbone of scalable, fault-tolerant microservices. When used properly, it ensures real-time, loosely coupled communication and boosts system resilience and extensibility.
