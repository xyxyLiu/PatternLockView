package com.reginald.patternlockview;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.reginald.patternlockview.demo.R;

public class DemoActivity extends Activity {

    private static final String TAG = "DemoActivity";

    private PatternLockView mCurLockView;

    private PatternLockView mCircleLockView;

    private PatternLockView mDotLockView;

    private TextView mPasswordTextView;

    private Button mSwitchButton;

    private Button mPatternVisibleButton;

    private String mPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_layout);
        mCircleLockView = (PatternLockView) findViewById(R.id.lock_view_circle);
        mDotLockView = (PatternLockView) findViewById(R.id.lock_view_dot);
        mCurLockView = mDotLockView;
        mPasswordTextView = (TextView) findViewById(R.id.password_text);
        mPatternVisibleButton = (Button) findViewById(R.id.pattern_visible_but);
        mPatternVisibleButton.setText("pattern visibility: visible");
        mPatternVisibleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurLockView.isPatternVisible()) {
                    mPatternVisibleButton.setText("pattern visibility: invisible");
                    mCurLockView.setPatternVisible(false);
                } else {
                    mCurLockView.setPatternVisible(true);
                    mPatternVisibleButton.setText("pattern visibility: visible");
                }
            }
        });
        mSwitchButton = (Button) findViewById(R.id.switch_but);
        mSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchLockViews();
            }
        });
        mPasswordTextView.setText("please enter your password!");
        switchLockViews();
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
