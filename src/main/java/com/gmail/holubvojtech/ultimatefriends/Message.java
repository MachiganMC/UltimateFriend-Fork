package com.gmail.holubvojtech.ultimatefriends;

public enum Message {
   PREFIX("§b[§eFriends§b] "),
   FRIEND_JOINED_SERVER("{§f§l}$ §6joined the server"),
   FRIEND_LEFT_SERVER("{§f§l}$ §5left the server"),
   FRIEND_SWITCH_SERVER("{§f§l}$ §2switched server {§b}$ §e§l-> {§d}$"),
   PRIVATE_MSG_FROM("§5[from {§6}$§5] {§d}$"),
   PRIVATE_MSG_TO("§5[to {§6}$§5] {§d}$"),
   BROADCAST_FROM("§5[§eBroadcast §5from {§6}$§5] {§d}$"),
   BROADCAST_TO("§5[§eBroadcast§5] {§d}$"),
   SOCIAL_SPY("§8[{§7}$ -> {§7}$§8] {§7}$"),
   REQUEST_SENT("§aRequest has been sent"),
   FRIEND_REQUEST("§bRequest from player {§e}$ §9[{§a§l}$§9]"),
   FRIEND_REQUEST_BUTTON_ACCEPT_TEXT("Accept"),
   FRIEND_REQUEST_BUTTON_ACCEPT_HOVER("§a§lAccept friendship"),
   FRIEND_ADDED("§a§lPlayer {§e}$ §a§lhas just become your friend"),
   FRIEND_REMOVED("§4§lFriend {§e}$ §4§lhas been removed from friends"),
   FRIEND_REMOVED_YOU("§4§lPlayer {§e}$ §4§lremoved you from friends"),
   CMD_SPAM("§cWait, you are sending this command too fast"),
   CMD_UNKNOWN("§cUnknown command, use /f to display friends"),
   CMD_UNKNOWN_MSG("§cUse /f msg <name> <message> to send messages"),
   CANNOT_ADD_YOURSELF("§dYou cannot add yourself"),
   FRIENDS_DISABLED("§cFriends are disabled on this server"),
   FRIENDS_NOT_LOADED("§cFriends have not been loaded yet"),
   FRIEND_NOT_FOUND("§cFriend not found"),
   FRIEND_LIST_FULL("§cYour friend list is full"),
   FRIEND_ON_DISABLED_SERVER("§cFriends are disabled on server where is your friend"),
   FRIEND_ON_DISABLED_CONNECTION_SERVER("§cConnections to server are disabled on this server"),
   PLAYER_NOT_FOUND("§cPlayer is offline"),
   PLAYER_IS_ALREADY_FRIEND("§cThis player is already your friend"),
   PLAYER_ALREADY_REQUESTED("§cFriend request already sent"),
   SOCIAL_SPY_ENABLED("§aSocial Spy enabled"),
   SOCIAL_SPY_DISABLED("§cSocial Spy disabled"),
   FRIEND_LIST_SPACER_TOP("§9§m■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■"),
   FRIEND_LIST_FRIEND_ONLINE("§9[{§c;1}$ {§b;2}$ {§e;3}$§9] {§a;4}$"),
   FRIEND_LIST_FRIEND_OFFLINE("§9[{§c;1}$§9] {§7;2}$"),
   FRIEND_LIST_FRIEND_LAST_SEEN("§7Last seen {§a}$"),
   FRIEND_LIST_PAGINATOR("§9[ {§b;1}$ {§7;2}$§7/{§7;3}$ {§b;4}$ §9]"),
   FRIEND_LIST_BUTTON_PREV_PAGE_TEXT("<<"),
   FRIEND_LIST_BUTTON_PREV_PAGE_HOVER("§3Previous page"),
   FRIEND_LIST_BUTTON_NEXT_PAGE_TEXT(">>"),
   FRIEND_LIST_BUTTON_NEXT_PAGE_HOVER("§3Next page"),
   FRIEND_LIST_EMPTY("\n§7You don't have any friends here,\n§7click on Add button to add some\n"),
   FRIEND_LIST_MENU("§9[{§2;1}$§9] §9[{§d;2}$§9] §9[{§6;3}$§9]"),
   FRIEND_LIST_BUTTON_REMOVE_TEXT("✖"),
   FRIEND_LIST_BUTTON_REMOVE_HOVER("§cRemove friend {§e}$\n§7Click here and confirm suggested command"),
   FRIEND_LIST_BUTTON_MSG_TEXT("✎"),
   FRIEND_LIST_BUTTON_MSG_HOVER("§2For private msg write\n§b§l/f msg {§3}$ §6<text>\n§2Or click here"),
   FRIEND_LIST_BUTTON_CONNECT_TEXT("➔"),
   FRIEND_LIST_BUTTON_CONNECT_HOVER("§2Click for connection to\n§2server {§e}$\n§2where is player {§a}$"),
   FRIEND_LIST_BUTTON_ADD_TEXT("Add"),
   FRIEND_LIST_BUTTON_ADD_HOVER("§2If you want to add friend, use\n§b§l/f add §6<player_name>"),
   FRIEND_LIST_BUTTON_BROADCAST_TEXT("Broadcast"),
   FRIEND_LIST_BUTTON_BROADCAST_HOVER("§2If you want to send message to all friends, use\n§b§l/f bmsg §6<text>\n§7Or click here"),
   FRIEND_LIST_BUTTON_OPTIONS_TEXT("Options"),
   FRIEND_LIST_BUTTON_OPTIONS_HOVER("§e§lOptions"),
   FRIEND_LIST_SPACER_BOTTOM("§9§m■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■"),
   OPTIONS_SPACER_TOP("§6§m■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■"),
   OPTIONS_OPT_SHOW_JOIN("Show player join msg"),
   OPTIONS_OPT_SHOW_LEAVE("Show player leave msg"),
   OPTIONS_OPT_SHOW_SWITCH("Show player switch server msg"),
   OPTIONS_OPT_ALLOW_REQUESTS("Allow friend requests"),
   OPTIONS_OPT_ALLOW_PRIVATE_MSG("Allow private messages"),
   OPTIONS_OPT_SHOW_BROADCAST("Show broadcasts from friends"),
   OPTIONS_ENABLED("§9[{§c;1}$ {§a;2}$§9] {§2;3}$"),
   OPTIONS_DISABLED("§9[{§c;1}$ {§a;2}$§9] {§4;3}$"),
   OPTIONS_BUTTON_ENABLE_TEXT("✔"),
   OPTIONS_BUTTON_ENABLE_HOVER("§aEnable"),
   OPTIONS_BUTTON_DISABLE_TEXT("✖"),
   OPTIONS_BUTTON_DISABLE_HOVER("§cDisable"),
   OPTIONS_MSG_FRIEND_REQUEST_DISABLED("§cThis player doesn't accept requests"),
   OPTIONS_MSG_FRIEND_PRIVATE_MSG_DISABLED("§cThis player doesn't accept private msg"),
   OPTIONS_MSG_CANNOT_SEND_PRIVATE_MSG("§cYou cannot send private msg, if you have it disabled"),
   OPTIONS_MSG_CANNOT_SEND_BROADCAST("§cYou cannot send broadcast, if you have it disabled"),
   OPTIONS_SPACER_BOTTOM("§6§m■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■"),
   ERROR("§cSomething went wrong");

   private String msg;

   private Message(String var3) {
      this.msg = var3;
   }

   public String getMsg() {
      return this.getMsg(false);
   }

   public void setMsg(String var1) {
      this.msg = var1;
   }

   public String getMsg(boolean var1) {
      return var1 ? PREFIX.msg + this.msg : this.msg;
   }
}
