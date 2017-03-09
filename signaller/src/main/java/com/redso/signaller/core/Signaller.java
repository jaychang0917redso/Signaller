package com.redso.signaller.core;

import android.app.Application;
import android.content.Context;

import com.jaychang.utils.AppStatusUtils;
import com.redso.signaller.core.push.SignallerGcmManager;
import com.redso.signaller.ui.ChatRoomActivity;
import com.redso.signaller.ui.UIConfig;
import com.redso.signaller.util.LogUtils;
import com.redso.signaller.util.StethoUtils;

public final class Signaller {

  public interface ChatRoomMetaCallback {
    void onChatRoomMetaReady(int count);
  }

  private static final Signaller INSTANCE = new Signaller();
  private Context appContext;
  private AppConfig appConfig;
  private UIConfig uiConfig;
  private Application app;

  private Signaller() {
  }

  public static void init(Application app, AppConfig appConfig, UIConfig uiConfig) {
    INSTANCE.app = app;
    INSTANCE.appContext = app.getApplicationContext();
    INSTANCE.appConfig = appConfig;
    INSTANCE.uiConfig = uiConfig;

    SignallerDbManager.getInstance().init(app.getApplicationContext());
  }

  public static Signaller getInstance() {
    return INSTANCE;
  }

  private static void registerAppCallback(Application app) {
    AppStatusUtils.registerAppStatusCallback(app, new AppStatusUtils.Callback() {
      @Override
      public void onAppEnterBackground() {
        SocketManager.getInstance().disconnect();
        LogUtils.d("onAppEnterBackground, disconnect socket");
      }

      @Override
      public void onAppEnterForeground() {
        SocketManager.getInstance().connect();
        LogUtils.d("onAppEnterForeground, connect socket");
      }
    });
  }

  private static void unregisterAppCallback(Application app) {
    AppStatusUtils.unregisterAppStatusCallback(app);
  }

  public boolean isPushNotificationEnabled() {
    String pushNotificationSenderId = appConfig.getPushNotificationSenderId();
    return pushNotificationSenderId != null && !pushNotificationSenderId.isEmpty();
  }

  public void connect(String accessToken, String userId) {
    UserData.getInstance().setAccessToken(accessToken);
    UserData.getInstance().setUserId(userId);
    SocketManager.getInstance().initSocket(accessToken);
    SocketManager.getInstance().connect();
    registerAppCallback(app);
    if (isPushNotificationEnabled()) {
      SignallerGcmManager.register(appContext);
    }
  }

  public void disconnect() {
    SocketManager.getInstance().disconnect();
    SocketManager.getInstance().invalidate();
    SignallerDbManager.getInstance().clear();
    if (isPushNotificationEnabled()) {
      SignallerGcmManager.unregister(appContext);
    }
    unregisterAppCallback(app);
    UserData.getInstance().clear();
    ChatRoomMeta.getInstance().clear();
  }

  public void chatWith(Context context, String userId, String toolbarTitle) {
    if (!SocketManager.getInstance().isConnected()) {
      SocketManager.getInstance().connect(new SocketConnectionCallbacks() {
        @Override
        void onConnected() {
          if (context == null) {
            return;
          }
          chatWithInternal(context, userId, toolbarTitle);
        }
      });
    } else {
      chatWithInternal(context, userId, toolbarTitle);
    }
  }

  private void chatWithInternal(Context context, String userId, String toolbarTitle) {
    String ownUserId = UserData.getInstance().getUserId();
    String chatRoomId = ownUserId.compareTo(userId) < 0 ?
      ownUserId + "_" + userId :
      userId + "_" + ownUserId;

    SocketManager.getInstance().join(userId, chatRoomId, new ChatRoomJoinCallback() {
      @Override
      public void onChatRoomJoined(String chatRoomId, String userId) {
        ChatRoomActivity.start(context, chatRoomId, userId, toolbarTitle);
      }
    });
  }

  public void leaveChatRoom(String chatRoomId, ChatRoomLeaveCallback callback) {
    SocketManager.getInstance().leave(chatRoomId, callback);
  }

  public Context getAppContext() {
    return appContext;
  }

  public UIConfig getUiConfig() {
    return uiConfig;
  }

  public AppConfig getAppConfig() {
    return appConfig;
  }

  public void setDebugEnabled(boolean enable) {
    LogUtils.setEnable(enable);
    StethoUtils.setEnable(enable);
  }

  public void getUnreadMessageCount(ChatRoomMetaCallback callback) {
    SignallerDataManager.getInstance().getChatRoomsFromNetwork(null)
      .subscribe(
        rooms -> {
          int totalUnreadCount = ChatRoomMeta.getInstance().getTotalUnreadCount();
          callback.onChatRoomMetaReady(totalUnreadCount);
          LogUtils.d("Unread message count: " + totalUnreadCount);
        },
        error -> {
          LogUtils.e("Fail to get unread message count: " + error.getMessage());
        });
  }

}