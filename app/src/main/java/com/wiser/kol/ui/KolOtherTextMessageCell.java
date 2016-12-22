package com.wiser.kol.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaychang.nrv.BaseViewHolder;
import com.jaychang.signaller.core.model.SignallerChatMessage;
import com.jaychang.signaller.ui.part.ChatMessageCell;
import com.vanniktech.emoji.EmojiTextView;
import com.wiser.kol.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KolOtherTextMessageCell extends ChatMessageCell{

  public KolOtherTextMessageCell(SignallerChatMessage message) {
    super(message);
  }

  @Override
  public BaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_other_text_message, viewGroup, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(BaseViewHolder viewHolder, int i, View.OnTouchListener onTouchListener) {
    ViewHolder holder = (ViewHolder) viewHolder;
    Context context = holder.itemView.getContext();

    holder.messageView.setText(message.getContent());
  }

  static class ViewHolder extends BaseViewHolder {
    @BindView(R.id.messageView)
    EmojiTextView messageView;

    ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

}
