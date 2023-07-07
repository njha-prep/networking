import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class HttpClient{
    public static void main(String[] args) throws IOException {
        // This takes time to get response from server due to the way exchange is authored in Http Server to write with bytes,
        // if checked with postman, it is fast
        String serverAddress = "localhost"; // Specify the server address
        int serverPort = 8095; // Specify the server port

        try (Socket socket = new Socket(serverAddress, serverPort);
             OutputStream outputStream = socket.getOutputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Send the HTTP request
            String request = "GET / HTTP/1.1\r\n" +
                    "Host: " + serverAddress + "\r\n" +
                    "\r\n";
            outputStream.write(request.getBytes());

            // Read the HTTP response
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
