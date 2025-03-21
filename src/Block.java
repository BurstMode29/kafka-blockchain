import java.util.Date;
import utils.*;

public class Block {
    private int index;
    private long timeStamp;;
    private String data;
    private String previousHash;
    private String hash;
    private int nonce;

    // Constructor
    public Block(int index, String data, String previousHash) {
        this.index = index;
        this.timeStamp = new Date().getTime();
        this.data = data;
        this.previousHash = previousHash;
        this.hash = calculateHash();
    }

    // SHA-256 Hash Function
    public String calculateHash() {
        String calculatedhash = StringUtil.applySha256(
                previousHash +
                        Long.toString(timeStamp) +
                        Integer.toString(nonce) +
                        data);
        return calculatedhash;
    }

    // Proof-of-Work: Finds a hash starting with 0000
    public void mineBlock(int difficulty) {
        String target = "0".repeat(difficulty);
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block Mined: " + hash);
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        this.hash = calculateHash();
    }

    public Integer getIndex() {
        return index;
    }
}
