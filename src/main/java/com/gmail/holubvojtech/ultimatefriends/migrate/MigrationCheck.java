package com.gmail.holubvojtech.ultimatefriends.migrate;

import com.gmail.holubvojtech.ultimatefriends.migrate.v2_6.Migration_260_v2_6_MySQL;
import java.io.File;
import java.io.IOException;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class MigrationCheck {
   private File file;
   private Configuration config;

   public MigrationCheck(File var1) throws IOException {
      this.file = var1;
      this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(var1);
   }

   public void checkAndMigrate() throws MigrationUnsuccessfulException, IOException {
      this.migrate(this.getMigrationCode());
   }

   public int getMigrationCode() {
      return this.config.getInt("migrate", 0);
   }

   public void migrate(int var1) throws MigrationUnsuccessfulException, IOException {
      if (var1 != 0) {
         this.config.set("migrate", 0);
         this.saveFile();
         Migration var2 = this.getMigration(var1);
         if (var2 != null) {
            try {
               var2.migrate();
            } catch (Throwable var4) {
               throw new MigrationCheck.MigrationUnsuccessfulException(var4);
            }
         }
      }
   }

   public Migration getMigration(int var1) {
      switch(var1) {
      case 260:
         return new Migration_260_v2_6_MySQL();
      default:
         return null;
      }
   }

   private void saveFile() throws IOException {
      ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.config, this.file);
   }

   public File getFile() {
      return this.file;
   }

   public Configuration getConfig() {
      return this.config;
   }

   public class MigrationUnsuccessfulException extends Exception {
      public MigrationUnsuccessfulException(Throwable var2) {
         super(var2);
      }
   }
}
