package com.gmail.holubvojtech.ultimatefriends.communication.redis;

import com.gmail.holubvojtech.ultimatefriends.PlayerProfile;
import com.gmail.holubvojtech.ultimatefriends.UltimateFriends;
import com.gmail.holubvojtech.ultimatefriends.communication.bungee.ServerSwitchListener;
import com.imaginarycode.minecraft.redisbungee.RedisBungee;
import com.imaginarycode.minecraft.redisbungee.events.PlayerChangedServerNetworkEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class RedisServerSwitchListener extends ServerSwitchListener implements Listener {
   private long last = 0L;

   @EventHandler
   public void onNetworkSwitch(PlayerChangedServerNetworkEvent var1) {
      final UUID var2 = var1.getUuid();
      final String var3 = var1.getPreviousServer();
      final String var4 = var1.getServer();
      if (this.last + 5L <= System.currentTimeMillis()) {
         this.last = System.currentTimeMillis();
         if (var3 != null && var4 != null && !var3.isEmpty() && !var4.isEmpty()) {
            UltimateFriends.server.getScheduler().runAsync(UltimateFriends.plugin, () -> {
               List<String> var11 = new ArrayList<>();
               String var2x = RedisBungee.getApi().getNameFromUuid(var2, false);
               if (var2x != null) {

                   for (PlayerProfile var4x : UltimateFriends.getPlayerProfiles()) {
                       if (var4x.getFriend(var2x) != null) {
                           var11.add(var4x.getName());
                       }
                   }

                  RedisServerSwitchListener.this.sendSwitchMsg(var2x, var3, var4, var11);
               }
            });
         }
      }
   }

   @EventHandler
   public void onServerConnected(ServerConnectedEvent var1) {
      super.onServerConnected(var1);
   }

   @EventHandler
   public void onLeft(PlayerDisconnectEvent var1) {
      super.onLeft(var1);
   }
}
