package com.reginald.patternlockview;

import java.util.Arrays;

import com.reginald.patternlockview.demo.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DemoActivity extends Activity {

    private static final String TAG = "DemoActivity";
    private static final PatternLockView.Password sDefaultPassword =
            new PatternLockView.Password(Arrays.asList(0, 1, 2, 3, 4, 5));

    private PatternLockView mCurLockView;

    private PatternLockView mCircleLockView;

    private PatternLockView mDotLockView;

    private TextView mPasswordTextView;

    private Button mSwitchButton;

    private Button mPatternVisibleButton;

    private Button mPatternShowButton;

    private Button mPatternShowAnimButton;

    private PatternLockView.Password mPassword = sDefaultPassword;

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

        mPatternShowButton = (Button) findViewById(R.id.switch_show);
        mPatternShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurLockView.showPassword(mPassword.list);
                mPasswordTextView.setText("show password: " + mPassword.string);
            }
        });

        mPatternShowAnimButton = (Button) findViewById(R.id.switch_show_anim);
        mPatternShowAnimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurLockView.isPasswordAnim()) {
                    mCurLockView.stopPasswordAnim();
                } else {
                    mPatternShowAnimButton.setText("stop password anim");
                    mCurLockView.showPasswordWithAnim(mPassword.list, -1,
                            new PatternLockView.onAnimFinishListener() {
                                @Override
                                public void onFinish(boolean isStopped) {
                                    mPatternShowAnimButton.setText("start password anim");
                                }
                            });
                    mPasswordTextView.setText("show password animation: " + mPassword.string);
                }
            }
        });

        switchLockViews();
    }

    private void switchLockViews() {
        mPassword = sDefaultPassword;
        mCurLockView.stopPasswordAnim();

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

                if (mPassword.equals(password)) {
                    return PatternLockView.CODE_PASSWORD_CORRECT;
                } else {
                    mPassword = password;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // DO NOT FORGET TO CALL IT!
        mCurLockView.stopPasswordAnim();
    }
}
