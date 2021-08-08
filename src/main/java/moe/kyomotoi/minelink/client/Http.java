package moe.kyomotoi.minelink.client;

import moe.kyomotoi.minelink.utils.ConfigDealer;
import moe.kyomotoi.minelink.MineLink;
import moe.kyomotoi.minelink.utils.ErrorDealer;
import net.md_5.bungee.api.plugin.PluginLogger;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.logging.Logger;

public class Http {

    public static void sendMessage(String sender, String message) throws IOException {
        Logger log = PluginLogger.getLogger("MineLink|HTTP");

        boolean isEnabled = MineLink.isEnabledSend;
        if (!isEnabled) {
            log.warning("The HTTP sending service has been disable.");
            return;
        }

        String host = ConfigDealer.getString("http.host");
        String port = ConfigDealer.getString("http.port");
        String route = ConfigDealer.getString("http.route");
        String token = ConfigDealer.getString("middleware.access-token");

        String params = "?token=" + token + "&sender=" + sender + "&message" + message;
        String url = "http://" + host + ":" + port + route + params;

        OkHttpClient client = new okhttp3.OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                log.warning("Failed to request, please check it.");
                ErrorDealer.paste(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                log.info("Sending message success! context: " + sender + ":" + message);
                response.body().close();
            }
        });
    }
}
