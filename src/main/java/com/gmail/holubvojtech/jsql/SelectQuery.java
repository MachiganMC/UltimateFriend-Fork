package com.gmail.holubvojtech.jsql;

import com.gmail.holubvojtech.jsql.mapper.ValueMapper;
import java.util.Collection;

public class SelectQuery extends Query {
   public SelectQuery(Database var1) {
      super(var1, Command.SELECT);
   }

   public String toSQL(ValueMapper var1) {
      StringBuilder var2 = new StringBuilder("SELECT ");
      if (this.columns == null) {
         var2.append("*");
      } else {
         for(int var3 = 0; var3 < this.columns.length; ++var3) {
            String var4 = this.columns[var3];
            if (var4.toLowerCase().contains(" as ")) {
               String[] var5 = var4.split(" as ");
               var4 = this.grave(var5[0]) + " as " + this.grave(var5[1]);
            } else {
               var4 = this.grave(var4);
            }

            if (var3 > 0) {
               var2.append(", ");
            }

            var2.append(var4);
         }
      }

      var2.append(" FROM ").append(Utils.grave(this.table));
      if (this.where != null) {
         var2.append(" WHERE ");
         var2.append(this.where.toSQL(var1));
      }

      if (this.groupBy != null) {
         var2.append(" GROUP BY ");
         var2.append(Utils.grave(this.groupBy));
      }

      if (this.order != null) {
         var2.append(" ORDER BY ");
         var2.append(Utils.join(", ", (Collection)this.order));
      }

      if (this.limit > -1) {
         var2.append(" LIMIT ").append(this.limit);
         if (this.offset > -1) {
            var2.append(" OFFSET ").append(this.offset);
         }
      }

      return var2.toString();
   }

   private String grave(String var1) {
      return var1.matches("[a-zA-Z]+\\(.*\\)") ? var1 : Utils.grave(var1);
   }
}
