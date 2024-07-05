package com.gmail.holubvojtech.ultimatefriends;

import org.jetbrains.annotations.NotNull;

public class Options {
   private boolean SHOW_JOIN_MSG = true;
   private boolean SHOW_LEAVE_MSG = true;
   private boolean SHOW_SWITCH_MSG = true;
   private boolean ALLOW_REQUESTS = true;
   private boolean ALLOW_PRIVATE_MSG = true;
   private boolean SHOW_BROADCASTS = true;

   public Options() {
      Config.Options.Values var1 = UltimateFriends.getConfig().getOptions().getDefaults();
      this.SHOW_JOIN_MSG = var1.show_msg_join();
      this.SHOW_LEAVE_MSG = var1.show_msg_left();
      this.SHOW_SWITCH_MSG = var1.show_msg_switch();
      this.ALLOW_REQUESTS = var1.allow_requests();
      this.ALLOW_PRIVATE_MSG = var1.allow_private_msg();
      this.SHOW_BROADCASTS = var1.show_broadcast();
   }

   public boolean get(@NotNull Options.Type var1) {
       return switch (var1) {
           case SHOW_JOIN_MSG -> this.SHOW_JOIN_MSG;
           case SHOW_LEAVE_MSG -> this.SHOW_LEAVE_MSG;
           case SHOW_SWITCH_MSG -> this.SHOW_SWITCH_MSG;
           case ALLOW_REQUESTS -> this.ALLOW_REQUESTS;
           case ALLOW_PRIVATE_MSG -> this.ALLOW_PRIVATE_MSG;
           case SHOW_BROADCASTS -> this.SHOW_BROADCASTS;
       };
   }

   public void set(@NotNull Options.Type var1, boolean var2) {
      switch(var1) {
      case SHOW_JOIN_MSG:
         this.SHOW_JOIN_MSG = var2;
         return;
      case SHOW_LEAVE_MSG:
         this.SHOW_LEAVE_MSG = var2;
         return;
      case SHOW_SWITCH_MSG:
         this.SHOW_SWITCH_MSG = var2;
         return;
      case ALLOW_REQUESTS:
         this.ALLOW_REQUESTS = var2;
         return;
      case ALLOW_PRIVATE_MSG:
         this.ALLOW_PRIVATE_MSG = var2;
         return;
      case SHOW_BROADCASTS:
         this.SHOW_BROADCASTS = var2;
         return;
      default:
         throw new IllegalArgumentException("Unknown type");
      }
   }

   public Object[] values() {
      Options.Type[] var1 = Options.Type.values();
      Object[] var2 = new Object[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = this.get(var1[var3]);
      }

      return var2;
   }

   public enum Type {
      SHOW_JOIN_MSG("show_msg_join", Message.OPTIONS_OPT_SHOW_JOIN),
      SHOW_LEAVE_MSG("show_msg_left", Message.OPTIONS_OPT_SHOW_LEAVE),
      SHOW_SWITCH_MSG("show_msg_switch", Message.OPTIONS_OPT_SHOW_SWITCH),
      ALLOW_REQUESTS("allow_requests", Message.OPTIONS_OPT_ALLOW_REQUESTS),
      ALLOW_PRIVATE_MSG("allow_private_msg", Message.OPTIONS_OPT_ALLOW_PRIVATE_MSG),
      SHOW_BROADCASTS("show_broadcast", Message.OPTIONS_OPT_SHOW_BROADCAST);

      private String node;
      private Message msg;

      private Type(String var3, Message var4) {
         this.node = var3;
         this.msg = var4;
      }

      public static String[] getNodes() {
         Options.Type[] var0 = values();
         String[] var1 = new String[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2].getNode();
         }

         return var1;
      }

      public String getNode() {
         return this.node;
      }

      public Message getMsg() {
         return this.msg;
      }
   }
}
