package com.lmkr.prmscemployee.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.lmkr.prmscemployee.data.database.models.LeaveRequest;

import java.util.List;

@Dao
public interface LeaveRequestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LeaveRequest leaveRequest);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<LeaveRequest> leaveRequest);

    @Update
    void update(LeaveRequest leaveRequest);

    @Delete
    void delete(LeaveRequest leaveRequest);

    @Query("Delete From leaverequest")
    void deleteAllLeaveRequest();

    @Query("Select * From leaverequest Order by from_date Desc")
    LiveData<List<LeaveRequest>> getLeaveRequests();

}
