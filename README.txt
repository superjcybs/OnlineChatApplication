Simple Java Chat Application

How to Run:

1. Compile the server and client classes:
   ```
   javac ChatServer.java ChatClient.java
   ```

2. Run the server:
   ```
   java ChatServer
   ```

3. In separate terminals, run the client(s):
   ```
   java ChatClient
   ```

Clients can send and receive messages in real-time. Each message is broadcasted to all connected clients.

Implementation Details:
- **ChatServer**: Manages connections, broadcasts messages, and assigns unique user IDs.
- **ChatClient**: Connects to the server, sends messages, and listens for broadcasts.
- **Socket Programming**: Used for managing communication between server and clients.