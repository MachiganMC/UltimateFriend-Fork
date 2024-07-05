package com.gmail.holubvojtech.ultimatefriends.communication.redis;

import com.gmail.holubvojtech.ultimatefriends.PlayerProfile;
import com.gmail.holubvojtech.ultimatefriends.UltimateFriends;
import com.gmail.holubvojtech.ultimatefriends.communication.bungee.DisconnectListener;
import com.imaginarycode.minecraft.redisbungee.RedisBungee;
import com.imaginarycode.minecraft.redisbungee.events.PlayerLeftNetworkEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class RedisDisconnectListener extends DisconnectListener implements Listener {
   private long last = 0L;

   @EventHandler
   public void onNetworkLeft(PlayerLeftNetworkEvent var1) {
      final UUID var2 = var1.getUuid();
      if (this.last + 5L <= System.currentTimeMillis()) {
         this.last = System.currentTimeMillis();

         try {
            UltimateFriends.server.getScheduler().runAsync(UltimateFriends.plugin, new Runnable() {
               public void run() {
                  ArrayList var1 = new ArrayList();
                  String var2x = RedisBungee.getApi().getNameFromUuid(var2, false);
                  if (var2x != null) {
                     Iterator var3 = UltimateFriends.getPlayerProfiles().iterator();

                     while(var3.hasNext()) {
                        PlayerProfile var4 = (PlayerProfile)var3.next();
                        if (var4.getFriend(var2x) != null) {
                           var1.add(var4.getName());
                        }
                     }

                     RedisDisconnectListener.this.sendLeaveMsg(var2x, var1);
                  }
               }
            });
         } catch (NoClassDefFoundError var4) {
         }

      }
   }

   @EventHandler
   public void onLeft(PlayerDisconnectEvent var1) {
      super.onLeft(var1);
   }
}
