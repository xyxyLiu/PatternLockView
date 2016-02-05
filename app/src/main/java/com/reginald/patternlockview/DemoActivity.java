package com.reginald.patternlockview;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.reginald.patternlockview.demo.R;

public class DemoActivity extends Activity {

    private static final String TAG = "DemoActivity";

    PatternLockView mLockView;

    TextView mPasswordTextView;

    String mPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_layout);
        mLockView = (PatternLockView) findViewById(R.id.lock_view);
        mPasswordTextView = (TextView) findViewById(R.id.password_text);
        mPasswordTextView.setText("please enter your password!");
        mLockView.setCallBack(new PatternLockView.CallBack() {
            @Override
            public int onFinish(PatternLockView.Password password) {
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

        mLockView.setOnNodeTouchListener(new PatternLockView.OnNodeTouchListener() {
            @Override
            public void onNodeTouched(int NodeId) {
                Log.d(TAG, "node " + NodeId + " has touched!");
            }
        });
    }
}
