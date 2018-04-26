package com.alex;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) {
        try {
            // open websocket
            final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(new URI("wss://www.bitmex.com/realtime"));

            // add listener
            clientEndPoint.addMessageHandler(System.out::println);

            // send message to websocket
            clientEndPoint.sendMessage("{\"op\": \"subscribe\", \"args\": [\"trade:XBTUSD\"]}");

            // wait 5 seconds for messages from websocket
            Thread.sleep(50000);
            clientEndPoint.userSession.close();

        } catch (InterruptedException ex) {
            System.err.println("InterruptedException exception: " + ex.getMessage());
        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}