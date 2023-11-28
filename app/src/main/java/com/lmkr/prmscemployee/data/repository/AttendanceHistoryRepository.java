package com.lmkr.prmscemployee.data.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.lmkr.prmscemployee.data.database.AppDatabase;
import com.lmkr.prmscemployee.data.database.dao.AttendanceHistoryDao;
import com.lmkr.prmscemployee.data.database.models.AttendanceHistory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AttendanceHistoryRepository {

    private final AttendanceHistoryDao attendanceHistoryDao;
    private final LiveData<List<AttendanceHistory>> attendanceHistory;

    public AttendanceHistoryRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        attendanceHistoryDao = database.attendanceDao();
        attendanceHistory = attendanceHistoryDao.getAttendanceHistory();
    }

    public LiveData<List<AttendanceHistory>> getAttendanceHistory() {
        return attendanceHistory;
    }

    public void insert(AttendanceHistory attendanceHistory) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            attendanceHistoryDao.insert(attendanceHistory);
            handler.post(() -> {
                //UI Thread work here
            });
        });

    }
    public void insert(List<AttendanceHistory> attendanceHistory) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            attendanceHistoryDao.insert(attendanceHistory);
            handler.post(() -> {
                //UI Thread work here
            });
        });

    }

    public void update(AttendanceHistory attendanceHistory) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            attendanceHistoryDao.update(attendanceHistory);
            handler.post(() -> {
                //UI Thread work here
            });
        });
    }

    public void delete(AttendanceHistory attendanceHistory) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            attendanceHistoryDao.delete(attendanceHistory);
            handler.post(() -> {
                //UI Thread work here
            });
        });
    }

    public void deleteAllAttendancesHistory() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            attendanceHistoryDao.deleteAllAttendanceHistory();
            handler.post(() -> {
                //UI Thread work here
            });
        });
    }
}
