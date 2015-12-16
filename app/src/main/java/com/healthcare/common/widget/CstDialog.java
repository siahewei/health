package com.healthcare.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcare.R;

public class CstDialog extends Dialog {
    private Context context;
    private TextView title;
    private TextView cancel;
    private TextView sure;
    private TextView other;
    private TextView horizontalDivider;
    private TextView verticalDivider;
    private boolean cancelOutSide;
    private LinearLayout contentView;
    private EditText editText;
    private CstDialogOnClickListener cstDialogOnClickListener;

    public CstDialog(Context context, CstDialogOnClickListener l) {
        this(context);
        this.cstDialogOnClickListener = l;
        register();
    }

    public CstDialog(Context context) {
        super(context);
        init(context);
    }

    public CstDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    protected CstDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        if (null == context) return;
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setGravity(Gravity.CENTER);
        View view = LayoutInflater.from(context).inflate(R.layout.cst_dialog_layout, null);
        if (null != view) {
            title = (TextView) view.findViewById(R.id.cst_dialog_title);
            cancel = (TextView) view.findViewById(R.id.cst_dialog_cancel);
            sure = (TextView) view.findViewById(R.id.cst_dialog_sure);
            other = (TextView) view.findViewById(R.id.cst_dialog_other);
            editText = (EditText) view.findViewById(R.id.edit_name);
            horizontalDivider = (TextView) view.findViewById(R.id.cst_dialog_horizontal_divider);
            verticalDivider = (TextView) view.findViewById(R.id.cst_dialog_vertical_divider);
            contentView = (LinearLayout) view.findViewById(R.id.cst_dialog_content_view);
            setContentView(view);
            setCanceledOnTouchOutside(true);
        }
    }

    public void setView(boolean isOtherBtn, boolean isEditTextView, String btnName[], String titelName){
        setVisible(other, isOtherBtn);
        setVisible(editText, isEditTextView);

        if (btnName.length == 2){
            setText(cancel, btnName[0]);
            setText(sure, btnName[1]);
        }else if (btnName.length == 3){
            setText(cancel, btnName[0]);
            setText(other, btnName[1]);
            setText(sure, btnName[2]);
        }

        setText(title, titelName);
    }

    public void setEditText(String tag){
        editText.setText(tag);
        editText.setSelection(tag.length());
    }

    public String getEiditText(){
        return editText.getText().toString();
    }

    private void register() {
        setOnClickListener(sure, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != cstDialogOnClickListener) {
                    cstDialogOnClickListener.onClickSure();
                    cancel();
                }
            }
        });
        setOnClickListener(cancel, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != cstDialogOnClickListener) {
                    cstDialogOnClickListener.onClickCancel();
                    cancel();
                }
            }
        });

        setOnClickListener(other, new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (null != cstDialogOnClickListener) {
                    cstDialogOnClickListener.onOther();
                    cancel();
                }
            }
        });
    }

    public LinearLayout getContentView() {
        return contentView;
    }

    public void setCstDialogOnClickListener(CstDialogOnClickListener l) {
        this.cstDialogOnClickListener = l;
        register();
    }

    public void setCancelOutSide(boolean cancelOutSide) {
        this.cancelOutSide = cancelOutSide;
        setCanceledOnTouchOutside(cancelOutSide);
    }


    public interface CstDialogOnClickListener {
        void onClickSure();

        void onClickCancel();

        void onOther();
    }

    public void setDividerColor(int color) {
        setTextColor(horizontalDivider, color);
        setTextColor(verticalDivider, color);
    }

    /**
     * 模拟IOS的警告框时调用
     *
     */
    public void setTitleImitateIos(String name, String hint) {
        if (null != name && null != title) {
            SpannableString spanString = new SpannableString(name + "\n" + hint);
            spanString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanString.setSpan(new AbsoluteSizeSpan(sp2px(context, 16)), 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanString.setSpan(new AbsoluteSizeSpan(sp2px(context, 14)), name.length(), spanString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            title.setText(spanString);
        }
    }


    public void setTitle(String s) {
        setText(title, s);
    }

    public void setSure(String s) {
        setText(sure, s);
    }

    public void setCancel(String s) {
        setText(cancel, s);
    }

    public void setTitleColor(int color) {
        setTextColor(title, color);
    }

    public void setSureColor(int color) {
        setTextColor(sure, color);
    }

    public void setCancelColor(int color) {
        setTextColor(cancel, color);
    }

    private void setText(TextView t, String s) {
        if (null != t && null != s) {
            t.setText(s);
        }
    }

    private void setVisible(View view, boolean visible){
        if (view != null){
            if (visible){
                view.setVisibility(View.VISIBLE);
            }else {
                view.setVisibility(View.GONE);
            }
        }
    }

    private void setTextColor(TextView t, int color) {
        if (null != t) {
            t.setTextColor(color);
        }
    }

    private void setOnClickListener(View v, View.OnClickListener l) {
        if (null != v) {
            v.setOnClickListener(l);
        }
    }

    private int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
