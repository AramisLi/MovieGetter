package com.aramis.library.component.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aramis.library.R;


/**
 * 默认提示dialog
 * Created by Aramis on 2017/4/26.
 */

public class DefaultHintDialog implements BunnyDialog {
    private Dialog dialog;
    private TextView text_dialog_message, text_dialog_positive, text_dialog_negative, text_dialog_title,
            text_dialog_single;
    private LinearLayout layout_dialog_button;
    private int buttonCount;


    public DefaultHintDialog(@NonNull Context context) {
        init(context, null, null, 2);
    }

    public DefaultHintDialog(@NonNull Context context, String message) {
        init(context, null, message, 2);
    }

    public DefaultHintDialog(@NonNull Context context, String title, String message) {
        init(context, title, message, 2);
    }

    public DefaultHintDialog(@NonNull Context context, String title, String message, int buttonCount) {
        init(context, title, message, buttonCount);
    }

    private void init(Context context, String title, String message, int buttonCount) {
        this.buttonCount = buttonCount;
        dialog = new Dialog(context, R.style.new_custom_dialog);
//        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_default_invoice, null);
        dialog.setContentView(R.layout.dialog_default);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        View s = dialog.getWindow().getDecorView();
        text_dialog_message = (TextView) s.findViewById(R.id.text_dialog_message);
        text_dialog_positive = (TextView) s.findViewById(R.id.text_dialog_positive);
        text_dialog_negative = (TextView) s.findViewById(R.id.text_dialog_negative);
        text_dialog_title = (TextView) s.findViewById(R.id.text_dialog_title);
        layout_dialog_button = (LinearLayout) s.findViewById(R.id.layout_dialog_button);
        text_dialog_single = (TextView) s.findViewById(R.id.text_dialog_single);
        if (!TextUtils.isEmpty(message)) text_dialog_message.setText(message);
        if (!TextUtils.isEmpty(title)) text_dialog_title.setText(title);

        switch (buttonCount){
            case 2:
                break;
            case 1:
                layout_dialog_button.setVisibility(View.GONE);
                text_dialog_single.setVisibility(View.VISIBLE);
                break;
            case 0:
                layout_dialog_button.setVisibility(View.GONE);
                break;
        }
//        if (buttonCount) {
//
//        }
    }

    public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside){
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
    }

    public void setMessageGravity(int gravity) {
        text_dialog_message.setGravity(gravity);
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        dialog.setOnDismissListener(onDismissListener);
    }

    public void setMessage(String message) {
        text_dialog_message.setText(message);
    }

    public void setPositiveClickListener(View.OnClickListener onClickListener) {
        setPositiveClickListener(null, onClickListener);
    }

    public void setNegativeClickListener(View.OnClickListener onClickListener) {
        setNegativeClickListener(null, onClickListener);
    }

    public void setSingleBtnClickListener(String text, View.OnClickListener onClickListener) {
        text_dialog_single.setText(text);
        text_dialog_single.setOnClickListener(onClickListener);
    }

    public void setSingleBtnBackground(int res) {
        text_dialog_single.setBackgroundResource(res);
    }

    public void setSingleBtnTextColor(int color) {
        text_dialog_single.setTextColor(color);
    }

    public void setPositiveClickListener(String text, View.OnClickListener onClickListener) {
        if (!TextUtils.isEmpty(text)) text_dialog_positive.setText(text);
        text_dialog_positive.setOnClickListener(onClickListener);
    }

    public void setNegativeClickListener(String text, View.OnClickListener onClickListener) {
        if (!TextUtils.isEmpty(text)) text_dialog_negative.setText(text);
        text_dialog_negative.setOnClickListener(onClickListener);
    }

    @Override
    public void show() {
        dialog.show();
    }

    public void show(String message) {
        text_dialog_message.setText(message);
        dialog.show();
    }

    @Override
    public void dismiss() {
        dialog.dismiss();
    }

    @Override
    public boolean isShowing() {
        return dialog.isShowing();
    }
}
