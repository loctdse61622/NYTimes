package com.coderschool.nytimes.Dialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.coderschool.nytimes.Model.ArticleSearchRequest;
import com.coderschool.nytimes.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 6/23/2017.
 */

public class OptionDialog extends DialogFragment {
    @BindView(R.id.etBeginDate)
    EditText etBeginDate;
    @BindView(R.id.spSort)
    AppCompatSpinner spinner;
    @BindView(R.id.cbArts)
    CheckBox cbArts;
    @BindView(R.id.cbFashionAndStyle)
    CheckBox cbFashionAndStyle;
    @BindView(R.id.cbSports)
    CheckBox cbSports;
    @BindView(R.id.btSave)
    Button btSave;

    Context context;
    ArticleSearchRequest mSearchRequest;
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = null;

    public interface Listener{
        void onSaveOption(ArticleSearchRequest searchRequest);
    }

    public OptionDialog(){}

    public static OptionDialog newInstance(ArticleSearchRequest searchRequest, Context context){
        OptionDialog dialog = new OptionDialog();
        Bundle args = new Bundle();
        dialog.setArguments(args);
        dialog.setSearchRequest(searchRequest);
        dialog.setContext(context);
        return dialog;
    }

    private void setContext(Context context) {
        this.context = context;
    }

    private void setSearchRequest(ArticleSearchRequest searchRequest) {
        mSearchRequest = searchRequest;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.option_fragment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setUpSpinner();
        setUpDatePicker();

        etBeginDate.setOnClickListener(v -> {
            new DatePickerDialog(context, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        btSave.setOnClickListener(v -> {
            saveBeginDate();
            saveOrder();
            saveDesk();

            Listener listener = (Listener) getActivity();
            listener.onSaveOption(mSearchRequest);
            getDialog().dismiss();
        });
    }

    private void setUpSpinner(){
        List<String> orderList = new ArrayList<>();
        orderList.add("Newest");
        orderList.add("Oldest");
        ArrayAdapter<String> orderAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, orderList);
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(orderAdapter);
    }

    private void setUpDatePicker(){
        date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd/MM/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            etBeginDate.setText(sdf.format(myCalendar.getTime()));
        };
    }

    private void saveOrder(){
        String order = spinner.getSelectedItem().toString().toLowerCase();
        mSearchRequest.setOrder(order);
    }

    private void saveBeginDate(){
        String beginDate = null;
        try {
            Date tmpDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(etBeginDate.getText().toString());
            beginDate = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).format(tmpDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mSearchRequest.setBeginDate(beginDate);
    }

    private void saveDesk(){
        boolean hasArt = cbArts.isChecked();
        boolean hasFashionAndStyle = cbFashionAndStyle.isChecked();
        boolean hasSports = cbSports.isChecked();
        mSearchRequest.hasArts = hasArt;
        mSearchRequest.hasFashionStyle = hasFashionAndStyle;
        mSearchRequest.hasSports = hasSports;
    }
}
