package com.gmail.holubvojtech.ultimatefriends.commands;

import com.gmail.holubvojtech.ultimatefriends.UltimateFriends;
import com.gmail.holubvojtech.ultimatefriends.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MsgCmd extends Command {
   private String cmd;

   public MsgCmd(String var1) {
      super(var1);
      this.cmd = var1;
   }

   public void execute(CommandSender var1, String[] var2) {
      if (var1 instanceof ProxiedPlayer) {
         if (var2.length >= 2) {
            String[] var3 = (String[])Utils.concat((Object[])(new String[]{"msg"}), (Object[])var2);
            UltimateFriends.getConfig().getCmds().execute(var1, var3);
         }
      }
   }

   public String getCmd() {
      return this.cmd;
   }
}
