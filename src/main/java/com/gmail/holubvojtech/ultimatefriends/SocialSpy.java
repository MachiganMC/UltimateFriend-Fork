package com.gmail.holubvojtech.ultimatefriends;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class SocialSpy {
   private static List<String> players = new ArrayList();

   public static void spy(String var0, String var1, String var2) {
      Iterator var3 = players.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         if (!var1.equalsIgnoreCase(var4) && !var0.equalsIgnoreCase(var4)) {
            ProxiedPlayer var5 = UltimateFriends.server.getPlayer(var4);
            if (var5 != null) {
               var5.sendMessage((new ClickableMessage(Message.SOCIAL_SPY.getMsg(true))).clickable(var0).append().clickable(var1).append().clickable(var2).append().build());
            }
         }
      }

   }

   public static void enableSpy(ProxiedPlayer var0) {
      if (!isSpy(var0)) {
         players.add(var0.getName().toLowerCase());
      }

   }

   public static void disableSpy(ProxiedPlayer var0) {
      players.remove(var0.getName().toLowerCase());
   }

   public static boolean isSpy(ProxiedPlayer var0) {
      return players.contains(var0.getName().toLowerCase());
   }
}
