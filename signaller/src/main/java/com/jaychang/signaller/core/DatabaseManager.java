package com.jaychang.signaller.core;

import android.content.Context;

import com.jaychang.signaller.core.model.ChatMessage;
import com.jaychang.signaller.core.model.ChatRoom;
import com.jaychang.signaller.core.model.PendingChatMessage;

import java.util.List;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Observable;

class DatabaseManager {

  private static final int DB_VERSION = 1;
  private static final DatabaseManager INSTANCE = new DatabaseManager();

  private RealmConfiguration realmConfig;

  private DatabaseManager() {
  }

  public static DatabaseManager getInstance() {
    return INSTANCE;
  }

  void init(Context appContext) {
    Realm.init(appContext);

    RealmMigration migration = new RealmMigration() {
      @Override
      public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

      }
    };

    realmConfig = new RealmConfiguration.Builder()
      .schemaVersion(DB_VERSION)
      .migration(migration)
      .name("signaller.realm")
      .modules(new SignallerModule())
      .build();
  }

  private Realm getRealm() {
    return Realm.getInstance(realmConfig);
  }

  Observable<List<PendingChatMessage>> getPendingChatMessages() {
    Realm realm = getRealm();

    return realm
      .where(PendingChatMessage.class)
      .findAllAsync()
      .asObservable()
      .filter(RealmResults::isLoaded)
      .map(realm::copyFromRealm)
      .first()
      .doOnCompleted(realm::close);
  }

  Observable<List<ChatRoom>> getChatRooms(String userId) {
    Realm realm = getRealm();

    return realm
      .where(ChatRoom.class)
      .equalTo("userId", userId)
      .findAllSortedAsync("mtime", Sort.DESCENDING)
      .asObservable()
      .filter(RealmResults::isLoaded)
      .map(realm::copyFromRealm)
      .first()
      .doOnCompleted(realm::close);
  }

  // todo paging
  Observable<List<ChatMessage>> getChatMessages() {
    Realm realm = getRealm();

    return realm
      .where(ChatMessage.class)
      .findAllSortedAsync("mtime", Sort.DESCENDING)
      .asObservable()
      .filter(RealmResults::isLoaded)
      .map(realm::copyFromRealm)
      .first()
      .doOnCompleted(realm::close);
  }

  void saveChatRooms(final List<ChatRoom> chatRooms) {
    getRealm().executeTransactionAsync(realm -> {
      for (ChatRoom chatRoom : chatRooms) {
        chatRoom.userId = UserData.getInstance().getUserId();
      }
      realm.insertOrUpdate(chatRooms);
    });
  }

  void saveChatMessages(final List<ChatMessage> chatMessages) {
    getRealm().executeTransactionAsync(realm -> {
      for (ChatMessage chatMessage : chatMessages) {
        chatMessage.userId = UserData.getInstance().getUserId();
      }
      realm.insertOrUpdate(chatMessages);
    });
  }

  void saveChatMessage(ChatMessage msg) {
    getRealm().executeTransactionAsync(realm -> {
      realm.insertOrUpdate(msg);
    });
  }

  void removeChatMessage(long timestamp) {
    getRealm().executeTransactionAsync(realm -> {
      ChatMessage msg = realm.where(ChatMessage.class)
        .equalTo("timestamp", timestamp).findFirst();
      if (msg != null) {
        msg.deleteFromRealm();
      }
    });
  }

  void removePendingChatMsg(long timestamp) {
    getRealm().executeTransactionAsync(realm -> {
      PendingChatMessage msg = realm.where(PendingChatMessage.class)
        .equalTo("socketChatMessage.payload.timestamp", timestamp).findFirst();
      if (msg != null) {
        msg.deleteFromRealm();
      }
    });
  }

}
