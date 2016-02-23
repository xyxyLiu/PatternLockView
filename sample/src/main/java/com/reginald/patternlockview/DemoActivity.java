package com.reginald.patternlockview;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.reginald.patternlockview.demo.R;

public class DemoActivity extends Activity {

    private static final String TAG = "DemoActivity";

    PatternLockView mCurLockView;

    PatternLockView mCircleLockView;

    PatternLockView mDotLockView;

    TextView mPasswordTextView;

    Button mSwitchButton;

    String mPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_layout);
        mCircleLockView = (PatternLockView) findViewById(R.id.lock_view_circle);
        mDotLockView = (PatternLockView) findViewById(R.id.lock_view_dot);
        mCurLockView = mDotLockView;
        mPasswordTextView = (TextView) findViewById(R.id.password_text);
        mSwitchButton = (Button) findViewById(R.id.switch_but);
        mSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchLockViews();
            }
        });
        mPasswordTextView.setText("please enter your password!");
        switchLockViews();

        test();
    }

    private void showIEEE745(float num) {
        String ieee754 = Integer.toBinaryString(Float.floatToRawIntBits(num));
        while (ieee754.length() < 32) {
            ieee754 = "0" + ieee754;
        }
        Log.d(TAG,String.format(" float %.20f: sign = %s, exponent = %s, fraction = %s", num, ieee754.substring(0,1), ieee754.substring(1,9), ieee754.substring(9,ieee754.length())));
    }

    private void test(){

//        float a = 5f;
//        float b = 3f;
//        float c = a / b;
//        float s = 1.66666662693f * b;
//        showIEEE745(c);
//        showIEEE745(b);
//        showIEEE745(s);
//        Log.d(TAG, String.format(" a = %.23f , c = %.23f, int = %d, s = %.23f(%d), s==(int)s = %b", a, c, (int)c, s, (int)s, s==(int)s)); //13421773 / 134217728
//
//
//        float   f = 1.0000001f;
//        Log.d(TAG, String.format("%.23f * 10f = %.23f", f , f * 10f) );
//
//                f = 0.01f + 0.01f;
//        Log.d(TAG, String.format("%.23f * 10f = %.23f", f , f * 100f) );
//
//                f = 1.000000001f;
//        Log.d(TAG, String.format("%.23f * 10f = %.23f", f , f * 10f) );
//
//                f = 1.0000000001f;
//        Log.d(TAG, String.format("%.23f * 10f = %.23f", f , f * 10f) );

        Dialog dialog = new Dialog(getApplicationContext());
        TextView textView = new TextView(this);
        textView.setText("hello");
        textView.setTextSize(40);
        dialog.setContentView(new TextView(this));
//        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        dialog.show();

//        View decor = dialog.getWindow().getDecorView();
//        ViewParent root = decor.getParent();
//        Log.d(TAG, String.format("dialog decor = %s, decor.parent = %s", decor, root));
//        dialog.dismiss();
//
//
//        mPasswordTextView.post(new Runnable() {
//            @Override
//            public void run() {
//                PopupWindow popupWindow = new PopupWindow(DemoActivity.this);
//                TextView textView = new TextView(DemoActivity.this);
//                textView.setText("hello");
//                textView.setTextSize(40);
//                popupWindow.setContentView(new TextView(DemoActivity.this));
//                popupWindow.showAsDropDown(mPasswordTextView);
//
//                View decor = popupWindow.getContentView().getRootView();
//                ViewParent root = decor.getParent();
//
//                Log.d(TAG, String.format("popupWindow decor = %s, decor.parent = %s", decor, root));
////        popupWindow.dismiss();
//            }
//        });

    }

    private void switchLockViews() {
        mCurLockView = mCurLockView == mCircleLockView ? mDotLockView : mCircleLockView;
        mCurLockView.setVisibility(View.VISIBLE);

        /*
        // config the PatternLockView with code

        // set size
        mCurLockView.setSize(5);
        // set finish timeout, if 0, reset the lock view immediately after user input one password
        mCircleLockView.setFinishTimeout(2000);
        // set whether user can interrupt the finish timeout.
        // if true, the lock view will be reset when user touch a new node.
        // if false, the lock view will be reset only when the finish timeout expires
        mCircleLockView.setFinishInterruptable(false);
        // set whether the lock view can auto link the nodes in the path of two selected nodes
        mCircleLockView.setAutoLinkEnabled(false);
        */

        mCurLockView.reset();
        if (mCurLockView != mCircleLockView) {
            mCircleLockView.setVisibility(View.GONE);
            mCircleLockView.setCallBack(null);
            mCircleLockView.setOnNodeTouchListener(null);
            mSwitchButton.setText("switch to circle lock view");
        } else {
            mDotLockView.setVisibility(View.GONE);
            mDotLockView.setCallBack(null);
            mDotLockView.setOnNodeTouchListener(null);
            mSwitchButton.setText("switch to dot lock view");
        }

        mCurLockView.setCallBack(new PatternLockView.CallBack() {
            @Override
            public int onFinish(PatternLockView.Password password) {
                Log.d(TAG, "password length " + password.list.size());
                if (password.string.length() != 0) {
                    mPasswordTextView.setText("password is " + password.string);
                } else {
                    mPasswordTextView.setText("please enter your password!");
                }

                if (mPassword.equals(password.string)) {
                    return PatternLockView.CODE_PASSWORD_CORRECT;
                } else {
                    mPassword = password.string;
                    return PatternLockView.CODE_PASSWORD_ERROR;
                }
            }
        });

        mCurLockView.setOnNodeTouchListener(new PatternLockView.OnNodeTouchListener() {
            @Override
            public void onNodeTouched(int NodeId) {
                Log.d(TAG, "node " + NodeId + " has touched!");
            }
        });

    }
}
