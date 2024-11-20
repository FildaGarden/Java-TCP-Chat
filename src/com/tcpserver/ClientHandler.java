package src.com.tcpserver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final Socket clientSocket;
    private PrintWriter out;
    private String clientName;
    private String ip;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ip = clientSocket.getInetAddress().getHostAddress();

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            clientName = in.readLine();
            System.out.printf("%s(%s) has joined a chat.\n", clientName, ip);
            Server.broadcastMessage(clientName + " has joined the chat.", this);

            String message;
            while ((message = in.readLine()) != null) {

                String formattedMessage = clientName + ": " + message;
                System.out.println(formattedMessage);
                Server.broadcastMessage(formattedMessage, this);

                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
                
            }
        }
        catch (IOException e) {
            System.out.println("[ERROR]: " + e.getMessage());
        } 
        finally {
            try {
                clientSocket.close();
            }   
            catch (IOException e) {
                System.out.println("[ERROR]:" + e.getMessage());
            }

            Server.removeClient(this);
            System.out.printf("%s(%s) has left the chat\n", clientName, ip);
            Server.broadcastMessage(clientName + " has left the chat.", this);
        }
    }
    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }
    
}
