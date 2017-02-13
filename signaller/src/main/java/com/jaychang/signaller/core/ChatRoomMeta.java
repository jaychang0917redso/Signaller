package com.jaychang.signaller.core;

public final class ChatRoomMeta {

  public static String cursor;
  public static boolean hasMoreData;
  public static int totalUnreadCount;

  public static void clear() {
    cursor = null;
    hasMoreData = false;
    totalUnreadCount = -1;
  }

}
