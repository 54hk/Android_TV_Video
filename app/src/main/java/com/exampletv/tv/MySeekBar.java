package com.exampletv.tv;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

public class MySeekBar
        extends SeekBar {
    /**
     * 定义一个展现时间的PopupWindow
     */
    private PopupWindow mPopupWindow;

    private View mView;
    /**
     * 显示时间的TextView
     */
    private TextView dialogSeekTime;
    /**
     * 用来表示该组件在整个屏幕内的绝对坐标，其中 mPosition[0] 代表X坐标,mPosition[1] 代表Y坐标。
     */
    private int[] mPosition;
    /**
     * SeekBar上的Thumb的宽度，即那个托动的小黄点的宽度
     */
    private final int mThumbWidth = 25;

    public MySeekBar(Context context) {
        this(context, null);
    }

    public MySeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mView = LayoutInflater.from(context).inflate(R.layout.seek_popu, null);
        dialogSeekTime = (TextView) mView.findViewById(R.id.dialogSeekTime);
        mPopupWindow = new PopupWindow(mView, mView.getWidth(), mView.getHeight(), true);
        mPosition = new int[2];
    }

    /**
     * 获取控件的宽度
     *
     * @param v
     * @return 控件的宽度
     */
    private int getViewWidth(View v) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
        return v.getMeasuredWidth();
    }

    /**
     * 获取控件的高度
     *
     * @param v
     * @return 控件的高度
     */
    private int getViewHeight(View v) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
        return v.getMeasuredHeight();
    }

    /**
     * 隐藏进度拖动条的PopupWindow
     */
    public void hideSeekDialog() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    /**
     * 显示进度拖动条的PopupWindow
     *
     * @param str 时间值
     */
    public void showSeekDialog(String str) {
        dialogSeekTime.setText(str);
        int progress = this.getProgress();
        // 计算每个进度值所占的宽度
        int thumb_x = (int) (progress * (1.0f * (this.getWidth() - 22) / this.getMax())); //22是两边的空白部分宽度
        // 更新后的PopupWindow的Y坐标
        int middle = this.getHeight() / 2 + 120;
        if (mPopupWindow != null) {
            try {
                /*
                 * 获取在整个屏幕内的绝对坐标，注意这个值是要从屏幕顶端算起，也就是包括了通知栏的高度。
                 * 其中 mPosition[0] 代表X坐标,mPosition[1]代表Y坐标。
                 */
                this.getLocationOnScreen(mPosition);
                // 相对某个控件的位置（正左下方），在X、Y方向各有偏移
                mPopupWindow.showAsDropDown(this, (int) mPosition[0], mPosition[1]);
                /*
                 * 更新后的PopupWindow的X坐标
                 * 首先要把当前坐标值减去PopWindow的宽度的一半，再加上Thumb的宽度一半。
                 * 这样才能使PopWindow的中心点和Thumb的中心点的X坐标相等
                 */
                int x = thumb_x + mPosition[0] - getViewWidth(mView) / 2 + mThumbWidth / 2;
                // 更新popup窗口的位置
                mPopupWindow.update(x, middle, getViewWidth(mView), getViewHeight(mView));
            } catch (Exception e) {
            }
        }
    }

}
