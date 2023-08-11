
package com.lmkr.prmscemployeeapp.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import androidx.room.Database;
import androidx.room.TypeConverters;

import com.lmkr.prmscemployeeapp.data.database.dao.AttendanceHistoryDao;
import com.lmkr.prmscemployeeapp.data.database.dao.EmergencyContactDao;
import com.lmkr.prmscemployeeapp.data.database.dao.LeaveRequestDao;
import com.lmkr.prmscemployeeapp.data.database.models.AttendanceHistory;
import com.lmkr.prmscemployeeapp.data.database.models.LeaveRequest;
import com.lmkr.prmscemployeeapp.data.database.typeConverters.AttendanceHistoryConverter;
import com.lmkr.prmscemployeeapp.data.webservice.models.EmergencyContact;

@Database(entities = {AttendanceHistory.class, LeaveRequest.class, EmergencyContact.class}, version = 1)
@TypeConverters({AttendanceHistoryConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
    private static AppDatabase instance;


    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "prmsc_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    public static void deleteAllDatabase() {
    }

    public abstract AttendanceHistoryDao attendanceDao();
    public abstract LeaveRequestDao leaveRequestDao();

    public abstract EmergencyContactDao emergencyContactDao();

}

