package JavaSeries.SocketProgramming;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientX {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public ClientX(Socket socket, String username){
        try {
            this.socket = socket;
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        }
        catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage() {
            try {
                bufferedWriter.write(username);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                Scanner input = new Scanner(System.in);

                while (socket.isConnected()) {
                    String messageToSend = input.nextLine();
                    bufferedWriter.write(username + ": " + messageToSend);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    if (messageToSend.equalsIgnoreCase("bye")) {
                        System.exit(0);
                    }
                }

            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }


    public void recieveMessage() {
        new Thread(() -> {

            String messageFromGroup;

            while (socket.isConnected()) {
                try {
                    messageFromGroup = bufferedReader.readLine();
                    System.out.println(messageFromGroup);
                }
                catch (Exception e) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    break;
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null ) {
                bufferedWriter.close();
            }
            if (socket != null)
                socket.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
        Scanner input = new Scanner(System.in);
        System.out.print("Enter your username for the group chat: ");
        String username = input.nextLine();

        Socket socket1 = new Socket("localhost", 69);
        ClientX client = new ClientX(socket1, username);
        client.recieveMessage();
        client.sendMessage();
    }

}
