package com.gmail.holubvojtech.ultimatefriends.communication.redis;

import com.gmail.holubvojtech.ultimatefriends.ClickableMessage;
import com.gmail.holubvojtech.ultimatefriends.Friend;
import com.gmail.holubvojtech.ultimatefriends.Message;
import com.gmail.holubvojtech.ultimatefriends.Options;
import com.gmail.holubvojtech.ultimatefriends.PlayerID;
import com.gmail.holubvojtech.ultimatefriends.PlayerProfile;
import com.gmail.holubvojtech.ultimatefriends.SocialSpy;
import com.gmail.holubvojtech.ultimatefriends.UltimateFriends;
import com.gmail.holubvojtech.ultimatefriends.Utils;
import com.imaginarycode.minecraft.redisbungee.RedisBungee;
import com.imaginarycode.minecraft.redisbungee.events.PubSubMessageEvent;
import java.util.UUID;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PubSubListener implements Listener {
   private final String SPLITTER = UltimateFriends.getConfig().getCore().redis_msgSplit();

   @EventHandler
   public void onPubSub(PubSubMessageEvent var1) {
      String var2 = var1.getChannel();
      String var3 = var1.getMessage();
      final String[] var4;
      if (var2.equals(UltimateFriends.getConfig().getCore().redis_channel_friendRequest())) {
         var4 = var3.split(this.SPLITTER);
         final String[] var9 = var4[0].split(",");
         final PlayerProfile var10 = UltimateFriends.getPlayerProfile(var4[1]);
         if (var10 != null) {
            UltimateFriends.server.getScheduler().runAsync(UltimateFriends.plugin, new Runnable() {
               public void run() {
                  PubSubListener.this.onFriendRequest(new PlayerID(Integer.parseInt(var9[0]), var9[1], UUID.fromString(var9[2])), var10, var4[2]);
               }
            });
         }
      } else if (var2.equals(UltimateFriends.getConfig().getCore().redis_channel_friendRemove())) {
         var4 = var3.split(this.SPLITTER);
         this.onFriendRemove(var4[0], var4[1]);
      } else {
         String var5;
         String var6;
         String var7;
         int var8;
         if (var2.equals(UltimateFriends.getConfig().getCore().redis_channel_friendMessage())) {
            var4 = var3.split(this.SPLITTER);
            var5 = var4[0];
            var6 = var4[1];
            var7 = var4[2];

            for(var8 = 3; var8 < var4.length; ++var8) {
               var7 = var7 + this.SPLITTER + var4[var8];
            }

            this.onFriendMessage(var5, var6, var7);
         } else {
            if (var2.equals(UltimateFriends.getConfig().getCore().redis_channel_friendBroadcast())) {
               var4 = var3.split(this.SPLITTER);
               var5 = var4[0];
               var6 = var4[1];
               var7 = var4[2];

               for(var8 = 3; var8 < var4.length; ++var8) {
                  var7 = var7 + this.SPLITTER + var4[var8];
               }

               this.onFriendBroadcast(var5, var6.split(","), var7);
            }

         }
      }
   }

   private void onFriendRequest(PlayerID var1, PlayerProfile var2, String var3) {
      ProxiedPlayer var4;
      if (var3.equals("REQUEST")) {
         var4 = var2.getProxiedPlayer();
         if (var4 != null) {
            if (!var2.getOptions().get(Options.Type.ALLOW_REQUESTS)) {
               return;
            }

            if (Utils.containsIgnoreCase(var2.getRequests(), var1.getName())) {
               return;
            }

            if (var2.getFriends().size() >= UltimateFriends.getConfig().getMaxFriends(var4)) {
               return;
            }

            if (Utils.containsIgnoreCase(var2.getSentRequests(), var1.getName())) {
               var2.getRequests().remove(var1.getName());
               var2.getSentRequests().remove(var1.getName());
               var2.getFriends().add(new Friend(var1));
               if (!UltimateFriends.getStorage().addFriend(var2, var1)) {
                  throw new RuntimeException("cannot save new friendship");
               }

               RedisBungee.getApi().sendChannelMessage(UltimateFriends.getConfig().getCore().redis_channel_friendRequest(), var2.getId() + "," + var2.getName() + "," + var2.getUuid() + this.SPLITTER + var1.getName() + this.SPLITTER + "ACCEPT");
               var4.sendMessage((new ClickableMessage(Message.FRIEND_ADDED.getMsg(true))).clickable(var1.getName()).append().build());
            } else {
               var2.getRequests().add(var1.getName());
               var4.sendMessage((new ClickableMessage(Message.FRIEND_REQUEST.getMsg(true))).clickable(var1.getName()).append().clickable(Message.FRIEND_REQUEST_BUTTON_ACCEPT_TEXT.getMsg()).hoverEvent(Action.SHOW_TEXT, Message.FRIEND_REQUEST_BUTTON_ACCEPT_HOVER.getMsg()).clickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/" + UltimateFriends.getConfig().getCmds().getCmd() + " add " + var1.getName()).append().build());
            }
         }
      } else {
         var2.getRequests().remove(var1.getName());
         var2.getSentRequests().remove(var1.getName());
         var2.getFriends().add(new Friend(var1));
         var4 = var2.getProxiedPlayer();
         if (var4 != null) {
            var4.sendMessage((new ClickableMessage(Message.FRIEND_ADDED.getMsg(true))).clickable(var1.getName()).append().build());
         }
      }

   }

   private void onFriendRemove(String var1, String var2) {
      ProxiedPlayer var3 = UltimateFriends.server.getPlayer(var2);
      if (var3 != null) {
         PlayerProfile var4 = UltimateFriends.getPlayerProfile(var3.getName());
         if (var4 != null) {
            Friend var5 = var4.getFriend(var1);
            if (var5 != null) {
               var4.getFriends().remove(var5);
               var3.sendMessage((new ClickableMessage(Message.FRIEND_REMOVED_YOU.getMsg(true))).clickable(var1).append().build());
            }
         }
      }

   }

   private void onFriendMessage(String var1, String var2, String var3) {
      if (!var1.equals(var2)) {
         ProxiedPlayer var4 = UltimateFriends.server.getPlayer(var2);
         if (var4 != null) {
            PlayerProfile var5 = UltimateFriends.getPlayerProfile(var4.getName());
            if (var5 != null && var5.getOptions().get(Options.Type.ALLOW_PRIVATE_MSG)) {
               var4.sendMessage((new ClickableMessage(Message.PRIVATE_MSG_FROM.getMsg(true))).clickable(var1).hoverEvent(Action.SHOW_TEXT, (new ClickableMessage(Message.FRIEND_LIST_BUTTON_MSG_HOVER.getMsg())).clickable(var1).append().buildString()).clickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND, "/" + UltimateFriends.getConfig().getCmds().getCmd() + " msg " + var1 + " ").append().clickable(var3).append().build());
               var5.setLastMsgSender(var1);
            }
         }

         SocialSpy.spy(var1, var2, var3);
      }
   }

   private void onFriendBroadcast(String var1, String[] var2, String var3) {
      String[] var4 = var2;
      int var5 = var2.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String var7 = var4[var6];
         ProxiedPlayer var8 = UltimateFriends.server.getPlayer(var7);
         if (var8 != null) {
            PlayerProfile var9 = UltimateFriends.getPlayerProfile(var8.getName());
            if (var9 != null && var9.getOptions().get(Options.Type.SHOW_BROADCASTS)) {
               var8.sendMessage((new ClickableMessage(Message.BROADCAST_FROM.getMsg(true))).clickable(var1).hoverEvent(Action.SHOW_TEXT, (new ClickableMessage(Message.FRIEND_LIST_BUTTON_MSG_HOVER.getMsg())).clickable(var1).append().buildString()).clickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND, "/" + UltimateFriends.getConfig().getCmds().getCmd() + " msg " + var1 + " ").append().clickable(var3).append().build());
            }
         }
      }

      SocialSpy.spy(var1, "(Broadcast)", var3);
   }
}
