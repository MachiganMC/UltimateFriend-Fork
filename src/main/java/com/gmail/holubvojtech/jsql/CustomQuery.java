package com.gmail.holubvojtech.jsql;

import com.gmail.holubvojtech.jsql.mapper.ValueMapper;
import java.sql.PreparedStatement;

public class CustomQuery extends Query {
   private PreparedStatement ps;
   private String query;

   public CustomQuery(Database var1, PreparedStatement var2, String var3) {
      super(var1, Command.detect(var3));
      this.ps = var2;
      this.query = var3;
   }

   public String toSQL(ValueMapper var1) {
      return this.query;
   }

   protected PreparedStatement prepareStatement() {
      return this.ps;
   }
}
