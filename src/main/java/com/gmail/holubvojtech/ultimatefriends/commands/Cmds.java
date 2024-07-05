package com.gmail.holubvojtech.ultimatefriends.commands;

import com.gmail.holubvojtech.ultimatefriends.ClickableMessage;
import com.gmail.holubvojtech.ultimatefriends.Config;
import com.gmail.holubvojtech.ultimatefriends.Friend;
import com.gmail.holubvojtech.ultimatefriends.ListPaginator;
import com.gmail.holubvojtech.ultimatefriends.Message;
import com.gmail.holubvojtech.ultimatefriends.Options;
import com.gmail.holubvojtech.ultimatefriends.PlayerProfile;
import com.gmail.holubvojtech.ultimatefriends.SocialSpy;
import com.gmail.holubvojtech.ultimatefriends.UltimateFriends;
import com.gmail.holubvojtech.ultimatefriends.Utils;
import com.gmail.holubvojtech.ultimatefriends.exceptions.CannotAddYourself;
import com.gmail.holubvojtech.ultimatefriends.exceptions.ConnectionDisabledOnServer;
import com.gmail.holubvojtech.ultimatefriends.exceptions.FriendListExceeded;
import com.gmail.holubvojtech.ultimatefriends.exceptions.FriendOnDisabledServer;
import com.gmail.holubvojtech.ultimatefriends.exceptions.PlayerAlreadyFriend;
import com.gmail.holubvojtech.ultimatefriends.exceptions.PlayerAlreadyRequested;
import com.gmail.holubvojtech.ultimatefriends.exceptions.PlayerDenied;
import com.gmail.holubvojtech.ultimatefriends.exceptions.PlayerIsOffline;
import com.gmail.holubvojtech.ultimatefriends.exceptions.PlayerNotFriend;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.jetbrains.annotations.Nullable;

public class Cmds extends Command {
   public static HashMap<String, Long> cooledDown = new HashMap();
   private String cmd;
   private int coolDown;

   public Cmds(String var1, String[] var2, @Nullable String var3, int var4) {
      super(var1, var3, var2);
      this.coolDown = var4;
      this.cmd = var1;
   }

