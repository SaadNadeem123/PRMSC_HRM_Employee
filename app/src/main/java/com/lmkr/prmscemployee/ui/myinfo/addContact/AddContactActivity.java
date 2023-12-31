package com.lmkr.prmscemployee.ui.myinfo.addContact;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonObject;
import com.lmkr.prmscemployee.data.webservice.models.AddContactModel;
import com.lmkr.prmscemployee.data.webservice.models.ApiBaseResponse;
import com.lmkr.prmscemployee.databinding.ActivityAddContactBinding;
import com.lmkr.prmscemployee.ui.utilities.AppUtils;
import com.lmkr.prmscemployee.ui.utilities.AppWideWariables;
import com.lmkr.prmscemployee.ui.utilities.SharedPreferenceHelper;
import com.lmkr.prmscemployee.viewModel.ContactViewModel;
import com.lmkr.prmscemployee.viewModel.EmergencyContactViewModel;

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
    private int employeeId, getEmployeeId, staticId;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddContactBinding.inflate(getLayoutInflater());
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

            Log.d("BundleValue:" , receivedBundle.toString());

        }

        binding.submitButton.setVisibility(View.VISIBLE);
        binding.updateButton.setVisibility(View.GONE);


        if (staticId == 100) {

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


        List<String> relationList = new ArrayList<>();
        relationList.add("Mother");
        relationList.add("Father");
        relationList.add("Brother");
        relationList.add("Sister");
        relationList.add("Spouse");
        relationList.add("Daughter");
        relationList.add("Son");
        relationList.add("Friend");

        binding.designSpinner.setSelection(relationList.indexOf("Mother"));


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, relationList);
        binding.designSpinner.setAdapter(adapter);
        binding.designSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedRelationship = relationList.get(position);
                dropDownValue = Relationship.getIntegerValue(selectedRelationship);

            }
        });



        setListeners();

    }

    private void setListeners() {

        binding.mobilePhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString();

                if (!input.startsWith("03")) {
                    binding.mobilePhone.setText("03");
                    binding.mobilePhone.setSelection(binding.mobilePhone.getText().length());
                } else if (input.length() > 11) {
                    binding.mobilePhone.setText(input.substring(0, 11));
                    binding.mobilePhone.setSelection(11);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.workPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString();

                if (!input.startsWith("03")) {
                    binding.workPhone.setText("03");
                    binding.workPhone.setSelection(binding.workPhone.getText().length());
                } else if (input.length() > 11) {
                    binding.workPhone.setText(input.substring(0, 11));
                    binding.workPhone.setSelection(11);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.homePhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString();

                if (!input.startsWith("03")) {
                    binding.homePhone.setText("03");
                    binding.homePhone.setSelection(binding.homePhone.getText().length());
                }else if (input.length() > 11) {
                    binding.homePhone.setText(input.substring(0, 11));
                    binding.homePhone.setSelection(11);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.submitButton.setOnClickListener(v -> {

            if (Objects.requireNonNull(binding.username.getText()).toString().isEmpty()) {
                AppUtils.makeNotification("Enter name !", AddContactActivity.this);
                return;
            }
            if (Objects.requireNonNull(binding.email.getText()).toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(binding.email.getText().toString()).matches()) {
                AppUtils.makeNotification("Enter valid Email address !", AddContactActivity.this);
                return;
            }

            if (dropDownValue == 0) {

                AppUtils.makeNotification("Please select your Relation !", AddContactActivity.this);
                return;
            }

            if (Objects.requireNonNull(binding.mobilePhone.getText()).toString().isEmpty() || binding.mobilePhone.getText().toString().length() < 11) {
                AppUtils.makeNotification("Enter valid mobile number !", AddContactActivity.this);
                return;
            }

            if (Objects.requireNonNull(binding.workPhone.getText()).toString().isEmpty() || binding.workPhone.getText().toString().length() < 11) {
                AppUtils.makeNotification("Enter valid work number !", AddContactActivity.this);
                return;
            }

            if (Objects.requireNonNull(binding.homePhone.getText()).toString().isEmpty() || binding.homePhone.getText().toString().length() < 11) {
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
            }
            if (Objects.requireNonNull(binding.email.getText()).toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(binding.email.getText().toString()).matches()) {
                AppUtils.makeNotification("Enter valid Email address !", AddContactActivity.this);
                return;
            }

            if (dropDownValue == 0) {

                AppUtils.makeNotification("Please select your Relation !", AddContactActivity.this);
                return;
            }

            if (Objects.requireNonNull(binding.mobilePhone.getText()).toString().isEmpty() || binding.mobilePhone.getText().toString().length() < 11) {
                AppUtils.makeNotification("Enter valid mobile number !", AddContactActivity.this);
                return;
            }

            if (Objects.requireNonNull(binding.workPhone.getText()).toString().isEmpty() || binding.workPhone.getText().toString().length() < 11) {
                AppUtils.makeNotification("Enter valid work number !", AddContactActivity.this);
                return;
            }

            if (Objects.requireNonNull(binding.homePhone.getText()).toString().isEmpty() || binding.homePhone.getText().toString().length() < 11) {
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
        updatedContact.setEmployeeId(employeeId);
        updatedContact.setName(Objects.requireNonNull(binding.username.getText()).toString());
        updatedContact.setEmail(Objects.requireNonNull(binding.email.getText()).toString());
        updatedContact.setRelation(String.valueOf(dropDownValue));
        updatedContact.setMobile(Objects.requireNonNull(binding.mobilePhone.getText()).toString());
        updatedContact.setWorkNumber(Objects.requireNonNull(binding.workPhone.getText()).toString());
        updatedContact.setHomeNumber(Objects.requireNonNull(binding.homePhone.getText()).toString());
        updatedContact.setAddress(Objects.requireNonNull(binding.address.getText()).toString());

        JsonObject body = new JsonObject();


                body.addProperty("employee_id",employeeId);                                                                     //: "1",
                body.addProperty("name",Objects.requireNonNull(binding.username.getText()).toString());                        //: "TEST",
                body.addProperty("email",Objects.requireNonNull(binding.email.getText()).toString());                       //: "test@lmkt.com",
                body.addProperty("relation",String.valueOf(dropDownValue));                                                  //:"5", //1 for Mother, 1 for Father etc ... ENUM('Mother', 'Father', 'Brother', 'Sister', 'Spouse', 'Daughter', 'Son', 'Friend')
                body.addProperty("mobile",Objects.requireNonNull(binding.mobilePhone.getText()).toString());                      //: "03365759000",
                body.addProperty("work_number",Objects.requireNonNull(binding.workPhone.getText()).toString());                 //: "05441122334",
                body.addProperty("home_number",Objects.requireNonNull(binding.homePhone.getText()).toString());                 //: "0511212009",
                body.addProperty("address",Objects.requireNonNull(binding.address.getText()).toString());                     //: "TEST"



        contactViewModel.updateEmergencyContact(
                token,
                getEmployeeId,
                body,
                new Callback<ApiBaseResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiBaseResponse> call, @NonNull Response<ApiBaseResponse> response) {
                        binding.progressBar.setVisibility(View.GONE);
                        if (!AppUtils.isErrorResponse(AppWideWariables.API_METHOD_POST, response, AddContactActivity.this)) {
                            if (!response.isSuccessful()) {
                                return;
                            }
                            new EmergencyContactViewModel(getApplication()).loadEmergencyContacts(token,employeeId);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 2000);

//                            new EmergencyContactViewModel(getApplication()).loadEmergencyContacts(token,employeeId);
                        }


                        if (response.isSuccessful()) {
                            Toast.makeText(AddContactActivity.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(AddContactActivity.this, "Updation Error!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiBaseResponse> call, @NonNull Throwable t) {
                        binding.progressBar.setVisibility(View.GONE);
//                        AppUtils.makeNotification(t.toString(), AddContactActivity.this);
                        AppUtils.ApiError(t,AddContactActivity.this);
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


        contactViewModel.createEmergencyContact(token, newContact, new Callback<ApiBaseResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiBaseResponse> call, @NonNull Response<ApiBaseResponse> response) {

                binding.progressBar.setVisibility(View.GONE);
                if (!AppUtils.isErrorResponse(AppWideWariables.API_METHOD_POST, response, AddContactActivity.this)) {
                    if (!response.isSuccessful()) {
                        return;
                    }

                    new EmergencyContactViewModel(getApplication()).loadEmergencyContacts(token,employeeId);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 2000);
                }
/*

*/
            }

            @Override
            public void onFailure(@NonNull Call<ApiBaseResponse> call, @NonNull Throwable t) {

                binding.progressBar.setVisibility(View.GONE);

//                AppUtils.makeNotification(t.toString(), AddContactActivity.this);
                Log.d("ErrorMsg", t.toString());

                AppUtils.ApiError(t,AddContactActivity.this);
            }
        });


    }

}