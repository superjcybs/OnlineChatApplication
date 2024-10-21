package online.chat.application;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static int clientId = 0;
    private static Set<ClientHandler> clientHandlers = new HashSet<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Chat server started. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientId++;
                ClientHandler clientHandler = new ClientHandler(clientSocket, clientId);
                clientHandlers.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Broadcast message to all clients
    public static void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler != sender) {
                clientHandler.sendMessage(message);
            }
        }
    }

    // Remove client from the handler list
    public static void removeClient(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private int clientId;

    public ClientHandler(Socket socket, int clientId) {
        this.socket = socket;
        this.clientId = clientId;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Greet the client
            out.println("Welcome! You are user " + clientId);

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("User " + clientId + ": " + message);
                ChatServer.broadcastMessage("User " + clientId + ": " + message, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    // Send message to the client
    public void sendMessage(String message) {
        out.println(message);
    }

    // Close the connection
    private void closeConnection() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ChatServer.removeClient(this);
    }
}
