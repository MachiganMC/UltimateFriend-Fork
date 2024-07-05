package com.gmail.holubvojtech.ultimatefriends.communication.redis;

import com.gmail.holubvojtech.ultimatefriends.PlayerProfile;
import com.gmail.holubvojtech.ultimatefriends.UltimateFriends;
import com.gmail.holubvojtech.ultimatefriends.communication.bungee.JoinListener;
import com.imaginarycode.minecraft.redisbungee.RedisBungee;
import com.imaginarycode.minecraft.redisbungee.events.PlayerJoinedNetworkEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class RedisJoinListener extends JoinListener implements Listener {
   private long last = 0L;

   @EventHandler
   public void onNetworkJoin(PlayerJoinedNetworkEvent var1) {
      final UUID var2 = var1.getUuid();
      if (this.last + 5L <= System.currentTimeMillis()) {
         this.last = System.currentTimeMillis();
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

                  RedisJoinListener.this.sendJoinMsg(var2x, var1);
               }
            }
         });
      }
   }

   @EventHandler
   public void onJoin(PostLoginEvent var1) {
      super.onJoin(var1);
   }
}
