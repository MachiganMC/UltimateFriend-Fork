package com.gmail.holubvojtech.ultimatefriends;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class Friend extends PlayerID {
   private long lastSeen;

   public Friend(@NotNull PlayerID var1) {
      this(var1.getId(), var1.getName(), var1.getUuid());
   }

   public Friend(int var1, @NotNull String var2, @NotNull UUID var3) {
      super(var1, var2, var3);
      this.refreshLastSeen();
   }

   public Friend(int var1, @NotNull String var2, @NotNull String var3) {
      this(var1, var2, UUID.fromString(var3));
   }

   public long getLastSeen() {
      return this.lastSeen;
   }

   public void setLastSeen(long var1) {
      this.lastSeen = var1;
   }

   public void refreshLastSeen() {
      this.lastSeen = System.currentTimeMillis();
   }

   public String toString() {
      return this.getName();
   }
}
