package org.reprogle.sex;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.google.inject.Inject;
import lombok.Getter;
import org.slf4j.Logger;

@Plugin(
        id = "sex",
        name = "Sex",
        version = "1.0.0"
)
public class Main {

    @Getter
    private final ProxyServer server;

    @Getter
    private final Logger logger;

    @Inject
    public Main(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;

        logger.info("Lobby plugin has initialized!");
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // 이벤트 리스너 등록
        server.getEventManager().register(this, new PlayerJoinListener(server));
        server.getCommandManager().register("mycommand", new MyCommand());
    }
}