package com.gmail.holubvojtech.jsql.connectors;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnector implements DatabaseConnector {
   private File file;

   public SQLiteConnector(File var1) {
      try {
         Class.forName("org.sqlite.JDBC");
      } catch (ClassNotFoundException var3) {
         throw new RuntimeException("driver not found", var3);
      }

      this.file = var1;
   }

   public Connection connect() {
      if (!this.file.exists()) {
         try {
            if (!this.file.createNewFile()) {
               return null;
            }
         } catch (IOException var3) {
            return null;
         }
      }

      try {
         return DriverManager.getConnection("jdbc:sqlite:" + this.file.getAbsolutePath());
      } catch (SQLException var2) {
         var2.printStackTrace();
         return null;
      }
   }
}
