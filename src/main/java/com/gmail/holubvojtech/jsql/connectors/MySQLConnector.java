package com.gmail.holubvojtech.jsql.connectors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnector implements DatabaseConnector {
   private final String user;
   private final String database;
   private final String password;
   private final int port;
   private final String hostname;
   private final String charset;

   public MySQLConnector(String var1, String var2, String var3, String var4) {
      this(var1, 3306, var2, var3, var4);
   }

   public MySQLConnector(String var1, int var2, String var3, String var4, String var5) {
      this(var1, var2, var3, var4, var5, "utf8");
   }

   public MySQLConnector(String var1, int var2, String var3, String var4, String var5, String var6) {
      try {
         Class.forName("com.mysql.jdbc.Driver");
      } catch (ClassNotFoundException var8) {
         throw new RuntimeException("driver not found", var8);
      }

      this.hostname = var1;
      this.port = var2;
      this.database = var3;
      this.user = var4;
      this.password = var5;
      this.charset = var6;
   }

   public Connection connect() {
      String var1 = "jdbc:mysql://" + this.hostname + ":" + this.port;
      if (this.database != null) {
         var1 = var1 + "/" + this.database;
      }

      if (this.charset != null) {
         var1 = var1 + "?characterEncoding=" + this.charset;
      }

      try {
         return DriverManager.getConnection(var1, this.user, this.password);
      } catch (SQLException var3) {
         var3.printStackTrace();
         return null;
      }
   }
}
