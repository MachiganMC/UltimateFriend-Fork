package com.gmail.holubvojtech.ultimatefriends;

import com.gmail.holubvojtech.jsql.connectors.DatabaseConnector;
import com.gmail.holubvojtech.jsql.connectors.SQLiteConnector;
import com.gmail.holubvojtech.ultimatefriends.commands.Cmds;
import com.gmail.holubvojtech.ultimatefriends.commands.MsgCmd;
import com.gmail.holubvojtech.ultimatefriends.commands.ReplyCmd;
import com.gmail.holubvojtech.ultimatefriends.communication.CommunicationModule;
import com.gmail.holubvojtech.ultimatefriends.communication.bungee.BungeeModule;
import com.gmail.holubvojtech.ultimatefriends.communication.redis.RedisModule;
import com.gmail.holubvojtech.ultimatefriends.storage.Storage;
import com.gmail.holubvojtech.ultimatefriends.storage.mysql.MySQL;
import com.gmail.holubvojtech.ultimatefriends.storage.sqlite.SQLite;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

public class Config {
   private File configFile;
   private Configuration config;
   private Storage storage;
   private Cmds cmds;
   private ReplyCmd replyCmd;
   private MsgCmd msgCmd;
   private LanguageLoader languageLoader;
   private Config.Core core;
   private CommunicationModule communicationModule;
   private Config.Options options;
   private Config.Disable disable;
   private List<String> defaultSocialSpyPlayers;
   private List<Config.CustomButton> customButtons;
   private Config.ServerAliases serverAliases;
   private Config.ChatFilter chatFilter;
   private Config.ChatLogger filterLog;
   private String msgOverride;
   private int maxFriends;
   private int perPage;
   private Map<String, Integer> maxFriendsGroup;
   private Config.SortType sort;
   private PlayerLookup playerLookup;
   private SimpleDateFormat dateFormat;

