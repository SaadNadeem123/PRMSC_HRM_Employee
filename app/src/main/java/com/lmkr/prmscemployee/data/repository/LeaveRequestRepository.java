package com.lmkr.prmscemployee.data.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.lmkr.prmscemployee.data.database.AppDatabase;
import com.lmkr.prmscemployee.data.database.dao.LeaveRequestDao;
import com.lmkr.prmscemployee.data.database.models.LeaveRequest;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LeaveRequestRepository {

    private final LeaveRequestDao leaveRequestDao;
    private final LiveData<List<LeaveRequest>> leaveRequests;

    public LeaveRequestRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        leaveRequestDao = database.leaveRequestDao();
        leaveRequests = leaveRequestDao.getLeaveRequests();
    }

    public LiveData<List<LeaveRequest>> getLeaveRequests() {
        return leaveRequests;
    }

    public void insert(LeaveRequest leaveRequest) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            leaveRequestDao.insert(leaveRequest);
            handler.post(() -> {
                //UI Thread work here
            });
        });

    }
    public void insert(List<LeaveRequest> leaveRequest) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            leaveRequestDao.insert(leaveRequest);
            handler.post(() -> {
                //UI Thread work here
            });
        });

    }

    public void update(LeaveRequest leaveRequest) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            leaveRequestDao.update(leaveRequest);
            handler.post(() -> {
                //UI Thread work here
            });
        });
    }

    public void delete(LeaveRequest leaveRequest) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            leaveRequestDao.delete(leaveRequest);
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
            leaveRequestDao.deleteAllLeaveRequest();
            handler.post(() -> {
                //UI Thread work here
            });
        });
    }
}
