package steps;

import okhttp3.WebSocket;

public class SubscriptionSteps {
    private static final String SUBSCRIPTION_MESSAGE = "{\"method\":\"SUBSCRIPTION\", \"params\":[\"spot@public.deals.v3.api@BTCUSDT\"]}";
    public void sendSubscription(WebSocket webSocket) {
        webSocket.send(SUBSCRIPTION_MESSAGE);
    }
}