import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BlockchainProducerTest {
    @Test
    void testProducerSendsTransaction() {
        try (// Use Kafka's MockProducer with proper serializers
             MockProducer<String, String> mockProducer = new MockProducer<>(
                     true,  // autoComplete
                     new StringSerializer(),  // Key Serializer
                     new StringSerializer()   // Value Serializer
             )) {
            // Simulate sending a transaction
            String testTopic = "transactions";
            String testKey = "tx123";
            String testValue = "{\"from\":\"Alice\",\"to\":\"Bob\",\"amount\":50}";

            mockProducer.send(new ProducerRecord<>(testTopic, testKey, testValue));

            // Verify message was sent
            assertEquals(1, mockProducer.history().size());
            ProducerRecord<String, String> record = mockProducer.history().get(0);
            assertEquals(testTopic, record.topic());
            assertEquals(testValue, record.value());
        }

        System.out.println("Test passed: Transaction sent correctly");
    }
}
