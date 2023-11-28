package com.lmkr.prmscemployee.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.lmkr.prmscemployee.data.webservice.models.EmergencyContact;

import java.util.List;

@Dao
public interface EmergencyContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(EmergencyContact emergencyContacts);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<EmergencyContact> emergencyContacts);

    @Update
    void update(EmergencyContact emergencyContacts);

    @Delete
    void delete(EmergencyContact emergencyContacts);

    @Query("Delete From EmergencyContact")
    void deleteAllEmergencyContacts();

    @Query("SELECT * FROM EmergencyContact order by name asc")
    LiveData<List<EmergencyContact>> getAllEmergencyContacts();
}
