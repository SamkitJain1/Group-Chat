package JavaSeries.SocketProgramming;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerX {
    private final ServerSocket serverSocket;

    public ServerX(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void startServer(){

        try {
            System.out.println("\nServer has started\n\nWaiting for Clients...\n");
            System.out.println("[To close the server, type 'close']\n");

            new Thread(() -> {
                Scanner input = new Scanner(System.in);
                String command = input.nextLine();
                if (command.equalsIgnoreCase("close"))
                    System.exit(0);
            }).start();

            while(!serverSocket.isClosed()){


                Socket socket = serverSocket.accept();
                System.out.println("A new client has joined the chat!");
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread tt = new Thread(clientHandler);
                tt.start();

            }
        }
        catch (IOException e) {
            closeServerSocket();
        }
    }

    public void closeServerSocket() {
        try{
            if (serverSocket != null)
                serverSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket1 = new ServerSocket(69);
        ServerX server = new ServerX(serverSocket1);
        server.startServer();
    }
}
