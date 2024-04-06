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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.okhttp.*;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.ProxyServer;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PlayerJoinListener {

    private static final String BASE_URL = "https://your-nocodb-instance.com/api/v1/db/data/v1/";
    private static final String TABLE_NAME = "PLer";
    private static final String API_TOKEN = "YOUR_API_TOKEN";

    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

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

    private static void getData(String plerId) {
        Request request = new Request.Builder()
                .url(BASE_URL + TABLE_NAME + "?where=(PLer_id,eq," + plerId + ")")
                .addHeader("xc-token", API_TOKEN)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                System.out.println("요청 실패: " + e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JsonArray data = gson.fromJson(responseBody, JsonArray.class);
                    if (data.size() > 0) {
                        JsonObject item = data.get(0).getAsJsonObject();
                        System.out.println("PLer_id: " + item.get("PLer_id").getAsString());
                    } else {
                        System.out.println("데이터를 찾을 수 없습니다.");
                    }
                } else {
                    System.out.println("요청 실패: " + response.code());
                }
            }
        });
    }

    private static void executeCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            System.out.println("명령어 실행 실패: " + e.getMessage());
        }
    }

}