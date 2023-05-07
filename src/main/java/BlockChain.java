import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Author: Aarushi Sinha ( aarushis )
 * Email: aarushis@andrew.cmu.edu
 * Project 3
 * Last Modified: 27th October 2022
 * This is the Blockchain class. Which
 * helps do ops on the blockchain.
 */
public class BlockChain {
    //to be ignored by gson
    transient int numberOfHashes;
    String chainHash;
    List<Block> ds_chain ;

    public BlockChain() {
        this.numberOfHashes=0;
        this.chainHash="";
        this.ds_chain=new ArrayList<>();
    }

    public String getChainHash(){
        return this.chainHash;
    }

    public Timestamp getTime(){
        return  new Timestamp(System.currentTimeMillis());
    }

    public Block getLatestBlock(){
        return ds_chain.get(ds_chain.size()-1);
    }

    public int getChainSize(){
        return ds_chain.size();
    }

    public void computeHashesPerSecond(){
        String hash="00000000";
        int total= 2000000;
        int i=0;
        long startTime= System.currentTimeMillis();
        // loop to find 2000000 hashes
        while(i<total){

            MessageDigest msg= null;
            try {
                msg = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            msg.update(hash.getBytes());
            hash=new String(msg.digest(), StandardCharsets.UTF_8);
            i++;
        }

        long endTime=System.currentTimeMillis();
        //made it 2000000*1000 because time is in Milliseconds.
        this.numberOfHashes= (int) (2000000000/(endTime-startTime));
    }

    public int getHashesPerSecond(){
        return this.numberOfHashes;
    }

    public void addBlock(Block newBlock) throws NoSuchAlgorithmException {

        //If adding first block
        if(this.ds_chain.size()==0)
        {
            newBlock.previousHash="";
            this.ds_chain.add(newBlock);
        }else{
            //setting previous hash of new block
            if(this.getLatestBlock() !=null)
                newBlock.setPreviousHash(this.getLatestBlock().proofOfWork());
            this.ds_chain.add(newBlock);

        }
        // setting new blocks hash as latest chainHash
        this.chainHash=newBlock.proofOfWork();


    }

    @Override
    public String toString() {
        //Converting object to JSON and formatting the same to print as expected.
        Gson g= new Gson();
        String jString=g.toJson(this);
        String[] jArr=jString.split("},");
        String result="";
        for(int i=0;i<jArr.length;i++){
            if(i<jArr.length-1)
                result=result + jArr[i] + "},\n";
            else
                result=result + jArr[i] ;
        }
        return result;
    }

    public Block getBlock(int i){
        return  this.ds_chain.get(i);
    }

    public  int getTotalDifficulty(){
        //total Difficulty of all blocks

        int totalDiff=0;

        for(Block b : this.ds_chain){
            totalDiff=totalDiff+b.getDifficulty();
        }
        return totalDiff;
    }

    /**
     * Expected hashes for each block = 16^difficulty.
     * calculates total
     * @return total hashes
     */
    public double getTotalExpectedHashes(){

        double expectedHashes=0.000000;
        for(Block b : this.ds_chain){
            int diff=b.getDifficulty();
            expectedHashes=expectedHashes+ Math.pow(16,diff);
        }

        return expectedHashes;
    }

    /**
     * Checking validity of chain
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String isChainValid() throws NoSuchAlgorithmException {

        //for block size 1
        if(this.ds_chain.size() ==1){
            Block firstBlock=this.ds_chain.get(0);
            String hash=firstBlock.calculateHash();
            //Condition 1: if Chainhash does not match
            if(!this.chainHash.equals(hash))
                return "FALSE \nThe chain hash does not match first block hash ";
            // Condition 2: if hash is incorrect w.r.t difficulty
            for(int i=0 ;i<firstBlock.getDifficulty();i++){
                if(hash.charAt(i)!='0')
                    return "FALSE \nFirst block doesnt satisfy the difficulty criteria";
            }
            return "TRUE";

        }
        // for chain with size>1
        else if(this.ds_chain.size() >1){
            for(int i=0;i<this.ds_chain.size();i++){
                //for all blocks except last
                if(i!=this.ds_chain.size() -1){
                    Block first= this.ds_chain.get(i);
                    Block second=this.ds_chain.get(i+1);
                    String firstHash=first.calculateHash();
                    //Condition: if hash is computed correctly
                    if(!firstHash.equals(second.previousHash))
                        return "FALSE \nImproper hash on node " + i + " Does not begin with 00";
                }
                else{
                    //for last block
                    Block last= this.ds_chain.get(i);
                    String lastHash= last.calculateHash();
                    //checking with chain hash as there is no next block
                    if(!lastHash.equals(this.chainHash))
                        return "FALSE \nThe chain hash does not match the hash at : " + i;
                }

            }
            return "TRUE";
        }
        return null;
    }

    /**
     * Repairs the blockchain
     * @throws NoSuchAlgorithmException
     */
    public void repairChain() throws NoSuchAlgorithmException {

        //count to check the hash in the next block with current hash
        int count=0;
        for(Block b: this.ds_chain){
            String hash=b.calculateHash();
            boolean recalculate=false;
            //checking the difficulty criteria
            for(int i=0;i<b.difficulty;i++){
                if(hash.charAt(i)!='0'){
                    recalculate=true;
                    break;
                }
            }
            if(recalculate){
                //System.out.println("Repairing for " + count);
                //fix the hash
                if(count+1==this.ds_chain.size()){
                    //last block
                    this.chainHash=b.proofOfWork();
                }else{
                    this.ds_chain.get(count+1).setPreviousHash(b.proofOfWork());
                }

            }
            count++;
        }

    }

}
