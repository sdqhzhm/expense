package com.sky.expense;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.sky.expense.UI.DatePickerFragment;
import com.sky.expense.UI.MoneyDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class EditActivity extends Activity {

    private TextView mMoneyText, mDateText;

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
        }
        return super.onOptionsItemSelected(item);
    }
}
