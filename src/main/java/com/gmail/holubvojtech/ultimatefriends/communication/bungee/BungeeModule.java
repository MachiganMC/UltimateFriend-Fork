package com.gmail.holubvojtech.ultimatefriends.communication.bungee;

import com.gmail.holubvojtech.ultimatefriends.ClickableMessage;
import com.gmail.holubvojtech.ultimatefriends.Friend;
import com.gmail.holubvojtech.ultimatefriends.Message;
import com.gmail.holubvojtech.ultimatefriends.Options;
import com.gmail.holubvojtech.ultimatefriends.PlayerProfile;
import com.gmail.holubvojtech.ultimatefriends.SocialSpy;
import com.gmail.holubvojtech.ultimatefriends.UltimateFriends;
import com.gmail.holubvojtech.ultimatefriends.Utils;
import com.gmail.holubvojtech.ultimatefriends.communication.CommunicationModule;
import com.gmail.holubvojtech.ultimatefriends.exceptions.CannotAddYourself;
import com.gmail.holubvojtech.ultimatefriends.exceptions.ConnectionDisabledOnServer;
import com.gmail.holubvojtech.ultimatefriends.exceptions.FriendListExceeded;
import com.gmail.holubvojtech.ultimatefriends.exceptions.FriendOnDisabledServer;
import com.gmail.holubvojtech.ultimatefriends.exceptions.PlayerAlreadyFriend;
import com.gmail.holubvojtech.ultimatefriends.exceptions.PlayerAlreadyRequested;
import com.gmail.holubvojtech.ultimatefriends.exceptions.PlayerDenied;
import com.gmail.holubvojtech.ultimatefriends.exceptions.PlayerIsOffline;
import com.gmail.holubvojtech.ultimatefriends.exceptions.PlayerNotFriend;
import java.util.Iterator;
import java.util.List;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.PluginManager;
import org.jetbrains.annotations.Nullable;

public class BungeeModule implements CommunicationModule {
   public void registerListeners() {
      PluginManager var1 = UltimateFriends.server.getPluginManager();
      var1.registerListener(UltimateFriends.plugin, new JoinListener());
      var1.registerListener(UltimateFriends.plugin, new DisconnectListener());
      var1.registerListener(UltimateFriends.plugin, new ServerSwitchListener());
   }

   public boolean isOnline(String var1) {
      return UltimateFriends.server.getPlayer(var1) != null;
   }

   @Nullable
   public String getServer(String var1) {
      return this.getServer(UltimateFriends.server.getPlayer(var1));
   }

   @Nullable
   public String getServer(ProxiedPlayer var1) {
      if (var1 == null) {
         return null;
      } else {
         Server var2 = var1.getServer();
         return var2 == null ? null : var2.getInfo().getName();
      }
   }

