package com.wenld.birdcage;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.seven.birdcage.R;


/**
 * Created by wenld on 2016/6/24.
 */
public class MyDialog extends Dialog {
    View view;
    Context paramContext;

    public MyDialog(Context paramContext) {
        super(paramContext);
        this.view = ((LayoutInflater) paramContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_mydilog, null);
        this.paramContext = paramContext;
        setContentView(this.view);
    }

    @Override
    public void show() {
        super.show();

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams params = ((Activity) paramContext).getWindow().getAttributes();
        params.alpha = bgAlpha;
        ((Activity) paramContext).getWindow().setAttributes(params);
    }
}
