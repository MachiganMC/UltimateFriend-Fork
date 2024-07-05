package com.gmail.holubvojtech.ultimatefriends.communication.bungee;

import com.gmail.holubvojtech.ultimatefriends.ClickableMessage;
import com.gmail.holubvojtech.ultimatefriends.Friend;
import com.gmail.holubvojtech.ultimatefriends.Message;
import com.gmail.holubvojtech.ultimatefriends.Options;
import com.gmail.holubvojtech.ultimatefriends.PlayerProfile;
import com.gmail.holubvojtech.ultimatefriends.SocialSpy;
import com.gmail.holubvojtech.ultimatefriends.UltimateFriends;
import com.gmail.holubvojtech.ultimatefriends.Utils;
import com.gmail.holubvojtech.ultimatefriends.commands.Cmds;
import java.util.Iterator;
import java.util.List;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class DisconnectListener implements Listener {
   @EventHandler
   public void onLeft(PlayerDisconnectEvent var1) {
      String var2 = var1.getPlayer().getName();
      PlayerProfile var3 = UltimateFriends.removePlayerProfile(var2);
      if (var3 != null) {
         this.sendLeaveMsg(var2, Utils.toStringList(var3.getFriends()));
         this.updateLastSeen(var2);
      }

      Cmds.cooledDown.remove(var2.toLowerCase());
      SocialSpy.disableSpy(var1.getPlayer());
   }

   protected final void updateLastSeen(String var1) {
      Iterator var2 = UltimateFriends.getPlayerProfiles().iterator();

      while(var2.hasNext()) {
         PlayerProfile var3 = (PlayerProfile)var2.next();
         Iterator var4 = var3.getFriends().iterator();

         while(var4.hasNext()) {
            Friend var5 = (Friend)var4.next();
            if (var5.getName().equals(var1)) {
               var5.refreshLastSeen();
            }
         }
      }

   }

   protected final void sendLeaveMsg(String var1, List<String> var2) {
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         ProxiedPlayer var5 = UltimateFriends.server.getPlayer(var4);
         if (var5 != null) {
            PlayerProfile var6 = UltimateFriends.getPlayerProfile(var4);
            if (var6 != null && var6.getOptions().get(Options.Type.SHOW_LEAVE_MSG)) {
               var5.sendMessage((new ClickableMessage(Message.FRIEND_LEFT_SERVER.getMsg(true))).clickable(var1).append().build());
            }
         }
      }

   }
}