   public void sendFriendMessage(PlayerProfile var1, String var2, String var3) throws PlayerDenied, PlayerIsOffline, FriendOnDisabledServer, PlayerNotFriend {
      Friend var4 = var1.getFriend(var2);
      if (var4 != null) {
         PlayerProfile var5 = UltimateFriends.getPlayerProfile(var2);
         if (var5 != null) {
            if (!var5.getOptions().get(Options.Type.ALLOW_PRIVATE_MSG)) {
               throw new PlayerDenied();
            } else {
               ProxiedPlayer var6 = UltimateFriends.server.getPlayer(var5.getPlayerName());
               if (var6 != null) {
                  String var7 = this.getServer(var6);
                  if (var7 != null && !UltimateFriends.getConfig().getDisable().getPlugin().contains(var7)) {
                     var6.sendMessage((new ClickableMessage(Message.PRIVATE_MSG_FROM.getMsg(true))).clickable(var1.getPlayerName()).hoverEvent(Action.SHOW_TEXT, (new ClickableMessage(Message.FRIEND_LIST_BUTTON_MSG_HOVER.getMsg())).clickable(var1.getPlayerName()).append().buildString()).clickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND, "/" + UltimateFriends.getConfig().getCmds().getCmd() + " msg " + var1.getPlayerName() + " ").append().clickable(var3).append().build());
                     var5.setLastMsgSender(var1.getPlayerName());
                     SocialSpy.spy(var1.getPlayerName(), var2, var3);
                  } else {
                     throw new FriendOnDisabledServer();
                  }
               } else {
                  SocialSpy.spy(var1.getPlayerName(), var2, var3);
                  throw new PlayerIsOffline();
               }
            }
         } else {
            SocialSpy.spy(var1.getPlayerName(), var2, var3);
            throw new PlayerIsOffline();
         }
      } else {
         throw new PlayerNotFriend();
      }
   }

   public void sendFriendBroadcastMessage(PlayerProfile var1, String var2) {
      this.sendFriendBroadcastMessage0(var1, var2, (List)null);
      SocialSpy.spy(var1.getPlayerName(), "(Broadcast)", var2);
   }

   protected void sendFriendBroadcastMessage0(PlayerProfile var1, String var2, @Nullable List<String> var3) {
      Iterator var4 = var1.getFriends().iterator();

      while(var4.hasNext()) {
         Friend var5 = (Friend)var4.next();
         PlayerProfile var6 = UltimateFriends.getPlayerProfile(var5.getPlayerName());
         if (var6 != null) {
            if (var6.getOptions().get(Options.Type.SHOW_BROADCASTS)) {
               ProxiedPlayer var7 = var6.getProxiedPlayer();
               if (var7 != null) {
                  String var8 = this.getServer(var7);
                  if (var8 != null && !UltimateFriends.getConfig().getDisable().getPlugin().contains(var8)) {
                     var7.sendMessage((new ClickableMessage(Message.BROADCAST_FROM.getMsg(true))).clickable(var1.getPlayerName()).hoverEvent(Action.SHOW_TEXT, (new ClickableMessage(Message.FRIEND_LIST_BUTTON_MSG_HOVER.getMsg())).clickable(var1.getPlayerName()).append().buildString()).clickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND, "/" + UltimateFriends.getConfig().getCmds().getCmd() + " msg " + var1.getPlayerName() + " ").append().clickable(var2).append().build());
                  }
               } else if (var3 != null) {
                  var3.add(var5.getPlayerName());
               }
            }
         } else if (var3 != null) {
            var3.add(var5.getPlayerName());
         }
      }

   }

   public void removeFriend(PlayerProfile var1, Friend var2) throws PlayerNotFriend {
      if (!var1.getFriends().contains(var2)) {
         throw new PlayerNotFriend();
      } else if (!UltimateFriends.getStorage().removeFriend(var1, var2)) {
         throw new RuntimeException("cannot save removed friendship");
      } else {
         PlayerProfile var3 = UltimateFriends.getPlayerProfile(var2.getPlayerName());
         if (var3 != null) {
            Friend var4 = var3.getFriend(var1.getPlayerName());
            if (var4 != null) {
               var3.getFriends().remove(var4);
            }

            ProxiedPlayer var5 = var3.getProxiedPlayer();
            if (var5 != null) {
               var5.sendMessage((new ClickableMessage(Message.FRIEND_REMOVED_YOU.getMsg(true))).clickable(var1.getPlayerName()).append().build());
            }
         }

         var1.getFriends().remove(var2);
      }
   }

   public boolean addFriend(PlayerProfile var1, String var2) throws PlayerIsOffline, FriendOnDisabledServer, CannotAddYourself, FriendListExceeded, PlayerAlreadyFriend, PlayerAlreadyRequested, PlayerDenied {
      if (var1.getPlayerName().equalsIgnoreCase(var2)) {
         throw new CannotAddYourself();
      } else if (var1.getFriends().size() >= UltimateFriends.getConfig().getMaxFriends(var1.getProxiedPlayer())) {
         throw new FriendListExceeded();
      } else {
         Friend var3 = var1.getFriend(var2);
         if (var3 != null) {
            throw new PlayerAlreadyFriend();
         } else if (Utils.containsIgnoreCase(var1.getSentRequests(), var2)) {
            throw new PlayerAlreadyRequested();
         } else {
            PlayerProfile var4 = UltimateFriends.getPlayerProfile(var2);
            if (var4 != null) {
               if (!var4.getOptions().get(Options.Type.ALLOW_REQUESTS)) {
                  throw new PlayerDenied();
               } else if (Utils.containsIgnoreCase(var4.getRequests(), var1.getPlayerName())) {
                  var1.getSentRequests().add(var2);
                  throw new PlayerAlreadyRequested();
               } else {
                  ProxiedPlayer var5 = UltimateFriends.server.getPlayer(var4.getPlayerName());
                  if (var5 != null) {
                     if (UltimateFriends.getConfig().getDisable().getPlugin().contains(var5.getServer().getInfo().getName())) {
                        throw new FriendOnDisabledServer();
                     } else if (var4.getFriends().size() >= UltimateFriends.getConfig().getMaxFriends(var5)) {
                        return false;
                     } else if (Utils.containsIgnoreCase(var1.getRequests(), var2)) {
                        var1.getRequests().remove(var2);
                        var1.getSentRequests().remove(var2);
                        var4.getRequests().remove(var1.getPlayerName());
                        var4.getSentRequests().remove(var1.getPlayerName());
                        if (!UltimateFriends.getStorage().addFriend(var1, var4)) {
                           throw new RuntimeException("cannot save new friendship");
                        } else {
                           var1.getFriends().add(new Friend(var4));
                           var4.getFriends().add(new Friend(var1));
                           var5.sendMessage((new ClickableMessage(Message.FRIEND_ADDED.getMsg(true))).clickable(var1.getPlayerName()).append().build());
                           return true;
                        }
                     } else {
                        var1.getSentRequests().add(var2);
                        var4.getRequests().add(var1.getPlayerName());
                        var5.sendMessage((new ClickableMessage(Message.FRIEND_REQUEST.getMsg(true))).clickable(var1.getPlayerName()).append().clickable(Message.FRIEND_REQUEST_BUTTON_ACCEPT_TEXT.getMsg()).hoverEvent(Action.SHOW_TEXT, Message.FRIEND_REQUEST_BUTTON_ACCEPT_HOVER.getMsg()).clickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/" + UltimateFriends.getConfig().getCmds().getCmd() + " add " + var1.getPlayerName()).append().build());
                        return false;
                     }
                  } else {
                     throw new PlayerIsOffline();
                  }
               }
            } else {
               throw new PlayerIsOffline();
            }
         }
      }
   }

   public void connect(PlayerProfile var1, String var2) throws PlayerIsOffline, FriendOnDisabledServer, ConnectionDisabledOnServer, PlayerNotFriend {
      Friend var3 = var1.getFriend(var2);
      if (var3 == null) {
         throw new PlayerNotFriend();
      } else {
         PlayerProfile var4 = UltimateFriends.getPlayerProfile(var2);
         if (var4 != null) {
            ProxiedPlayer var5 = UltimateFriends.server.getPlayer(var4.getPlayerName());
            if (var5 != null) {
               if (UltimateFriends.getConfig().getDisable().getPlugin().contains(var5.getServer().getInfo().getName())) {
                  throw new FriendOnDisabledServer();
               } else if (UltimateFriends.getConfig().getDisable().getConnection().contains(var5.getServer().getInfo().getName())) {
                  throw new ConnectionDisabledOnServer();
               } else {
                  ProxiedPlayer var6 = UltimateFriends.server.getPlayer(var1.getPlayerName());
                  Utils.safeConnect(var6, var5.getServer().getInfo());
               }
            } else {
               throw new PlayerIsOffline();
            }
         } else {
            throw new PlayerIsOffline();
         }
      }
   }

   public void unregisterListeners() {
      UltimateFriends.server.getPluginManager().unregisterListeners(UltimateFriends.plugin);
   }
}
