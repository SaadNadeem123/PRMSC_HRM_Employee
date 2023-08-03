package com.lmkr.prmscemployeeapp.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.lmkr.prmscemployeeapp.data.database.models.AttendanceHistory;

import java.util.List;

@Dao
public interface AttendanceHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AttendanceHistory attendanceHistory);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<AttendanceHistory> attendanceHistory);

    @Update
    void update(AttendanceHistory attendanceHistory);

    @Delete
    void delete(AttendanceHistory attendanceHistory);

    @Query("Delete From attendancehistory")
    void deleteAllAttendanceHistory();

    @Query("Select * From AttendanceHistory")
    LiveData<List<AttendanceHistory>> getAttendanceHistory();

}
