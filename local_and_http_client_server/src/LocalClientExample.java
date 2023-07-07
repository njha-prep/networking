import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class LocalClientExample {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // Specify the server address
        int serverPort = 8086; // Specify the server port

        try {
            // Create a socket and connect to the server
            Socket socket = new Socket(serverAddress, serverPort);
            System.out.println("Connected to server: " + socket.getInetAddress().getHostAddress());

            // Get the input and output streams from the socket
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            // Send a message to the server
            String message = "Hello from the client!";
            outputStream.write(message.getBytes());
            outputStream.flush();
            System.out.println("Sent message to server: " + message);

            // Receive the server's response
            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);
            String response = new String(buffer, 0, bytesRead);
            System.out.println("Received response from server: " + response);

            // Close the socket
            socket.close();
            System.out.println("Connection closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
