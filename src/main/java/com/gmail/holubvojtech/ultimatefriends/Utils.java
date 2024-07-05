package com.gmail.holubvojtech.ultimatefriends;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Utils {
   private static final Pattern PLAYER_NAME_PATTERN = Pattern.compile("[a-zA-Z0-9_]*");

   public static <T> List<String> toStringList(List<T> var0) {
      if (var0 == null) {
         return null;
      } else {
         ArrayList var1 = new ArrayList();
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            Object var3 = var2.next();
            var1.add(var3.toString());
         }

         return var1;
      }
   }

   public static <T> T[] concat(T[] var0, T[] var1) {
      int var2 = var0.length;
      int var3 = var1.length;
      T[] var4 = (T[]) Array.newInstance(var0.getClass().getComponentType(), var2 + var3);
      System.arraycopy(var0, 0, var4, 0, var2);
      System.arraycopy(var1, 0, var4, var2, var3);
      return var4;
   }

   public static BaseComponent[] concat(BaseComponent[] var0, BaseComponent[] var1) {
      int var2 = var0.length;
      int var3 = var1.length;
      BaseComponent[] var4 = new BaseComponent[var2 + var3];
      System.arraycopy(var0, 0, var4, 0, var2);
      System.arraycopy(var1, 0, var4, var2, var3);
      return var4;
   }

   public static int ordinalIndexOf(String var0, String var1, int var2) {
      int var3 = var0.indexOf(var1);

      while(true) {
         --var2;
         if (var2 <= 0 || var3 == -1) {
            return var3;
         }

         var3 = var0.indexOf(var1, var3 + 1);
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

   public static boolean containsIgnoreCase(List<String> var0, String var1) {
      Iterator var2 = var0.iterator();

      String var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (String)var2.next();
      } while(!var3.equalsIgnoreCase(var1));

      return true;
   }

   public static boolean isNameValid(String var0) {
      return PLAYER_NAME_PATTERN.matcher(var0).matches() && var0.length() <= 16;
   }

   public static void safeConnect(ProxiedPlayer var0, ServerInfo var1) {
      try {
         UltimateFriends.getHookManager().connectPlayer(var0, var1);
      } catch (Throwable var3) {
         var3.printStackTrace();
      }

   }

   public static String formatTime(long var0) {
      return UltimateFriends.getConfig().getDateFormat().format(new Date(var0));
   }
}
