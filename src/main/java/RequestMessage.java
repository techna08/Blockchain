/**
 * Author: Aarushi Sinha ( aarushis )
 * Email: aarushis@andrew.cmu.edu
 * Project 3
 * Last Modified: 27th October 2022
 * This is the Request class.
 * This contains the request format
 */
public class RequestMessage {

    int selection;
    int difficulty;
    String transaction;
    int blockIdCorrupt;
    String dataCorrupt;

    public int getSelection() {
        return selection;
    }

    public void setSelection(int selection) {
        this.selection = selection;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public int getBlockIdCorrupt() {
        return blockIdCorrupt;
    }

    public void setBlockIdCorrupt(int blockIdCorrupt) {
        this.blockIdCorrupt = blockIdCorrupt;
    }

    public String getDataCorrupt() {
        return dataCorrupt;
    }

    public void setDataCorrupt(String dataCorrupt) {
        this.dataCorrupt = dataCorrupt;
    }
}
