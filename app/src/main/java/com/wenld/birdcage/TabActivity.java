package com.wenld.birdcage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.seven.birdcage.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/1/13.
 */

public class TabActivity extends AppCompatActivity {
    public RecyclerView rlvAtyFilter;
    CommonAdapter adapter;
    List<String> list = new ArrayList<>();
    String items[] = {"点击 me ", "语音设置"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        list = Arrays.asList(items);
        this.rlvAtyFilter = (RecyclerView) findViewById(R.id.rlv_activity_tab);

        adapter = new CommonAdapter<String>(this, R.layout.list_items, list) {
            @Override
            protected void convert(ViewHolder holder, String s, final int position) {
                TextView btn = holder.getView(R.id.btn);
                btn.setText(s);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = null;
                        switch (position) {
                            case 0:
                                // 语音转写
                                intent = new Intent(TabActivity.this, MainActivity.class);
                                break;
                            case 1:
                                // 语音转写
                                intent = new Intent(TabActivity.this, IatSettings.class);
                                break;
                            default:
//                        showTip("在IsvDemo中哦，为了代码简洁，就不放在一起啦，^_^");
                                break;
                        }

                        if (intent != null) {
                            startActivity(intent);
                        }
                    }
                });
            }
        };
        rlvAtyFilter.setLayoutManager(new LinearLayoutManager(this));
        rlvAtyFilter.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener<String>() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, String o, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, String o, int position) {
                return false;
            }
        });
    }
}
