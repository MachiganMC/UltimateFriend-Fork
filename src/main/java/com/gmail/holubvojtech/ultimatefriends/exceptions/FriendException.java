package com.gmail.holubvojtech.ultimatefriends.exceptions;

public class FriendException extends Exception {
   public FriendException() {
   }

   public FriendException(String var1) {
      super(var1);
   }

   public FriendException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public FriendException(Throwable var1) {
      super(var1);
   }

   public FriendException(String var1, Throwable var2, boolean var3, boolean var4) {
      super(var1, var2, var3, var4);
   }
}
