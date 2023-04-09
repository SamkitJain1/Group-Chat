package JavaSeries.SocketProgramming;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientList = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public ClientHandler(Socket socket1){
        try {
            this.socket = socket1;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = bufferedReader.readLine();
            clientList.add(this);

            broadcast("SERVER: " + username + " has joined the chat!");
        }
        catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String messageFromClient;

        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                broadcast(messageFromClient);
            }
            catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void broadcast(String messageFromClient){

            for (ClientHandler client1: clientList){
                try {

                    if (!client1.username.equals(username)) {
                        client1.bufferedWriter.write(messageFromClient);
                        client1.bufferedWriter.newLine();
                        client1.bufferedWriter.flush();
                    }
                }

                catch (IOException e) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
    }

    public void removeClientHandler() {
        if (clientList.contains(this)) { // added check
            broadcast("SERVER: " + username + " has left the chat!");
            clientList.remove(this);
        }
    }


    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClientHandler();
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
}
