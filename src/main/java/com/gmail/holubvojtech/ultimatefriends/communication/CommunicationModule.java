package com.gmail.holubvojtech.ultimatefriends.communication;

import com.gmail.holubvojtech.ultimatefriends.Friend;
import com.gmail.holubvojtech.ultimatefriends.PlayerProfile;
import com.gmail.holubvojtech.ultimatefriends.exceptions.CannotAddYourself;
import com.gmail.holubvojtech.ultimatefriends.exceptions.ConnectionDisabledOnServer;
import com.gmail.holubvojtech.ultimatefriends.exceptions.FriendListExceeded;
import com.gmail.holubvojtech.ultimatefriends.exceptions.FriendOnDisabledServer;
import com.gmail.holubvojtech.ultimatefriends.exceptions.PlayerAlreadyFriend;
import com.gmail.holubvojtech.ultimatefriends.exceptions.PlayerAlreadyRequested;
import com.gmail.holubvojtech.ultimatefriends.exceptions.PlayerDenied;
import com.gmail.holubvojtech.ultimatefriends.exceptions.PlayerIsOffline;
import com.gmail.holubvojtech.ultimatefriends.exceptions.PlayerNotFriend;
import org.jetbrains.annotations.Nullable;

public interface CommunicationModule {
   void registerListeners();

   void unregisterListeners();

   boolean isOnline(String var1);

   @Nullable
   String getServer(String var1);

   void sendFriendMessage(PlayerProfile var1, String var2, String var3) throws PlayerIsOffline, PlayerNotFriend, PlayerDenied, FriendOnDisabledServer;

   void sendFriendBroadcastMessage(PlayerProfile var1, String var2);

   void removeFriend(PlayerProfile var1, Friend var2) throws PlayerNotFriend;

   boolean addFriend(PlayerProfile var1, String var2) throws PlayerIsOffline, PlayerAlreadyFriend, CannotAddYourself, PlayerDenied, FriendOnDisabledServer, PlayerAlreadyRequested, FriendListExceeded;

   void connect(PlayerProfile var1, String var2) throws PlayerIsOffline, FriendOnDisabledServer, PlayerNotFriend, ConnectionDisabledOnServer;
}
