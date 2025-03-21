import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BlockchainTest {
    private Blockchain blockchain;

    @BeforeEach
    public void setUp() {
        blockchain = new Blockchain();
    }

    @Test
    public void testAddBlock() {
        int initialChainSize = blockchain.getChain().size(); // Should start with just the genesis block (size = 1)

        // Add enough transactions to trigger block creation (5 transactions)
        blockchain.addTransaction("Transaction 1");
        blockchain.addTransaction("Transaction 2");
        blockchain.addTransaction("Transaction 3");
        blockchain.addTransaction("Transaction 4");
        blockchain.addTransaction("Transaction 5");

        int newChainSize = blockchain.getChain().size();
        assertEquals(initialChainSize + 1, newChainSize, "Chain size should increase by 1 after adding a block");
    }

    @Test
    void testBatchingAndBlockCreation() {
        Blockchain blockchain = new Blockchain();

        blockchain.addTransaction("Transaction 1");
        blockchain.addTransaction("Transaction 2");
        blockchain.addTransaction("Transaction 3");
        blockchain.addTransaction("Transaction 4");
        blockchain.addTransaction("Transaction 5");
        // At this point, a block should be created
        assertEquals(2, blockchain.getChain().size(), "A second block should have been created after 5 transactions");

        blockchain.addTransaction("Transaction 6");
        assertEquals(2, blockchain.getChain().size(),
                "No new block should be created until 5 transactions are reached");
    }

    @Test
    public void testIsChainValid() {
        // Add 5 transactions to trigger creation of 1 block
        blockchain.addTransaction("Transaction 1");
        blockchain.addTransaction("Transaction 2");
        blockchain.addTransaction("Transaction 3");
        blockchain.addTransaction("Transaction 4");
        blockchain.addTransaction("Transaction 5");

        // Add another set of 5 transactions to trigger a second block
        blockchain.addTransaction("Transaction 6");
        blockchain.addTransaction("Transaction 7");
        blockchain.addTransaction("Transaction 8");
        blockchain.addTransaction("Transaction 9");
        blockchain.addTransaction("Transaction 10");

        // Ensure the chain is valid
        assertTrue(blockchain.isChainValid(), "Chain should be valid initially");

        // Tamper with the first block's data and ensure chain becomes invalid
        blockchain.getChain().get(1).setData("Tampered Data");
        assertFalse(blockchain.isChainValid(), "Chain should be invalid after tampering with block data");
    }

    @Test
    public void testGenesisBlock() {
        Block genesisBlock = blockchain.getChain().get(0);
        assertEquals(0, genesisBlock.getIndex(), "Genesis block index should be 0");
        assertEquals("Genesis Block", genesisBlock.getData(), "Genesis block data should be 'Genesis Block'");
        assertEquals("0", genesisBlock.getPreviousHash(), "Genesis block previous hash should be '0'");
    }
}
