package com.gmail.holubvojtech.jsql;

public enum Operator {
   EQ("=", "=="),
   NEQ("!="),
   SEQ("<=>"),
   LEQ("<="),
   GEQ(">="),
   GT(">"),
   LT("<"),
   IN("IN"),
   NOT_IN("NOT IN"),
   IS("IS"),
   IS_NOT("IS NOT"),
   LIKE("LIKE", "~"),
   NOT_LIKE("NOT LIKE", "!~"),
   AND("AND"),
   OR("OR");

   private String string;
   private String alias;

   private Operator(String var3) {
      this.string = var3;
   }

   private Operator(String var3, String var4) {
      this.string = var3;
      this.alias = var4;
   }

   public static Operator fromString(String var0) {
      if (var0 == null) {
         return null;
      } else {
         Operator[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Operator var4 = var1[var3];
            if (var4.alias != null && var4.alias.equals(var0)) {
               return var4;
            }

            if (var4.string.equalsIgnoreCase(var0)) {
               return var4;
            }
         }

         return null;
      }
   }

   public String getString() {
      return this.string;
   }

   public String toString() {
      return this.getString();
   }
}
