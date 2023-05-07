import java.io.*;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Scanner;
import com.google.gson.Gson;
/**
 * Author: Aarushi Sinha ( aarushis )
 * Email: aarushis@andrew.cmu.edu
 * Project 3
 * Last Modified: 27th October 2022
 * This is the Client class.
 * this enables the client to pick the ops on the
 * blockchain.
 */
public class Client {
    public static  void main(String args[]){

        Socket clientSocket=null;
        try {
            int serverPort = 7777;
            clientSocket = new Socket("localhost", serverPort);
            //System.out.println("The client is running.");

            // to read and write to server
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));

            executeChoice(in,out);
        } catch (IOException e) {
            System.out.println("IO Exception:" + e.getMessage());
        } catch (Exception e) {
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

    private static void executeChoice(BufferedReader in, PrintWriter out) throws IOException {
        int choice;
        do{
            System.out.println("0. View basic blockchain status.\n" +
                    "1. Add a transaction to the blockchain.\n" +
                    "2. Verify the blockchain.\n" +
                    "3. View the blockchain.\n" +
                    "4. Corrupt the chain.\n" +
                    "5. Hide the corruption by repairing the chain.\n" +
                    "6. Exit");

            Scanner sc=new Scanner(System.in);
            choice=sc.nextInt();
            switch(choice){
                case 0:
                case 2:
                case 3:
                case 5: {
                    // All these cases have no user interaction. Only show the server's response.

                    //Creating a request
                    RequestMessage request=new RequestMessage();
                    request.setSelection(choice);
                    Gson gson=new Gson();
                    out.println(gson.toJson(request));
                    out.flush();

                    // Receiving the server's reply to our request in the buffer
                    String responseStr = in.readLine();
                    ResponseMessage response=gson.fromJson(responseStr,ResponseMessage.class);
                    System.out.println(response.getResponse());

                }break;
                case 1:{
                    //Adding a block
                    Scanner sc1= new Scanner(System.in);
                    Scanner sc11= new Scanner(System.in);
                    System.out.println("Enter difficulty > 0 ");
                    int difficultyEntered= sc1.nextInt();

                    System.out.println("Enter transaction");
                    String dataEntered= sc11.nextLine();
                    //Creating a request
                    RequestMessage request=new RequestMessage();
                    request.setSelection(choice);
                    request.setDifficulty(difficultyEntered);
                    request.setTransaction(dataEntered);

                    Gson gson=new Gson();
                    out.println(gson.toJson(request));
                    out.flush();

                    // Receiving the server's reply to our request in the buffer
                    String responseStr = in.readLine();
                    ResponseMessage response=gson.fromJson(responseStr,ResponseMessage.class);
                    System.out.println(response.getResponse());

                }break;
                case 4:{
                    System.out.println("corrupt the Blockchain\n" +
                            "Enter block ID of block to corrupt");

                    Scanner sc4= new Scanner(System.in);
                    Scanner sc44= new Scanner(System.in);
                    int blockId=sc4.nextInt();
                    System.out.println("Enter new data for block " + blockId);
                    String data=sc44.nextLine();
                    //Creating a request
                    RequestMessage request=new RequestMessage();
                    request.setSelection(choice);
                    request.setBlockIdCorrupt(blockId);
                    request.setDataCorrupt(data);

                    Gson gson=new Gson();
                    out.println(gson.toJson(request));
                    out.flush();

                    // Receiving the server's reply to our request in the buffer
                    String responseStr = in.readLine();
                    ResponseMessage response=gson.fromJson(responseStr,ResponseMessage.class);
                    System.out.println(response.getResponse());

                }break;
            }

        }while(choice!=6);
    }
}
