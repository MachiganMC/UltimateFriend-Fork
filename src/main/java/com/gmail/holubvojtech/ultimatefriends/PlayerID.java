package com.gmail.holubvojtech.ultimatefriends;

import java.util.Objects;
import java.util.UUID;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerID {
   private int id;
   @NotNull
   private String name;
   @NotNull
   private UUID uuid;

   public PlayerID(int var1, @NotNull String var2, @NotNull UUID var3) {
      super();
      this.id = var1;
      this.name = var2;
      this.uuid = var3;
   }

   public int getId() {
      return this.id;
   }

   @NotNull
   public String getName() {
      return this.name;
   }

   @NotNull
   public UUID getUuid() {

      return this.uuid;
   }

   public String getPlayerName() {
      return this.name;
   }

   @Nullable
   public ProxiedPlayer getProxiedPlayer() {
      return ProxyServer.getInstance().getPlayer(this.name);
   }

   public PlayerID toPlayerID() {
      return new PlayerID(this.id, this.name, this.uuid);
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         PlayerID var2 = (PlayerID)var1;
         return this.id == var2.id && this.name.equalsIgnoreCase(var2.name) && Objects.equals(this.uuid, var2.uuid);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.id, this.name.toLowerCase(), this.uuid});
   }

}
