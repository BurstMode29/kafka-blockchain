import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private List<Block> chain;
    private int difficulty = 4; // Number of leading zeroes in proof-of-work
    private List<String> currentTransactions = new ArrayList<>();
    private final int maxTransactionsPerBlock = 5; // Limit number of transactions per block

    // Constructor: Creates genesis block
    public Blockchain() {
        chain = new ArrayList<>();
        chain.add(new Block(0, "Genesis Block", "0"));
    }

    // Get the latest block in the chain
    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    // Add a transaction to the current pool
    public void addTransaction(String transactionData) {
        currentTransactions.add(transactionData);
        if (currentTransactions.size() >= maxTransactionsPerBlock) {
            createNewBlock();
        }
    }

    // Create and add a new block with proof-of-work
    private void createNewBlock() {
        String transactionData = String.join(", ", currentTransactions);
        currentTransactions.clear(); // Clear current transactions after adding them to the block

        Block newBlock = new Block(chain.size(), transactionData, getLatestBlock().getHash());
        newBlock.mineBlock(difficulty);
        chain.add(newBlock);
        System.out.println("Block added: " + newBlock.getHash());
    }

    // Validate the chain
    public boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                return false;
            }
            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                return false;
            }
        }
        return true;
    }

    public List<Block> getChain() {
        return chain;
    }

    // Print blockchain as a JSON string
    public String toJson() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}