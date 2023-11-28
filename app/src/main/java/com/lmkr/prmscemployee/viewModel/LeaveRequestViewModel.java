package com.lmkr.prmscemployee.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lmkr.prmscemployee.data.database.models.LeaveRequest;
import com.lmkr.prmscemployee.data.repository.LeaveRequestRepository;

import java.util.List;

public class LeaveRequestViewModel extends AndroidViewModel {
    private final LeaveRequestRepository leaveRequestRepository;
    private final LiveData<List<LeaveRequest>> leaveRequests;

    public LeaveRequestViewModel(@NonNull Application application) {
        super(application);
        leaveRequestRepository = new LeaveRequestRepository(application);
        leaveRequests = leaveRequestRepository.getLeaveRequests();
    }

    public void insert(LeaveRequest leaveRequest) {
        leaveRequestRepository.insert(leaveRequest);
    }

    public void insert(List<LeaveRequest> leaveRequest) {
        leaveRequestRepository.insert(leaveRequest);
    }

    public void update(LeaveRequest leaveRequest) {
        leaveRequestRepository.update(leaveRequest);
    }

    public void delete(LeaveRequest leaveRequest) {
        leaveRequestRepository.delete(leaveRequest);
    }

    public void deleteAllLeaveRequest() {
        leaveRequestRepository.deleteAllAttendancesHistory();
    }

    public LiveData<List<LeaveRequest>> getLeaveRequest() {
        return leaveRequests;
    }

}