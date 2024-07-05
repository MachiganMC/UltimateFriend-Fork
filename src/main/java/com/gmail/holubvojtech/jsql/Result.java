package com.gmail.holubvojtech.jsql;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Result extends ResultRow {
   public Result(ResultSet var1) {
      super(var1);
   }

   public boolean next() {
      try {
         return this.rs.next();
      } catch (SQLException var2) {
         throw new RuntimeException(var2);
      }
   }
}
