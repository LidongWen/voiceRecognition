package com.wenld.birdcage.adapter;

import android.content.Context;

import com.wenld.birdcage.model.InFo;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

/**
 * Created by zhy on 15/9/4.
 */
public class ChatAdapterForRv extends MultiItemTypeAdapter<InFo>
{
    public ChatAdapterForRv(Context context, List<InFo> datas)
    {
        super(context, datas);

        addItemViewDelegate(new MsgSendItemDelagate());
        addItemViewDelegate(new MsgComingItemDelagate());
    }
}
