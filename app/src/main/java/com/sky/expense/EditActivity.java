package com.sky.expense;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.sky.expense.DataBase.DBHelper;
import com.sky.expense.DataBase.DataProvider;
import com.sky.expense.UI.DatePickerFragment;
import com.sky.expense.UI.MoneyDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class EditActivity extends Activity {

    public final static String SORT_KEY_FOOD = "食品饮料";
    public final static String SORT_KEY_HEALTH = "健康";
    public final static String SORT_KEY_TRAFFIC = "交通";
    public final static String SORT_KEY_OTHER = "其它";


    private TextView mMoneyText, mDateText;
    private EditText mDescriptionEdit;

    private String mSortKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            int actionBarColor = getResources().getColor(R.color.actionbar);
            tintManager.setStatusBarTintColor(actionBarColor);
        }

        mMoneyText = (TextView) findViewById(R.id.money);
        mMoneyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float money = Float.parseFloat(mMoneyText.getText().toString());
                MoneyDialog dialog = MoneyDialog.newIntance(money);
                dialog.show(getFragmentManager(), null);
            }
        });

        mDateText = (TextView) findViewById(R.id.date);
        Calendar cal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        mDateText.setText(df.format(cal.getTime()));

        mDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        RadioGroup group = (RadioGroup) findViewById(R.id.group);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.food) {
                    mSortKey = SORT_KEY_FOOD;
                } else if (checkedId == R.id.health) {
                    mSortKey = SORT_KEY_HEALTH;
                } else if (checkedId == R.id.traffic) {
                    mSortKey = SORT_KEY_TRAFFIC;
                } else if (checkedId == R.id.other) {
                    mSortKey = SORT_KEY_OTHER;
                }
            }
        });

        mDescriptionEdit = (EditText) findViewById(R.id.description);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_save) {
            ContentResolver resolver =  getContentResolver();
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_AMOUNT, Float.parseFloat(mMoneyText.getText().toString()));
            values.put(DBHelper.KEY_NAME, mSortKey);
            values.put(DBHelper.KEY_TYPE, mSortKey);
            values.put(DBHelper.KEY_DESCRIPTION, mDescriptionEdit.getText().toString());
            DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();
            try {
                date = df.parse(mDateText.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DateFormat sqlDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            values.put(DBHelper.KEY_TIMESTAMP, sqlDF.format(date));
            Uri result = resolver.insert(DataProvider.CONTENT_URI, values);
            if (result == null) {
                Toast.makeText(this,R.string.save_fail,Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,R.string.save_success,Toast.LENGTH_SHORT).show();
            }
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
