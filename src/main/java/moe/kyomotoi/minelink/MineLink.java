package moe.kyomotoi.minelink;

import moe.kyomotoi.minelink.client.Server;
import moe.kyomotoi.minelink.commands.Manege;
import moe.kyomotoi.minelink.commands.Player;
import moe.kyomotoi.minelink.utils.ConfigDealer;
import moe.kyomotoi.minelink.utils.ErrorDealer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public final class MineLink extends Plugin {

    public static boolean isEnabledSend = true;
    public static boolean isEnabledAcceptMsg = true;
    public static List<String> disabledPlayer = new ArrayList<>();

    public static MineLink getInstance() {
        return ((MineLink) ProxyServer.getInstance().getPluginManager().getPlugin("MineLink"));
    }

    @Override
    public void onEnable() {
        getLogger().info("Initialize...");
        getLogger().info("Project: https://github.com/Kyomotoi/MineLink");

        loadConfig();
        registerCommands();

        try {
            Server.main();
        } catch (Exception err) {
            getLogger().warning("Failed to running HTTP server.");
            getLogger().warning("Received will be disable.");
            isEnabledAcceptMsg = false;
            ErrorDealer.paste(err);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Thanks for using!");
        getLogger().info("Bye nya~~~");
    }

    private void loadConfig() {
        getLogger().info("Dealing config...");

        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            try {
                InputStream is = getResourceAsStream("config.yml");
                Files.copy(is, file.toPath());
            } catch (Exception err) {
                getLogger().warning("Failed to get Resource from config.yml");
                ErrorDealer.paste(err);
            }
        }

        Configuration config = null;
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class)
                    .load(new InputStreamReader(new FileInputStream(file), "utf-8"));
        } catch (Exception err) {
            getLogger().warning("Failed to read config.yml");
            ErrorDealer.paste(err);
        }

        new ConfigDealer(config);
        getLogger().info("SUCCESS!");
    }

    private void registerCommands() {
        String manegeCmd = ConfigDealer.getString("commands.manege");
        String playerCmd = ConfigDealer.getString("commands.player");

        this.getProxy().getPluginManager().registerCommand(this, new Manege(manegeCmd, "ml.admin"));
        this.getProxy().getPluginManager().registerCommand(this, new Player(playerCmd, "ml.player"));
    }
}
