package com.lmkr.prmscemployeeapp.ui.customViews;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.lmkr.prmscemployeeapp.R;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;

import java.util.Calendar;
import java.util.TimeZone;

public class CustomTimePicker implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    public static final int START_TIME = 1;
    public static final int END_TIME = 2;
    private static final int DEFAULT = -1;
    private final Context _context;
    TextView _textView;
    EditText _editText;
    TextInputEditText _textInputEditText;
    private int timeType = DEFAULT;
    private int _hour_of_day;
    private int _minutes;
    int timePickerThemeResId = R.style.MyTimePickerDialogTheme;
    private int minimumTime =Integer.valueOf(AppUtils.getCurrentHourOfDay());
    private boolean is24HoursView = false;


    public CustomTimePicker(Context context, TextView textView) {
        Activity act = (Activity) context;
        this._textView = textView;
        this._textView.setOnClickListener(this);
        this._context = context;
    }

    public CustomTimePicker(Context context, EditText editText) {
        Activity act = (Activity) context;
        this._editText = editText;
        this._editText.setOnClickListener(this);
        this._context = context;
    }

    public CustomTimePicker(Context context, TextInputEditText editText) {
        Activity act = (Activity) context;
        this._textInputEditText = editText;
        this._textInputEditText.setOnClickListener(this);
        this._context = context;
    }

    public CustomTimePicker(Context context, TextView textView, int timeType) {
        Activity act = (Activity) context;
        this._textView = textView;
        this._textView.setOnClickListener(this);
        this._context = context;
        this.timeType = timeType;
    }

    public CustomTimePicker(Context context, EditText editText, int timeType) {
        Activity act = (Activity) context;
        this._editText = editText;
        this._editText.setOnClickListener(this);
        this._context = context;
        this.timeType = timeType;
    }

    public CustomTimePicker(Context context, TextInputEditText editText, int timeType) {
        Activity act = (Activity) context;
        this._textInputEditText = editText;
        this._textInputEditText.setOnClickListener(this);
        this._context = context;
        this.timeType = timeType;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        _hour_of_day = hourOfDay;
        _minutes = minute;
        updateDisplay();
    }

    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        CustomizedTimePicker dialog = new CustomizedTimePicker(_context, timePickerThemeResId,this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), is24HoursView);
//        dialog.getDatePicker().setSpinnersShown(true);
//        dialog.getDatePicker().setCalendarViewShown(false);


        CustomizedTimePicker finalDialog = dialog;
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                int positiveColor = ContextCompat.getColor(_context, R.color.black);
                int negativeColor = ContextCompat.getColor(_context, R.color.black);
                finalDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(positiveColor);
                finalDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(negativeColor);
            }
        });

        //        dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
//        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        if (_textView != null && _textView.getText().toString() != null && (!_textView.getText().toString().equals("")) && _textView.getText().toString().contains("/")) {
            String[] time = _textView.getTag().toString().trim().split(":");
            if (time.length == 2)
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                        Integer.parseInt(time[0]), Integer.parseInt(time[1].split(" ")[0]));

            dialog = new CustomizedTimePicker(_context,timePickerThemeResId, this,
                    calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    is24HoursView);
        }
        if (_editText != null && _editText.getText().toString() != null && (!_editText.getText().toString().equals("")) && _editText.getText().toString().contains("/")) {
            String[] time = _editText.getTag().toString().trim().split(":");
            if (time.length == 2)
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                        Integer.parseInt(time[0]), Integer.parseInt(time[1].split(" ")[0]));

            dialog = new CustomizedTimePicker(_context, timePickerThemeResId,this,
                    calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    is24HoursView);
        }
        if (_textInputEditText != null && _textInputEditText.getText().toString() != null && (!_textInputEditText.getText().toString().equals("")) && _textInputEditText.getText().toString().contains("/")) {
            String[] time = _textInputEditText.getTag().toString().trim().split(":");
            if (time.length == 2)
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                        Integer.parseInt(time[0]), Integer.parseInt(time[1].split(" ")[0]));

            dialog = new CustomizedTimePicker(_context, timePickerThemeResId,this,
                    calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    is24HoursView);
        }

        if (minimumTime != -1) {
            dialog.setMin(Integer.valueOf(AppUtils.getCurrentHourOfDay()),Integer.valueOf(AppUtils.getCurrentMinOfDay()));
        }

        dialog.show();

    }


    // updates the date in the birth date EditText
    private void updateDisplay() {
        int hourOfDay = _hour_of_day;
        int minutes = _minutes;
        StringBuilder dateTimeStamp = new StringBuilder();
        String hours = hourOfDay<10?"0"+hourOfDay:hourOfDay+"";
        String min = minutes<10?"0"+minutes:minutes+"";
        String time_24hrs = hours+":"+minutes;
        dateTimeStamp.append(AppUtils.getConvertedDateFromOneFormatToOther(time_24hrs,AppUtils.FORMAT6,AppUtils.FORMAT5));


        if (_textView != null) {
            _textView.setTag(time_24hrs);
            _textView.setText(dateTimeStamp);
        }
        if (_editText != null) {
            _editText.setTag(time_24hrs);
            _editText.setText(dateTimeStamp);
        }
        if (_textInputEditText != null) {
            _textInputEditText.setTag(time_24hrs);
            _textInputEditText.setText(dateTimeStamp);
        }
    }
}