   public Config(File var1) {
      this.sort = Config.SortType.NONE;
      this.configFile = var1;
       try {
           this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(var1);
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
       this.loadCore();
      this.loadStorage();
      this.loadCommands();
      this.loadMaxFriends();
       try {
           this.loadLanguage();
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
       this.loadPlayerLookup();
      this.loadCommunication();
      this.loadOptions();
      this.loadDisable();
      this.loadDefaultSocialSpyPlayers();
      this.loadCustomButtons();
      this.loadServerAliases();
      this.loadChatFilters();
      this.loadFilterLog();
   }

   public void reload() throws IOException {
      this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.configFile);
      this.loadServerAliases();
      this.loadCustomButtons();
      this.loadDefaultSocialSpyPlayers();
      this.loadDisable();
      this.loadOptions();
      this.loadLanguage();
      this.loadMaxFriends();
      this.loadChatFilters();
      this.loadFilterLog();
   }

   public Configuration getConfig() {
      return this.config;
   }

   public Storage getStorage() {
      return this.storage;
   }

   public Cmds getCmds() {
      return this.cmds;
   }

   public ReplyCmd getReplyCmd() {
      return this.replyCmd;
   }

   public MsgCmd getMsgCmd() {
      return this.msgCmd;
   }

   public String getMsgOverride() {
      return this.msgOverride;
   }

   public LanguageLoader getLanguageLoader() {
      return this.languageLoader;
   }

   public Config.Core getCore() {
      return this.core;
   }

   public CommunicationModule getCommunicationModule() {
      return this.communicationModule;
   }

   public Config.Options getOptions() {
      return this.options;
   }

   public Config.Disable getDisable() {
      return this.disable;
   }

   public int getMaxFriends() {
      return this.maxFriends;
   }

   public Config.SortType getSort() {
      return this.sort;
   }

   public int getPerPage() {
      return this.perPage;
   }

   public SimpleDateFormat getDateFormat() {
      return this.dateFormat;
   }

   public int getMaxFriends(ProxiedPlayer var1) {
      if (var1 == null) {
         return this.getMaxFriends();
      } else {
         Integer var2 = null;
         Iterator var3 = this.maxFriendsGroup.entrySet().iterator();

         while(true) {
            String var5;
            int var6;
            do {
               do {
                  if (!var3.hasNext()) {
                     if (var2 == null) {
                        var2 = this.getMaxFriends();
                     }

                     return var2;
                  }

                  Entry var4 = (Entry)var3.next();
                  var5 = (String)var4.getKey();
                  var6 = (Integer)var4.getValue();
               } while(!var1.hasPermission(var5));
            } while(var2 != null && var6 <= var2);

            var2 = var6;
         }
      }
   }

   public PlayerLookup getPlayerLookup() {
      return this.playerLookup;
   }

   public List<String> getDefaultSocialSpyPlayers() {
      return this.defaultSocialSpyPlayers;
   }

   public List<Config.CustomButton> getCustomButtons() {
      return this.customButtons;
   }

   public Config.ServerAliases getServerAliases() {
      return this.serverAliases;
   }

   public Config.ChatFilter getChatFilter() {
      return this.chatFilter;
   }

   private void loadFilterLog() {
      this.filterLog = new Config.ChatLogger();
      this.filterLog.enabled = this.config.getBoolean("chat.filterLog.enabled", false);
      this.filterLog.timeFormat = this.config.getString("chat.filterLog.timeFormat", this.filterLog.timeFormat);
      this.filterLog.format = this.config.getString("chat.filterLog.format", this.filterLog.format);
   }

   private void loadChatFilters() {
      this.chatFilter = new Config.ChatFilter();
      Configuration var1 = this.config.getSection("chat.filters");
      Collection var2 = var1.getKeys();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         String var5 = this.config.getString("chat.filters." + var4 + ".regex", "");
         String var6 = this.config.getString("chat.filters." + var4 + ".ignore", "");
         String var7 = this.config.getString("chat.filters." + var4 + ".runCmd", "");
         if (!var5.isEmpty()) {
            Config.ChatFilter.Filter var8 = this.chatFilter.createFilter(var4, var5, var6, var7);
            this.chatFilter.filters.add(var8);
         }
      }

      if (!this.chatFilter.filters.isEmpty()) {
         UltimateFriends.logger.info("Loaded " + this.chatFilter.filters.size() + " chat filters");
      }

   }

   private void loadServerAliases() {
      this.serverAliases = new Config.ServerAliases(this.config.getStringList("serverAliases"));
   }

