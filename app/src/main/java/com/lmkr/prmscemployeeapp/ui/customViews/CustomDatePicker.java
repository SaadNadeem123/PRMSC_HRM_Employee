package com.lmkr.prmscemployeeapp.ui.customViews;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.lmkr.prmscemployeeapp.R;

import java.util.Calendar;
import java.util.TimeZone;


public class CustomDatePicker implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    public static final int START_DATE = 1;
    public static final int END_DATE = 2;
    private static final int DEFAULT = -1;
    private final Context _context;
    TextView _textView, _textView2;
    EditText _editText, _editText2;
    TextInputEditText _textInputEditText, _textInputEditText2;
    //    int datePickerThemeResId = AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;
    int datePickerThemeResId = R.style.MyDatePickerDialogTheme;
    private int _day;
    private int _month;
    private int _birthYear;
    private int remaining_leave_count = DEFAULT;
    private final long minimumDate = System.currentTimeMillis();

    public CustomDatePicker(Context context, TextView textView, TextView textView2) {
        Activity act = (Activity) context;
        this._textView = textView;
        this._textView2 = textView2;
        this._textView.setOnClickListener(this);
        this._context = context;
    }

    public CustomDatePicker(Context context, EditText editText,EditText editText2) {
        Activity act = (Activity) context;
        this._editText = editText;
        this._editText2 = editText2;
        this._editText.setOnClickListener(this);
        this._context = context;
    }

    public CustomDatePicker(Context context, TextInputEditText editText, TextInputEditText editText2) {
        Activity act = (Activity) context;
        this._textInputEditText = editText;
        this._textInputEditText2 = editText2;
        this._textInputEditText.setOnClickListener(this);
        this._context = context;
    }

    public CustomDatePicker(Context context, TextView textView,TextView textView2, int remaining_leave_count) {
        Activity act = (Activity) context;
        this._textView = textView;
        this._textView2 = textView2;
        this._textView.setOnClickListener(this);
        this._context = context;
        this.remaining_leave_count = remaining_leave_count;
    }

    public CustomDatePicker(Context context, EditText editText,EditText editText2, int remaining_leave_count) {
        Activity act = (Activity) context;
        this._editText = editText;
        this._editText2 = editText2;
        this._editText.setOnClickListener(this);
        this._context = context;
        this.remaining_leave_count = remaining_leave_count;
    }

    public CustomDatePicker(Context context, TextInputEditText editText,TextInputEditText editText2, int remaining_leave_count) {
        Activity act = (Activity) context;
        this._textInputEditText = editText;
        this._textInputEditText2 = editText2;
        this._textInputEditText.setOnClickListener(this);
        this._context = context;
        this.remaining_leave_count = remaining_leave_count;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        _birthYear = year;
        _month = monthOfYear;
        _day = dayOfMonth;
        updateDisplay();
    }

    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog dialog = new DatePickerDialog(_context, datePickerThemeResId, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setSpinnersShown(true);
        dialog.getDatePicker().setCalendarViewShown(false);

        DatePickerDialog finalDialog = dialog;
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                int positiveColor = ContextCompat.getColor(_context, R.color.black);
                int negativeColor = ContextCompat.getColor(_context, R.color.black);
                finalDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(positiveColor);
                finalDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(negativeColor);
            }
        });
        if (minimumDate != -1) {
            dialog.getDatePicker().setMinDate(minimumDate);
        }


        //        dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
//        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        if (_textView != null && _textView.getText().toString() != null && (!_textView.getText().toString().equals("")) && _textView.getText().toString().contains("/")) {
            String[] date = _textView.getText().toString().trim().split("/");
            if (date.length == 3)
                calendar.set(Integer.parseInt(date[2]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[0]));

            dialog = new DatePickerDialog(_context, this,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
        }
        if (_editText != null && _editText.getText().toString() != null && (!_editText.getText().toString().equals("")) && _editText.getText().toString().contains("/")) {
            String[] date = _editText.getText().toString().trim().split("/");
            if (date.length == 3)
                calendar.set(Integer.parseInt(date[2]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[0]));

            dialog = new DatePickerDialog(_context, this,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
        }
        if (_textInputEditText != null && _textInputEditText.getText().toString() != null && (!_textInputEditText.getText().toString().equals("")) && _textInputEditText.getText().toString().contains("/")) {
            String[] date = _textInputEditText.getText().toString().trim().split("/");
            if (date.length == 3)
                calendar.set(Integer.parseInt(date[2]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[0]));

            dialog = new DatePickerDialog(_context, this,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
        }

        dialog.show();

    }

    // updates the date in the birth date EditText
    private void updateDisplay() {
        int year = _birthYear;
        int month = _month + 1; // Month is 0 based so add 1
        int day = _day;
        StringBuilder dateTimeStamp = new StringBuilder();
        if (remaining_leave_count == START_DATE) {
            dateTimeStamp.append(year).append("-").append(month < 10 ? "0" + month : month).append("-").append(day < 10 ? "0" + day : day);
            dateTimeStamp.append(" 00:00:00");
        } else if (remaining_leave_count == END_DATE) {
            dateTimeStamp.append(year).append("-").append(month < 10 ? "0" + month : month).append("-").append(day < 10 ? "0" + day : day);
            dateTimeStamp.append(" 23:59:59");
        } else {
            dateTimeStamp.append(day < 10 ? "0" + day : day).append("-").append(month < 10 ? "0" + month : month).append("-").append(year);
        }
        if (_textView != null) {
            _textView.setText(dateTimeStamp);
        }
        if (_editText != null) {
            _editText.setText(dateTimeStamp);
        }
        if (_textInputEditText != null) {
            _textInputEditText.setText(dateTimeStamp);
        }
    }
}
