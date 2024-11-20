package src.com.tcpserver;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static final int PORT = 9090;
    public static final String IP_ADDRESS = "192.168.0.111";

    private static final List<ClientHandler> clientList = new ArrayList<>();
        public static void main(String[] args) throws IOException {
            try (ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName(IP_ADDRESS))) {
                System.out.printf("Server is running on port %d\n", PORT);

                while (true) {
                    Socket clientSocket = serverSocket.accept(); 
                    System.out.printf("New client connected: %s\n", clientSocket.getInetAddress().getHostAddress());

                    ClientHandler client = new ClientHandler(clientSocket);
                    synchronized (clientList) {
                        clientList.add(client);
                    }
                    client.start();
                }
            }
            
            
    }

    public static void broadcastMessage(String message, ClientHandler sender) {
        synchronized (clientList) {
            for(ClientHandler client : clientList) {
                if(client != sender) {
                    client.sendMessage(message);
                }
            }
        }
    }

    public static void removeClient(ClientHandler client) {
        synchronized (clientList) {
            clientList.remove(client);
        }
    }
}
