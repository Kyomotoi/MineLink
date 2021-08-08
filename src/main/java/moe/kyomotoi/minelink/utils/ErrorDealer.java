package moe.kyomotoi.minelink.utils;

import net.md_5.bungee.api.plugin.PluginLogger;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

public class ErrorDealer {

    public static String getErrorMessage(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw, true));
        return sw.getBuffer().toString();
    }

    public static void paste(Exception err) {
        Logger log = PluginLogger.getLogger("MineLink|ErrorDealer");

        String url = "https://paste.ubuntu.com/";

        RequestBody body = new FormBody.Builder()
                .add("poster", "MineLink error track back")
                .add("syntax", "text")
                .add("expiration", "week")
                .add("content", getErrorMessage(err))
                .build();

        OkHttpClient client = new okhttp3.OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                log.warning("Failed to paste ERROR, here is original error info:");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resURL = response.request().url().toString();
                log.warning("A error has been coming! Track: " + resURL);
                response.body().close();
            }
        });
    }
}
