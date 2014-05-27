package com.sky.expense;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sky.expense.UI.ProgressCircle;

import java.lang.ref.WeakReference;

/**
 * Created by sky on 2014/5/9.
 */
public class MainFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    private ProgressCircle mProgressCircle;


    private MyHandler mIncrementHandler = new MyHandler(this);

    public static class MyHandler extends Handler {

        private int mSpentProgress = 40;
        private int mTempSpentProgress = 0;
        private int delayMillis = 10;

        WeakReference<MainFragment> mFragment = null;

        MyHandler(MainFragment mFragment) {
            this.mFragment = new WeakReference<MainFragment>(mFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            MainFragment fragment = mFragment.get();
            mTempSpentProgress += 2;
            if (mTempSpentProgress <= mSpentProgress) {
                fragment.getProgressCircle().refreshProgress(mTempSpentProgress);
                this.sendEmptyMessageDelayed(0, delayMillis);
            } else {
                fragment.getProgressCircle().refreshProgress(mSpentProgress);
                removeMessages(0);
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_main, container, false);
        mProgressCircle = (ProgressCircle) view.findViewById(R.id.spent);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mIncrementHandler.sendEmptyMessageDelayed(0,500);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mIncrementHandler.removeMessages(0);
    }

    public ProgressCircle getProgressCircle() {
        return mProgressCircle;
    }

}
