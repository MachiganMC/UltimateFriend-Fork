package com.gmail.holubvojtech.jsql;

public enum Command {
   OTHER,
   SELECT,
   INSERT,
   UPDATE,
   DELETE;

   public static Command detect(String var0) {
      var0 = var0.toLowerCase().trim();
      if (var0.startsWith("select")) {
         return SELECT;
      } else if (var0.startsWith("insert")) {
         return INSERT;
      } else if (var0.startsWith("update")) {
         return UPDATE;
      } else {
         return var0.startsWith("delete") ? DELETE : OTHER;
      }
   }
}
