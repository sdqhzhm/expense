package com.sky.expense;


import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sky.expense.UI.MoneyDialogForBudget;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BudgetFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class BudgetFragment extends Fragment {

    private TextView mBudgetText;

    private float mBudget;

    public static BudgetFragment newInstance() {
        BudgetFragment fragment = new BudgetFragment();
        return fragment;
    }

    public BudgetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedata = getActivity().getSharedPreferences("data", 0);
        mBudget = sharedata.getFloat("budget", 1500);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_budget, container, false);
        mBudgetText = (TextView) view.findViewById(R.id.budget);
        mBudgetText.setText(String.format("本月预算金额：%.2f￥",mBudget));
        view.findViewById(R.id.set_budget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoneyDialogForBudget dialog = MoneyDialogForBudget.newIntance(mBudget);
                dialog.show(getChildFragmentManager(), null);
            }
        });
        return view;
    }


}
