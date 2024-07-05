package com.gmail.holubvojtech.ultimatefriends.storage.sqlite;

import com.gmail.holubvojtech.jsql.Result;
import com.gmail.holubvojtech.jsql.connectors.DatabaseConnector;
import com.gmail.holubvojtech.ultimatefriends.UltimateFriends;
import com.gmail.holubvojtech.ultimatefriends.storage.Storage;
import com.gmail.holubvojtech.ultimatefriends.storage.mysql.MySQL;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class SQLite extends MySQL implements Storage {
   private static final String DRIVER_URL = "https://www.dropbox.com/s/l0q75u5lf7h4m9e/SQLiteDriver.jar?dl=1";
   public static final String USERS_TABLE = "uf_users";
   public static final String OPTIONS_TABLE = "uf_user_options";
   public static final String FRIENDS_TABLE = "uf_user_friends";

   public SQLite(@NotNull DatabaseConnector var1) {
      super(var1);
      this.limit = -1;
   }

   public static void downloadDriver() {
      File var0 = new File(ProxyServer.getInstance().getPluginsFolder(), "SQLiteDriver.jar");
      Plugin var1 = ProxyServer.getInstance().getPluginManager().getPlugin("SQLiteDriver");
      if (var1 == null && !var0.exists()) {
         UltimateFriends.logger.warning("SQLiteDriver not found");
         UltimateFriends.logger.info("Downloading SQLiteDriver.jar to /plugins folder...");
         InputStream var2 = null;
         ReadableByteChannel var3 = null;
         FileOutputStream var4 = null;

         try {
            URL var5 = new URL("https://www.dropbox.com/s/l0q75u5lf7h4m9e/SQLiteDriver.jar?dl=1");
            var2 = var5.openStream();
            var3 = Channels.newChannel(var2);
            var4 = new FileOutputStream(var0);
            var4.getChannel().transferFrom(var3, 0L, Long.MAX_VALUE);
         } catch (Throwable var19) {
            UltimateFriends.logger.severe("Cannot download SQLiteDriver.jar, please contact developers");
            UltimateFriends.logger.info("Try to download file manually from https://www.dropbox.com/s/l0q75u5lf7h4m9e/SQLiteDriver.jar?dl=1");
            throw new RuntimeException("Cannot download SQLiteDriver.jar", var19);
         } finally {
            if (var4 != null) {
               try {
                  var4.close();
               } catch (IOException var18) {
                  var18.printStackTrace();
               }
            }

            if (var3 != null) {
               try {
                  var3.close();
               } catch (IOException var17) {
                  var17.printStackTrace();
               }
            }

            if (var2 != null) {
               try {
                  var2.close();
               } catch (IOException var16) {
                  var16.printStackTrace();
               }
            }

         }

         UltimateFriends.logger.info("SQLiteDriver downloaded successfully, restarting BungeeCord...");
         ProxyServer.getInstance().stop();
      }

   }

   public boolean prepareDB() {
      if (!this.database.select("sqlite_master", "name").where("type", "table").andWhere("name", "uf_users").exists()) {
         if (!this.database.sql("CREATE TABLE uf_users (  id   integer PRIMARY KEY AUTOINCREMENT,  name varchar(16) NOT NULL COLLATE NOCASE,  uuid CHAR(36)    NOT NULL)").execute()) {
            return false;
         }

         if (!this.database.sql("CREATE INDEX uf_users_name_index ON uf_users (name)").execute()) {
            return false;
         }

         if (!this.database.sql("CREATE UNIQUE INDEX uf_users_uuid_uindex ON uf_users (uuid)").execute()) {
            return false;
         }
      }

      if (!this.database.select("sqlite_master", "name").where("type", "table").andWhere("name", "uf_user_friends").exists() && !this.database.sql("CREATE TABLE uf_user_friends (  user_id_1 int NOT NULL,  user_id_2 int NOT NULL,  CONSTRAINT uf_user_friends_user_1_user_2_pk PRIMARY KEY (user_id_1, user_id_2),  CONSTRAINT uf_user_friends_uf_users_id_fk FOREIGN KEY (user_id_1) REFERENCES uf_users (id) ON DELETE CASCADE,  CONSTRAINT uf_user_friends_uf_users_id_fk_2 FOREIGN KEY (user_id_2) REFERENCES uf_users (id) ON DELETE CASCADE)").execute()) {
         return false;
      } else if (!this.database.select("sqlite_master", "name").where("type", "table").andWhere("name", "uf_user_options").exists() && !this.database.sql("CREATE TABLE uf_user_options (  user_id integer NOT NULL PRIMARY KEY,  show_msg_join tinyint DEFAULT 0 NOT NULL,  show_msg_left tinyint DEFAULT 0 NOT NULL,  show_msg_switch tinyint DEFAULT 0 NOT NULL,  allow_requests tinyint DEFAULT 0 NOT NULL,  allow_private_msg tinyint DEFAULT 0 NOT NULL,  show_broadcast tinyint DEFAULT 0 NOT NULL,  CONSTRAINT uf_user_options_uf_users_id_fk FOREIGN KEY (user_id) REFERENCES uf_users (id) ON DELETE CASCADE)").execute()) {
         return false;
      } else {
         if (UltimateFriends.getConfig().getCore().isUuidNotUnique()) {
            this.database.sql("DROP INDEX IF EXISTS uf_users_uuid_uindex").execute();
         }

         if (!this.columnExists("uf_users", "lastSeen")) {
            UltimateFriends.logger.info("Updating database... (column:lastSeen)");
            this.database.sql("ALTER TABLE uf_users ADD lastSeen INTEGER DEFAULT 0 NOT NULL").execute();
            UltimateFriends.logger.info("Update done");
         }

         return true;
      }
   }

   protected boolean columnExists(String var1, String var2) {
      Result var3 = this.database.sql("SELECT * FROM (SELECT 1) LEFT JOIN " + var1 + " LIMIT 1").first();

      boolean var5;
      try {
         var3.getString(var2);
         return true;
      } catch (Exception var9) {
         var5 = false;
      } finally {
         var3.close();
      }

      return var5;
   }
}
