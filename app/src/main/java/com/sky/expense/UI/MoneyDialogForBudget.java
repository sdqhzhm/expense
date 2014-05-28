package com.sky.expense.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sky.expense.R;

/**
 * Created by sky on 2014/5/26.
 */
public class MoneyDialogForBudget extends DialogFragment implements View.OnClickListener{

    public final static String KEY_MONEY = "money";

    private float mMoney;

    private View mMainView;

    private TextView mMoneyText;

    private String mInput = "";

    public static MoneyDialogForBudget newIntance(float money){
        MoneyDialogForBudget dialog = new MoneyDialogForBudget();
        Bundle args = new Bundle();
        args.putFloat(KEY_MONEY, money);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        mMainView = mInflater.inflate(R.layout.number_softinput, null);
        bindClick(mMainView, R.id.num_0, R.id.num_1, R.id.num_2, R.id.num_3, R.id.num_4, R.id.num_5,
                R.id.num_6, R.id.num_7, R.id.num_8, R.id.num_9, R.id.num_point, R.id.backspace);
        mMoneyText = (TextView) mMainView.findViewById(R.id.money);
        mMoney = getArguments().getFloat(KEY_MONEY);
        if (mMoney > 0) {
            mInput = String.valueOf(mMoney);
        }
        mMoneyText.setText(String.format("%.2f",mMoney));

        mMainView.findViewById(R.id.backspace).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mInput = "0";
                mMoneyText.setText(String.format("%.2f", Float.parseFloat(mInput)));
                return true;
            }
        });
        return new AlertDialog.Builder(getActivity())
                .setView(mMainView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((TextView) getActivity().findViewById(R.id.money)).setText(String.format("%.2f",mMoney));
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.num_0:
                mInput = makeNumber("0");
                break;
            case R.id.num_1:
                mInput = makeNumber("1");
                break;
            case R.id.num_2:
                mInput = makeNumber("2");
                break;
            case R.id.num_3:
                mInput = makeNumber("3");
                break;
            case R.id.num_4:
                mInput = makeNumber("4");
                break;
            case R.id.num_5:
                mInput = makeNumber("5");
                break;
            case R.id.num_6:
                mInput = makeNumber("6");
                break;
            case R.id.num_7:
                mInput = makeNumber("7");
                break;
            case R.id.num_8:
                mInput = makeNumber("8");
                break;
            case R.id.num_9:
                mInput = makeNumber("9");
                break;
            case R.id.num_point:
                mInput = makeNumber(".");
                break;
            case R.id.backspace:
                if (mInput.length() > 0) {
                    mInput = mInput.substring(0,mInput.length()-1);
                    if (mInput.length() > 0 && ".".equals(mInput.substring(mInput.length()-1,mInput.length()))) {
                        mInput = mInput.substring(0,mInput.length()-1);
                    }
                }
                break;
        }
        if (mInput.equals("")) {
            mInput = "0";
        }
        mMoney = Float.parseFloat(mInput);
        mMoneyText.setText(String.format("%.2f",mMoney));
    }

    private void bindClick(View view,int... ids) {
        for (int id : ids) {
            view.findViewById(id).setOnClickListener(this);
        }
    }

    private String makeNumber(String input) {
        int point = mInput.indexOf(".");
        if ( point >= 0) {
            if (input.equals(".")) {
                return mInput;
            }
            if (point <= mInput.length() - 3){
                return mInput;
            }
        }
        return mInput + input;
    }
}
