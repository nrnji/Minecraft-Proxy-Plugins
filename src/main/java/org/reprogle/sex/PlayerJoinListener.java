package org.reprogle.sex;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import db.connect.JDBC.DBConnect;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PlayerJoinListener {

    private final ProxyServer server;

    public PlayerJoinListener(ProxyServer server) {
        this.server = server;
    }

    @Subscribe
    public void onPlayerJoin(ServerPostConnectEvent event) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        DBConnect dbc = new DBConnect();
        String sendServer = dbc.Query(event.getPlayer().getUsername());

        if (sendServer == null)
            return;

        executor.schedule(() -> {
            server.getCommandManager().executeAsync(server.getConsoleCommandSource(), "send " + event.getPlayer().getUsername() + " " + sendServer);
        }, 5, TimeUnit.SECONDS);  // 5초 후에 실행

        executor.shutdown();
    }

}