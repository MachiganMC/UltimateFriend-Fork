package com.gmail.holubvojtech.jsql;

import com.gmail.holubvojtech.jsql.mapper.ValueMapper;
import java.util.Collection;
import java.util.Iterator;

public class InsertQuery extends Query {
   public InsertQuery(Database var1) {
      super(var1, Command.INSERT);
   }

   public String toSQL(ValueMapper var1) {
      StringBuilder var2 = new StringBuilder("INSERT INTO ");
      var2.append(Utils.grave(this.table));
      var2.append(" (");

      int var3;
      for(var3 = 0; var3 < this.columns.length; ++var3) {
         String var4 = this.columns[var3];
         if (var4.toLowerCase().contains(" as ")) {
            String[] var5 = var4.split(" as ");
            var4 = Utils.grave(var5[0]);
         } else {
            var4 = Utils.grave(var4);
         }

         if (var3 > 0) {
            var2.append(", ");
         }

         var2.append(var4);
      }

      var2.append(") VALUES ");
      var3 = 0;

      for(Iterator var6 = this.values.iterator(); var6.hasNext(); ++var3) {
         Object[] var7 = (Object[])var6.next();
         if (var3 > 0) {
            var2.append(", ");
         }

         var2.append(Utils.sqlTuple(var7.length));
         var1.addAll((Object)var7);
      }

      if (this.where != null) {
         var2.append(" WHERE ");
         var2.append(this.where.toSQL(var1));
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
}
