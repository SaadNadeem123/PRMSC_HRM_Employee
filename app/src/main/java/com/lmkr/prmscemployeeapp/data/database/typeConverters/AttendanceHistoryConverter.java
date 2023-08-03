package com.lmkr.prmscemployeeapp.data.database.typeConverters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lmkr.prmscemployeeapp.data.database.models.AttendanceHistory;

import java.util.Collections;
import java.util.List;

public class AttendanceHistoryConverter {

    @TypeConverter
    public static List<AttendanceHistory> StringToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        return new Gson().fromJson(data,new TypeToken<List<AttendanceHistory>>() {}.getType());
    }

    @TypeConverter
    public static String ListToString(List<AttendanceHistory> list) {
        return new Gson().toJson(list,new TypeToken<List<AttendanceHistory>>() {}.getType());
    }
}
