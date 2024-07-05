package com.gmail.holubvojtech.jsql;

import com.gmail.holubvojtech.jsql.connectors.DatabaseConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {
   private DatabaseConnector connector;
   private Connection connection;

   public Database(DatabaseConnector var1) {
      this.connector = var1;
   }

   public Query select(String var1, String... var2) {
      return (new SelectQuery(this)).table(var1).columns(var2);
   }

   public Query insert(String var1, String... var2) {
      return (new InsertQuery(this)).table(var1).columns(var2);
   }

   public Query update(String var1) {
      return (new UpdateQuery(this)).table(var1);
   }

   public Query delete(String var1) {
      return (new DeleteQuery(this)).table(var1);
   }

   public Query sql(String var1) {
      return this.sql(var1, (StatementSetter)null);
   }

   public Query sql(String var1, StatementSetter var2) {
      PreparedStatement var3 = this.prepareStatement(var1);
      if (var3 != null && var2 != null) {
         try {
            var2.apply(var3);
         } catch (SQLException var5) {
            var5.printStackTrace();
            var3 = null;
         }
      }

      return new CustomQuery(this, var3, var1);
   }

   public boolean transactional(Transactional var1) {
      if (!this.checkConnected()) {
         return false;
      } else {
         try {
            this.connection.setAutoCommit(false);
         } catch (SQLException var8) {
            throw new RuntimeException(var8);
         }

         boolean var2;
         try {
            var2 = var1.transaction(this);
         } catch (Throwable var7) {
            var7.printStackTrace();
            var2 = false;
         }

         if (var2) {
            try {
               this.connection.commit();
            } catch (SQLException var6) {
               throw new RuntimeException(var6);
            }
         } else {
            try {
               this.connection.rollback();
            } catch (SQLException var5) {
               throw new RuntimeException(var5);
            }
         }

         try {
            this.connection.setAutoCommit(true);
            return var2;
         } catch (SQLException var4) {
            throw new RuntimeException(var4);
         }
      }
   }

   public boolean isConnected() {
      try {
         return this.connection != null && !this.connection.isClosed();
      } catch (SQLException var2) {
         var2.printStackTrace();
         return false;
      }
   }

   public boolean checkConnected() {
      return this.isConnected() || this.connect();
   }

   public Connection connection() {
      return this.connection;
   }

   public boolean connect() {
      if (this.connection != null) {
         this.disconnect();
      }

      this.connection = this.connector.connect();
      return this.connection != null;
   }

   public boolean disconnect() {
      try {
         this.connection.close();
         this.connection = null;
         return true;
      } catch (SQLException var2) {
         var2.printStackTrace();
         return false;
      }
   }

   public PreparedStatement prepareStatement(String var1) {
      if (!this.checkConnected()) {
         return null;
      } else {
         try {
            PreparedStatement var2 = this.connection().prepareStatement(var1);
            return var2;
         } catch (SQLException var4) {
            var4.printStackTrace();
            return null;
         }
      }
   }
}
