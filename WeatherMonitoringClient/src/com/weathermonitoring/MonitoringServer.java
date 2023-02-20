package com.weathermonitoring;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.InetSocketAddress;

public class MonitoringServer {
    public static void main(String[] args) throws Exception {
        // Create a new HTTP server
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);

        // Set up a handler for incoming requests
        server.createContext("/api/data", new MyHandler());

        // Start the server
        server.start();
    }

    private static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Get the request method
            String requestMethod = exchange.getRequestMethod();

            // Handle "POST" requests
            if ("POST".equals(requestMethod)) {
                // Get the request body
                InputStream requestBody = exchange.getRequestBody();
                BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody, "UTF-8"));

                // Read the request body into a string
                StringBuilder requestBodyBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    requestBodyBuilder.append(line);
                }
                String requestBodyString = requestBodyBuilder.toString();

                // Do something with the request body
                // Send a response to the client
                String responseBody = "Data has been successfully added to the database.";
                exchange.sendResponseHeaders(200, responseBody.length());
                OutputStream responseBodyStream = exchange.getResponseBody();
                responseBodyStream.write(responseBody.getBytes("UTF-8"));
                responseBodyStream.close();
            }
        }
    }
}
