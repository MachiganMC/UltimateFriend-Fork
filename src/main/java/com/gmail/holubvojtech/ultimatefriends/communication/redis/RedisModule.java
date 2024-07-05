package com.gmail.holubvojtech.ultimatefriends.communication.redis;

import com.gmail.holubvojtech.ultimatefriends.Friend;
import com.gmail.holubvojtech.ultimatefriends.Options;
import com.gmail.holubvojtech.ultimatefriends.PlayerProfile;
import com.gmail.holubvojtech.ultimatefriends.UltimateFriends;
import com.gmail.holubvojtech.ultimatefriends.Utils;
import com.gmail.holubvojtech.ultimatefriends.communication.CommunicationModule;
import com.gmail.holubvojtech.ultimatefriends.communication.bungee.BungeeModule;
import com.gmail.holubvojtech.ultimatefriends.exceptions.*;
import com.imaginarycode.minecraft.redisbungee.RedisBungee;
import com.imaginarycode.minecraft.redisbungee.RedisBungeeAPI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.PluginManager;
import org.jetbrains.annotations.Nullable;

public class RedisModule extends BungeeModule implements CommunicationModule {
   private String SPLITTER;

   public void registerListeners() {
      this.SPLITTER = UltimateFriends.getConfig().getCore().redis_msgSplit();
      PluginManager var1 = UltimateFriends.server.getPluginManager();
      var1.registerListener(UltimateFriends.plugin, new RedisJoinListener());
      var1.registerListener(UltimateFriends.plugin, new RedisDisconnectListener());
      var1.registerListener(UltimateFriends.plugin, new RedisServerSwitchListener());
      var1.registerListener(UltimateFriends.plugin, new PubSubListener());
      RedisBungee.getApi().registerPubSubChannels(UltimateFriends.getConfig().getCore().getRedisChannels());
   }

   public boolean isOnline(String var1) {
      if (super.isOnline(var1)) {
         return true;
      } else {
         RedisBungeeAPI var2 = RedisBungee.getApi();
         UUID var3 = var2.getUuidFromName(var1, false);
         return var3 != null && var2.isPlayerOnline(var3);
      }
   }

   @Nullable
   public String getServer(String var1) {
      String var2 = super.getServer(var1);
      if (var2 == null) {
         RedisBungeeAPI var3 = RedisBungee.getApi();
         UUID var4 = var3.getUuidFromName(var1, false);
         if (var4 != null) {
            ServerInfo var5 = var3.getServerFor(var4);
            return var5 != null ? var5.getName() : null;
         } else {
            return null;
         }
      } else {
         return var2;
      }
   }

   public void sendFriendMessage(PlayerProfile var1, String var2, String var3) throws PlayerDenied, PlayerIsOffline, FriendOnDisabledServer, PlayerNotFriend {
      Friend var4 = var1.getFriend(var2);
      if (var4 != null) {
         PlayerProfile var5 = UltimateFriends.getPlayerProfile(var2);
         if (var5 != null && !var5.getOptions().get(Options.Type.ALLOW_PRIVATE_MSG)) {
            throw new PlayerDenied();
         } else {
            RedisBungeeAPI var6 = RedisBungee.getApi();
            UUID var7 = var6.getUuidFromName(var2, false);
            if (var7 == null) {
               throw new PlayerIsOffline();
            } else if (RedisBungee.getApi().isPlayerOnline(var7)) {
               ServerInfo var8 = RedisBungee.getApi().getServerFor(var7);
               if (var8 != null && UltimateFriends.getConfig().getDisable().getPlugin().contains(var8.getName())) {
                  throw new FriendOnDisabledServer();
               } else {
                  var6.sendChannelMessage(UltimateFriends.getConfig().getCore().redis_channel_friendMessage(), var1.getPlayerName() + this.SPLITTER + var2 + this.SPLITTER + var3);
               }
            } else {
               throw new PlayerIsOffline();
            }
         }
      } else {
         throw new PlayerNotFriend();
      }
   }

   public void sendFriendBroadcastMessage(PlayerProfile var1, String var2) {
      ArrayList var3 = new ArrayList();
      this.sendFriendBroadcastMessage0(var1, var2, var3);
      StringBuilder var4 = new StringBuilder();
      Iterator var5 = var3.iterator();

      while(var5.hasNext()) {
         String var6 = (String)var5.next();
         var4.append(var6).append(",");
      }

      RedisBungee.getApi().sendChannelMessage(UltimateFriends.getConfig().getCore().redis_channel_friendBroadcast(), var1.getPlayerName() + this.SPLITTER + var4 + this.SPLITTER + var2);
   }

   public void removeFriend(PlayerProfile var1, Friend var2) throws PlayerNotFriend {
      super.removeFriend(var1, var2);
      if (var2.getProxiedPlayer() == null) {
         RedisBungee.getApi().sendChannelMessage(UltimateFriends.getConfig().getCore().redis_channel_friendRemove(), var1.getPlayerName() + this.SPLITTER + var2.getPlayerName());
      }

   }

   public boolean addFriend(PlayerProfile var1, String var2) throws PlayerIsOffline, FriendOnDisabledServer, PlayerAlreadyFriend, FriendListExceeded, CannotAddYourself, PlayerDenied, PlayerAlreadyRequested {
      try {
         return super.addFriend(var1, var2);
      } catch (PlayerIsOffline var6) {
         RedisBungeeAPI var3 = RedisBungee.getApi();
         UUID var4 = var3.getUuidFromName(var2, false);
         if (var4 == null) {
            throw new PlayerIsOffline();
         } else if (RedisBungee.getApi().isPlayerOnline(var4)) {
            ServerInfo var5 = RedisBungee.getApi().getServerFor(var4);
            if (var5 != null && UltimateFriends.getConfig().getDisable().getPlugin().contains(var5.getName())) {
               throw new FriendOnDisabledServer();
            } else {
               var1.getSentRequests().add(var2);
               var3.sendChannelMessage(UltimateFriends.getConfig().getCore().redis_channel_friendRequest(), var1.getId() + "," + var1.getName() + "," + var1.getUuid() + this.SPLITTER + var2 + this.SPLITTER + "REQUEST");
               return false;
            }
         } else {
            throw new PlayerIsOffline();
         }
      }
   }

   public void connect(PlayerProfile var1, String var2) throws PlayerIsOffline, FriendOnDisabledServer, ConnectionDisabledOnServer, PlayerNotFriend {
      try {
         super.connect(var1, var2);
      } catch (PlayerIsOffline var6) {
         RedisBungeeAPI var3 = RedisBungee.getApi();
         UUID var4 = var3.getUuidFromName(var2, false);
         if (var4 == null) {
            throw new PlayerIsOffline();
         } else if (RedisBungee.getApi().isPlayerOnline(var4)) {
            ServerInfo var5 = RedisBungee.getApi().getServerFor(var4);
            if (var5 != null) {
               if (UltimateFriends.getConfig().getDisable().getPlugin().contains(var5.getName())) {
                  throw new FriendOnDisabledServer();
               } else if (UltimateFriends.getConfig().getDisable().getConnection().contains(var5.getName())) {
                  throw new ConnectionDisabledOnServer();
               } else {
                  Utils.safeConnect(UltimateFriends.server.getPlayer(var1.getPlayerName()), var5);
               }
            } else {
               throw new NullPointerException("server info is null");
            }
         } else {
            throw new PlayerIsOffline();
         }
      }
   }
}
