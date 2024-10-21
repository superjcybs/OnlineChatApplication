package online.chat.application;

/**
 *
 * @author jerry babatunde
 */
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ChatClient(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Start threads for message sending and receiving
            new Thread(new MessageReceiver()).start();
            sendMessage();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to send messages from the client to the server
    private void sendMessage() {
        Scanner scanner = new Scanner(System.in);
        String message;
        while (true) {
            message = scanner.nextLine();
            out.println(message);
        }
    }

    // Thread class to handle incoming messages
    private class MessageReceiver implements Runnable {
        @Override
        public void run() {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new ChatClient("localhost", 12345);
    }
}
