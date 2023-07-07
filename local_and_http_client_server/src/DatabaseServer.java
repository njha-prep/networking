import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class DatabaseServer {
    private static final int PORT = 8074;
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "12345678";
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server listening on port " + PORT);

            // Establish database connection
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                // Handle client request
                Thread clientThread = new Thread(() -> handleClient(clientSocket, connection));
                clientThread.start();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket, Connection connection) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            // Read client request
            String request = reader.readLine();

            // Process request and generate response
            String response;
            if (request != null) {
                response = processRequest(request, connection);
            } else {
                response = gson.toJson(new ErrorResponse("Invalid request"));
            }

            // Set the Content-Type header to specify JSON format
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: application/json");
            writer.println();

            // Send response back to the client
            writer.println(response);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                System.out.println("Client connection closed");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String processRequest(String request, Connection connection) throws SQLException {
        String[] requestParts = request.split(" ");

        if (requestParts.length < 1) {
            return gson.toJson(new ErrorResponse("Invalid request"));
        }

        String operation = requestParts[1].toUpperCase();
        if (operation.equals("/GET_TABLES")) {
            // GET_TABLES logic
            System.out.println("in herejenj");
            List<String> tables = getTables(connection);
            return gson.toJson(new GetTablesResponse(tables));
        } else if (operation.equals("/CREATE")) {
            // CREATE TABLE logic
            // Implement your code to create a table in the database
            return gson.toJson(new SuccessResponse("Table created"));
        } else if (operation.equals("/READ")) {
            // READ logic
            // Implement your code to read data from the table in the database
            return gson.toJson(new SuccessResponse("Read data from table"));
        } else if (operation.equals("/UPDATE")) {
            // UPDATE logic
            // Implement your code to update the table in the database
            return gson.toJson(new SuccessResponse("Table updated"));
        } else if (operation.equals("/REPLACE")) {
            // REPLACE logic
            // Implement your code to replace the table in the database
            return gson.toJson(new SuccessResponse("Table replaced"));
        } else if (operation.equals("/DELETE")) {
            // DELETE logic
            // Implement your code to delete the table from the database
            return gson.toJson(new SuccessResponse("Table deleted"));
        } else {
            return gson.toJson(new ErrorResponse("Unsupported operation"));
        }
    }

    private static List<String> getTables(Connection connection) throws SQLException {
        List<String> tables = new ArrayList<>();
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, null, new String[] { "TABLE" });

        while (resultSet.next()) {
            String tableName = resultSet.getString("TABLE_NAME");
            tables.add(tableName);
        }

        resultSet.close();
        return tables;
    }

    private static class SuccessResponse {
        private String message;

        public SuccessResponse(String message) {
            this.message = message;
        }
    }

    private static class GetTablesResponse {
        private List<String> tables;

        public GetTablesResponse(List<String> tables) {
            this.tables = tables;
        }
    }

    private static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }
    }
}
