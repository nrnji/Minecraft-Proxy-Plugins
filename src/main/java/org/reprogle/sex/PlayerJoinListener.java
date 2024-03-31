package org.reprogle.sex;

//import com.velocitypowered.api.event.Subscribe;
//import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
//import net.kyori.adventure.text.Component;
//
//import java.util.UUID;
//
//public class PlayerJoinListener {
//
//    @Subscribe
//    public void onPlayerJoin(PlayerChooseInitialServerEvent event) {
//        // 접속한 플레이어의 닉네임 가져오기
//        String playerName = event.getPlayer().getUsername();
//
//        // 접속한 플레이어의 UUID 가져오기
//        UUID playerUUID = event.getPlayer().getUniqueId();
//
//        // 닉네임과 UUID를 채팅창에 표시
//        event.getPlayer().sendMessage(Component.text("EEEEEEEEEEEEEEEEEEEVENT Welcome " + playerName + "! Your UUID is: " + playerUUID));
//    }
//}

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

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

        executor.schedule(() -> {
            server.getCommandManager().executeAsync(server.getConsoleCommandSource(), "send " + event.getPlayer().getUsername() + " factions");
        }, 3, TimeUnit.SECONDS);  // 3초 후에 실행

        executor.shutdown();
    }
}