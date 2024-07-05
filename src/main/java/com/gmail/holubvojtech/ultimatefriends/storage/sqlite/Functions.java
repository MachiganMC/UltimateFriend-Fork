package com.gmail.holubvojtech.ultimatefriends.storage.sqlite;

import com.gmail.holubvojtech.jsql.Database;
import com.gmail.holubvojtech.ultimatefriends.Utils;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Pattern;
import org.sqlite.Function;

public class Functions {
   public static void declare(Database var0) throws SQLException {
      Connection var1 = var0.connection();
      if (var1 == null) {
         throw new NullPointerException("connection");
      } else {
         Function.create(var0.connection(), "regexp", new Function() {
            protected void xFunc() throws SQLException {
               String var1 = this.value_text(0);
               String var2 = this.value_text(1);
               if (var2 == null) {
                  var2 = "";
               }

               this.result(Pattern.compile(var1).matcher(var2).matches() ? 1 : 0);
            }
         });
         Function.create(var0.connection(), "substring_index", new Function() {
            protected void xFunc() throws SQLException {
               String var1 = this.value_text(0);
               if (var1 == null) {
                  var1 = "";
               }

               String var2 = this.value_text(0);
               int var3 = this.value_int(0);
               int var4 = Utils.ordinalIndexOf(var1, var2, var3);
               if (var4 < 0) {
                  var4 = var1.length();
               }

               this.result(var1.substring(0, var4));
            }
         });
         Function.create(var0.connection(), "least", new Function() {
            protected void xFunc() throws SQLException {
               this.result(Math.min(this.value_int(0), this.value_int(1)));
            }
         });
         Function.create(var0.connection(), "greatest", new Function() {
            protected void xFunc() throws SQLException {
               this.result(Math.max(this.value_int(0), this.value_int(1)));
            }
         });
      }
   }
}