   private void loadCustomButtons() {
      this.customButtons = new ArrayList();
      List var1 = this.config.getStringList("customButtons");
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         String[] var4 = var3.split(";");
         if (var4.length == 5) {
            this.customButtons.add(new Config.CustomButton(ChatColor.translateAlternateColorCodes('&', var4[0]), var4[1], ChatColor.translateAlternateColorCodes('&', var4[2]), var4[3], ChatColor.translateAlternateColorCodes('&', var4[4])));
         }
      }

   }

   private void loadDefaultSocialSpyPlayers() {
      this.defaultSocialSpyPlayers = this.config.getStringList("socialSpy.default");
   }

   private void loadDisable() {
      this.disable = new Config.Disable();
      this.disable.setConnection(this.config.getStringList("disable.connection"));
      this.disable.setConnectionFrom(this.config.getStringList("disable.connectionFrom"));
      this.disable.setPlugin(this.config.getStringList("disable.plugin"));
      this.disable.setOnlineStatus(this.config.getStringList("disable.onlineStatus"));
   }

   private void loadOptions() {
      this.options = new Config.Options();
      this.options.getDefaults().setShow_msg_join(this.config.getBoolean("options.default.show_msg_join", true));
      this.options.getDefaults().setShow_msg_left(this.config.getBoolean("options.default.show_msg_left", true));
      this.options.getDefaults().setShow_msg_switch(this.config.getBoolean("options.default.show_msg_switch", true));
      this.options.getDefaults().setAllow_requests(this.config.getBoolean("options.default.allow_requests", true));
      this.options.getDefaults().setAllow_private_msg(this.config.getBoolean("options.default.allow_private_msg", true));
      this.options.getDefaults().setShow_broadcast(this.config.getBoolean("options.default.show_broadcast", true));
   }

   private void loadCommunication() {
      String var1 = this.config.getString("communication.module");
      if (var1.equalsIgnoreCase("bungee")) {
         this.communicationModule = new BungeeModule();
      } else if (var1.equalsIgnoreCase("redis")) {
         this.communicationModule = new RedisModule();
      } else {
         if (!var1.equalsIgnoreCase("cobweb")) {
            throw new IllegalArgumentException("Unknown communication module");
         }


         if (this.playerLookup == PlayerLookup.UID) {
            this.playerLookup = PlayerLookup.PLAYER_NAME;
         }
      }

   }

   private void loadPlayerLookup() {
      String var1 = this.config.getString("playerLookup.type");
      if (var1.equalsIgnoreCase("name")) {
         this.playerLookup = PlayerLookup.PLAYER_NAME;
      } else {
         if (!var1.equalsIgnoreCase("uuid")) {
            throw new IllegalArgumentException("Unknown player lookup type");
         }

         if (!ProxyServer.getInstance().getConfig().isOnlineMode() && !this.core.isAllowUuidInOfflineMode()) {
            throw new IllegalStateException("cannot use uuid lookup with online-mode disabled");
         }

         this.playerLookup = PlayerLookup.UID;
      }

   }

   private void loadCore() {
      this.core = new Config.Core();
      this.core.setVer(this.config.getInt("core.ver"));
      this.core.setGtApiPass(this.config.getString("core.gtApiPass"));
      this.core.setResetLang(this.config.getBoolean("core.lang.reset"));
      this.core.setRedis_msgSplit(this.config.getString("core.redisBungee.msgSplit"));
      this.core.setRedis_channel_friendRequest(this.config.getString("core.redisBungee.channels.fReq"));
      this.core.setRedis_channel_friendRemove(this.config.getString("core.redisBungee.channels.fRem"));
      this.core.setRedis_channel_friendMessage(this.config.getString("core.redisBungee.channels.msg"));
      this.core.setRedis_channel_friendBroadcast(this.config.getString("core.redisBungee.channels.bMsg"));
      this.core.setDb_tables_fList(this.config.getString("core.db.tables.fList"));
      this.core.setDb_tables_options(this.config.getString("core.db.tables.options"));
      this.core.setUuidNotUnique(true);
      this.core.setAllowUuidInOfflineMode(this.config.getBoolean("core.allowUuidInOfflineMode"));
   }

   private void loadLanguage() throws IOException {
      File var1 = new File(UltimateFriends.plugin.getDataFolder(), "lang");
      InputStream var2;
      InputStream var3;
      if (!var1.exists()) {
         if (!var1.mkdir()) {
            throw new IOException("Cannot create lang folder!");
         }

         var2 = UltimateFriends.plugin.getResourceAsStream("lang/lang_en_GB.yml");
         Files.copy(var2, new File(var1, "lang_en_GB.yml").toPath());
         var2.close();
         var3 = UltimateFriends.plugin.getResourceAsStream("lang/README.txt");
         Files.copy(var3, new File(var1, "README.txt").toPath());
         var3.close();
      }

      var2 = UltimateFriends.plugin.getResourceAsStream("lang/lang_en_GB.yml");
      Files.copy(var2, (new File(var1, "lang_en_GB.yml")).toPath(), new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
      var2.close();
      var3 = UltimateFriends.plugin.getResourceAsStream("lang/README.txt");
      Files.copy(var3, (new File(var1, "README.txt")).toPath(), new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
      var3.close();

      File var4 = new File(var1, this.config.getString("language.file"));
      this.languageLoader = new LanguageLoader(var4);
      this.dateFormat = new SimpleDateFormat(this.config.getString("language.dateFormat", "dd.MM. HH:mm"));
   }

   private void loadMaxFriends() {
      this.maxFriends = this.config.getInt("friendList.maxFriends", 24);
      this.maxFriendsGroup = new HashMap();
      Configuration var1 = this.config.getSection("friendList.groups");
      Collection var2 = var1.getKeys();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         String var5 = var1.getString(var4 + ".permission", (String)null);
         int var6 = var1.getInt(var4 + ".maxFriends", this.maxFriends);
         this.maxFriendsGroup.put(var5, var6);
      }

      String var8 = this.config.getString("friendList.sort", "none");

      try {
         this.sort = Config.SortType.valueOf(var8.toUpperCase());
      } catch (IllegalArgumentException var7) {
      }

      this.perPage = this.config.getInt("friendList.perPage", -1);
   }

   private void loadCommands() {
      int var1 = this.config.getInt("commands.coolDown", 1500);
      String var2 = this.config.getString("commands.cmd", "f");
      List var3 = this.config.getStringList("commands.aliases");
      String var4 = this.config.getBoolean("commands.usePermission") ? "ultimatefriends.cmd" : null;
      this.cmds = new Cmds(var2, (String[])var3.toArray(new String[var3.size()]), var4, Math.max(400, var1));
      boolean var5 = this.config.getBoolean("commands.reply.enable", false);
      if (var5) {
         String var6 = this.config.getString("commands.reply.cmd", (String)null);
         if (var6 != null) {
            this.replyCmd = new ReplyCmd(var6);
         }
      }

      boolean var7 = this.config.getBoolean("commands.msgCmd", false);
      if (var7) {
         this.msgCmd = new MsgCmd("msg");
      }

      this.msgOverride = this.config.getString("commands.override.msg", (String)null);
   }

   private void loadStorage() {
      String var1 = this.config.getString("storage.module", "");
      final String var2;
      if (var1.equalsIgnoreCase("mysql")) {
         var2 = this.config.getString("storage.mysql.hostName");
         final int var3 = this.config.getInt("storage.mysql.port");
         final String var4 = this.config.getString("storage.mysql.database");
         final String var5 = this.config.getString("storage.mysql.user");
         final String var6 = this.config.getString("storage.mysql.password");
         String var7 = "utf8";
         final boolean var8 = this.config.getBoolean("storage.mysql.autoReconnect");
         final boolean var9 = this.config.getBoolean("storage.mysql.ignoreSsl");
         DatabaseConnector var10 = new DatabaseConnector() {
            public Connection connect() {
               String var1 = "jdbc:mysql://" + var2 + ":" + var3;
               if (var4 != null) {
                  var1 = var1 + "/" + var4;
               }

               var1 = var1 + "?characterEncoding=utf8";
               if (var8) {
                  var1 = var1 + "&autoReconnect=true";
               }

               if (var9) {
                  var1 = var1 + "&useSSL=false";
               }

               try {
                  return DriverManager.getConnection(var1, var5, var6);
               } catch (SQLException var3x) {
                  var3x.printStackTrace();
                  return null;
               }
            }
         };
         this.storage = new MySQL(var10);
      } else {
         if (!var1.equalsIgnoreCase("sqlite")) {
            throw new IllegalArgumentException("Unknown storage");
         }

         var2 = this.config.getString("storage.sqlite.file", "sql-storage.db");

         try {
            Class.forName("org.sqlite.JDBC");
         } catch (ClassNotFoundException var11) {
            SQLite.downloadDriver();
            throw new RuntimeException("driver not found, try restarting your bungeecord");
         }

         File var12 = UltimateFriends.plugin.getDataFolder();
         File var13 = new File(var12, var2);
         this.storage = new SQLite(new SQLiteConnector(var13));
      }

   }

   private void notNull(Object var1) {
      if (var1 == null) {
         throw new NullPointerException();
      }
   }

   public final class Core {
      private int ver;
      private String gtApiPass;
      private boolean resetLang;
      private String redis_msgSplit;
      private String redis_channel_friendRequest;
      private String redis_channel_friendRemove;
      private String redis_channel_friendMessage;
      private String redis_channel_friendBroadcast;
      private String db_tables_fList;
      private String db_tables_options;
      private boolean uuidNotUnique;
      private boolean allowUuidInOfflineMode;

      private Core() {
         this.ver = 2;
         this.gtApiPass = "";
         this.resetLang = false;
         this.redis_msgSplit = ";";
         this.redis_channel_friendRequest = "ultimateFriends_friendRequest";
         this.redis_channel_friendRemove = "ultimateFriends_friendRemove";
         this.redis_channel_friendMessage = "ultimateFriends_message";
         this.redis_channel_friendBroadcast = "ultimateFriends_broadcast";
         this.db_tables_fList = "friend_list";
         this.db_tables_options = "options";
         this.uuidNotUnique = false;
         this.allowUuidInOfflineMode = false;
      }

      public String[] getRedisChannels() {
         return new String[]{this.redis_channel_friendRequest, this.redis_channel_friendRemove, this.redis_channel_friendMessage, this.redis_channel_friendBroadcast};
      }

      public int ver() {
         return this.ver;
      }

      private void setVer(int var1) {
         this.ver = var1;
      }

      public String gtApiPass() {
         return this.gtApiPass;
      }

      private void setGtApiPass(String var1) {
         Config.this.notNull(var1);
         this.gtApiPass = var1;
      }

      public boolean resetLang() {
         return this.resetLang;
      }

      private void setResetLang(boolean var1) {
         Config.this.notNull(var1);
         this.resetLang = var1;
      }

      public String redis_msgSplit() {
         return this.redis_msgSplit;
      }

      public void setRedis_msgSplit(String var1) {
         Config.this.notNull(var1);
         this.redis_msgSplit = var1;
      }

      public String redis_channel_friendRequest() {
         return this.redis_channel_friendRequest;
      }

      private void setRedis_channel_friendRequest(String var1) {
         Config.this.notNull(var1);
         this.redis_channel_friendRequest = var1;
      }

      public String redis_channel_friendRemove() {
         return this.redis_channel_friendRemove;
      }

      private void setRedis_channel_friendRemove(String var1) {
         Config.this.notNull(var1);
         this.redis_channel_friendRemove = var1;
      }

      public String redis_channel_friendMessage() {
         return this.redis_channel_friendMessage;
      }

      private void setRedis_channel_friendMessage(String var1) {
         Config.this.notNull(var1);
         this.redis_channel_friendMessage = var1;
      }

      public String redis_channel_friendBroadcast() {
         return this.redis_channel_friendBroadcast;
      }

      private void setRedis_channel_friendBroadcast(String var1) {
         Config.this.notNull(var1);
         this.redis_channel_friendBroadcast = var1;
      }

      /** @deprecated */
      @Deprecated
      public String db_tables_fList() {
         return this.db_tables_fList;
      }

      public void setDb_tables_fList(String var1) {
         Config.this.notNull(var1);
         this.db_tables_fList = var1;
      }

      /** @deprecated */
      @Deprecated
      public String db_tables_options() {
         return this.db_tables_options;
      }

      public void setDb_tables_options(String var1) {
         Config.this.notNull(var1);
         this.db_tables_options = var1;
      }

      public boolean isUuidNotUnique() {
         return this.uuidNotUnique;
      }

      public void setUuidNotUnique(boolean var1) {
         this.uuidNotUnique = var1;
      }

      public boolean isAllowUuidInOfflineMode() {
         return this.allowUuidInOfflineMode;
      }

      public void setAllowUuidInOfflineMode(boolean var1) {
         this.allowUuidInOfflineMode = var1;
      }

      // $FF: synthetic method
      Core(Object var2) {
         this();
      }
   }

   public final class Disable {
      private List<String> connection;
      private List<String> connectionFrom;
      private List<String> plugin;
      private List<String> onlineStatus;

      private Disable() {
      }

      public List<String> getConnectionFrom() {
         return this.connectionFrom;
      }

      public void setConnectionFrom(List<String> var1) {
         this.connectionFrom = var1;
      }

      public List<String> getConnection() {
         return this.connection;
      }

      private void setConnection(List<String> var1) {
         this.connection = var1;
      }

      public List<String> getPlugin() {
         return this.plugin;
      }

      private void setPlugin(List<String> var1) {
         this.plugin = var1;
      }

      public List<String> getOnlineStatus() {
         return this.onlineStatus;
      }

      public void setOnlineStatus(List<String> var1) {
         this.onlineStatus = var1;
      }

      // $FF: synthetic method
      Disable(Object var2) {
         this();
      }
   }

   public final class ChatFilter {
      List<Config.ChatFilter.Filter> filters;

      private ChatFilter() {
         this.filters = new ArrayList();
      }

      public boolean check(ProxiedPlayer var1, String var2) {
         Iterator var3 = this.filters.iterator();

         Config.ChatFilter.Filter var4;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            var4 = (Config.ChatFilter.Filter)var3.next();
         } while(!var4.check(var1, var2));

         if (Config.this.filterLog.isEnabled()) {
            Config.this.filterLog.log(var4.id, var1.getName(), var2);
         }

         return true;
      }

      private Config.ChatFilter.Filter createFilter(String var1, String var2, String var3, String var4) {
         return new Config.ChatFilter.Filter(var1, var2, var3, var4);
      }

      // $FF: synthetic method
      ChatFilter(Object var2) {
         this();
      }

      public final class Filter {
         private Pattern regex;
         private Pattern ignore;
         private String id;
         private String runCmd;

         public Filter(String var2, String var3, String var4, String var5) {
            this.id = var2;
            this.regex = Pattern.compile(var3);
            if (!var4.isEmpty()) {
               this.ignore = Pattern.compile(var4);
            } else {
               this.ignore = null;
            }

            this.runCmd = var5;
         }

         private boolean check(ProxiedPlayer var1, String var2) {
            if (this.regex.matcher(var2).find()) {
               if (this.ignore != null && this.ignore.matcher(var2).find()) {
                  return false;
               } else {
                  UltimateFriends.logger.info("[Filter] Message \"" + var2 + "\" from \"" + var1.getName() + "\" was blocked by filter \"" + this.id + "\"");
                  if (!this.runCmd.isEmpty()) {
                     String var3 = this.runCmd.replace("$player", var1.getName()).replace("$msg", var2);
                     UltimateFriends.logger.info("[Filter] Running command \"" + var3 + "\"");
                     UltimateFriends.server.getPluginManager().dispatchCommand(UltimateFriends.server.getConsole(), var3);
                  }

                  return true;
               }
            } else {
               return false;
            }
         }
      }
   }

   public final class ChatLogger {
      private boolean enabled = false;
      private String timeFormat = "yyyy-MM-dd HH:mm:ss";
      private String format = "[$time] - $filter: $player ($msg)";

      public void log(String var1, String var2, String var3) {
         BufferedWriter var4 = null;

         try {
            File var5 = new File(UltimateFriends.plugin.getDataFolder(), "filterLog.txt");
            if (var5.exists() || var5.createNewFile()) {
               String var6 = this.format.replace("$filter", var1).replace("$player", var2).replace("$msg", var3);
               if (var6.contains("$time")) {
                  SimpleDateFormat var7 = new SimpleDateFormat(this.timeFormat);
                  Date var8 = new Date(System.currentTimeMillis());
                  String var9 = var7.format(var8);
                  var6 = var6.replace("$time", var9);
               }

               var4 = new BufferedWriter(new FileWriter(var5, true));
               var4.append(var6);
               var4.newLine();
               return;
            }
         } catch (Exception var19) {
            var19.printStackTrace();
            return;
         } finally {
            if (var4 != null) {
               try {
                  var4.close();
               } catch (IOException var18) {
                  var18.printStackTrace();
               }
            }

         }

      }

      public boolean isEnabled() {
         return this.enabled;
      }
   }

   public class CustomButton {
      private String format;
      private String buttonText;
      private String hoverText;
      private Config.CustomButtonType type;
      private String value;

      private CustomButton(String var2, String var3, String var4, String var5, String var6) {
         this.format = var2;
         this.buttonText = var3;
         this.hoverText = var4;

         try {
            this.type = Config.CustomButtonType.valueOf(var5);
         } catch (IllegalArgumentException var8) {
            this.type = Config.CustomButtonType.CMD;
         }

         this.value = var6;
      }

      public String getFormat() {
         return this.format;
      }

      public String getButtonText() {
         return this.buttonText;
      }

      public String getHoverText() {
         return this.hoverText;
      }

      public Config.CustomButtonType getType() {
         return this.type;
      }

      public String getValue() {
         return this.value;
      }

   }

   public class ServerAliases {
      private HashMap<String, String> aliases;

      private ServerAliases(List<String> var2) {
         this.aliases = new HashMap();
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            String[] var5 = var4.split(";");
            if (var5.length == 2) {
               this.aliases.put(var5[0], var5[1]);
            }
         }

      }

      @NotNull
      public String translate(String var1) {
         String var2 = this.aliases.get(var1);
         if (var2 == null) {
            return var1;
         } else {
            return var2;
         }
      }
   }

   public final class Options {
      private Config.Options.Values defaults;

      private Options() {
         this.defaults = new Config.Options.Values();
      }

      public Config.Options.Values getDefaults() {
         return this.defaults;
      }

      public final class Values {
         private boolean show_msg_join;
         private boolean show_msg_left;
         private boolean show_msg_switch;
         private boolean allow_requests;
         private boolean allow_private_msg;
         private boolean show_broadcast;

         private Values() {
            this.show_msg_join = true;
            this.show_msg_left = true;
            this.show_msg_switch = true;
            this.allow_requests = true;
            this.allow_private_msg = true;
            this.show_broadcast = true;
         }

         public void setShow_msg_join(boolean var1) {
            this.show_msg_join = var1;
         }

         public void setShow_msg_left(boolean var1) {
            this.show_msg_left = var1;
         }

         public void setShow_msg_switch(boolean var1) {
            this.show_msg_switch = var1;
         }

         public void setAllow_requests(boolean var1) {
            this.allow_requests = var1;
         }

         public void setAllow_private_msg(boolean var1) {
            this.allow_private_msg = var1;
         }

         public void setShow_broadcast(boolean var1) {
            this.show_broadcast = var1;
         }

         public boolean show_msg_join() {
            return this.show_msg_join;
         }

         public boolean show_msg_left() {
            return this.show_msg_left;
         }

         public boolean show_msg_switch() {
            return this.show_msg_switch;
         }

         public boolean allow_requests() {
            return this.allow_requests;
         }

         public boolean allow_private_msg() {
            return this.allow_private_msg;
         }

         public boolean show_broadcast() {
            return this.show_broadcast;
         }

         // $FF: synthetic method
         Values(Object var2) {
            this();
         }
      }
   }

   public static enum CustomButtonType {
      TEXT,
      CMD,
      SUGGEST,
      URL;
   }

   public static enum SortType {
      NONE,
      ALPHA,
      ONLINE;
   }
}
