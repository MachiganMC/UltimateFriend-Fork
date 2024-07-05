package com.gmail.holubvojtech.ultimatefriends.hook;

public abstract class Hook {
   private boolean isEnabled;
   private String pluginName;
   private String pluginClass;

   public Hook(String var1) {
      this.pluginName = var1;
   }

   public Hook(String var1, String var2) {
      this.pluginName = var1;
      this.pluginClass = var2;
   }

   public abstract boolean enable();

   public void disable() {
   }

   public boolean isEnabled() {
      return this.isEnabled;
   }

   public void setEnabled(boolean var1) {
      this.isEnabled = var1;
   }

   public String getPluginName() {
      return this.pluginName;
   }

   public String getPluginClass() {
      return this.pluginClass;
   }
}
