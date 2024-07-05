package com.gmail.holubvojtech.jsql.mapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Iterator;

public class StatementMapper extends ValueMapper<PreparedStatement> {
   public static void set(PreparedStatement var0, int var1, Object var2) throws SQLException {
      if (var2 == null) {
         var0.setNull(var1, 0);
      } else if (var2 instanceof String) {
         var0.setString(var1, (String)var2);
      } else if (var2 instanceof Integer) {
         var0.setInt(var1, (Integer)var2);
      } else if (var2 instanceof Long) {
         var0.setLong(var1, (Long)var2);
      } else if (var2 instanceof Double) {
         var0.setDouble(var1, (Double)var2);
      } else if (var2 instanceof Boolean) {
         var0.setBoolean(var1, (Boolean)var2);
      } else if (var2 instanceof Byte) {
         var0.setByte(var1, (Byte)var2);
      } else if (var2 instanceof Short) {
         var0.setShort(var1, (Short)var2);
      } else if (var2 instanceof Float) {
         var0.setFloat(var1, (Float)var2);
      } else if (var2 instanceof BigDecimal) {
         var0.setBigDecimal(var1, (BigDecimal)var2);
      } else if (var2 instanceof Date) {
         var0.setDate(var1, (Date)var2);
      } else if (var2 instanceof Time) {
         var0.setTime(var1, (Time)var2);
      } else {
         var0.setString(var1, var2.toString());
      }
   }

   public void apply(PreparedStatement var1, int var2) {
      int var3;
      try {
         var3 = var1.getParameterMetaData().getParameterCount();
      } catch (SQLException var9) {
         throw new RuntimeException("cannot retrieve parameter count", var9);
      }

      int var4 = var2;

      for(Iterator var5 = this.list.iterator(); var5.hasNext(); ++var4) {
         Object var6 = var5.next();
         if (var4 >= var3) {
            return;
         }

         try {
            set(var1, var4 + 1, var6);
         } catch (SQLException var8) {
            throw new RuntimeException("cannot map values", var8);
         }
      }

   }
}
