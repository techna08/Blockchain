import java.math.BigInteger;

/**
 * Author: Aarushi Sinha ( aarushis )
 * Email: aarushis@andrew.cmu.edu
 * Project 3
 * Last Modified: 27th October 2022
 * This is the Response class.
 * This contains the response format
 */
public class ResponseMessage {
    int selection;
    String response;
    int size;

    String chainHash;
    double totalHashes;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getChainHash() {
        return chainHash;
    }

    public void setChainHash(String chainHash) {
        this.chainHash = chainHash;
    }

    public double getTotalHashes() {
        return totalHashes;
    }

    public void setTotalHashes(double totalHashes) {
        this.totalHashes = totalHashes;
    }

    public int getTotalDiff() {
        return totalDiff;
    }

    public void setTotalDiff(int totalDiff) {
        this.totalDiff = totalDiff;
    }

    public BigInteger getRecentNonce() {
        return recentNonce;
    }

    public void setRecentNonce(BigInteger recentNonce) {
        this.recentNonce = recentNonce;
    }

    public int getDiff() {
        return diff;
    }

    public void setDiff(int diff) {
        this.diff = diff;
    }

    public int getHps() {
        return hps;
    }

    public void setHps(int hps) {
        this.hps = hps;
    }

    int totalDiff;
    BigInteger recentNonce;
    int diff;
    int hps;



    public int getSelection() {
        return selection;
    }

    public void setSelection(int selection) {
        this.selection = selection;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
