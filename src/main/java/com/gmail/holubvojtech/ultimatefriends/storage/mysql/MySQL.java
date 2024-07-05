package com.gmail.holubvojtech.ultimatefriends.storage.mysql;

import com.gmail.holubvojtech.jsql.Database;
import com.gmail.holubvojtech.jsql.Query;
import com.gmail.holubvojtech.jsql.Result;
import com.gmail.holubvojtech.jsql.ResultIterator;
import com.gmail.holubvojtech.jsql.ResultRow;
import com.gmail.holubvojtech.jsql.StatementSetter;
import com.gmail.holubvojtech.jsql.connectors.DatabaseConnector;
import com.gmail.holubvojtech.ultimatefriends.Friend;
import com.gmail.holubvojtech.ultimatefriends.Options;
import com.gmail.holubvojtech.ultimatefriends.PlayerID;
import com.gmail.holubvojtech.ultimatefriends.PlayerLookup;
import com.gmail.holubvojtech.ultimatefriends.PlayerProfile;
import com.gmail.holubvojtech.ultimatefriends.UltimateFriends;
import com.gmail.holubvojtech.ultimatefriends.Utils;
import com.gmail.holubvojtech.ultimatefriends.storage.Storage;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class MySQL implements Storage {
   public static final String USERS_TABLE = "uf_users";
   public static final String OPTIONS_TABLE = "uf_user_options";
   public static final String FRIENDS_TABLE = "uf_user_friends";
   protected final Database database;
   protected PlayerLookup lookup;
   protected int limit;

   public MySQL(@NotNull DatabaseConnector var1) {
      super();
      this.limit = 1;
      this.database = new Database(var1);
   }

   public boolean connect() {
      this.lookup = UltimateFriends.getConfig().getPlayerLookup();

      try {
         if (!this.database.connect()) {
            return false;
         } else {
            return this.prepareDB();
         }
      } catch (Throwable var2) {
         var2.printStackTrace();
         return false;
      }
   }

   public boolean disconnect() {
      return this.database.disconnect();
   }

   public PlayerProfile loadPlayerProfile(String var1, UUID var2) {
      return this.loadOrCreateProfile(var1, var2, false);
   }

   private PlayerProfile loadOrCreateProfile(String var1, UUID var2, boolean var3) {
      PlayerProfile var4 = this.loadData(var1, var2, var3);
      if (var4 != null) {
         return var4;
      } else if (var3) {
         throw new RuntimeException("value was just inserted but not found");
      } else {
         this.database.insert("uf_users").columns("name", "uuid", "lastSeen").values(var1, var2, System.currentTimeMillis()).execute();
         return this.loadOrCreateProfile(var1, var2, true);
      }
   }

   private PlayerProfile loadData(String var1, UUID var2, boolean var3) {
      Result var4 = this.database.select("uf_users").where(this.lookup.toString(), this.lookup.select(var1, var2)).first();
      if (var4 != null) {
         PlayerProfile var5 = new PlayerProfile(var4.getInt("id"), var4.getString("name"), UUID.fromString(var4.getString("uuid")));
         var4.close();
         if (!var3) {
            List var6 = this.getFriends(var5);
            if (var6 != null) {
               var5.getFriends().addAll(var6);
            }

            Options var7 = this.getOptions(var5);
            var5.setOptions(var7);
            PlayerLookup var8 = this.lookup.negate();
            this.database.update("uf_users").set(var8.toString(), var8.select(var1, var2)).set("lastSeen", System.currentTimeMillis()).where(this.lookup.toString(), this.lookup.select(var1, var2)).limit(this.limit).execute();
         }

         return var5;
      } else {
         return null;
      }
   }

   public Options getOptions(@NotNull PlayerProfile var1) {
      Options var2 = new Options();
      Result var3 = this.database.select("uf_user_options").where("user_id", var1.getId()).first();
      if (var3 != null) {
         Options.Type[] var4 = Options.Type.values();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Options.Type var7 = var4[var6];
            var2.set(var7, var3.getBoolean(var7.getNode()));
         }

         var3.close();
      }

      return var2;
   }

   public boolean saveOptions(@NotNull PlayerProfile var1) {
      Options var2 = var1.getOptions();
      Query var3;
      if (this.database.select("uf_user_options").where("user_id", var1.getId()).exists()) {
         var3 = this.database.update("uf_user_options").where("user_id", var1.getId());
         Options.Type[] var4 = Options.Type.values();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Options.Type var7 = var4[var6];
            var3.set(var7.getNode(), var2.get(var7));
         }

         var3.limit(this.limit);
      } else {
         var3 = this.database.insert("uf_user_options").columns((String[])Utils.concat((Object[])(new String[]{"user_id"}), (Object[])Options.Type.getNodes())).values(Utils.concat(new Object[]{var1.getId()}, var2.values()));
      }

      return var3.execute();
   }

   public List<Friend> getFriends(@NotNull PlayerProfile var1) {
      final int var2 = var1.getId();
      final ArrayList var3 = new ArrayList();
      int var4 = this.database.sql("SELECT id, name, uuid, lastSeen FROM uf_user_friends INNER JOIN uf_users ON (user_id_1 = ? AND user_id_2 = id) OR (user_id_2 = ? AND user_id_1 = id) WHERE user_id_1 = ? OR user_id_2 = ?", new StatementSetter() {
         public void apply(PreparedStatement var1) throws SQLException {
            var1.setInt(1, var2);
            var1.setInt(2, var2);
            var1.setInt(3, var2);
            var1.setInt(4, var2);
         }
      }).iterate(var11 -> {
         Friend var21 = new Friend(var11.getInt("id"), var11.getString("name"), var11.getString("uuid"));
         var21.setLastSeen(var11.getLong("lastSeen"));
         var3.add(var21);
      });
      return var4 < 0 ? null : var3;
   }

   public boolean addFriend(@NotNull PlayerProfile var1, @NotNull PlayerID var2) {
      int var3 = Math.min(var1.getId(), var2.getId());
      int var4 = Math.max(var1.getId(), var2.getId());
      return var3 == var4 ? false : this.database.insert("uf_user_friends").columns("user_id_1", "user_id_2").values(var3, var4).execute();
   }

   public boolean removeFriend(@NotNull PlayerProfile var1, @NotNull PlayerID var2) {
      int var3 = Math.min(var1.getId(), var2.getId());
      int var4 = Math.max(var1.getId(), var2.getId());
      return var3 == var4 ? false : this.database.delete("uf_user_friends").where("user_id_1", var3).andWhere("user_id_2", var4).limit(this.limit).execute();
   }

   public boolean prepareDB() {
      if (!this.database.sql("SHOW TABLES LIKE 'uf_users'").exists()) {
         if (!this.database.sql("CREATE TABLE uf_users (  id   integer PRIMARY KEY AUTO_INCREMENT,  name varchar(16) NOT NULL,  uuid CHAR(36)    NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci").execute()) {
            return false;
         }

         if (!this.database.sql("CREATE INDEX uf_users_name_index ON uf_users (name)").execute()) {
            return false;
         }

         if (!this.database.sql("CREATE UNIQUE INDEX uf_users_uuid_uindex ON uf_users (uuid)").execute()) {
            return false;
         }
      }

      if (!this.database.sql("SHOW TABLES LIKE 'uf_user_friends'").exists() && !this.database.sql("CREATE TABLE uf_user_friends (  user_id_1 int NOT NULL,  user_id_2 int NOT NULL,  CONSTRAINT uf_user_friends_user_1_user_2_pk PRIMARY KEY (user_id_1, user_id_2),  CONSTRAINT uf_user_friends_uf_users_id_fk FOREIGN KEY (user_id_1) REFERENCES uf_users (id) ON DELETE CASCADE,  CONSTRAINT uf_user_friends_uf_users_id_fk_2 FOREIGN KEY (user_id_2) REFERENCES uf_users (id) ON DELETE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci").execute()) {
         return false;
      } else if (!this.database.sql("SHOW TABLES LIKE 'uf_user_options'").exists() && !this.database.sql("CREATE TABLE uf_user_options (  user_id integer NOT NULL PRIMARY KEY,  show_msg_join tinyint DEFAULT 0 NOT NULL,  show_msg_left tinyint DEFAULT 0 NOT NULL,  show_msg_switch tinyint DEFAULT 0 NOT NULL,  allow_requests tinyint DEFAULT 0 NOT NULL,  allow_private_msg tinyint DEFAULT 0 NOT NULL,  show_broadcast tinyint DEFAULT 0 NOT NULL,  CONSTRAINT uf_user_options_uf_users_id_fk FOREIGN KEY (user_id) REFERENCES uf_users (id) ON DELETE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci").execute()) {
         return false;
      } else {
         if (UltimateFriends.getConfig().getCore().isUuidNotUnique() && this.database.sql("SHOW INDEX FROM uf_users WHERE Key_name = 'uf_users_uuid_uindex'").exists()) {
            this.database.sql("DROP INDEX uf_users_uuid_uindex ON uf_users").execute();
         }

         if (!this.columnExists("uf_users", "lastSeen")) {
            UltimateFriends.logger.info("Updating database... (column:lastSeen)");
            this.database.sql("ALTER TABLE uf_users ADD lastSeen BIGINT DEFAULT 0 NOT NULL").execute();
            UltimateFriends.logger.info("Update done");
         }

         return true;
      }
   }

   public Database getDatabase() {
      return this.database;
   }

   protected boolean columnExists(String var1, String var2) {
      Result var3 = this.database.sql("SELECT * FROM (SELECT 1) t LEFT JOIN " + var1 + " u ON FALSE LIMIT 1").first();

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
