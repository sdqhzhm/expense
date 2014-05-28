package com.sky.expense;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sky.expense.DataBase.DBHelper;
import com.sky.expense.DataBase.DataProvider;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HistoryFragment extends Fragment {

    private ViewPager mViewPager;
    private PagerTabStrip mPagerTab;
    private Map<Integer,ArrayList<HashMap<String,Object>>> mData;
    private List<ListView> mListViews;

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mPagerTab = (PagerTabStrip) view.findViewById(R.id.pager_tab);
        mPagerTab.setTabIndicatorColor(getResources().getColor(R.color.actionbar));
        LayoutInflater lf = getActivity().getLayoutInflater();
        mListViews = new ArrayList<ListView>();
        for (int i = 0; i < 12; i++) {
            mListViews.add((ListView) lf.inflate(R.layout.simple_list, null));
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        query();
        List<HistoryItemAdapter> mAdapters = new ArrayList<HistoryItemAdapter>();
        for (int i = 0; i < 12; i++) {
            mAdapters.add(null);
        }
        HistoryPagerAdapter pagerAdapter = new HistoryPagerAdapter(mListViews, mAdapters);
        mViewPager.setAdapter(pagerAdapter);
    }

    public void query(){
        ContentResolver mCR = getActivity().getContentResolver();

        String selection = DBHelper.KEY_TIMESTAMP + " BETWEEN " +
                "'2014-01-01' AND  '2014-12-31' ";

        Cursor cursor = mCR.query(DataProvider.CONTENT_URI, null
                , selection, null, DBHelper.KEY_TIMESTAMP + " asc");


        mData = new HashMap<Integer, ArrayList<HashMap<String, Object>>>();
        for (int i = 0; i < 12; i++) {
            ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String, Object>>();
            mData.put(i,list);
        }

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        while (cursor != null && cursor.moveToNext()) {
            String time = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TIMESTAMP));
            Calendar cal = Calendar.getInstance();
            try {
                Date date = df.parse(time);
                cal.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            HashMap<String,Object> map = new HashMap<String, Object>();
            map.put(DBHelper.KEY_NAME, cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME)));
            map.put(DBHelper.KEY_TYPE, cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TYPE)));
            map.put(DBHelper.KEY_AMOUNT, cursor.getFloat(cursor.getColumnIndex(DBHelper.KEY_AMOUNT)));
            map.put(DBHelper.KEY_TIMESTAMP, cal);
            map.put(DBHelper.KEY_DESCRIPTION, cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION)));

            mData.get(cal.get(cal.MONTH)).add(map);
        }
        cursor.close();

    }

    private class HistoryItemAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        private ArrayList<HashMap<String, Object>> mList;
        private DateFormat df;

        public HistoryItemAdapter(Context context, ArrayList<HashMap<String, Object>> list) {
            this.mInflater = LayoutInflater.from(context);
            this.mList = list;
            df = new SimpleDateFormat("yyyy-MM-dd");
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.history_item,null);
                holder = new ViewHolder();
                holder.indictor = convertView.findViewById(R.id.indictor);
                holder.date = (TextView) convertView.findViewById(R.id.date);
                holder.type = (TextView) convertView.findViewById(R.id.type);
                holder.money = (TextView) convertView.findViewById(R.id.money);
                convertView.setTag(holder);//绑定ViewHolder对象
            } else {
                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
            }
            String type = (String) mList.get(position).get(DBHelper.KEY_TYPE);
            if (EditActivity.SORT_KEY_FOOD.equals(type)) {
                holder.indictor.setBackgroundColor(getResources().getColor(R.color.blue));
            } else if (EditActivity.SORT_KEY_HEALTH.equals(type)) {
                holder.indictor.setBackgroundColor(getResources().getColor(R.color.red));
            } else if (EditActivity.SORT_KEY_TRAFFIC.equals(type)) {
                holder.indictor.setBackgroundColor(getResources().getColor(R.color.orange));
            } else if (EditActivity.SORT_KEY_OTHER.equals(type)) {
                holder.indictor.setBackgroundColor(getResources().getColor(R.color.green));
            }
            holder.type.setText(type);
            holder.date.setText(df.format(((Calendar)mList.get(position).get(DBHelper.KEY_TIMESTAMP)).getTime()));

            holder.money.setText(String.format("%.2f￥", (Float) mList.get(position).get(DBHelper.KEY_AMOUNT)));

            return convertView;
        }

        public final class ViewHolder{
            public View indictor;
            public TextView date;
            public TextView type;
            public TextView money;
        }
    }

    private class HistoryPagerAdapter extends PagerAdapter {

        private List<ListView> mListViews;
        private List<HistoryItemAdapter> mAdapters;
        private String[] titles = new String[] {
                "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"
        };

        public HistoryPagerAdapter(List<ListView> lists, List<HistoryItemAdapter> adapters){
            this.mListViews = lists;
            this.mAdapters = adapters;
        }

        @Override
        public int getCount() {
            return 12;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mListViews.get(position));
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            ListView mListView = mListViews.get(position);
            container.addView(mListView);
            HistoryItemAdapter mAdapter = mAdapters.get(position);
            if (mAdapter == null) {
                mAdapter = new HistoryItemAdapter(getActivity().getApplicationContext(), mData.get(position));
            }
            mListView.setAdapter(mAdapter);
            return mListView;
        }
    }
}
