import com.google.gson.Gson;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

/**
 * Author: Aarushi Sinha ( aarushis )
 * Email: aarushis@andrew.cmu.edu
 * Project 3
 * Last Modified: 27th October 2022
 * This is the Block class. Which
 * is each Block of the chain.
 */
public class Block {
    //the position of the block on the chain. The first block (the so called Genesis block) has an index of 0.
    int index;

    //it holds the time of the block's creation.
    Timestamp timestamp;

    //a String holding the block's single transaction details.
    String data;

    // the SHA256 hash of a block's parent. This is also called a hash pointer.
    String previousHash;

    //it is an int that specifies the minimum number of left most hex digits
    // needed by a proper hash. The hash is represented in hexadecimal.
    // If, for example, the difficulty is 3, the hash must have at least three leading
    // hex 0's (or,1 and 1/2 bytes). Each hex digit represents 4 bits.
    int difficulty;

    //This has to be found by the proof of work logic.
    // It has to be found so that this block has a hash of the proper difficulty.
    // The difficulty is specified by a small integer representing the minimum number
    // of leading hex zeroes the hash must have.
    int nonce;

    public Block(int index,
                 Timestamp timestamp,
                 String data,
                 int difficulty){

        this.index=index;
        this.timestamp=timestamp;
        this.data=data;
        this.difficulty=difficulty;
        //nonce set to 0 at the beginning
        this.nonce=0;
    }

    /**
     *  method computes a hash of the concatenation of the
     *  index, timestamp, data, previousHash, nonce, and difficulty.
     * @return hashed string
     */
    public String calculateHash(){
        String value= this.index + this.timestamp.toString() +this.data + this.previousHash +this.nonce + this.difficulty;

        MessageDigest msg= null;
        try {
            msg = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        msg.update(value.getBytes());
        String message = bytesToHex(msg.digest());
        return message;
    }

    /**
     * Used for hashing
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public BigInteger getNonce()   {

        return  new BigInteger(String.valueOf(this.nonce));
    }

    /**
     * The proof of work methods finds a good hash.
     * It increments the nonce until it produces a good hash.
     * @return Good hash
     * @throws NoSuchAlgorithmException
     */
    public String proofOfWork() throws NoSuchAlgorithmException{
        int nonce=0;
        String hash= this.calculateHash();
        boolean gotNonce=true;
        for(int i=0;i<this.difficulty;i++){
            if(hash.charAt(i)!='0')
                gotNonce=false;
        }

        while(!gotNonce ){
            gotNonce=true;
            nonce=nonce+1;
            this.nonce=nonce;
            hash=this.calculateHash();
            for(int i=0;i<this.difficulty;i++){
                if(hash.charAt(i)!='0')
                    gotNonce=false;
            }

        }
        return hash;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    /**
     * A JSON representation of all of this
     * block's data is returned.
     * @return
     */
    @Override
    public String toString() {
        Gson gson = new Gson();

        return gson.toJson(this);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
