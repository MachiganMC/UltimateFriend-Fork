package com.gmail.holubvojtech.jsql;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class Utils {
   public static int length(Object var0) {
      if (var0 != null) {
         if (var0 instanceof Collection) {
            return ((Collection)var0).size();
         }

         if (var0.getClass().isArray()) {
            return Array.getLength(var0);
         }
      }

      return -1;
   }

   public static boolean isListing(Object var0) {
      return var0 != null && (var0.getClass().isArray() || var0 instanceof Collection);
   }

   public static boolean containsIC(String var0, String var1) {
      return var0.toLowerCase().contains(var1.toLowerCase());
   }

   public static String grave(String var0) {
      return '`' + var0 + '`';
   }

   public static String[] grave(String... var0) {
      String[] var1 = new String[var0.length];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1[var2] = grave(var0[var2]);
      }

      return var1;
   }

   public static String sqlTuple(int var0) {
      if (var0 < 1) {
         throw new IllegalArgumentException();
      } else {
         StringBuilder var1 = new StringBuilder("(?");

         for(int var2 = 1; var2 < var0; ++var2) {
            var1.append(", ?");
         }

         return var1.append(")").toString();
      }
   }

   public static String join(String var0, String... var1) {
      return join(var0, (Collection)Arrays.asList(var1));
   }

   public static String join(String var0, Collection var1) {
      if (var1 != null && var0 != null) {
         if (var1.isEmpty()) {
            return "";
         } else {
            StringBuilder var2 = new StringBuilder();
            int var3 = 0;

            for(Iterator var4 = var1.iterator(); var4.hasNext(); ++var3) {
               Object var5 = var4.next();
               if (var3 > 0) {
                  var2.append(var0);
               }

               var2.append(var5.toString());
            }

            return var2.toString();
         }
      } else {
         return null;
      }
   }
}
