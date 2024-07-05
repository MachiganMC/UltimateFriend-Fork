package com.gmail.holubvojtech.ultimatefriends;

import java.util.UUID;

public enum PlayerLookup {
   PLAYER_NAME("name"),
   UID("uuid");

   private String node;

   private PlayerLookup(String var3) {
      this.node = var3;
   }

   public String select(String var1, UUID var2) {
      return this.select(var1, var2.toString());
   }

   public String select(String var1, String var2) {
      return this == PLAYER_NAME ? var1 : var2;
   }

   public PlayerLookup negate() {
      return this == PLAYER_NAME ? UID : PLAYER_NAME;
   }

   public String toString() {
      return this.node;
   }
}
