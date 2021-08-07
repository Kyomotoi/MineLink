package moe.kyomotoi.minelink.client;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import moe.kyomotoi.minelink.utils.ConfigDealer;
import moe.kyomotoi.minelink.MineLink;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.PluginLogger;

import java.util.List;
import java.util.logging.Logger;

public class Server {

    private static String res_token;
    private static String res_sender;
    private static String res_message;

    public static void main() throws Exception {
        Logger log = PluginLogger.getLogger("MineLink|Server");

        Boolean isEnabled = MineLink.isEnabledAcceptMsg;
        if (!isEnabled) {
            log.info("Server received GET, but the func has been disable.");
            return;
        }

        List<String> disabledPlayers = MineLink.disabledPlayer;

        String host = ConfigDealer.getString("server.host");
        Integer port = ConfigDealer.getInt("server.port");
        String route = ConfigDealer.getString("server.route");
        String token = ConfigDealer.getString("middleware.access-token");
        String prefix = ConfigDealer.getString("message.prefix");
        String suffix = ConfigDealer.getString("message.suffix");

        log.info("Http server will running at: "+host+":"+port);

        Vertx vt = Vertx.vertx();
        HttpServer httpServer = vt.createHttpServer();

        Router router = Router.router(vt);
        router.route().handler(BodyHandler.create());
        router.get(route).handler(
                routingContext -> {
                    try {
                        res_token = routingContext.queryParam("token").get(0);
                    } catch (Exception err) {
                        log.warning("Sender request params incorrect, should be: token");
                        routingContext.response().end();
                        return;
                    }

                    if (!res_token.equalsIgnoreCase(token)) {
                        log.info("Server received GET, but the token inconsistent.");
                        routingContext.response().end();
                    } else {
                        try {
                            res_sender = routingContext.queryParam("sender").get(0);
                        } catch (Exception err) {
                            log.warning("Sender request params incorrect, should be: sender");
                            routingContext.response().end();
                            return;
                        }

                        try {
                            res_message = routingContext.queryParam("message").get(0);
                        } catch (Exception err) {
                            log.warning("Sender request params incorrect, should be: message");
                            routingContext.response().end();
                            return;
                        }

                        log.info("Received GET[ "+res_sender+": "+res_message+" ]");

                        String repo = prefix + res_sender + suffix + res_message;

                        for (ProxiedPlayer player: MineLink.getInstance().getProxy().getPlayers()) {
                            for (String disabledPlayer: disabledPlayers) {
                                if (!disabledPlayer.contains(player.getName())) {
                                    player.sendMessage(repo);
                                }
                            }
                        }

                        routingContext.response().end();
                    }
                }
        );

        httpServer.requestHandler(router);
        httpServer.listen(port, host);
    }
}
