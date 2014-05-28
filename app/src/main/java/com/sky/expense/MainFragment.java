package com.sky.expense;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sky.expense.DataBase.DBHelper;
import com.sky.expense.DataBase.DataProvider;
import com.sky.expense.UI.DensityUtil;
import com.sky.expense.UI.ProgressCircle;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by sky on 2014/5/9.
 */
public class MainFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    public MainFragment(){

    }

    private ProgressCircle mProgressCircle;

    private float mFoodMoney = 0, mHealthMoney = 0, mTrafficMoney = 0, mOtherMoney = 0;
    private TextView mFoodText, mHealthText, mTrafficText, mOtherText, mPercentText, mAllText, mBudgetText, mOverText;
    private View mFoodFlag, mHealthFlag, mTrafficFlag, mOtherFlag;

    private float mBudget = 1500;

    private MyHandler mIncrementHandler = new MyHandler(this);

    public static class MyHandler extends Handler {

        private int mSpentProgress = 0;
        private int mTempSpentProgress = 0;
        private int delayMillis = 10;

        public void setSpentProgress(int progress){
            this.mSpentProgress = progress;
        }

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
        SharedPreferences sharedata = getActivity().getSharedPreferences("data", 0);
        mBudget = sharedata.getFloat("budget", 1500);
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
        mFoodText = (TextView) view.findViewById(R.id.food);
        mHealthText = (TextView) view.findViewById(R.id.health);
        mTrafficText = (TextView) view.findViewById(R.id.traffic);
        mOtherText = (TextView) view.findViewById(R.id.other);

        mFoodFlag = view.findViewById(R.id.food_flag);
        mHealthFlag = view.findViewById(R.id.health_flag);
        mTrafficFlag = view.findViewById(R.id.traffic_flag);
        mOtherFlag = view.findViewById(R.id.other_flag);

        mAllText = (TextView) view.findViewById(R.id.all);
        mBudgetText = (TextView) view.findViewById(R.id.quota);
        mOverText = (TextView) view.findViewById(R.id.over);

        mPercentText = (TextView) view.findViewById(R.id.percent);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mFoodMoney = 0;
        mHealthMoney = 0;
        mTrafficMoney = 0;
        mOtherMoney = 0;
        mBudgetText.setText(String.format("of ￥%.2f", mBudget));
        count();
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

    private void count(){
        ContentResolver mCR = getActivity().getContentResolver();

        Calendar cal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        cal.set(Calendar.DATE,1);
        String start = df.format(cal.getTime());
        int lastday = cal.getMaximum(Calendar.DATE);
        cal.set(Calendar.DATE,lastday);
        String end = df.format(cal.getTime());

        String[] projection = new String[]{DBHelper.KEY_AMOUNT,DBHelper.KEY_TYPE
                ,DBHelper.KEY_TIMESTAMP,DBHelper.KEY_NAME,"SUM("+DBHelper.KEY_AMOUNT+")"};

        String selection = DBHelper.KEY_TIMESTAMP + " BETWEEN " +
                "'"+start+"' AND  '"+end+"' " + "GROUP BY (" + DBHelper.KEY_TYPE+")";

        Cursor cursor = mCR.query(DataProvider.CONTENT_URI, projection
                , selection, null, DBHelper.KEY_TYPE + " desc");

        while (cursor != null && cursor.moveToNext()) {
            String type = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TYPE));
            if (EditActivity.SORT_KEY_FOOD.equals(type)) {
                mFoodMoney = cursor.getLong(cursor.getColumnIndex("SUM("+DBHelper.KEY_AMOUNT+")"));
                mFoodText.setText(String.format("食品饮料 ￥%.2f",mFoodMoney));
                ViewGroup.LayoutParams linearParams =  mFoodFlag.getLayoutParams();
                float p = mFoodMoney/1000;
                linearParams.width = (int)(p * DensityUtil.dip2px(getActivity(), 360));
                mFoodFlag.setLayoutParams(linearParams);
            } else if (EditActivity.SORT_KEY_HEALTH.equals(type)) {
                mHealthMoney = cursor.getLong(cursor.getColumnIndex("SUM("+DBHelper.KEY_AMOUNT+")"));
                mHealthText.setText(String.format("健康 ￥%.2f",mHealthMoney));
                ViewGroup.LayoutParams linearParams =  mHealthFlag.getLayoutParams();
                float p = mHealthMoney/1000;
                linearParams.width = (int)(p * DensityUtil.dip2px(getActivity(), 360));
                mHealthFlag.setLayoutParams(linearParams);
            } else if (EditActivity.SORT_KEY_TRAFFIC.equals(type)) {
                mTrafficMoney = cursor.getLong(cursor.getColumnIndex("SUM("+DBHelper.KEY_AMOUNT+")"));
                mTrafficText.setText(String.format("交通 ￥%.2f",mTrafficMoney));
                ViewGroup.LayoutParams linearParams =  mTrafficFlag.getLayoutParams();
                float p = mTrafficMoney/1000;
                linearParams.width = (int)(p * DensityUtil.dip2px(getActivity(), 360));
                mTrafficFlag.setLayoutParams(linearParams);
            } else if (EditActivity.SORT_KEY_OTHER.equals(type)) {
                mOtherMoney = cursor.getLong(cursor.getColumnIndex("SUM("+DBHelper.KEY_AMOUNT+")"));
                mOtherText.setText(String.format("其它 ￥%.2f",mOtherMoney));
                ViewGroup.LayoutParams linearParams =  mOtherFlag.getLayoutParams();
                float p = mOtherMoney/1000;
                linearParams.width = (int)(p * DensityUtil.dip2px(getActivity(), 360));
                mOtherFlag.setLayoutParams(linearParams);
            }
        }
        cursor.close();

        float all = mFoodMoney+mHealthMoney+mTrafficMoney+mOtherMoney;
        mAllText.setText(String.format("%.2f",all));
        if (all <= mBudget) {
            mOverText.setText(String.format("距离你的限额还有 %.2f￥",mBudget - all));
        } else {
            mOverText.setText(String.format("本月已经超出预算 %.2f￥",all - mBudget));
        }

        int p = (int) (all / mBudget * 100) ;
        mIncrementHandler.setSpentProgress(p);
        mPercentText.setText(p + "%");
    }

}
