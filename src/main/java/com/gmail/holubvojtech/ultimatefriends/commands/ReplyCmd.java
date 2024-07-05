package com.gmail.holubvojtech.ultimatefriends.commands;

import com.gmail.holubvojtech.ultimatefriends.PlayerProfile;
import com.gmail.holubvojtech.ultimatefriends.UltimateFriends;
import com.gmail.holubvojtech.ultimatefriends.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReplyCmd extends Command {
   private String cmd;

   public ReplyCmd(String var1) {
      super(var1);
      this.cmd = var1;
   }

   public void execute(CommandSender var1, String[] var2) {
      if (var1 instanceof ProxiedPlayer) {
         ProxiedPlayer var3 = (ProxiedPlayer)var1;
         PlayerProfile var4 = UltimateFriends.getPlayerProfile(var3.getName());
         if (var4 != null) {
            String var5 = var4.getLastMsgSender();
            if (var5 != null) {
               String[] var6 = (String[])Utils.concat((Object[])(new String[]{"msg", var5}), (Object[])var2);
               UltimateFriends.getConfig().getCmds().execute(var3, var6);
            }

         }
      }
   }

   public String getCmd() {
      return this.cmd;
   }
}
