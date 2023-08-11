package com.lmkr.prmscemployeeapp.ui.myinfo.addContact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.lmkr.prmscemployeeapp.data.webservice.models.AddContactModel;
import com.lmkr.prmscemployeeapp.databinding.ActivityAddContactBinding;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;
import com.lmkr.prmscemployeeapp.ui.utilities.SharedPreferenceHelper;
import com.lmkr.prmscemployeeapp.viewModel.ContactViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddContactActivity extends AppCompatActivity {

    private ActivityAddContactBinding binding;
    private int dropDownValue;
    private ContactViewModel contactViewModel;

    private String token, name, email, mobileNum, homeNum, workNum, personalAddress, relationship;
    private int employeeId, getEmployeeId,staticId;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityAddContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);

        token = AppUtils.getStandardHeaders(SharedPreferenceHelper.getLoggedinUser(this));
        employeeId = SharedPreferenceHelper.getLoggedinUser(this).getBasicData().get(0).getId();


        Bundle receivedBundle = getIntent().getExtras();

        if (receivedBundle != null) {
            name = receivedBundle.getString("Name");
            email = receivedBundle.getString("Email");
            mobileNum = receivedBundle.getString("Mobile");
            homeNum = receivedBundle.getString("Home");
            workNum = receivedBundle.getString("Work");
            personalAddress = receivedBundle.getString("Address");
            relationship = receivedBundle.getString("Relation");
            getEmployeeId = receivedBundle.getInt("Id");
            staticId = receivedBundle.getInt("staticId");
        }

        binding.submitButton.setVisibility(View.VISIBLE);
        binding.updateButton.setVisibility(View.GONE);


        if(staticId == 100){

            binding.submitButton.setVisibility(View.GONE);
            binding.updateButton.setVisibility(View.VISIBLE);

            binding.username.setText(name);
            binding.email.setText(email);
            binding.homePhone.setText(homeNum);
            binding.workPhone.setText(workNum);
            binding.mobilePhone.setText(mobileNum);
            binding.address.setText(personalAddress);
            binding.titleTv.setText("Update Contact");


        }


        List<String> relationList =  new ArrayList<>();
        relationList.add("Mother");
        relationList.add("Father");
        relationList.add("Brother");
        relationList.add("Sister");
        relationList.add("Spouse");
        relationList.add("Daughter");
        relationList.add("Son");
        relationList.add("Friend");

        binding.designSpinner.setSelection(relationList.indexOf("Mother"));


        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,relationList);
        binding.designSpinner.setAdapter(adapter);
        binding.designSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedRelationship = relationList.get(position);
                dropDownValue = Relationship.getIntegerValue(selectedRelationship);

            }
        });


        binding.submitButton.setOnClickListener(v ->{

            if (Objects.requireNonNull(binding.username.getText()).toString().isEmpty()) {
                AppUtils.makeNotification("Enter name !", AddContactActivity.this);
                return;
            }if (Objects.requireNonNull(binding.email.getText()).toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(binding.email.getText().toString()).matches()) {
                AppUtils.makeNotification("Enter valid Email address !", AddContactActivity.this);
                return;
            }

            if(dropDownValue == 0){

                AppUtils.makeNotification("Please select your Relation !", AddContactActivity.this);
                return;
            }

            if (Objects.requireNonNull(binding.mobilePhone.getText()).toString().isEmpty() || binding.mobilePhone.getText().toString().length() < 7) {
                AppUtils.makeNotification("Enter valid mobile number !", AddContactActivity.this);
                return;
            }

            if (Objects.requireNonNull(binding.workPhone.getText()).toString().isEmpty() || binding.workPhone.getText().toString().length() < 7) {
                AppUtils.makeNotification("Enter valid work number !", AddContactActivity.this);
                return;
            }

            if (Objects.requireNonNull(binding.homePhone.getText()).toString().isEmpty() || binding.homePhone.getText().toString().length() < 7) {
                AppUtils.makeNotification("Enter valid home number !", AddContactActivity.this);
                return;
            }

            if (Objects.requireNonNull(binding.username.getText()).toString().isEmpty()) {
                AppUtils.makeNotification("Enter address !", AddContactActivity.this);
                return;
            }


            createApi();


        });


        binding.updateButton.setOnClickListener(v -> {
            if (Objects.requireNonNull(binding.username.getText()).toString().isEmpty()) {
                AppUtils.makeNotification("Enter name !", AddContactActivity.this);
                return;
            }if (Objects.requireNonNull(binding.email.getText()).toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(binding.email.getText().toString()).matches()) {
                AppUtils.makeNotification("Enter valid Email address !", AddContactActivity.this);
                return;
            }

            if(dropDownValue == 0){

                AppUtils.makeNotification("Please select your Relation !", AddContactActivity.this);
                return;
            }

            if (Objects.requireNonNull(binding.mobilePhone.getText()).toString().isEmpty() || binding.mobilePhone.getText().toString().length() < 7) {
                AppUtils.makeNotification("Enter valid mobile number !", AddContactActivity.this);
                return;
            }

            if (Objects.requireNonNull(binding.workPhone.getText()).toString().isEmpty() || binding.workPhone.getText().toString().length() < 7) {
                AppUtils.makeNotification("Enter valid work number !", AddContactActivity.this);
                return;
            }

            if (Objects.requireNonNull(binding.homePhone.getText()).toString().isEmpty() || binding.homePhone.getText().toString().length() < 7) {
                AppUtils.makeNotification("Enter valid home number !", AddContactActivity.this);
                return;
            }

            if (Objects.requireNonNull(binding.username.getText()).toString().isEmpty()) {
                AppUtils.makeNotification("Enter address !", AddContactActivity.this);
                return;
            }


            updateApi();

        });

    }

    private void updateApi() {

        binding.progressBar.setVisibility(View.VISIBLE);

        AddContactModel updatedContact = new AddContactModel();
        updatedContact.setEmployeeId(getEmployeeId);
        updatedContact.setName(Objects.requireNonNull(binding.username.getText()).toString());
        updatedContact.setEmail(Objects.requireNonNull(binding.email.getText()).toString());
        updatedContact.setRelation(String.valueOf(dropDownValue));
        updatedContact.setMobile(Objects.requireNonNull(binding.mobilePhone.getText()).toString());
        updatedContact.setWorkNumber(Objects.requireNonNull(binding.workPhone.getText()).toString());
        updatedContact.setHomeNumber(Objects.requireNonNull(binding.homePhone.getText()).toString());
        updatedContact.setAddress(Objects.requireNonNull(binding.address.getText()).toString());

        contactViewModel.updateEmergencyContact(
                token,
                getEmployeeId,
                updatedContact,
                new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        binding.progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            Toast.makeText(AddContactActivity.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddContactActivity.this, "Updation Error!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        binding.progressBar.setVisibility(View.GONE);
                        AppUtils.makeNotification(t.toString(), AddContactActivity.this);
                        Log.d("ErrorMsg", t.toString());
                    }
                }
        );

    }

    private void createApi() {

        binding.progressBar.setVisibility(View.VISIBLE);

        AddContactModel newContact = new AddContactModel();
        newContact.setEmployeeId(employeeId);
        newContact.setName(Objects.requireNonNull(binding.username.getText()).toString());
        newContact.setEmail(Objects.requireNonNull(binding.email.getText()).toString());
        newContact.setRelation(String.valueOf(dropDownValue));
        newContact.setMobile(Objects.requireNonNull(binding.mobilePhone.getText()).toString());
        newContact.setWorkNumber(Objects.requireNonNull(binding.workPhone.getText()).toString());
        newContact.setHomeNumber(Objects.requireNonNull(binding.homePhone.getText()).toString());
        newContact.setAddress(Objects.requireNonNull(binding.address.getText()).toString());



        contactViewModel.createEmergencyContact(
                token,
                employeeId,
                newContact,
                new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {

                        binding.progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            Toast.makeText(AddContactActivity.this, "Insertion Successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddContactActivity.this, "Insertion Error!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

                        binding.progressBar.setVisibility(View.GONE);

                        AppUtils.makeNotification(t.toString(), AddContactActivity.this);
                        Log.d("ErrorMsg", t.toString());

                    }
                }
        );

    }

}