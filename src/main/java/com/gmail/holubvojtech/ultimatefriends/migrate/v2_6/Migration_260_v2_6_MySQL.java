package com.gmail.holubvojtech.ultimatefriends.migrate.v2_6;

import com.gmail.holubvojtech.jsql.Database;
import com.gmail.holubvojtech.jsql.Result;
import com.gmail.holubvojtech.ultimatefriends.Config;
import com.gmail.holubvojtech.ultimatefriends.UltimateFriends;
import com.gmail.holubvojtech.ultimatefriends.migrate.Migration;
import com.gmail.holubvojtech.ultimatefriends.storage.mysql.MySQL;
import com.gmail.holubvojtech.ultimatefriends.storage.sqlite.Functions;
import com.gmail.holubvojtech.ultimatefriends.storage.sqlite.SQLite;

import java.sql.SQLException;

public class Migration_260_v2_6_MySQL implements Migration {
   private boolean sqlite;
   private String oldFriendsTable = "friend_list";
   private Database database;

   public void migrate() throws SQLException {
      UltimateFriends.logger.info("Migration: Preparing for the action...");
      Config var1 = UltimateFriends.getConfig();
      this.oldFriendsTable = var1.getCore().db_tables_fList();
      MySQL var2 = (MySQL)var1.getStorage();
      this.database = var2.getDatabase();
      UltimateFriends.logger.info("Migration: Connecting to the database...");
      if (!this.database.connect()) {
         throw new RuntimeException("Cannot connect to the database");
      } else {
         UltimateFriends.logger.info("Migration: Clearing target tables...");
         this.database.sql("DROP TABLE IF EXISTS uf_user_options").execute();
         this.database.sql("DROP TABLE IF EXISTS uf_user_friends").execute();
         this.database.sql("DROP TABLE IF EXISTS uf_users").execute();
         UltimateFriends.logger.info("Migration: Opening storage...");
         if (!var2.connect()) {
            throw new RuntimeException("Cannot connect to the database");
         } else {
            UltimateFriends.logger.info("Migration: Converting data to the new format (be patient)...");
            if (var2 instanceof SQLite) {
               this.sqlite = true;
               Functions.declare(this.database);
            }

            try {
               this.run();
               UltimateFriends.logger.info("Migration: Done");
            } finally {
               this.database.disconnect();
            }

         }
      }
   }

   private void run() {
      Result var2 = this.database.select(this.oldFriendsTable, "COUNT(DISTINCT uid) as count").where("friends is not", (Object)null).first();
      int var1 = var2.getInt("count");
      var2.close();
      if (var1 <= 0) {
         UltimateFriends.logger.info("Migration: Nothing to import");
      } else {
         UltimateFriends.logger.info("Migration: Found " + var1 + " rows to process");
         UltimateFriends.logger.info("Migration: Importing profiles...");
         if (!this.database.sql("INSERT INTO uf_users (id, name, uuid) SELECT   MIN(id),   MIN(playerName),   uid FROM " + this.oldFriendsTable + " WHERE friends IS NOT NULL AND uid IS NOT NULL AND playerName REGEXP '^[a-zA-Z0-9_]*$' GROUP BY uid ORDER BY id ASC").execute()) {
            throw new RuntimeException();
         } else {
            UltimateFriends.logger.info("Migration: Preparing for the friends import...");
            if (!this.database.sql("DROP TABLE IF EXISTS tmp_uf_migrate").execute()) {
               throw new RuntimeException();
            } else if (!this.database.sql("CREATE TABLE tmp_uf_migrate AS SELECT ROUND(            (              length(friends)              - length(REPLACE(friends, ',', ''))            ) / length(',')        ) + 1 AS count FROM " + this.oldFriendsTable + " WHERE friends IS NOT NULL AND uid IS NOT NULL AND playerName REGEXP '^[a-zA-Z0-9_]*$' GROUP BY count ORDER BY count DESC").execute()) {
               throw new RuntimeException();
            } else {
               UltimateFriends.logger.info("Migration: Importing friends (be patient)...");
               String var3 = "";
               if (this.sqlite) {
                  var3 = var3 + 'o';
                  var3 = var3 + 'r';
               }

               if (!this.database.sql("insert " + var3 + " ignore into " + "uf_user_friends" + " select distinct least(id1, id2) as user_id_1, greatest(id1, id2) as user_id_2 FROM (  select    id as id1,    substring_index(        substring_index(friends, ',', count),        ',',        -1    )  as id2  from " + this.oldFriendsTable + "    join tmp_uf_migrate      on length(friends)         - length(replace(friends, ',', ''))         >= count - 1) sub").execute()) {
                  throw new RuntimeException();
               } else if (!this.database.sql("DROP TABLE tmp_uf_migrate").execute()) {
                  throw new RuntimeException();
               }
            }
         }
      }
   }
}
