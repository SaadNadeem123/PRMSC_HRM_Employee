package com.lmkr.prmscemployeeapp.ui.customViews;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.lmkr.prmscemployeeapp.R;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;

import java.util.Calendar;
import java.util.TimeZone;

public class CustomDatePickerDialog implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    public static final int START_DATE = 1;
    public static final int END_DATE = 2;
    private static final int DEFAULT = -1;
    private final Context _context;
    private final long minimumDate = System.currentTimeMillis();
    TextView _textView, _textView2;
    EditText _editText, _editText2;
    TextInputEditText _textInputEditText, _textInputEditText2;

    //    int datePickerThemeResId = AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;
    int datePickerThemeResId = R.style.MyDatePickerDialogTheme;
    private int dateType = DEFAULT;
    private int _day;
    private int _month;
    private int _birthYear;
    private float remaining_leave_count = DEFAULT;

    public CustomDatePickerDialog(Context context, TextView textView, TextView textView2, float remaining_leave_count, int dateType) {
        this._textView = textView;
        this._textView2 = textView2;
        this.dateType = dateType;
        this._textView.setOnClickListener(this);
        this._context = context;
        this.remaining_leave_count = remaining_leave_count;
    }

    public CustomDatePickerDialog(Context context, EditText editText, EditText editText2, float remaining_leave_count, int dateType) {
        this._editText = editText;
        this._editText2 = editText2;
        this.dateType = dateType;
        this._editText.setOnClickListener(this);
        this._context = context;
        this.remaining_leave_count = remaining_leave_count;
    }

    public CustomDatePickerDialog(Context context, TextInputEditText editText, TextInputEditText editText2, float remaining_leave_count, int dateType) {
        this._textInputEditText = editText;
        this._textInputEditText2 = editText2;
        this.dateType = dateType;
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


        // dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        // dialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        if (_textView != null && _textView.getText().toString() != null && (!_textView.getText().toString().equals("")) && _textView.getText().toString().contains("-")) {
            String[] date = _textView.getText().toString().trim().split("-");
            if (date.length == 3)
                calendar.set(Integer.parseInt(date[2]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[0]));

            dialog = new DatePickerDialog(_context, datePickerThemeResId, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }

        if (_editText != null && _editText.getText().toString() != null && (!_editText.getText().toString().equals("")) && _editText.getText().toString().contains("-")) {
            String[] date = _editText.getText().toString().trim().split("-");
            if (date.length == 3)
                calendar.set(Integer.parseInt(date[2]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[0]));

            dialog = new DatePickerDialog(_context, datePickerThemeResId, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }

        if (_textInputEditText != null && _textInputEditText.getText().toString() != null && (!_textInputEditText.getText().toString().equals("")) && _textInputEditText.getText().toString().contains("")) {
            String[] date = _textInputEditText.getText().toString().trim().split("-");
            if (date.length == 3)
                calendar.set(Integer.parseInt(date[2]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[0]));

            dialog = new DatePickerDialog(_context, datePickerThemeResId, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }


        if (minimumDate != -1) {
            dialog.getDatePicker().setMinDate(minimumDate);
        }

        String startDate = "";

        if (_textView2 != null) {
            startDate = _textView2.getText().toString();
        }

        if (_editText2 != null) {
            startDate = _editText2.getText().toString();
        }

        if (_textInputEditText2 != null) {
            startDate = _textInputEditText2.getText().toString();
        }

        if (dateType == END_DATE && !TextUtils.isEmpty(startDate)) {
            //add remaining day in start date and set it as max date selection limit

            long maxdate;
            if ((int) remaining_leave_count > 1) {//                dialog.getDatePicker().setMaxDate(System.currentTimeMillis()+ 30L * 24 * 60 * 60 * 1000);
                maxdate = System.currentTimeMillis() + ((long) ((int) remaining_leave_count) * 24 * 60 * 60 * 1000) + AppUtils.getDateDifference(AppUtils.getDateFromString(AppUtils.getCurrentDateGMT5String() + " 00:00:00"/*+AppUtils.getCurrentTimeOfDayWithSeconds()*/, AppUtils.FORMAT14), AppUtils.getDateFromString(AppUtils.getConvertedDateFromOneFormatToOther(startDate, AppUtils.FORMAT15, AppUtils.FORMAT3) + " 00:00:00"/*+AppUtils.getCurrentTimeOfDayWithSeconds()*/, AppUtils.FORMAT14));
            } else {
                maxdate = System.currentTimeMillis() + AppUtils.getDateDifference(AppUtils.getDateFromString(AppUtils.getCurrentDateGMT5String() + " " + AppUtils.getCurrentTimeOfDayWithSeconds(), AppUtils.FORMAT14), AppUtils.getDateFromString(AppUtils.getConvertedDateFromOneFormatToOther(startDate, AppUtils.FORMAT15, AppUtils.FORMAT3) + " 00:00:00" /*+ AppUtils.getCurrentTimeOfDayWithSeconds()*/, AppUtils.FORMAT14));
            }
            dialog.getDatePicker().setMaxDate(maxdate);

            long minLimit =  AppUtils.getDateFromString(AppUtils.getConvertedDateFromOneFormatToOther(startDate, AppUtils.FORMAT15, AppUtils.FORMAT3) + " 00:00:00"/*+AppUtils.getCurrentTimeOfDayWithSeconds()*/, AppUtils.FORMAT14).getTime();
            dialog.getDatePicker().setMinDate(minLimit);
        }

        dialog.show();

        dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        dialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        dialog.getButton(DatePickerDialog.BUTTON_NEUTRAL).setTextColor(Color.BLACK);


    }

    // updates the date in the birth date EditText
    private void updateDisplay() {
        int year = _birthYear;
        int month = _month + 1; // Month is 0 based so add 1
        int day = _day;
        StringBuilder dateTimeStamp = new StringBuilder();

        /*if (dateType == START_DATE) {
            dateTimeStamp.append(year).append("-").append(month < 10 ? "0" + month : month).append("-").append(day < 10 ? "0" + day : day);
            dateTimeStamp.append(" 00:00:00");
        } else if (dateType == END_DATE) {
            dateTimeStamp.append(year).append("-").append(month < 10 ? "0" + month : month).append("-").append(day < 10 ? "0" + day : day);
            dateTimeStamp.append(" 23:59:59");
        } else {
            dateTimeStamp.append(day < 10 ? "0" + day : day).append("-").append(month < 10 ? "0" + month : month).append("-").append(year);
        }*/


        dateTimeStamp.append(day < 10 ? "0" + day : day).append("-").append(month < 10 ? "0" + month : month).append("-").append(year);

        if (_textView != null) {
            _textView.setText(dateTimeStamp);
        }
        if (_editText != null) {
            _editText.setText(dateTimeStamp);
        }
        if (_textInputEditText != null) {
            _textInputEditText.setText(dateTimeStamp);
        }

        if (dateType == START_DATE && remaining_leave_count < 2) {
            if (_textView2 != null) {
                _textView2.setText(dateTimeStamp);
            }
            if (_editText2 != null) {
                _editText2.setText(dateTimeStamp);
            }
            if (_textInputEditText2 != null) {
                _textInputEditText2.setText(dateTimeStamp);
            }
        }

    }
}
