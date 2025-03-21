import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class BlockchainConsumer {
    private static final String TOPIC = "transactions";
    private static final AtomicBoolean running = new AtomicBoolean(true);
    private static Blockchain blockchain = new Blockchain();
    private static String lastLoggedState = ""; // To keep track of the last blockchain state logged

    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "blockchain-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singletonList(TOPIC));

            // Add a shutdown hook to handle termination gracefully
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Shutdown signal received. Closing consumer...");
                running.set(false);
                consumer.wakeup(); // Interrupt consumer.poll()
            }));

            while (running.get()) {
                try {
                    ConsumerRecords<String, String> records = consumer.poll(100);
                    boolean updated = false;

                    for (ConsumerRecord<String, String> record : records) {
                        System.out.println("Received Transaction: " + record.value());

                        // Add transaction to the blockchain
                        blockchain.addTransaction(record.value());
                        updated = true; // Mark that the blockchain has been updated
                    }

                    // Check and log the blockchain state only if it's updated
                    if (updated) {
                        // Clear the console/log screen
                        clearConsole();

                        // Check blockchain validity before logging the state
                        System.out.println("Blockchain valid? " + blockchain.isChainValid());

                        // Get the current blockchain state as JSON
                        String currentState = blockchain.toJson();

                        // Log the updated state only if it's changed
                        if (!currentState.equals(lastLoggedState)) {
                            lastLoggedState = currentState; // Update the last logged state
                            System.out.println(currentState);
                        }
                    }
                } catch (WakeupException e) {
                    // Ignore exception if shutting down
                    if (!running.get()) {
                        break;
                    }
                    throw e;
                }
            }
        } catch (Exception e) {
            System.err.println("Error in consumer loop: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("Consumer closed.");
        }
    }

    /**
     * Utility to clear the console (platform-independent).
     */
    private static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // If clearing the console fails, just continue without it
            System.err.println("Failed to clear console: " + e.getMessage());
        }
    }
}