package org.reprogle.sex;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

public class MyCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        if (invocation.source() instanceof Player) {
            Player player = (Player) invocation.source();

            // 서버에 메시지 보내기
            player.sendMessage(Component.text("You executed a command!"));
        }
    }
}