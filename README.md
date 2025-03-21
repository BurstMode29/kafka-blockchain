# Kafka Blockchain System

## Overview
This project is a simple implementation of a blockchain integrated with Apache Kafka for handling transactions. The system simulates a distributed ledger where transactions are collected, validated, and stored as blocks through a `KafkaProducer` and `KafkaConsumer` workflow. The blockchain uses a Proof-of-Work consensus mechanism to validate blocks.

### Key Features
1. **Blockchain Implementation**: 
   - Each block contains an `index`, `timestamp`, `data` (transactions), `previousHash`, `hash`, and `nonce`.
   - Blocks are mined using a Proof-of-Work algorithm requiring a hash with `n` leading zeros.
   - Adds new blocks after batching transactions (default: 5 transactions per block).
   - Ensures blockchain immutability and validity.

2. **Kafka Integration**:
   - `KafkaProducer` sends simulated transaction data to a Kafka topic (`transactions`).
   - `KafkaConsumer` listens for transaction messages and adds them to the blockchain when enough transactions accumulate.

3. **Validation and Testing**: 
   - Tests validate block creation, blockchain validity, transaction batching, hash integrity, and Kafka producer-consumer operation.

---

## Code Structure
### 1. Core Classes
#### `Block`
- Represents the basic building block of the blockchain.
- Contains methods to calculate SHA-256 hash and perform Proof-of-Work mining.

#### `Blockchain`
- Manages the blockchain and maintains the chain's validity.
- Handles transactions, creates new blocks, and ensures immutability.

### 2. Kafka Integration
#### `BlockchainProducer`
- Sends transaction data to a Kafka topic (`transactions`) in JSON format.
- Example Transaction: `{ "from": "Alice", "to": "Bob", "amount": 50 }`.

#### `BlockchainConsumer`
- Listens to the `transactions` topic, processes transactions, and adds them to the blockchain.
- Mines new blocks once `maxTransactionsPerBlock` is reached and ensures logged blockchain state is always valid.

### 3. Test Cases
- Comprehensive JUnit testing for the following:
  - Blockchain (`BlockchainTest`)
    - `testAddBlock`: Verifies that blocks are added when transaction threshold is met.
    - `testIsChainValid`: Validates blockchain integrity.
    - `testGenesisBlock`: Ensures the genesis block is correctly initialized.
  - Block (`BlockTest`)
    - `testCalculateHash`: Ensures SHA-256 hash generation.
    - `testMineBlock`: Verifies hash starts with the required number of leading `0`s based on difficulty.
  - Kafka Consumer (`BlockchainConsumerTest`) 
    - Tests consumer ability to process transactions.
  - Kafka Producer (`BlockchainProducerTest`)
    - Validates that the producer sends transaction messages correctly.

---

## Prerequisites
1. **Java**: Ensure JDK 8+ is installed.
2. **Apache Kafka**: Install and start Kafka on `localhost:9092`.
3. **Dependencies**:
   - Add the following dependencies to your project:
     - **Gson**: For JSON serialization/deserialization.
     - **Apache Kafka**: For producer and consumer APIs.
     - **JUnit 5**: For unit testing.
   - Add the required dependencies to your `pom.xml` (if using Maven) or `build.gradle` (if using Gradle).

---

## How to Run
### Step 1: Start Kafka
Start your Kafka instance and create a topic named `transactions`.

```bash
# Start Zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties
or 
bin\windows\zookeeper-server-start.bat config\zookeeper.properties

# Start Kafka
bin/kafka-server-start.sh config/server.properties

# Create 'transactions' topic
bin/kafka-topics.sh --create --topic transactions --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1

# Monitor 'transactions' topic
.\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic transactions --from-beginning
