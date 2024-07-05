package com.gmail.holubvojtech.jsql;

import com.gmail.holubvojtech.jsql.mapper.StringMapper;
import com.gmail.holubvojtech.jsql.mapper.ValueMapper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Where {
   private List<Object> conditions = new ArrayList();

   public Where where(String var1, Object var2) {
      this.parseCond(var1, var2, (Operator)null);
      return this;
   }

   public Where andWhere(String var1, Object var2) {
      this.parseCond(var1, var2, Operator.AND);
      return this;
   }

   public Where andWhere(ConditionBuilder var1) {
      Where var2 = new Where();
      var1.build(var2);
      this.conditions.add(Operator.AND);
      this.conditions.add(var2);
      return this;
   }

   public Where orWhere(String var1, Object var2) {
      this.parseCond(var1, var2, Operator.OR);
      return this;
   }

   public Where orWhere(ConditionBuilder var1) {
      Where var2 = new Where();
      var1.build(var2);
      this.conditions.add(Operator.OR);
      this.conditions.add(var2);
      return this;
   }

   private void parseCond(String var1, Object var2, Operator var3) {
      String var4 = var1;
      String var5 = null;
      int var6 = var1.indexOf(32);
      if (var6 > 0) {
         var4 = var1.substring(0, var6);
         var5 = var1.substring(var6 + 1, var1.length());
         if (var5.isEmpty()) {
            var5 = null;
         }
      }

      if (var5 == null) {
         if (var2 == null) {
            var5 = "IS";
         } else if (Utils.isListing(var2)) {
            var5 = "IN";
         } else {
            var5 = "=";
         }
      }

      Operator var7 = Operator.fromString(var5);
      if (var7 == null) {
         throw new IllegalArgumentException();
      } else {
         if (var3 != null) {
            this.conditions.add(var3);
         }

         this.conditions.add(new Where.Condition(var4, var7, var2));
      }
   }

   public String toSQL() {
      StringMapper var1 = new StringMapper();
      StringBuilder var2 = new StringBuilder(this.toSQL(var1));
      var1.apply(var2);
      return var2.toString();
   }

   public String toSQL(ValueMapper var1) {
      if (var1 == null) {
         var1 = ValueMapper.VOID_MAPPER;
      }

      StringBuilder var2 = new StringBuilder();
      var2.append("(");
      Iterator var3 = this.conditions.iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         if (var4 instanceof Where) {
            var2.append(((Where)var4).toSQL(var1));
         }

         if (var4 instanceof Where.Condition) {
            Where.Condition var5 = (Where.Condition)var4;
            var2.append(Utils.grave(var5.key)).append(" ").append(var5.operator).append(" ");
            if (var5.operator == Operator.IN) {
               if (!Utils.isListing(var5.value)) {
                  throw new RuntimeException("operator IN requires Collection or Array");
               }

               var2.append(Utils.sqlTuple(Utils.length(var5.value)));
               var1.addAll(var5.value);
            } else if (var5.value == null) {
               var2.append("NULL");
            } else {
               var2.append("?");
               var1.add(var5.value);
            }
         }

         if (var4 instanceof Operator) {
            var2.append(" ").append(var4).append(" ");
         }
      }

      var2.append(")");
      return var2.toString();
   }

   private static class Condition {
      private String key;
      private Operator operator;
      private Object value;

      public Condition(String var1, Operator var2, Object var3) {
         this.key = var1;
         this.operator = var2;
         this.value = var3;
      }
   }
}
