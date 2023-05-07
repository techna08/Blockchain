import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * Author: Aarushi Sinha ( aarushis )
 * Email: aarushis@andrew.cmu.edu
 * Project 3
 * Last Modified: 27th October 2022
 * This is the Client class.
 * this enables the client to perform the ops on the
 * blockchain and fulfill the client request
 * and return a response.
 */
public class Server {

    static BlockChain blockChain;
    public static void main(String args[]){
        Socket clientSocket = null;
        try {

            int serverPort = 7777; // the server port we are using

            // Create a new server socket
            ServerSocket listenSocket = new ServerSocket(serverPort);
            System.out.println("Blockchain server running");
            /*
             * Block waiting for a new connection request from a client.
             * When the request is received, "accept" it, and the rest
             * the tcp protocol handshake will then take place, making
             * the socket ready for reading and writing.
             */
            clientSocket = listenSocket.accept();
            // If we get here, then we are now connected to a client.

            // Set up "in" to read from the client socket
            Scanner in;
            in = new Scanner(clientSocket.getInputStream());

            // Set up "out" to write to the client socket
            PrintWriter out;
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));


            blockChain=new BlockChain();
            blockChain.computeHashesPerSecond();

            Block genesisBlock=new Block(0,blockChain.getTime(),"",2);
            genesisBlock.setPreviousHash("");
            blockChain.addBlock(genesisBlock);
            /*
             * Forever,
             *   read a line from the socket
             *   print it to the console
             *   echo it (i.e. write it) back to the client
             */
            int count=0;
            while (true) {
                if(in.hasNextLine()) {
                    if(count==0){
                        //When we receive the first request from client
                        System.out.println("We have a visitor");
                        count++;
                    }
                    String data = in.nextLine();
                    Gson gson=new Gson();
                    RequestMessage request=gson.fromJson(data,RequestMessage.class);
                    ResponseMessage response = createResponse(request);
                    out.println(gson.toJson(response));
                    out.flush();
                }else{
                    clientSocket.close();
                    clientSocket = listenSocket.accept();
                    in = new Scanner(clientSocket.getInputStream());
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));

                }
            }


            // Handle exceptions
        } catch (IOException e) {
            System.out.println("IO Exception:" + e.getMessage());

            // If quitting (typically by you sending quit signal) clean up sockets
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                // ignore exception on close
            }
        }
    }

    private static ResponseMessage createResponse(RequestMessage request) throws NoSuchAlgorithmException {
        //We will make a separate response msg for each field as response variables vary
        ResponseMessage responseMessage = null;
        int choice=request.getSelection();

        switch(choice){
            case 0:{
                responseMessage=new ResponseMessage();
                responseMessage.setSelection(choice);
                String response ="Current size of chain: " + blockChain.getChainSize() + "\n" +
                        "Difficulty of most recent block: " + blockChain.getLatestBlock().getDifficulty() + "\n" +
                        "Total difficulty for all blocks: " + blockChain.getTotalDifficulty() + "\n" +
                        "Approximate hashes per second on this machine: " + blockChain.getHashesPerSecond()+ "\n" +
                        "Expected total hashes required for the whole chain: " + blockChain.getTotalExpectedHashes()+ "\n" +
                        "Nonce for most recent block: " + blockChain.getLatestBlock().getNonce() + "\n" +
                        "Chain hash: " + blockChain.chainHash;
                Gson g=new Gson();
                responseMessage.setSize(blockChain.getChainSize());
                responseMessage.setChainHash(blockChain.chainHash);
                responseMessage.setTotalHashes(blockChain.getTotalExpectedHashes());
                responseMessage.setTotalDiff(blockChain.getTotalDifficulty());
                responseMessage.setRecentNonce(blockChain.getLatestBlock().getNonce());
                responseMessage.setDiff(blockChain.getLatestBlock().getDifficulty());
                responseMessage.setHps(blockChain.getHashesPerSecond());
                System.out.println("Response : " + g.toJson(responseMessage));
                responseMessage.setResponse(response);

            }break;
            case 1:{

                responseMessage=new ResponseMessage();
                responseMessage.setSelection(choice);
                int difficultyEntered= request.getDifficulty();
                String dataEntered= request.getTransaction();
                long start=System.currentTimeMillis();
                //adding the block
                System.out.println("Adding a block");
                Block newBlock= new Block(blockChain.getChainSize(),blockChain.getTime(),dataEntered,difficultyEntered);
                blockChain.addBlock(newBlock);

                long end= System.currentTimeMillis();
                String response = "Total execution time to add this block was " + (end - start) + " milliseconds";
                responseMessage.setResponse(response);
                System.out.println("Setting response to " + response);
                Gson g=new Gson();
                System.out.println("..." + g.toJson(responseMessage));

            }break;
            case 2:{
                responseMessage=new ResponseMessage();
                responseMessage.setSelection(choice);
                long start=System.currentTimeMillis();
                System.out.println("Verifying entire chain");
                String response2 = "Chain verification: " + blockChain.isChainValid();
                System.out.println(response2);
                long end= System.currentTimeMillis();
                String response = "Total execution time to verify the chain was " + (end - start) + " milliseconds";
                System.out.println("Total execution time required to verify the chain was " + (end - start) + " milliseconds");
                System.out.println("Setting response to " + response);
                responseMessage.setResponse(response2 + "\n" + response);

            }break;
            case 3:{
                responseMessage=new ResponseMessage();
                responseMessage.setSelection(choice);
                String response2="View the Blockchain";
                System.out.println(response2);
                String response = blockChain.toString();
                System.out.println("Setting response to " + response);
                responseMessage.setResponse(response2 + "\n" + response);


            }break;
            case 4:{
                responseMessage=new ResponseMessage();
                responseMessage.setSelection(choice);
                System.out.println("Corrupt the Blockchain");

                int blockId=request.getBlockIdCorrupt();
                String data=request.getDataCorrupt();
                blockChain.getBlock(blockId).setData(data);
               String response= "Block " + blockId + " now holds " + data;
               System.out.println(response);
                System.out.println("Setting response to " + response);
                responseMessage.setResponse(response);

            }break;
            case 5:{

                responseMessage=new ResponseMessage();
                responseMessage.setSelection(choice);
                System.out.println("Repairing the entire chain");
                long start=System.currentTimeMillis();
                blockChain.repairChain();
                long end= System.currentTimeMillis();
                String response="Total execution time required to repair the chain was " + (end - start) + " milliseconds";
                System.out.println("Setting response to " + response);
                responseMessage.setResponse(response);
            }break;
        }
return responseMessage;

    }
}
