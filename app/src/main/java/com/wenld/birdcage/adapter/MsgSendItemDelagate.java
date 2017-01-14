package com.wenld.birdcage.adapter;

import com.seven.birdcage.R;
import com.wenld.birdcage.model.InFo;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by zhy on 16/6/22.
 */
public class MsgSendItemDelagate implements ItemViewDelegate<InFo> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.main_chat_send_msg;
    }

    @Override
    public boolean isForViewType(InFo item, int position) {
        return item.getContent().equals(InFo.CONTENT_ROBOT);
    }

    @Override
    public void convert(ViewHolder holder, InFo chatMessage, int position) {
        holder.setText(R.id.chat_send_content, chatMessage.getPalyString());
//        holder.setText(R.id.chat_send_name, chatMessage.getName());
//        holder.setImageResource(R.id.chat_send_icon, chatMessage.getIcon());
    }
}
