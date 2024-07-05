package com.gmail.holubvojtech.jsql;

import com.gmail.holubvojtech.jsql.mapper.ValueMapper;
import java.util.Collection;

public class DeleteQuery extends Query {
   public DeleteQuery(Database var1) {
      super(var1, Command.DELETE);
   }

   public String toSQL(ValueMapper var1) {
      StringBuilder var2 = new StringBuilder("DELETE FROM ");
      var2.append(Utils.grave(this.table));
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
