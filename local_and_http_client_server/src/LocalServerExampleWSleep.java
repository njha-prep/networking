import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class LocalServerExampleWSleep {
    public static void main(String[] args) {
        try {
            // Create a ServerSocket and bind it to a specific port
            int port = 8086; // Choose any available port
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server listening on port " + port);

            // Listen for incoming client connections
            while (true) {
                // Accept a client connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                //If a client is connected to a server and the server is processing current clients request, what happens when another client tries to connect to the same server
                //When another client tries to connect to a server that is already processing requests from existing clients, the following scenarios can occur:
                //
                //1. Server Limitations: The server may have a limit on the maximum number of simultaneous connections it can handle. If the server has reached its limit, the new client connection request may be rejected or put in a queue until a slot becomes available.
                //
                //2. Connection Queueing: If the server supports a queuing mechanism, the new client connection request can be queued until the server finishes processing requests from the existing clients. The server will process the requests in the order they were received, including both the existing clients and the queued clients.
                //
                //3. Concurrent Processing: If the server is designed to handle concurrent connections using techniques such as multithreading or asynchronous processing, it can accept the new client connection and create a separate thread or process to handle the new client's request concurrently with the existing clients. This allows multiple clients to be serviced simultaneously.
                //
                //The behavior depends on the server's implementation, configuration, and available resources. The server may prioritize certain clients or apply specific rules based on factors such as client authentication, load balancing, or resource allocation.
                //
                //It's important to note that the scalability and handling of concurrent connections can vary based on the server's capabilities, hardware resources, network bandwidth, and the specific architecture and design of the server application.

                //sleep for sometime
                for(long i =1; i<1000000000; i++) for(long j =1; j<100; j++);

                System.out.println("Wokeup from sleep");

                //During the sleep period, the server is not responsive to any other client connections. If another client attempts to connect while the server is sleeping, the new client connection request will be queued by the operating system until the server finishes sleeping and resumes accepting connections.

                //The queue mentioned above, where incoming client connection requests are held until they can be processed by the server, is managed by the operating system (OS) of the server machine.
                //
                //When a client connection request is received while the server is busy or unavailable, the OS places the request in a queue known as the "backlog queue" or "pending connections queue." This queue is maintained by the OS and is separate from the server application's handling of client connections.
                //
                //The length of the backlog queue can be specified when creating the server's `ServerSocket` object. For example, in the code provided, the queue length is not explicitly set, so it uses the default value provided by the OS. The specific default value may vary depending on the operating system.
                //
                //The OS manages the backlog queue by accepting client connection requests on behalf of the server application and placing them in the queue until the server is available to process them. The OS typically uses a first-in, first-out (FIFO) policy to manage the order of pending connections.
                //
                //Once the server is ready to handle a new client connection, it retrieves a pending connection request from the backlog queue. This process is typically handled by the OS when the server calls the `accept()` method on the `ServerSocket` object. The server then proceeds to establish a connection with the client and handle the communication.
                //
                //It's important to note that the size of the backlog queue is limited by the operating system, and if the queue becomes full, new client connection requests may be rejected or experience delays until space becomes available in the queue.

                // Handle the client connection in a separate thread
                handleClient(clientSocket);
               // clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try {
            // Get the input and output streams from the client socket
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();

            // Read client request
            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);
            String request = new String(buffer, 0, bytesRead);
            System.out.println("Received request from client: " + request);

            // Prepare and send the response to the client
            String response = "Hello from the server!";
            outputStream.write(response.getBytes());

            // Close the client socket
            clientSocket.close();
            System.out.println("Client connection closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
