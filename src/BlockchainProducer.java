import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Properties;

public class BlockchainProducer {
    // private static final String TOPIC = "transactions";
    @SuppressWarnings("unused")
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        ObjectMapper objectMapper = new ObjectMapper();

        // Simulating a blockchain transaction
        // String transactionJson = objectMapper.writeValueAsString(new Transaction("Alice", "Bob", 50));
        // ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, transactionJson);

        // producer.send(record, (metadata, exception) -> {
        //     if (exception == null) {
        //         System.out.println("Transaction sent successfully!");
        //     } else {
        //         exception.printStackTrace();
        //     }
        // });

        producer.close();
    }
}
