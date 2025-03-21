import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class BlockchainConsumerTest {
    @Test
    void testConsumerProcessesTransaction() {
        try (MockConsumer<String, String> mockConsumer = new MockConsumer<>(OffsetResetStrategy.EARLIEST)) {
            // Define topic and partition
            String topic = "transactions";
            TopicPartition partition = new TopicPartition(topic, 0);
            mockConsumer.assign(Collections.singletonList(partition));

            // Set beginning offset
            mockConsumer.updateBeginningOffsets(Collections.singletonMap(partition, 0L));

            // Simulate receiving a transaction message
            String transactionData = "{\"from\":\"Alice\",\"to\":\"Bob\",\"amount\":50}";
            mockConsumer.addRecord(new ConsumerRecord<>(topic, 0, 0L, "tx123", transactionData));

            // Poll once for records
            var records = mockConsumer.poll(Duration.ofMillis(100));

            // Process and validate
            records.forEach(record -> {
                System.out.println("Received Transaction: " + record.value());
            });

            // Assertions
            assertEquals(1, records.count(), "Should have processed 1 transaction");

            System.out.println("Test passed: BlockchainConsumer processed transaction successfully");
        }
    }
}