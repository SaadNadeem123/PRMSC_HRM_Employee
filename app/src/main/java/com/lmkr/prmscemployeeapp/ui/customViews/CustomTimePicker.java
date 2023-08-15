package com.lmkr.prmscemployeeapp.ui.customViews;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.lmkr.prmscemployeeapp.R;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;
import com.lmkr.prmscemployeeapp.ui.utilities.AppWideWariables;

import java.util.Calendar;
import java.util.TimeZone;

public class CustomTimePicker implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    public static final int START_TIME = 1;
    public static final int END_TIME = 2;
    private static final int DEFAULT = -1;
    private final Context _context;
    private final int minimumTime = Integer.valueOf(AppUtils.getCurrentHourOfDay());
    private final boolean is24HoursView = false;
    TextView _textView, _textView2;
    EditText _editText, _editText2;
    TextInputEditText _textInputEditText, _textInputEditText2;
    int timePickerThemeResId = R.style.MyTimePickerDialogTheme;
    private int timeType = DEFAULT;
    private int _hour_of_day;
    private int _minutes;
    private float halfLeaveLimit = -1;

    public CustomTimePicker(Context _context, TextView _textView, TextView _textView2, int timeType, float halfLeaveLimit) {
        this._context = _context;
        this._textView = _textView;
        this._textView.setOnClickListener(this);
        this._textView2 = _textView2;
        this.timeType = timeType;
        this.halfLeaveLimit = halfLeaveLimit;
    }

    public CustomTimePicker(Context _context, EditText _editText, EditText _editText2, int timeType, float halfLeaveLimit) {
        this._context = _context;
        this._editText = _editText;
        this._editText.setOnClickListener(this);
        this._editText2 = _editText2;
        this.timeType = timeType;
        this.halfLeaveLimit = halfLeaveLimit;
    }

    public CustomTimePicker(Context _context, TextInputEditText _textInputEditText, TextInputEditText _textInputEditText2, int timeType, float halfLeaveLimit) {
        this._context = _context;
        this._textInputEditText = _textInputEditText;
        this._textInputEditText.setOnClickListener(this);
        this._textInputEditText2 = _textInputEditText2;
        this.timeType = timeType;
        this.halfLeaveLimit = halfLeaveLimit;
    }

    public Context get_context() {
        return _context;
    }

    public TextInputEditText get_textInputEditText() {
        return _textInputEditText;
    }

    public void set_textInputEditText(TextInputEditText _textInputEditText) {
        this._textInputEditText = _textInputEditText;
    }

    public TextInputEditText get_textInputEditText2() {
        return _textInputEditText2;
    }

    public void set_textInputEditText2(TextInputEditText _textInputEditText2) {
        this._textInputEditText2 = _textInputEditText2;
    }

    public int getTimeType() {
        return timeType;
    }

    public void setTimeType(int timeType) {
        this.timeType = timeType;
    }

    public float getHalfLeaveLimit() {
        return halfLeaveLimit;
    }

    public void setHalfLeaveLimit(float halfLeaveLimit) {
        this.halfLeaveLimit = halfLeaveLimit;
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

        CustomizedTimePicker dialog = new CustomizedTimePicker(_context, timePickerThemeResId, this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), is24HoursView);
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

        if (_textView != null && _textView.getText().toString() != null && (!_textView.getText().toString().equals("")) && _textView.getText().toString().contains(":")) {
            String[] time = _textView.getTag().toString().trim().split(":");
            if (time.length == 2)
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), Integer.parseInt(time[0]), Integer.parseInt(time[1]));

            dialog = new CustomizedTimePicker(_context, timePickerThemeResId, this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), is24HoursView);
        }
        if (_editText != null && _editText.getText().toString() != null && (!_editText.getText().toString().equals("")) && _editText.getText().toString().contains(":")) {
            String[] time = _editText.getTag().toString().trim().split(":");
            if (time.length == 2)
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), Integer.parseInt(time[0]), Integer.parseInt(time[1]));

            dialog = new CustomizedTimePicker(_context, timePickerThemeResId, this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), is24HoursView);
        }
        if (_textInputEditText != null && _textInputEditText.getText().toString() != null && (!_textInputEditText.getText().toString().equals("")) && _textInputEditText.getText().toString().contains(":")) {
            String[] time = _textInputEditText.getTag().toString().trim().split(":");
            if (time.length == 2)
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), Integer.parseInt(time[0]), Integer.parseInt(time[1]));

            dialog = new CustomizedTimePicker(_context, timePickerThemeResId, this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), is24HoursView);
        }


        String startTime = "";

        if (_textView2 != null) {
            startTime = _textView2.getText().toString();
        }

        if (_editText2 != null) {
            startTime = _editText2.getText().toString();
        }

        if (_textInputEditText2 != null) {
            startTime = _textInputEditText2.getText().toString();
        }

        if (!TextUtils.isEmpty(startTime)) {
            int hours = Integer.parseInt(AppUtils.getConvertedDateFromOneFormatToOther(startTime, AppUtils.FORMAT5, AppUtils.FORMAT_HOUR));
            int min = Integer.parseInt(AppUtils.getConvertedDateFromOneFormatToOther(startTime, AppUtils.FORMAT5, AppUtils.FORMAT_MIN));
            dialog.setMin(hours, min);
            if (timeType == END_TIME) {
                if (halfLeaveLimit > 0 && halfLeaveLimit < 1) {
                    dialog.setMax((int) (hours + AppWideWariables.HALF_LEAVE_HOUR_LIMIT), min);
                } else {
                    if (min + AppWideWariables.MINIMUM_TIME_DIFF < 60) {
                        dialog.setMin((int) (hours + AppWideWariables.HALF_LEAVE_HOUR_LIMIT), min + AppWideWariables.MINIMUM_TIME_DIFF);
                    } else {
                        dialog.setMin((int) (hours + 1), AppWideWariables.MINIMUM_TIME_DIFF-(60-min));
                    }
                }
            }
        } if (timeType == START_TIME && minimumTime != -1) {
            dialog.setMin(Integer.valueOf(AppUtils.getCurrentHourOfDay()), Integer.valueOf(AppUtils.getCurrentMinOfDay()));
        }


        dialog.show();

    }


    // updates the date in the birth date EditText
    private void updateDisplay() {
        int hourOfDay = _hour_of_day;
        int minutes = _minutes;
        StringBuilder dateTimeStamp = new StringBuilder();
        String hours = hourOfDay < 10 ? "0" + hourOfDay : hourOfDay + "";
        String min = minutes < 10 ? "0" + minutes : minutes + "";
        String time_24hrs = hours + ":" + minutes;
        dateTimeStamp.append(AppUtils.getConvertedDateFromOneFormatToOther(time_24hrs, AppUtils.FORMAT6, AppUtils.FORMAT5));


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

        if (timeType == START_TIME && halfLeaveLimit > 0 && halfLeaveLimit < 1) {
            if (_textView2 != null) {
                _textView2.setTag(time_24hrs);
                _textView2.setText(dateTimeStamp);
            }
            if (_editText2 != null) {
                _editText2.setTag(time_24hrs);
                _editText2.setText(dateTimeStamp);
            }
            if (_textInputEditText2 != null) {
                _textInputEditText2.setTag(time_24hrs);
                _textInputEditText2.setText(dateTimeStamp);
            }
        }

    }
}
