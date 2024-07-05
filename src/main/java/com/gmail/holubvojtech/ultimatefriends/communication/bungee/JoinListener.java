package com.gmail.holubvojtech.ultimatefriends.communication.bungee;

import com.gmail.holubvojtech.ultimatefriends.ClickableMessage;
import com.gmail.holubvojtech.ultimatefriends.Message;
import com.gmail.holubvojtech.ultimatefriends.Options;
import com.gmail.holubvojtech.ultimatefriends.PlayerProfile;
import com.gmail.holubvojtech.ultimatefriends.SocialSpy;
import com.gmail.holubvojtech.ultimatefriends.UltimateFriends;
import com.gmail.holubvojtech.ultimatefriends.Utils;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinListener implements Listener {
   @EventHandler
   public void onJoin(PostLoginEvent var1) {
      if (!UltimateFriends.shuttingDown) {
         ProxiedPlayer var2 = var1.getPlayer();
         final String var3 = var2.getName();
         if (!Utils.isNameValid(var3)) {
            UltimateFriends.logger.warning("Won't load profile for player '" + var3 + "', invalid characters found");
         } else {
            final UUID var4 = var2.getUniqueId();
            if (Utils.containsIgnoreCase(UltimateFriends.getConfig().getDefaultSocialSpyPlayers(), var3)) {
               SocialSpy.enableSpy(var2);
            }

            if (var2.hasPermission("ultimatefriends.autospy")) {
               SocialSpy.enableSpy(var2);
            }

            UltimateFriends.server.getScheduler().runAsync(UltimateFriends.plugin, new Runnable() {
               public void run() {
                  PlayerProfile var1 = UltimateFriends.getStorage().loadPlayerProfile(var3, var4);
                  UltimateFriends.addPlayerProfile(var3, var1);
                  JoinListener.this.sendJoinMsg(var3, Utils.toStringList(var1.getFriends()));
               }
            });
         }
      }
   }

   protected final void sendJoinMsg(String var1, List<String> var2) {
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         ProxiedPlayer var5 = UltimateFriends.server.getPlayer(var4);
         if (var5 != null) {
            PlayerProfile var6 = UltimateFriends.getPlayerProfile(var4);
            if (var6 != null && var6.getOptions().get(Options.Type.SHOW_JOIN_MSG)) {
               var5.sendMessage((new ClickableMessage(Message.FRIEND_JOINED_SERVER.getMsg(true))).clickable(var1).hoverEvent(Action.SHOW_TEXT, (new ClickableMessage(Message.FRIEND_LIST_BUTTON_MSG_HOVER.getMsg())).clickable(var1).append().buildString()).clickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND, "/" + UltimateFriends.getConfig().getCmds().getCmd() + " msg " + var1 + " ").append().build());
            }
         }
      }

   }
}
