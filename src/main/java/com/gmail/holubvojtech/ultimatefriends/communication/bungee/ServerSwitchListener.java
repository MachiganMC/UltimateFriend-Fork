package com.gmail.holubvojtech.ultimatefriends.communication.bungee;

import com.gmail.holubvojtech.ultimatefriends.ClickableMessage;
import com.gmail.holubvojtech.ultimatefriends.Message;
import com.gmail.holubvojtech.ultimatefriends.Options;
import com.gmail.holubvojtech.ultimatefriends.PlayerProfile;
import com.gmail.holubvojtech.ultimatefriends.UltimateFriends;
import com.gmail.holubvojtech.ultimatefriends.Utils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerSwitchListener implements Listener {
   protected HashMap<String, String> connectingTo = new HashMap<>();

   @EventHandler
   public void onServerConnected(ServerConnectedEvent var1) {
       System.out.println("2");
      ProxiedPlayer var2 = var1.getPlayer();
      String var3 = var2.getName();
      String var4 = this.connectingTo.remove(var3);
      this.connectingTo.put(var3, var1.getServer().getInfo().getName());
      if (var4 != null) {
          PlayerProfile var6 = UltimateFriends.getPlayerProfile(var3);
          if (var6 != null) {
              this.sendSwitchMsg(var3, var4, var1.getServer().getInfo().getName(), Utils.toStringList(var6.getFriends()));
          }
      }
   }

   @EventHandler
   public void onLeft(PlayerDisconnectEvent var1) {
       System.out.println("3");
      this.connectingTo.remove(var1.getPlayer().getName());
   }

   protected final void sendSwitchMsg(String var1, String var2, String var3, List<String> var4) {
      var2 = UltimateFriends.getConfig().getServerAliases().translate(var2);
      var3 = UltimateFriends.getConfig().getServerAliases().translate(var3);
       for (String var6 : var4) {
           ProxiedPlayer var7 = UltimateFriends.server.getPlayer(var6);
           if (var7 != null) {
               PlayerProfile var8 = UltimateFriends.getPlayerProfile(var6);
               if (var8 != null && var8.getOptions().get(Options.Type.SHOW_SWITCH_MSG)) {
                   var7.sendMessage((new ClickableMessage(Message.FRIEND_SWITCH_SERVER.getMsg(true)))
                           .clickable(var1).append()
                           .clickable(var2).append()
                           .clickable(var3)
                           .clickEvent(Action.RUN_COMMAND, "/" + UltimateFriends.getConfig().getCmds().getCmd() + " connect " + var1)
                           .hoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
                                   (new ClickableMessage(Message.FRIEND_LIST_BUTTON_CONNECT_HOVER.getMsg()))
                                           .clickable(var3).append().clickable(var1).append().buildString()).append().build());
               }
           }
       }

   }
}
