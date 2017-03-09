package com.redso.signaller.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.redso.signaller.core.ChatRoomLeaveCallback;
import com.redso.signaller.core.Signaller;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PeopleFragment extends Fragment {

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_friends, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @OnClick(R.id.joinJay10Button)
  void chatWithJay10() {
    Signaller.getInstance().chatWith(getActivity(), Constant.USER_ID_JAY10, "jay10");
  }

  @OnClick(R.id.joinJay11Button)
  void chatWithJay11() {
    Signaller.getInstance().chatWith(getActivity(), Constant.USER_ID_JAY11, "jay11");
  }

  @OnClick(R.id.joinJay12Button)
  void chatWithJay12() {
    Signaller.getInstance().chatWith(getActivity(), Constant.USER_ID_JAY12, "jay12");
  }

  @OnClick(R.id.joinJay13Button)
  void chatWithJay13() {
    Signaller.getInstance().chatWith(getActivity(), Constant.USER_ID_JAY13, "jay13");
  }

  @OnClick(R.id.joinJay14Button)
  void chatWithJay14() {
    Signaller.getInstance().chatWith(getActivity(), Constant.USER_ID_JAY14, "jay14");
  }

  private String makeChatRoomId(String userId) {
    String ownUserId = App.currentUserId;
    return ownUserId.compareTo(userId) < 0 ?
      ownUserId + "_" + userId :
      userId + "_" + ownUserId;
  }

  @OnClick(R.id.leaveJay10Button)
  void leaveChatRoomWithJay10() {
    Signaller.getInstance().leaveChatRoom(makeChatRoomId(Constant.USER_ID_JAY10), new ChatRoomLeaveCallback() {
      @Override
      public void onChatRoomLeft(String chatRoomId) {
        Utils.showToast(getActivity(), "onChatRoomLeft: " + chatRoomId);
      }
    });
  }

  @OnClick(R.id.leaveJay11Button)
  void leaveChatRoomWithJay11() {
    Signaller.getInstance().leaveChatRoom(makeChatRoomId(Constant.USER_ID_JAY11), new ChatRoomLeaveCallback() {
      @Override
      public void onChatRoomLeft(String chatRoomId) {
        Utils.showToast(getActivity(), "onChatRoomLeft: " + chatRoomId);
      }
    });
  }

  @OnClick(R.id.leaveJay12Button)
  void leaveChatRoomWithJay12() {
    Signaller.getInstance().leaveChatRoom(makeChatRoomId(Constant.USER_ID_JAY12), new ChatRoomLeaveCallback() {
      @Override
      public void onChatRoomLeft(String chatRoomId) {
        Utils.showToast(getActivity(), "onChatRoomLeft: " + chatRoomId);
      }
    });
  }

  @OnClick(R.id.leaveJay13Button)
  void leaveChatRoomWithJay13() {
    Signaller.getInstance().leaveChatRoom(makeChatRoomId(Constant.USER_ID_JAY13), new ChatRoomLeaveCallback() {
      @Override
      public void onChatRoomLeft(String chatRoomId) {
        Utils.showToast(getActivity(), "onChatRoomLeft: " + chatRoomId);
      }
    });
  }

  @OnClick(R.id.leaveJay14Button)
  void leaveChatRoomWithJay14() {
    Signaller.getInstance().leaveChatRoom(makeChatRoomId(Constant.USER_ID_JAY14), new ChatRoomLeaveCallback() {
      @Override
      public void onChatRoomLeft(String chatRoomId) {
        Utils.showToast(getActivity(), "onChatRoomLeft: " + chatRoomId);
      }
    });
  }

}