   public void execute(final CommandSender var1, String[] var2) {
      if (var2.length == 1 && var2[0].equalsIgnoreCase("reload")) {
         if (var1.hasPermission("ultimatefriends.reload")) {
            final long var22 = System.currentTimeMillis();
            UltimateFriends.server.getScheduler().runAsync(UltimateFriends.plugin, () -> {
               try {
                  UltimateFriends.logger.info("Reloading (" + var1.getName() + ")");
                  var1.sendMessage("Reloading...");
                  UltimateFriends.getConfig().reload();
                  String var1x = "Reloaded in " + (System.currentTimeMillis() - var22) + " ms!";
                  var1.sendMessage(var1x);
                  UltimateFriends.logger.info(var1x);
               } catch (IOException var213) {
                  var213.printStackTrace();
                  var1.sendMessage(Message.ERROR.getMsg(true));
               }

            });
         }

      } else if (var1 instanceof ProxiedPlayer) {
         final ProxiedPlayer var3 = (ProxiedPlayer)var1;
         if (this.canUse(var3)) {
            this.setCoolDown(var3);
            if (UltimateFriends.getConfig().getDisable().getPlugin().contains(var3.getServer().getInfo().getName())) {
               var3.sendMessage(Message.FRIENDS_DISABLED.getMsg(true));
            } else {
               final PlayerProfile var4 = UltimateFriends.getPlayerProfile(var3.getName());
               if (var4 == null) {
                  var3.sendMessage(Message.FRIENDS_NOT_LOADED.getMsg(true));
               } else {
                  int var5;
                  if (var2.length != 0 && (var2.length != 2 || !var2[0].equalsIgnoreCase("page"))) {
                     String var6;
                     if (var2[0].equalsIgnoreCase("options")) {
                        if (var2.length == 1) {
                           this.renderOptions(var3, var4);
                        } else if (var2.length == 3) {
                           Options var27 = var4.getOptions();
                           var6 = var2[1];
                           boolean var31 = var2[2].equals("true");

                           Options.Type var29;
                           try {
                              var29 = Options.Type.valueOf(var6);
                           } catch (IllegalArgumentException var12) {
                              return;
                           }

                           boolean var32 = var27.get(var29);
                           if (var31 != var32) {
                              var27.set(var29, var31);
                              UltimateFriends.server.getScheduler().runAsync(UltimateFriends.plugin, new Runnable() {
                                 public void run() {
                                    UltimateFriends.getStorage().saveOptions(var4);
                                    Cmds.this.renderOptions(var3, var4);
                                 }
                              });
                           }
                        }
                     } else {
                        String var23;
                        if (var2[0].equalsIgnoreCase("msg")) {
                           if (var2.length < 3) {
                              var3.sendMessage(Message.CMD_UNKNOWN_MSG.getMsg(true));
                           } else {
                              var23 = var2[1];
                              var6 = var2[2];

                              for(int var28 = 3; var28 < var2.length; ++var28) {
                                 var6 = var6 + " " + var2[var28];
                              }

                              String var30 = UltimateFriends.getConfig().getMsgOverride();
                              if (var30 != null && !var30.isEmpty()) {
                                 var30 = var30.replace("$player", var23).replace("$msg", var6);
                                 ProxyServer.getInstance().getPluginManager().dispatchCommand(var3, var30);
                              } else if (!var4.getOptions().get(Options.Type.ALLOW_PRIVATE_MSG)) {
                                 var3.sendMessage(Message.OPTIONS_MSG_CANNOT_SEND_PRIVATE_MSG.getMsg(true));
                              } else if (var3.hasPermission("ultimatefriends.filter.bypass") || !UltimateFriends.getConfig().getChatFilter().check(var3, var6)) {
                                 try {
                                    UltimateFriends.getCommunicationModule().sendFriendMessage(var4, var23, var6);
                                 } catch (PlayerNotFriend var13) {
                                    var3.sendMessage(Message.FRIEND_NOT_FOUND.getMsg(true));
                                    return;
                                 } catch (PlayerIsOffline var14) {
                                    var3.sendMessage(Message.PLAYER_NOT_FOUND.getMsg(true));
                                    return;
                                 } catch (PlayerDenied var15) {
                                    var3.sendMessage(Message.OPTIONS_MSG_FRIEND_PRIVATE_MSG_DISABLED.getMsg(true));
                                    return;
                                 } catch (FriendOnDisabledServer var16) {
                                    var3.sendMessage(Message.FRIEND_ON_DISABLED_SERVER.getMsg(true));
                                    return;
                                 } catch (Throwable var17) {
                                    var3.sendMessage(Message.ERROR.getMsg(true));
                                    var17.printStackTrace();
                                    return;
                                 }

                                 var3.sendMessage((new ClickableMessage(Message.PRIVATE_MSG_TO.getMsg(true))).clickable(var23).hoverEvent(Action.SHOW_TEXT, (new ClickableMessage(Message.FRIEND_LIST_BUTTON_MSG_HOVER.getMsg())).clickable(var23).append().buildString()).clickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND, "/" + UltimateFriends.getConfig().getCmds().getCmd() + " msg " + var23 + " ").append().clickable(var6).append().build());
                                 var4.setLastMsgSender(var23);
                              }
                           }
                        } else if (var2[0].equalsIgnoreCase("bmsg")) {
                           if (var2.length >= 2) {
                              if (!var4.getOptions().get(Options.Type.SHOW_BROADCASTS)) {
                                 var3.sendMessage(Message.OPTIONS_MSG_CANNOT_SEND_BROADCAST.getMsg(true));
                                 return;
                              }

                              var23 = var2[1];

                              for(int var25 = 2; var25 < var2.length; ++var25) {
                                 var23 = var23 + " " + var2[var25];
                              }

                              if (!var3.hasPermission("ultimatefriends.filter.bypass") && UltimateFriends.getConfig().getChatFilter().check(var3, var23)) {
                                 return;
                              }

                              try {
                                 UltimateFriends.getCommunicationModule().sendFriendBroadcastMessage(var4, var23);
                              } catch (Throwable var18) {
                                 var3.sendMessage(Message.ERROR.getMsg(true));
                                 var18.printStackTrace();
                                 return;
                              }

                              var3.sendMessage((new ClickableMessage(Message.BROADCAST_TO.getMsg(true))).clickable(var23).append().build());
                           }

                        } else if (var2[0].equalsIgnoreCase("remove")) {
                           if (var2.length == 2) {
                              final Friend var24 = var4.getFriend(var2[1]);
                              if (var24 != null) {
                                 UltimateFriends.server.getScheduler().runAsync(UltimateFriends.plugin, new Runnable() {
                                    public void run() {
                                       try {
                                          UltimateFriends.getCommunicationModule().removeFriend(var4, var24);
                                       } catch (PlayerNotFriend var2) {
                                          var3.sendMessage(Message.FRIEND_NOT_FOUND.getMsg(true));
                                          return;
                                       } catch (Throwable var3x) {
                                          var3.sendMessage(Message.ERROR.getMsg(true));
                                          var3x.printStackTrace();
                                          return;
                                       }

                                       var3.sendMessage((new ClickableMessage(Message.FRIEND_REMOVED.getMsg(true))).clickable(var24.getPlayerName()).append().build());
                                    }
                                 });
                              } else {
                                 var3.sendMessage(Message.FRIEND_NOT_FOUND.getMsg(true));
                              }
                           }

                        } else if (var2[0].equalsIgnoreCase("add")) {
                           if (var2.length == 2) {
                              var23 = var2[1];
                              String finalVar2 = var23;
                              UltimateFriends.server.getScheduler().runAsync(UltimateFriends.plugin, () -> {
                                 try {
                                    if (!UltimateFriends.getCommunicationModule().addFriend(var4, finalVar2)) {
                                       var3.sendMessage(Message.REQUEST_SENT.getMsg(true));
                                    } else {
                                       var3.sendMessage((new ClickableMessage(Message.FRIEND_ADDED.getMsg(true))).clickable(finalVar2).append().build());
                                    }
                                 } catch (PlayerIsOffline var212) {
                                    var3.sendMessage(Message.PLAYER_NOT_FOUND.getMsg(true));
                                 } catch (PlayerAlreadyFriend var3x) {
                                    var3.sendMessage(Message.PLAYER_IS_ALREADY_FRIEND.getMsg(true));
                                 } catch (CannotAddYourself var4x) {
                                    var3.sendMessage(Message.CANNOT_ADD_YOURSELF.getMsg(true));
                                 } catch (PlayerDenied var51) {
                                    var3.sendMessage(Message.OPTIONS_MSG_FRIEND_REQUEST_DISABLED.getMsg(true));
                                 } catch (FriendOnDisabledServer var61) {
                                    var3.sendMessage(Message.FRIEND_ON_DISABLED_SERVER.getMsg(true));
                                 } catch (PlayerAlreadyRequested var7) {
                                    var3.sendMessage(Message.PLAYER_ALREADY_REQUESTED.getMsg(true));
                                 } catch (FriendListExceeded var8) {
                                    var3.sendMessage(Message.FRIEND_LIST_FULL.getMsg(true));
                                 } catch (Throwable var9) {
                                    var3.sendMessage(Message.ERROR.getMsg(true));
                                    var9.printStackTrace();
                                 }

                              });
                           }

                        } else if (var2[0].equalsIgnoreCase("connect")) {
                           if (var2.length == 2) {
                              if (UltimateFriends.getConfig().getDisable().getConnectionFrom().contains(var3.getServer().getInfo().getName())) {
                                 var3.sendMessage(Message.FRIEND_ON_DISABLED_CONNECTION_SERVER.getMsg(true));
                                 return;
                              }

                              var23 = var2[1];
                              String finalVar21 = var23;
                              UltimateFriends.server.getScheduler().runAsync(UltimateFriends.plugin, new Runnable() {
                                 public void run() {
                                    try {
                                       UltimateFriends.getCommunicationModule().connect(var4, finalVar21);
                                    } catch (PlayerNotFriend | PlayerIsOffline var2) {
                                       var3.sendMessage(Message.PLAYER_NOT_FOUND.getMsg(true));
                                    } catch (FriendOnDisabledServer var3x) {
                                       var3.sendMessage(Message.FRIEND_ON_DISABLED_SERVER.getMsg(true));
                                    } catch (ConnectionDisabledOnServer var4x) {
                                       var3.sendMessage(Message.FRIEND_ON_DISABLED_CONNECTION_SERVER.getMsg(true));
                                    } catch (Throwable var5) {
                                       var3.sendMessage(Message.ERROR.getMsg(true));
                                       var5.printStackTrace();
                                    }

                                 }
                              });
                           }

                        } else if (var2[0].equalsIgnoreCase("spy")) {
                           if (var3.hasPermission("ultimatefriends.spy")) {
                              if (!SocialSpy.isSpy(var3)) {
                                 SocialSpy.enableSpy(var3);
                                 var3.sendMessage(Message.SOCIAL_SPY_ENABLED.getMsg(true));
                              } else {
                                 SocialSpy.disableSpy(var3);
                                 var3.sendMessage(Message.SOCIAL_SPY_DISABLED.getMsg(true));
                              }
                           }

                        } else if (!var2[0].equalsIgnoreCase("showButtonText")) {
                           var3.sendMessage(Message.CMD_UNKNOWN.getMsg(true));
                        } else {
                           if (var2.length == 2) {
                              try {
                                 var5 = Integer.parseInt(var2[1]);
                              } catch (NumberFormatException var20) {
                                 return;
                              }

                              var6 = null;

                              try {
                                 Config.CustomButton var7 = (Config.CustomButton)UltimateFriends.getConfig().getCustomButtons().get(var5);
                                 if (var7.getType() == Config.CustomButtonType.TEXT) {
                                    var6 = var7.getValue();
                                 }
                              } catch (IndexOutOfBoundsException var19) {
                              }

                              if (var6 != null) {
                                 String[] var26 = var6.split("\\n");
                                 String[] var8 = var26;
                                 int var9 = var26.length;

                                 for(int var10 = 0; var10 < var9; ++var10) {
                                    String var11 = var8[var10];
                                    var3.sendMessage(var11);
                                 }
                              }
                           }

                        }
                     }
                  } else {
                     if (var2.length == 2) {
                        try {
                           var5 = Integer.parseInt(var2[1]);
                        } catch (NumberFormatException var21) {
                           return;
                        }

                        var4.setPage(var5);
                     }

                     UltimateFriends.server.getScheduler().runAsync(UltimateFriends.plugin, new Runnable() {
                        public void run() {
                           HashMap var1 = new HashMap();
                           Iterator var2 = var4.getFriends().iterator();

                           while(var2.hasNext()) {
                              Friend var3x = (Friend)var2.next();
                              String var4x = UltimateFriends.getCommunicationModule().getServer(var3x.getPlayerName());
                              if (var4x != null && !UltimateFriends.getConfig().getDisable().getOnlineStatus().contains(var4x)) {
                                 var1.put(var3x.getPlayerName(), var4x);
                              }
                           }

                           Cmds.this.renderFriendsList(var3, var4, var1);
                        }
                     });
                  }
               }
            }
         }
      }
   }

   private void renderFriendsList(ProxiedPlayer var1, PlayerProfile var2, final HashMap<String, String> var3) {
      Object var4 = new ArrayList(var2.getFriends());
      Config.SortType var5 = UltimateFriends.getConfig().getSort();
      if (var5 == Config.SortType.ALPHA) {
         Collections.sort((List)var4, new Comparator<Friend>() {
            public int compare(Friend var1, Friend var2) {
               return var1.getPlayerName().compareToIgnoreCase(var2.getPlayerName());
            }
         });
      } else if (var5 == Config.SortType.ONLINE) {
         Collections.sort((List)var4, new Comparator<Friend>() {
            public int compare(Friend var1, Friend var2) {
               boolean var3x = var3.containsKey(var1.getPlayerName());
               boolean var4 = var3.containsKey(var2.getPlayerName());
               if (var3x == var4) {
                  return var1.getPlayerName().compareToIgnoreCase(var2.getPlayerName());
               } else {
                  return var3x ? 1 : -1;
               }
            }
         });
      }

      boolean var6 = false;
      int var7 = 0;
      int var8 = var2.getPage();
      int var9 = UltimateFriends.getConfig().getPerPage();
      if (var9 > 0) {
         Collections.reverse((List)var4);
         ListPaginator var10 = new ListPaginator((List)var4);
         var10.perPage(var9).page(var8);
         var6 = var10.pages() > 1;
         var8 = var10.page();
         var7 = var10.pages();
         var4 = var10.sublist();
         Collections.reverse((List)var4);
      }

      var1.sendMessage(Message.FRIEND_LIST_SPACER_TOP.getMsg());
      if (((List)var4).isEmpty()) {
         var1.sendMessage(Message.FRIEND_LIST_EMPTY.getMsg());
      }

      Iterator var17 = ((List)var4).iterator();

      while(var17.hasNext()) {
         Friend var11 = (Friend)var17.next();
         String var12 = (String)var3.get(var11.getPlayerName());
         if (var12 != null) {
            var11.refreshLastSeen();
            var12 = UltimateFriends.getConfig().getServerAliases().translate(var12);
            var1.sendMessage((new ClickableMessage(Message.FRIEND_LIST_FRIEND_ONLINE.getMsg())).clickable(Message.FRIEND_LIST_BUTTON_REMOVE_TEXT.getMsg()).clickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND, "/" + UltimateFriends.getConfig().getCmds().getCmd() + " remove " + var11.getPlayerName()).hoverEvent(Action.SHOW_TEXT, (new ClickableMessage(Message.FRIEND_LIST_BUTTON_REMOVE_HOVER.getMsg())).clickable(var11.getPlayerName()).append().buildString()).append().clickable(Message.FRIEND_LIST_BUTTON_MSG_TEXT.getMsg()).clickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND, "/" + UltimateFriends.getConfig().getCmds().getCmd() + " msg " + var11.getPlayerName() + " ").hoverEvent(Action.SHOW_TEXT, (new ClickableMessage(Message.FRIEND_LIST_BUTTON_MSG_HOVER.getMsg())).clickable(var11.getPlayerName()).append().buildString()).append().clickable(Message.FRIEND_LIST_BUTTON_CONNECT_TEXT.getMsg()).clickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/" + UltimateFriends.getConfig().getCmds().getCmd() + " connect " + var11.getPlayerName()).hoverEvent(Action.SHOW_TEXT, (new ClickableMessage(Message.FRIEND_LIST_BUTTON_CONNECT_HOVER.getMsg())).clickable(var12).append().clickable(var11.getPlayerName()).append().buildString()).append().clickable(var11.getPlayerName()).append().build());
         } else {
            ClickableMessage var13 = (new ClickableMessage(Message.FRIEND_LIST_FRIEND_OFFLINE.getMsg())).clickable(Message.FRIEND_LIST_BUTTON_REMOVE_TEXT.getMsg()).clickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND, "/" + UltimateFriends.getConfig().getCmds().getCmd() + " remove " + var11.getPlayerName()).hoverEvent(Action.SHOW_TEXT, (new ClickableMessage(Message.FRIEND_LIST_BUTTON_REMOVE_HOVER.getMsg())).clickable(var11.getPlayerName()).append().buildString()).append();
            if (var11.getLastSeen() > 0L) {
               var13 = var13.clickable(var11.getPlayerName()).hoverEvent(Action.SHOW_TEXT, (new ClickableMessage(Message.FRIEND_LIST_FRIEND_LAST_SEEN.getMsg())).clickable(Utils.formatTime(var11.getLastSeen())).append().buildString()).append();
            } else {
               var13 = var13.clickable(var11.getPlayerName()).append();
            }

            var1.sendMessage(var13.build());
         }
      }

      if (var6) {
         ClickableMessage var18 = (new ClickableMessage(Message.FRIEND_LIST_PAGINATOR.getMsg())).clickable(Message.FRIEND_LIST_BUTTON_PREV_PAGE_TEXT.getMsg()).clickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/" + UltimateFriends.getConfig().getCmds().getCmd() + " page " + (var8 - 1)).hoverEvent(Action.SHOW_TEXT, Message.FRIEND_LIST_BUTTON_PREV_PAGE_HOVER.getMsg()).append().clickable(var8 + 1 + "").append().clickable(var7 + "").append().clickable(Message.FRIEND_LIST_BUTTON_NEXT_PAGE_TEXT.getMsg()).clickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/" + UltimateFriends.getConfig().getCmds().getCmd() + " page " + (var8 + 1)).hoverEvent(Action.SHOW_TEXT, Message.FRIEND_LIST_BUTTON_NEXT_PAGE_HOVER.getMsg()).append();
         var1.sendMessage(var18.build());
      }

      StringBuilder var19 = new StringBuilder(Message.FRIEND_LIST_MENU.getMsg());
      List var20 = UltimateFriends.getConfig().getCustomButtons();
      Iterator var21 = var20.iterator();

      while(var21.hasNext()) {
         Config.CustomButton var23 = (Config.CustomButton)var21.next();
         var19.append(var23.getFormat());
      }

      ClickableMessage var22 = (new ClickableMessage(var19.toString())).clickable(Message.FRIEND_LIST_BUTTON_ADD_TEXT.getMsg()).clickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND, "/" + UltimateFriends.getConfig().getCmds().getCmd() + " add ").hoverEvent(Action.SHOW_TEXT, Message.FRIEND_LIST_BUTTON_ADD_HOVER.getMsg()).append().clickable(Message.FRIEND_LIST_BUTTON_BROADCAST_TEXT.getMsg()).clickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND, "/" + UltimateFriends.getConfig().getCmds().getCmd() + " bmsg ").hoverEvent(Action.SHOW_TEXT, Message.FRIEND_LIST_BUTTON_BROADCAST_HOVER.getMsg()).append().clickable(Message.FRIEND_LIST_BUTTON_OPTIONS_TEXT.getMsg()).clickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/" + UltimateFriends.getConfig().getCmds().getCmd() + " options").hoverEvent(Action.SHOW_TEXT, Message.FRIEND_LIST_BUTTON_OPTIONS_HOVER.getMsg()).append();
      int var24 = 0;

      for(Iterator var14 = var20.iterator(); var14.hasNext(); ++var24) {
         Config.CustomButton var15 = (Config.CustomButton)var14.next();
         ClickableMessage.ClickablePart var16 = var22.clickable(var15.getButtonText());
         switch(var15.getType()) {
         case TEXT:
            var16.clickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/" + UltimateFriends.getConfig().getCmds().getCmd() + " showButtonText " + var24);
            break;
         case SUGGEST:
            var16.clickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND, var15.getValue());
            break;
         case URL:
            var16.clickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_URL, var15.getValue());
            break;
         default:
            var16.clickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, var15.getValue());
         }

         if (!var15.getHoverText().isEmpty() && !var15.getHoverText().equals(" ")) {
            var16.hoverEvent(Action.SHOW_TEXT, var15.getHoverText());
         }

         var16.append();
      }

      var1.sendMessage(var22.build());
      var1.sendMessage(Message.FRIEND_LIST_SPACER_BOTTOM.getMsg());
   }

   private void renderOptions(ProxiedPlayer var1, PlayerProfile var2) {
      var1.sendMessage(Message.OPTIONS_SPACER_TOP.getMsg());
      Options var3 = var2.getOptions();
      Options.Type[] var4 = Options.Type.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Options.Type var7 = var4[var6];
         boolean var8 = var3.get(var7);
         Message var9;
         if (var8) {
            var9 = Message.OPTIONS_ENABLED;
         } else {
            var9 = Message.OPTIONS_DISABLED;
         }

         if (!var7.getMsg().getMsg().trim().isEmpty()) {
            var1.sendMessage((new ClickableMessage(var9.getMsg())).clickable(Message.OPTIONS_BUTTON_DISABLE_TEXT.getMsg()).clickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/" + UltimateFriends.getConfig().getCmds().getCmd() + " options " + var7.name() + " false").hoverEvent(Action.SHOW_TEXT, Message.OPTIONS_BUTTON_DISABLE_HOVER.getMsg()).append().clickable(Message.OPTIONS_BUTTON_ENABLE_TEXT.getMsg()).clickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/" + UltimateFriends.getConfig().getCmds().getCmd() + " options " + var7.name() + " true").hoverEvent(Action.SHOW_TEXT, Message.OPTIONS_BUTTON_ENABLE_HOVER.getMsg()).append().clickable(var7.getMsg().getMsg()).append().build());
         }
      }

      var1.sendMessage(Message.OPTIONS_SPACER_BOTTOM.getMsg());
   }

   private long setCoolDown(ProxiedPlayer var1) {
      long var2 = System.currentTimeMillis();
      cooledDown.put(var1.getName().toLowerCase(), var2);
      return var2;
   }

   private boolean canUse(ProxiedPlayer var1) {
      String var2 = var1.getName().toLowerCase();
      if (!cooledDown.containsKey(var2)) {
         return true;
      } else {
         long var3 = (Long)cooledDown.get(var2);
         long var5 = System.currentTimeMillis() - var3;
         if (var5 < (long)this.coolDown) {
            var1.sendMessage(Message.CMD_SPAM.getMsg(true));
            return false;
         } else {
            return true;
         }
      }
   }

   public String getCmd() {
      return this.cmd;
   }
}
