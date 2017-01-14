package com.wenld.birdcage.adapter;

import com.seven.birdcage.R;
import com.wenld.birdcage.model.InFo;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by zhy on 16/6/22.
 */
public class MsgComingItemDelagate implements ItemViewDelegate<InFo> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.main_chat_from_msg;
    }

    @Override
    public boolean isForViewType(InFo item, int position) {
        return item.getContent().equals(InFo.CONTENT_client);
    }

    @Override
    public void convert(ViewHolder holder, InFo chatMessage, int position) {
        holder.setText(R.id.chat_from_content, chatMessage.getPalyString());
    }
}
