package com.lmkr.prmscemployeeapp.data.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.lmkr.prmscemployeeapp.data.database.AppDatabase;
import com.lmkr.prmscemployeeapp.data.database.dao.EmergencyContactDao;
import com.lmkr.prmscemployeeapp.data.webservice.models.EmergencyContact;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmergencyContactRepository {

    private EmergencyContactDao emergencyContactDao;
    private final LiveData<List<EmergencyContact>> emergencyContact;

    public EmergencyContactRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        emergencyContactDao = database.emergencyContactDao();
        emergencyContact = emergencyContactDao.getAllEmergencyContacts();
    }

    public LiveData<List<EmergencyContact>> getAllEmergencyContacts() {
        return emergencyContact;
    }

    public void insert(EmergencyContact emergencyContact) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            emergencyContactDao.insert(emergencyContact);
            handler.post(() -> {
                //UI Thread work here
            });
        });

    }
    public void insert(List<EmergencyContact> emergencyContactList) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            emergencyContactDao.insert(emergencyContactList);
            handler.post(() -> {
                //UI Thread work here
            });
        });

    }

    public void update(EmergencyContact emergencyContact) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            emergencyContactDao.update(emergencyContact);
            handler.post(() -> {
                //UI Thread work here
            });
        });
    }

    public void delete(EmergencyContact emergencyContact) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            emergencyContactDao.delete(emergencyContact);
            handler.post(() -> {
                //UI Thread work here
            });
        });
    }

    public void deleteAllEmergencyContacts() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            emergencyContactDao.deleteAllEmergencyContacts();
            handler.post(() -> {
                //UI Thread work here
            });
        });
    }
}
