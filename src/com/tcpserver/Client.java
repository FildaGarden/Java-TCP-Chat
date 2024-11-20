package src.com.tcpserver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket(Server.IP_ADDRESS, Server.PORT);
        System.out.println("Connection established..\n");

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);
            
        System.out.println("Enter your nickname: ");
        String name = scanner.nextLine();
        out.println(name);

        new Thread(() -> {
            try {
                String serverMessage;
                while ((serverMessage = in.readLine()) != null) {
                    synchronized(System.out){
                        System.out.println("\r" + serverMessage);
                        System.out.printf("%s: ", name);
                    }
                    }
            } catch (IOException e) {
                System.err.println("[ERROR]: " + e.getMessage());
            }
        }).start();

        String message;
        while(true) {
            System.out.printf("%s: ", name);
            message = scanner.nextLine();
            out.println(message);

            if(message.equalsIgnoreCase("exit")) {
                System.out.println("Disconnected\n");
                break;
            }
            
        }
        scanner.close();
        socket.close();
        out.close();
        in.close();

        
    }
}