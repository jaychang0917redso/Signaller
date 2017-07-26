package com.jaychang.signaller.core;

import com.jaychang.signaller.core.model.ChatMessageResponse;
import com.jaychang.signaller.core.model.ChatRoomResponse;
import com.jaychang.signaller.core.model.SignallerImage;

import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface SignallerApi {

  @GET("api/chats")
  Observable<ChatRoomResponse> getChatRooms(@Query("cursor") String cursor,
                                            @Query("hits") int hits);

  @GET("api/chats/{user_id}/messages")
  Observable<ChatMessageResponse> getChatMessages(@Path("user_id") String userId,
                                                  @Query("cursor") String cursor,
                                                  @Query("hits") int hits);

  @POST("/api/resources/images")
  @Multipart
  Observable<SignallerImage> uploadPhoto(@Part MultipartBody.Part file);

  @GET("api/chatrooms/{room_id}/count")
  Observable<Void> resetUnreadCount(@Query("room_id") String roomId);

}
