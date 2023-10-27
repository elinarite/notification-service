//package steps;
//
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.WebSocket;
//import okhttp3.WebSocketListener;
//
//import java.util.concurrent.TimeUnit;
//public class WebSocketSteps {
//    private static final String WEBSOCKET_ENDPOINT = "wss://wbs.mexc.com/ws";
//    private OkHttpClient client;
//    private WebSocket webSocket;
//
//    public WebSocketSteps() {
//        client = new OkHttpClient.Builder()
//                .readTimeout(0, TimeUnit.MILLISECONDS)
//                .build();
//    }
//
//    public WebSocket connectToWebSocket(WebSocketListener listener) {
//        Request request = new Request.Builder().url(WEBSOCKET_ENDPOINT).build();
//        webSocket = client.newWebSocket(request, listener);
//        return webSocket;
//    }
//
//    public void closeWebSocket(int code, String reason) {
//        if (webSocket != null) {
//            webSocket.close(code, reason);
//        }
//    }
//}