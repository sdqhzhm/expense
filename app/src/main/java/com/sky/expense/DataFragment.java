package com.sky.expense;


import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sky.expense.DataBase.BackUp;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DataFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class DataFragment extends Fragment {

    public static DataFragment newInstance() {
        DataFragment fragment = new DataFragment();
        return fragment;
    }
    public DataFragment() {
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
        View view = inflater.inflate(R.layout.fragment_data, container, false);

        view.findViewById(R.id.backup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BackUpTask(getActivity()).execute();
            }
        });
        view.findViewById(R.id.restore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RestoreTask(getActivity()).execute();
            }
        });

        return view;
    }

    private class BackUpTask extends AsyncTask<Void, Void, Boolean> {

        private BackUp mBackUp;

        private BackUpTask(Context context) {
            mBackUp = new BackUp(context);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return mBackUp.doBackup();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                Toast.makeText(getActivity(),"备份数据成功",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(),"备份数据失败",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private class RestoreTask extends AsyncTask<Void, Void, Boolean> {

        private BackUp mBackUp;

        private RestoreTask(Context context) {
            mBackUp = new BackUp(context);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return mBackUp.doRestore();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                Toast.makeText(getActivity(),"恢复数据成功",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(),"恢复数据失败",Toast.LENGTH_LONG).show();
            }
        }

    }

}
