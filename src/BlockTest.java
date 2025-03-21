import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BlockTest {
    private Block block;

    @BeforeEach
    public void setUp() {
        block = new Block(1, "Sample Data", "0");
    }

    @Test
    public void testCalculateHash() {
        String hash = block.calculateHash();
        assertNotNull(hash, "Hash should not be null");
        assertEquals(64, hash.length(), "Hash should have length of 64 characters (SHA-256)");
    }

    @Test
    public void testMineBlock() {
        int difficulty = 4;
        block.mineBlock(difficulty);
        String hash = block.getHash();
        assertTrue(hash.startsWith("0000"), "Hash should start with 0000 for difficulty of 4");
    }

    @Test
    public void testBlockConstructor() {
        assertEquals(1, block.getIndex(), "Block index should be 1");
        assertEquals("Sample Data", block.getData(), "Block data should be 'Sample Data'");
        assertEquals("0", block.getPreviousHash(), "Previous hash should be '0'");
    }
}

