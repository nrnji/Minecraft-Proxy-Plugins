package org.reprogle.sex;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import db.connect.JDBC.DBConnect;
import porie.console.log.LogMessage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PlayerJoinListener {

    private final ProxyServer server;  // 프록시 서버
    private final DBConnect dbc;  // 데이터베이스 연결
    private final ScheduledExecutorService executor;  // 스케줄링을 위한 실행자 서비스

    public PlayerJoinListener(ProxyServer server) {
        this.server = server;
        this.dbc = new DBConnect();  // DB 연결 객체 생성
        this.dbc.dbConnection();  // DB 연결
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    @Subscribe
    public void onPlayerJoin(ServerPostConnectEvent event) {
        String username = event.getPlayer().getUsername();  // 사용자 이름 가져오기

        String returnValue = dbc.getCheckGLEE(username);  // GLEE 체크
        if (returnValue != null) {
            LogMessage.logMessage(returnValue);
            scheduleCommand(returnValue);
            return;
        }

        String sendServer = dbc.Query(username); // 일반 쿼리 실행
        if (sendServer != null) {
            LogMessage.logMessage("send " + username + " " + sendServer);
            scheduleCommand("send " + username + " " + sendServer);

        }
    }

    private void scheduleCommand(String command) {
        executor.schedule(() -> {
            server.getCommandManager().executeAsync(server.getConsoleCommandSource(), command);
        }, 5, TimeUnit.SECONDS);  // 5초 후에 실행
    }

    public void shutdown() {
        executor.shutdown();
    }
}