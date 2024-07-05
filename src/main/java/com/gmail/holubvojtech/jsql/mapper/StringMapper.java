package com.gmail.holubvojtech.jsql.mapper;

public class StringMapper extends ValueMapper<StringBuilder> {
   public void apply(StringBuilder var1, int var2) {
      int var4 = 0;
      int var5 = 0;

      while(true) {
         int var3;
         while((var3 = var1.indexOf("?", var5)) >= 0) {
            if (var4 >= this.list.size()) {
               return;
            }

            if (var4 < var2) {
               var5 = var3 + 1;
               ++var4;
            } else {
               Object var6 = this.list.get(var4);
               String var7;
               if (var6 == null) {
                  var7 = "NULL";
               } else if (!(var6 instanceof Number) && !(var6 instanceof Boolean)) {
                  var7 = "'" + var6.toString().replace("'", "''") + "'";
               } else {
                  var7 = var6.toString().toUpperCase();
               }

               var1.replace(var3, var3 + 1, var7);
               ++var4;
            }
         }

         return;
      }
   }
}
