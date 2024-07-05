package com.gmail.holubvojtech.jsql;

import com.gmail.holubvojtech.jsql.mapper.StatementMapper;
import com.gmail.holubvojtech.jsql.mapper.StringMapper;
import com.gmail.holubvojtech.jsql.mapper.ValueMapper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Query {
   protected Database database;
   protected Command command;
   protected String table;
   protected String[] columns;
   protected Map<String, Object> sets;
   protected List<Object[]> values;
   protected Where where;
   protected String groupBy;
   protected List<ColumnOrder> order;
   protected int offset = -1;
   protected int limit = -1;

   public Query(Database var1, Command var2) {
      this.database = var1;
      this.command = var2;
   }

   public Query table(String var1) {
      this.table = var1;
      return this;
   }

   public Query columns(String... var1) {
      if (var1 != null && var1.length != 0) {
         this.columns = (String[])Arrays.copyOf(var1, var1.length);
         return this;
      } else {
         this.columns = null;
         return this;
      }
   }

   public Query set(String var1, Object var2) {
      if (this.sets == null) {
         this.sets = new HashMap();
      }

      this.sets.put(var1, var2);
      return this;
   }

   public Query values(Object... var1) {
      if (this.values == null) {
         this.values = new ArrayList();
      }

      this.values.add(Arrays.copyOf(var1, var1.length));
      return this;
   }

   public Query where(String var1, Object var2) {
      if (this.where == null) {
         this.where = new Where();
      }

      this.where.where(var1, var2);
      return this;
   }

   public Query andWhere(String var1, Object var2) {
      this.where.andWhere(var1, var2);
      return this;
   }

   public Query orWhere(String var1, Object var2) {
      this.where.orWhere(var1, var2);
      return this;
   }

   public Query andWhere(ConditionBuilder var1) {
      this.where.andWhere(var1);
      return this;
   }

   public Query orWhere(ConditionBuilder var1) {
      this.where.orWhere(var1);
      return this;
   }

   public Query group(String var1) {
      this.groupBy = var1;
      return this;
   }

   public Query order(String var1) {
      return this.orderAsc(var1);
   }

   public Query orderAsc(String var1) {
      if (this.order == null) {
         this.order = new ArrayList();
      }

      this.order.add(new ColumnOrder(var1, Order.ASC));
      return this;
   }

   public Query orderDesc(String var1) {
      if (this.order == null) {
         this.order = new ArrayList();
      }

      this.order.add(new ColumnOrder(var1, Order.DESC));
      return this;
   }

   public Query limit(int var1) {
      return this.limit(-1, var1);
   }

   public Query limit(int var1, int var2) {
      this.offset = var1;
      this.limit = var2;
      return this;
   }

   public int iterate(ResultIterator var1) {
      Result var2 = this.fetch();
      if (var2 == null) {
         return -1;
      } else {
         int var3 = 0;

         try {
            while(var2.next()) {
               var1.row(var2);
               ++var3;
               if (var2.isClosed()) {
                  break;
               }
            }
         } catch (Throwable var8) {
            var8.printStackTrace();
         } finally {
            var2.close();
         }

         return var3;
      }
   }

   public Result first() {
      int var1 = this.offset;
      int var2 = this.limit;
      this.limit(1);

      Result var4;
      try {
         Result var3 = this.fetch();
         if (var3 == null) {
            var4 = null;
            return var4;
         }

         if (!var3.next()) {
            var3.close();
            var4 = null;
            return var4;
         }

         var4 = var3;
      } finally {
         this.offset = var1;
         this.limit = var2;
      }

      return var4;
   }

   public boolean exists() {
      Result var1 = this.first();
      if (var1 != null) {
         var1.close();
         return true;
      } else {
         return false;
      }
   }

   public Result fetch() {
      try {
         PreparedStatement var1 = this.prepareStatement();
         if (var1 == null) {
            return null;
         } else {
            ResultSet var2 = var1.executeQuery();
            return new Result(var2);
         }
      } catch (SQLException var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public boolean execute() {
      try {
         PreparedStatement var1 = this.prepareStatement();
         if (var1 == null) {
            return false;
         } else {
            var1.execute();
            return true;
         }
      } catch (SQLException var2) {
         var2.printStackTrace();
         return false;
      }
   }

   protected PreparedStatement prepareStatement() {
      StatementMapper var1 = new StatementMapper();
      String var2 = this.toSQL(var1);
      PreparedStatement var3 = this.database.prepareStatement(var2);
      if (var3 == null) {
         return null;
      } else {
         var1.apply(var3);
         return var3;
      }
   }

   public String toSQL() {
      StringMapper var1 = new StringMapper();
      StringBuilder var2 = new StringBuilder(this.toSQL(var1));
      var1.apply(var2);
      return var2.toString();
   }

   public abstract String toSQL(ValueMapper var1);
}
