package vn.techres.order.online.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.client.util.Strings;

import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.OkHttpClient;

@Component
public class SocketClientSingleton {

	private static Socket socket;

	private static String socketUrl;

	private SocketClientSingleton() {
	}

	public static synchronized Socket getInstance() {
		if (!Strings.isNullOrEmpty(socketUrl) && (socket == null || !socket.connected())) {
			try {

				final Map<String, String> token = new HashMap<>();
				token.put("token", "java_api");

				OkHttpClient okHttpClient = new OkHttpClient.Builder()
						.readTimeout(5, TimeUnit.MINUTES) // important for HTTP long-polling
						.pingInterval(30, TimeUnit.MINUTES)
						.build();

				IO.Options options = IO.Options.builder()
						.setReconnection(true)
						.setAuth(token)
						.build();

				options.callFactory = okHttpClient;

				System.out.println(options.auth);
				System.out.println(socketUrl);

				socket = IO.socket(socketUrl, options);
				socket.connect();
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				socket = null;
			}
		}
		// System.out.println(socketUrl);
		return socket;
	}

	public static void setRealtimeUrl(String url) {
		if (Strings.isNullOrEmpty(socketUrl) || socket == null || !socket.connected()) {
			socketUrl = url;
		}

	}

}