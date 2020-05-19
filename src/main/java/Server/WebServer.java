package Server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.concurrent.Executors;

public class WebServer {
    private static final String STATUS_ENDPOINT = "/status";
    private static final String TASK_ENDPOINT = "/task";
    private final int port;
    private HttpServer httpServer;

    public static void main(String[] args) {
        int port = 8080;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        }
        WebServer webServer = new WebServer(port);
        webServer.startServer();
        System.out.println("Server is listening on port " + port);
    }

    public WebServer(int port) {
        this.port = port;
    }

    public void startServer() {
        try {
            this.httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        HttpContext status = httpServer.createContext(STATUS_ENDPOINT);
        HttpContext task = httpServer.createContext(TASK_ENDPOINT);

        status.setHandler(this::handleStatusRequest);
        task.setHandler(this::handleTaskRequest);

        httpServer.setExecutor(Executors.newFixedThreadPool(4));
        httpServer.start();
    }

    HttpContext status = httpServer.createContext(STATUS_ENDPOINT);
    HttpContext task = httpServer.createContext(TASK_ENDPOINT);


    private void handleStatusRequest(HttpExchange httpExchange) throws IOException {
        if (!httpExchange.getRequestMethod().equalsIgnoreCase("get")) {
            httpExchange.close();
            return;
        }
        String responseMessage = "Server is alive";
        sendResponse(responseMessage.getBytes(), httpExchange);
    }

    private void handleTaskRequest(HttpExchange httpExchange) throws IOException {
        if (!httpExchange.getRequestMethod().equalsIgnoreCase("post")) {
            httpExchange.close();
            return;
        }
        Headers headers = httpExchange.getRequestHeaders();
        if (headers.containsKey("X-Test") && headers.get("X-Test").get(0).equalsIgnoreCase("true")) {
            String dummyResponse = "123\n";
            sendResponse(dummyResponse.getBytes(), httpExchange);
        }
        boolean isDebugMode = false;
        if (headers.containsKey("X-Debug") && headers.get("X-Debug").get(0).equalsIgnoreCase("true")) {
            isDebugMode = true;
        }
        long startTime = System.nanoTime();

        byte[] requestBytes = httpExchange.getRequestBody().readAllBytes();
        byte[] responseBytes = calculateResponse(requestBytes);

        long finishTime = System.nanoTime();

        if (isDebugMode) {
            String debugMessage = String.format("Operation took %d ns\n", finishTime - startTime);
            httpExchange.getResponseHeaders().put("X-Debug-Info", Arrays.asList(debugMessage));
        }

        sendResponse(responseBytes, httpExchange);
    }

    private byte[] calculateResponse(byte[] requestBytes) {
        String bodyString = new String(requestBytes);
        String[] stringNumbers = bodyString.split(",");

        BigInteger result = BigInteger.ONE;

        for (String number : stringNumbers) {
            BigInteger bigInteger = new BigInteger(number);
            result = result.multiply(bigInteger);
        }

        return String.format("Result of the multiplication is %s\n", result).getBytes();
    }

    private void sendResponse(byte[] responseBytes, HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, responseBytes.length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(responseBytes);
        outputStream.flush();
        outputStream.close();
    }
}

