package com.gmail.holubvojtech.ultimatefriends;

import java.io.File;
import java.io.IOException;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class LanguageLoader {
   private Configuration configuration;

   public LanguageLoader(File var1) throws IOException {
      UltimateFriends.logger.info("Lang file: " + var1.getName());
      this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(var1);
      Message[] var2 = Message.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Message var5 = var2[var4];
         String var6 = this.configuration.getString(var5.name(), (String)null);
         if (var6 == null) {
            UltimateFriends.logger.warning("Translation: " + var5.name() + " not found!");
         } else {
            var6 = ChatColor.translateAlternateColorCodes('&', var6);
            var5.setMsg(var6);
         }
      }

   }

   public Configuration getConfiguration() {
      return this.configuration;
   }
}
