package com.gmail.holubvojtech.ultimatefriends.hook;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public class HookManager {
   private Set<Hook> hooks = new HashSet();
   private Function connectFn = new Function() {
      public void invoke(Object... var1) {
         ProxiedPlayer var2 = (ProxiedPlayer)var1[0];
         ServerInfo var3 = (ServerInfo)var1[1];
         var2.connect(var3);
      }
   };

   public void registerHook(Hook var1) {
      this.hooks.add(var1);
   }

   public void enableHooks() {
      Iterator var1 = this.hooks.iterator();

      while(true) {
         Hook var2;
         while(true) {
            Plugin var3;
            do {
               do {
                  if (!var1.hasNext()) {
                     return;
                  }

                  var2 = (Hook)var1.next();
               } while(var2.isEnabled());

               var3 = ProxyServer.getInstance().getPluginManager().getPlugin(var2.getPluginName());
            } while(var3 == null);

            String var4 = var2.getPluginClass();
            if (var4 == null) {
               break;
            }

            try {
               Class.forName(var4);
               break;
            } catch (Throwable var7) {
            }
         }

         try {
            var2.enable();
            var2.setEnabled(true);
         } catch (Throwable var6) {
            var6.printStackTrace();
         }
      }
   }

   public List<Hook> getHooks() {
      return new ArrayList<>(this.hooks);
   }

   public void disableHooks() {
      Iterator var1 = this.hooks.iterator();

      while(var1.hasNext()) {
         Hook var2 = (Hook)var1.next();
         if (var2.isEnabled()) {
            try {
               var2.disable();
               var2.setEnabled(false);
            } catch (Throwable var4) {
               var4.printStackTrace();
            }
         }
      }

   }

   private void invokeFn(Function var1, Object... var2) {
      try {
         var1.invoke(var2);
      } catch (Throwable var4) {
         var4.printStackTrace();
      }

   }

   public void connectPlayer(ProxiedPlayer var1, ServerInfo var2) {
      this.invokeFn(this.connectFn, var1, var2);
   }

   public void setConnectFn(Function var1) {
      this.connectFn = var1;
   }
}
