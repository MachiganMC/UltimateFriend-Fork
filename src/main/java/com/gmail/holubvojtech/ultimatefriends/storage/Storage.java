package com.gmail.holubvojtech.ultimatefriends.storage;

import com.gmail.holubvojtech.ultimatefriends.CheckReturnValue;
import com.gmail.holubvojtech.ultimatefriends.Friend;
import com.gmail.holubvojtech.ultimatefriends.Options;
import com.gmail.holubvojtech.ultimatefriends.PlayerID;
import com.gmail.holubvojtech.ultimatefriends.PlayerProfile;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public interface Storage {
   @CheckReturnValue
   boolean connect();

   boolean disconnect();

   @CheckReturnValue
   PlayerProfile loadPlayerProfile(String var1, UUID var2);

   @CheckReturnValue
   Options getOptions(@NotNull PlayerProfile var1);

   boolean saveOptions(@NotNull PlayerProfile var1);

   @CheckReturnValue
   List<Friend> getFriends(@NotNull PlayerProfile var1);

   @CheckReturnValue
   boolean addFriend(@NotNull PlayerProfile var1, @NotNull PlayerID var2);

   @CheckReturnValue
   boolean removeFriend(@NotNull PlayerProfile var1, @NotNull PlayerID var2);
}
