package com.gmail.holubvojtech.jsql;

import com.gmail.holubvojtech.jsql.mapper.ValueMapper;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

public class UpdateQuery extends Query {
   public UpdateQuery(Database var1) {
      super(var1, Command.UPDATE);
   }

   public String toSQL(ValueMapper var1) {
      StringBuilder var2 = new StringBuilder("UPDATE ");
      var2.append(Utils.grave(this.table));
      var2.append(" SET ");
      int var3 = 0;

      for(Iterator var4 = this.sets.entrySet().iterator(); var4.hasNext(); ++var3) {
         Entry var5 = (Entry)var4.next();
         if (var3 > 0) {
            var2.append(", ");
         }

         var2.append(Utils.grave((String)var5.getKey())).append(" = ").append("?");
         var1.add(var5.getValue());
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
