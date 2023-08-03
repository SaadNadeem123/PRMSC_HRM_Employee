package com.lmkr.prmscemployeeapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lmkr.prmscemployeeapp.data.database.models.AttendanceHistory;
import com.lmkr.prmscemployeeapp.data.repository.AttendanceHistoryRepository;

import java.util.List;

public class AttendanceHistoryViewModel extends AndroidViewModel {
    private final AttendanceHistoryRepository attendanceHistoryRepository;
    private final LiveData<List<AttendanceHistory>> attendance;

    public AttendanceHistoryViewModel(@NonNull Application application) {
        super(application);
        attendanceHistoryRepository = new AttendanceHistoryRepository(application);
        attendance = attendanceHistoryRepository.getAttendanceHistory();
    }

    public void insert(AttendanceHistory attendanceHistory) {
        attendanceHistoryRepository.insert(attendanceHistory);
    }

    public void insert(List<AttendanceHistory> attendanceHistory) {
        attendanceHistoryRepository.insert(attendanceHistory);
    }

    public void update(AttendanceHistory attendanceHistory) {
        attendanceHistoryRepository.update(attendanceHistory);
    }

    public void delete(AttendanceHistory attendanceHistory) {
        attendanceHistoryRepository.delete(attendanceHistory);
    }

    public void deleteAllAttendanceHistory() {
        attendanceHistoryRepository.deleteAllAttendancesHistory();
    }

    public LiveData<List<AttendanceHistory>> getAttendanceHistory() {
        return attendance;
    }

}