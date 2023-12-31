package com.lmkr.prmscemployee.ui.leaverequest;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lmkr.prmscemployee.App;
import com.lmkr.prmscemployee.R;
import com.lmkr.prmscemployee.data.database.models.FileModel;
import com.lmkr.prmscemployee.data.database.models.LeaveRequest;
import com.lmkr.prmscemployee.data.webservice.api.ApiCalls;
import com.lmkr.prmscemployee.data.webservice.api.ApiManager;
import com.lmkr.prmscemployee.data.webservice.api.Urls;
import com.lmkr.prmscemployee.data.webservice.models.CreateLeaveRequestResponse;
import com.lmkr.prmscemployee.data.webservice.models.LeaveCount;
import com.lmkr.prmscemployee.data.webservice.models.LeaveRequestResponse;
import com.lmkr.prmscemployee.data.webservice.models.UserData;
import com.lmkr.prmscemployee.databinding.FragmentLeaveRequestBinding;
import com.lmkr.prmscemployee.ui.activities.LeaveManagementDetailActivity;
import com.lmkr.prmscemployee.ui.adapter.AttachmentsRecyclerAdapter;
import com.lmkr.prmscemployee.ui.adapter.LeaveRequestRecyclerAdapter;
import com.lmkr.prmscemployee.ui.adapter.LeaveTypeSpinnerAdapter;
import com.lmkr.prmscemployee.ui.adapter.LeavesRemainingRecyclerAdapter;
import com.lmkr.prmscemployee.ui.customViews.CustomDatePickerDialog;
import com.lmkr.prmscemployee.ui.customViews.CustomTimePicker;
import com.lmkr.prmscemployee.ui.utilities.AppUtils;
import com.lmkr.prmscemployee.ui.utilities.AppWideWariables;
import com.lmkr.prmscemployee.ui.utilities.FileUtils;
import com.lmkr.prmscemployee.ui.utilities.SharedPreferenceHelper;
import com.lmkr.prmscemployee.viewModel.LeaveRequestViewModel;
import com.lmkr.prmscemployee.viewModel.LeaveRequestViewModelFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LeaveRequestFragment extends Fragment {

    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_WRITE_PERMISSION = 0;
    private static final List<LeaveRequest> leaveRequests = new ArrayList<>();
    private final List<FileModel> files = new ArrayList<>();
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result) {
                // PERMISSION GRANTED
                showFileChooser();
            } else {
                // PERMISSION NOT GRANTED
            }
        }
    });
    private final List<LeaveCount> lc = new ArrayList<>();
    private boolean fromAttachment = false;
    private FragmentLeaveRequestBinding binding;
    private final Observer<? super List<LeaveRequest>> leaveRequestObserver = new Observer<List<LeaveRequest>>() {
        @Override
        public void onChanged(List<LeaveRequest> leaveRequests) {
            LeaveRequestFragment.leaveRequests.clear();
            LeaveRequestFragment.leaveRequests.addAll(leaveRequests);
            loadLeaveRequestData();
        }
    };
    private LeaveRequestViewModel leaveRequestViewModel;
    private LeaveCount leaveType = null;
    private final TextWatcher textChangeListenerFromDate = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            binding.dateTimeTo.setText("");
            binding.timeFrom.setText("");
            binding.timeFrom.setTag("");
            binding.timeTo.setText("");
            binding.timeTo.setTag("");
            binding.layoutTime.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(binding.dateTimeFrom.getText())) {
                new CustomDatePickerDialog(getActivity(), binding.dateTimeTo, binding.dateTimeFrom, leaveType.getRemaining(), CustomDatePickerDialog.END_DATE);
            } else {
                binding.dateTimeTo.setOnClickListener(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private final TextWatcher textChangeListenerToDate = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(binding.dateTimeFrom.getText()) && !TextUtils.isEmpty(binding.dateTimeTo.getText()) && binding.dateTimeFrom.getText().toString().equals(binding.dateTimeTo.getText().toString())) {
                binding.layoutTime.setVisibility(View.VISIBLE);
                binding.timeFrom.setText("");
                binding.timeTo.setText("");
                new CustomTimePicker(getActivity(), binding.timeFrom, binding.timeTo,binding.dateTimeTo, binding.dateTimeFrom, CustomTimePicker.START_TIME, leaveType.getRemaining());
            } else {
                binding.layoutTime.setVisibility(View.GONE);

                binding.timeFrom.setOnClickListener(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private final TextWatcher textChangeListenerFromTime = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            binding.timeTo.setText("");
            if (!TextUtils.isEmpty(binding.timeFrom.getText())) {
                new CustomTimePicker(getActivity(), binding.timeTo, binding.timeFrom,binding.dateTimeTo, binding.dateTimeFrom, CustomTimePicker.END_TIME, leaveType.getRemaining());
            } else {
                binding.timeTo.setOnClickListener(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private final AdapterView.OnItemSelectedListener spinnerLeaveTypeItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            leaveType = lc.get(position);

            binding.dateTimeFrom.setText("");
            binding.dateTimeTo.setText("");
            binding.timeFrom.setText("");
            binding.timeTo.setText("");
            binding.layoutTime.setVisibility(View.GONE);

            if (leaveType == null || leaveType.getType().equals(getString(R.string.select)) || leaveType.getId() == -1) {

                binding.dateTimeFrom.setOnClickListener(null);
                binding.dateTimeTo.setOnClickListener(null);
                binding.timeFrom.setOnClickListener(null);
                binding.timeTo.setOnClickListener(null);
                return;
            }
            new CustomDatePickerDialog(getActivity(), binding.dateTimeFrom, binding.dateTimeTo, leaveType.getRemaining(), CustomDatePickerDialog.START_DATE);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    private String fdate = "";
    private String tdate = "";
    private String ftime = "";
    private String ttime = "";
    private AttachmentsRecyclerAdapter adapter;

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri uri) {
            // Handle the returned Uri
            importFile(uri);
        }
    });

    private void loadLeaveRequestData() {
        binding.recyclerViewLeaveRequest.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        LeaveRequestRecyclerAdapter adapter = new LeaveRequestRecyclerAdapter(getActivity(), leaveRequests);
        adapter.setListener(new LeaveRequestRecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(LeaveRequest leaveRequest) {
//                if(leaveRequest.getStatus().toLowerCase().equals(AppWideWariables.REJECTED)) {
//                    AppUtils.makeNotification(leaveRequest.getApprover_reason(),getActivity());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(AppWideWariables.LEAVE_REQUEST_OBJECT_KEY , leaveRequest);
                    AppUtils.switchActivity(getContext() , LeaveManagementDetailActivity.class , bundle);
//                }
            }
        });
        binding.recyclerViewLeaveRequest.setAdapter(adapter);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        leaveRequestViewModel = new ViewModelProvider(this, new LeaveRequestViewModelFactory(App.getInstance(), "%" + AppUtils.getCurrentDate() + "%")).get(LeaveRequestViewModel.class);
        leaveRequestViewModel.getLeaveRequest().observe(getViewLifecycleOwner(), leaveRequestObserver);

        binding = FragmentLeaveRequestBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }


    private void callRequestTimeOffApi() {

        if (!AppUtils.checkNetworkState(getActivity())) {
            return;
        }

        if (leaveType == null || leaveType.getType().equals(getString(R.string.select)) || leaveType.getId() == -1) {
            AppUtils.makeNotification(getResources().getString(R.string.select_leave_type), getActivity());
            return;
        }

        if (TextUtils.isEmpty(binding.dateTimeFrom.getText())) {
            AppUtils.makeNotification(getResources().getString(R.string.provide_from_date), getActivity());
            return;
        }

        if (TextUtils.isEmpty(binding.dateTimeTo.getText())) {
            AppUtils.makeNotification(getResources().getString(R.string.provide_to_date), getActivity());
            return;
        }

        if (binding.dateTimeFrom.getText().toString().equals(binding.dateTimeTo.getText().toString())) {
            if (TextUtils.isEmpty(binding.timeFrom.getText())) {
                AppUtils.makeNotification(getResources().getString(R.string.provide_from_time), getActivity());
                return;
            }

            if (TextUtils.isEmpty(binding.timeTo.getText())) {
                AppUtils.makeNotification(getResources().getString(R.string.provide_to_time), getActivity());
                return;
            }
        }

        if (TextUtils.isEmpty(binding.note.getText())) {
            AppUtils.makeNotification(getResources().getString(R.string.provide_notes), getActivity());
            return;
        }

        if (binding.note.getText().length() < 10) {
            AppUtils.makeNotification(getResources().getString(R.string.minimum_length_notes), getActivity());
            return;
        }

        if (binding.note.getText().length() > 3000) {
            AppUtils.makeNotification(getResources().getString(R.string.maximum_length_notes), getActivity());
            return;
        }

        float days = AppUtils.getDaysBetweenDates(binding.dateTimeFrom.getText().toString(), AppUtils.FORMAT15, binding.timeFrom.getText().toString(), AppUtils.FORMAT5, binding.dateTimeTo.getText().toString(), AppUtils.FORMAT15, binding.timeTo.getText().toString(), AppUtils.FORMAT5, getActivity());
//        AppUtils.makeNotification("Days = "+days,getActivity());

        if (days >= leaveType.getRemaining()) {
            AppUtils.makeNotification(getString(R.string.requested_leaves_exceed_limit), getActivity());
            return;
        }


        FileModel fileModel = files != null && files.size() > 0 ? files.get(0) : null;
        RequestBody fpath = null;
        MultipartBody.Part body = null;

        if (fileModel != null) {
            File file = new File(fileModel.getPath());

            fpath = RequestBody.create(MediaType.parse(fileModel.getMimeType()), file);
            body = MultipartBody.Part.createFormData("file", file.getName(), fpath);
        }

        UserData user = SharedPreferenceHelper.getLoggedinUser(getActivity());

        RequestBody employee_id = RequestBody.create(MediaType.parse("text/plain"), user.getBasicData().get(0).getId() + "");
        RequestBody leave_type_id = RequestBody.create(MediaType.parse("text/plain"), leaveType.getId() + "");
        RequestBody from_date = RequestBody.create(MediaType.parse("text/plain"), AppUtils.getConvertedDateFromOneFormatToOther(binding.dateTimeFrom.getText().toString(), AppUtils.FORMAT15, AppUtils.FORMAT3));
        RequestBody to_date = RequestBody.create(MediaType.parse("text/plain"), AppUtils.getConvertedDateFromOneFormatToOther(binding.dateTimeTo.getText().toString(), AppUtils.FORMAT15, AppUtils.FORMAT3));
        RequestBody from_time = RequestBody.create(MediaType.parse("text/plain"), !TextUtils.isEmpty(binding.timeFrom.getText().toString()) ? AppUtils.getConvertedDateFromOneFormatToOther(binding.timeFrom.getText().toString(), AppUtils.FORMAT5, AppUtils.FORMAT18) : "00:00:00");
        RequestBody to_time = RequestBody.create(MediaType.parse("text/plain"), !TextUtils.isEmpty(binding.timeTo.getText().toString()) ? AppUtils.getConvertedDateFromOneFormatToOther(binding.timeTo.getText().toString(), AppUtils.FORMAT5, AppUtils.FORMAT18) : "00:00:00");
        RequestBody total_days = RequestBody.create(MediaType.parse("text/plain"), days + "");
        RequestBody reason = RequestBody.create(MediaType.parse("text/plain"), binding.note.getText().toString());
        RequestBody emergency_contact = RequestBody.create(MediaType.parse("text/plain"), "03001234567");
        RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), 0 + "");
        RequestBody lng = RequestBody.create(MediaType.parse("text/plain"), 0 + "");
        RequestBody source = RequestBody.create(MediaType.parse("text/plain"), AppWideWariables.SOURCE_MOBILE_ENUM);
        RequestBody first_approver = RequestBody.create(MediaType.parse("text/plain"), user.getBasicData().get(0).getManager_id() + "");
        RequestBody status = RequestBody.create(MediaType.parse("text/plain"), "1");


        ProgressDialog mProgressDialog = new ProgressDialog(getActivity(), R.style.CustomProgressDialog);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
//        mProgressDialog.setMessage("File upload in progress please wait...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();


        OkHttpClient httpClient = new OkHttpClient.Builder().retryOnConnectionFailure(true).connectTimeout(10, TimeUnit.MINUTES).readTimeout(10, TimeUnit.MINUTES).writeTimeout(10, TimeUnit.MINUTES)
//                .addInterceptor(new NetInterceptor())
/*                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        String token = user.getToken();
                        request = request.newBuilder()
                                .addHeader("Authorization", token)
                                .build();
                        return chain.proceed(request);
                    }
                })*/.build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiCalls.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(httpClient).build();

        Urls jsonPlaceHolderApi = retrofit.create(Urls.class);

        Call<CreateLeaveRequestResponse> call = jsonPlaceHolderApi.leaveRequestMultipart(AppUtils.getStandardHeaders(user), body, employee_id, leave_type_id, from_date, to_date, from_time, to_time, total_days, reason, emergency_contact, lat, lng, source, first_approver, status);

        call.enqueue(new Callback<CreateLeaveRequestResponse>() {
            @Override
            public void onResponse(Call<CreateLeaveRequestResponse> call, Response<CreateLeaveRequestResponse> response) {
                Log.i("response", response.toString());
                if (mProgressDialog.isShowing()) mProgressDialog.dismiss();

                if (!AppUtils.isErrorResponse(AppWideWariables.API_METHOD_POST, response, getActivity())) {

                    if (!response.isSuccessful()) {
                        //tv.setText("Code :" + response.code());
                        return;
                    }

                    /*
                    JsonObjectResponse jsonObjectResponse = response.body();

                    Log.i("response", jsonObjectResponse.toString());

                    if (response.body().isStatus()) {
                        JsonObject jsonObject = (JsonObject) response.body().getResponse();
                    }
                    */

                    resetViews();
                    getLeaveRequest();
                }
            }

            @Override
            public void onFailure(Call<CreateLeaveRequestResponse> call, Throwable t) {
                t.printStackTrace();
                if (mProgressDialog.isShowing()) mProgressDialog.dismiss();
                Log.i("response", t.toString());
//                tv.setText(t.getMessage());
//                AppUtils.makeNotification(t.getMessage(), getActivity());
                AppUtils.ApiError(t, getActivity());
            }

        });
    }

    private void resetViews() {
        binding.spinnerLeaveTypes.setSelection(0);
        binding.dateTimeFrom.setText("");
        binding.timeFrom.setText("");
        binding.dateTimeTo.setText("");
        binding.timeTo.setText("");
        binding.note.setText("");
        deleteAttachment();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new AttachmentsRecyclerAdapter(files, LeaveRequestFragment.this, fromAttachment);
        binding.rvAttachment.setAdapter(adapter);
        binding.rvAttachment.setLayoutManager(new LinearLayoutManager(getActivity()));

        resetViews();
        refreshApiCalls();
        setListeners();
    }

    private void refreshLeaveCountView() {

        try {
            if (binding == null) {
                return;
            }
            
            UserData userData = SharedPreferenceHelper.getLoggedinUser(getActivity());

            if (binding.recyclerviewLeaveProgress != null) {
                binding.recyclerviewLeaveProgress.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                LeavesRemainingRecyclerAdapter adapter = new LeavesRemainingRecyclerAdapter(getActivity(), userData.getLeaveCount());
               
                binding.recyclerviewLeaveProgress.setAdapter(adapter);
            }
            lc.clear();
            lc.add(new LeaveCount(-1, getString(R.string.select), 0, 0));

            for (LeaveCount leaveCount : userData.getLeaveCount()) {
                if (leaveCount.getRemaining() > 0) {
                    lc.add(leaveCount);
                }
            }

            removeSpinnerListeners();

            LeaveCount selected = leaveType;

            if (binding.dateTimeFrom != null) {
                fdate = binding.dateTimeFrom.getText().toString();
            }
            if (binding.dateTimeTo != null) {
                tdate = binding.dateTimeTo.getText().toString();
            }
            if (binding.timeFrom != null) {
                ftime = binding.timeFrom.getText().toString();
            }
            if (binding.timeTo != null) {
                ttime = binding.timeTo.getText().toString();
            }

            if (binding.spinnerLeaveTypes != null) {
                binding.spinnerLeaveTypes.setAdapter(new LeaveTypeSpinnerAdapter(lc, getActivity()));

                int i = 0;
                if (selected != null) {
                    for (LeaveCount leaveCount : lc) {
                        if (leaveCount.getId() == selected.getId()) {
                            break;
                        }
                        i++;
                    }
                }

                binding.spinnerLeaveTypes.setSelection(i);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

/*
                binding.dateTimeFrom.setText(fdate==null?"":fdate);
                binding.dateTimeTo.setText(tdate==null?"":tdate);
                binding.timeFrom.setText(ftime==null?"":ftime);
                binding.timeTo.setText(ttime==null?"":ttime);
*/
                        setSpinnerListener();
                    }
                }, 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSpinnerListener() {
        try {
            if (binding.spinnerLeaveTypes != null) {
                binding.spinnerLeaveTypes.setOnItemSelectedListener(spinnerLeaveTypeItemSelectedListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeSpinnerListeners() {
        try {
            if (binding.spinnerLeaveTypes != null) {
                binding.spinnerLeaveTypes.setOnItemSelectedListener(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setListeners() {

        binding.dateTimeFrom.addTextChangedListener(textChangeListenerFromDate);
        binding.dateTimeTo.addTextChangedListener(textChangeListenerToDate);
        binding.timeFrom.addTextChangedListener(textChangeListenerFromTime);
        setSpinnerListener();

        binding.requestTimeOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRequestTimeOffApi();
            }
        });

        binding.attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.attachment.setEnabled(false);
                requestPermission();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.attachment.setEnabled(true);
                    }
                }, 2000);
            }
        });

        binding.note.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.noteCount.setText("(" + binding.note.getText().length() + ")");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showFileChooser();
        }
    }

    /* private void requestPermission() {

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             //            if(ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {
             //                requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
             //            }else {
             //                showFileChooser();
             //            }
             requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
         } else {
             showFileChooser();
         }

     }
 */
    private void requestPermission() {

        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            showFileChooser();
        } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected, and what
            // features are disabled if it's declined. In this UI, include a
            // "cancel" or "no thanks" button that lets the user continue
            // using your app without granting the permission.
//            showInContextUI(...)
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
//            requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                showFileChooser();
            } else {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
            }
        }

    }

    public void showFileChooser() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        // Update with mime types
        intent.setType("*/*");

        // Update with additional mime types here using a String[].
        // intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        // Only pick openable and local files. Theoretically we could pull files from google drive
        // or other applications that have networked files, but that's unnecessary for this example.
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

        // REQUEST_CODE = <some-integer>
        // mGetContent.launch("*/*");
        // mGetContent.launch("image/*");

        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If the user doesn't pick a file just return
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_CODE || resultCode != RESULT_OK || data == null || data.getData() == null) {
            return;
        }

        // Import the file
        importFile(data.getData());

    }


    public void importFile(Uri returnUri) {

        try {
            binding.attachment.setVisibility(View.GONE);

            //        String fileName = getFileName(returnUri);

            /*
             * Get the file's content URI from the incoming Intent,
             * then query the server app to get the file's display name
             * and size.
             */

            Cursor returnCursor = getActivity().getContentResolver().query(returnUri, null, null, null, null);
            /*
             * Get the column indexes of the data in the Cursor,
             * move to the first row in the Cursor, get the data,
             * and display it.
             */

            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);

            returnCursor.moveToFirst();
            String fileName = returnCursor.getString(nameIndex);
            String mimeType = getActivity().getContentResolver().getType(returnUri);
            String size = Long.toString(returnCursor.getLong(sizeIndex));

            FileModel file = new FileModel();
            file.setPath(FileUtils.getPath(getActivity(), returnUri));
            file.setUri(returnUri);
            file.setFileName(fileName);
            file.setMimeType(mimeType);
            file.setSize(size);
            file.setType(null);

            if (!AppUtils.sizeLessThan2MB(new File(file.getPath()))) {
                binding.attachment.setVisibility(View.VISIBLE);
                AppUtils.makeNotification(getResources().getString(R.string.size_greater_than_2mb), getActivity());
                return;
            }

            fromAttachment = true;
            adapter.setFromAttachment(fromAttachment);
            adapter.removeItems(files);
            updateFileAdapter(file);

//        AppUtils.makeNotification(AppUtils.getFileSizeInMB(new File(file.getPath())),getActivity());
//        AppUtils.makeNotification(AppUtils.getFileSize(new File(file.getPath())),getActivity());
        } catch (Exception e) {
            e.printStackTrace();
            binding.attachment.setVisibility(View.VISIBLE);
        }

    }


    public void deleteAttachment() {
        if (files != null && files.size() > 0) {
            adapter.removeItems(files);
            binding.attachment.setVisibility(View.VISIBLE);
            fromAttachment = false;
            adapter.setFromAttachment(fromAttachment);
        }
        files.clear();
    }
    /*
    public void viewImage(FileModel fileModel) {
        findViewById(R.id.fl_image).setVisibility(View.VISIBLE);
        AppUtils.loadWithGlide(BookingDetailsActivity.this, ApiCalls.BASE_DOCUMENT_URL + fileModel.getPath(), findViewById(R.id.image));
    }
    */

    private void updateFileAdapter(FileModel fileModel) {
        adapter.addItem(fileModel);
    }


    private void getLeaveRequest() {
        if (!AppUtils.checkNetworkState(getActivity())) {
            return;
        }

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiCalls.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        Urls urls = retrofit.create(Urls.class);
        Call<LeaveRequestResponse> call = urls.getLeaveRequest(AppUtils.getStandardHeaders(SharedPreferenceHelper.getLoggedinUser(getActivity())));
        call.enqueue(new Callback<LeaveRequestResponse>() {
            @Override
            public void onResponse(Call<LeaveRequestResponse> call, Response<LeaveRequestResponse> response) {
                Log.i("response", response.toString());

                if (!AppUtils.isErrorResponse(AppWideWariables.API_METHOD_GET, response, getActivity())) {
                    if (!response.isSuccessful()) {
//                    tv.setText("Code :" + response.code());
                    }

                    if (response.body() != null && response.body().getLeaveRequest() != null) {
                        leaveRequestViewModel.insert(response.body().getLeaveRequest());
                    }
                }
            }

            @Override
            public void onFailure(Call<LeaveRequestResponse> call, Throwable t) {
                t.printStackTrace();
                AppUtils.ApiError(t, getActivity());
//                AppUtils.makeNotification(t.toString(), getActivity());
                Log.i("response", t.toString());
//                tv.setText(t.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void refreshApiCalls() {
        ApiManager.getInstance().getToken();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getLeaveRequest();
                refreshLeaveCountView();
            }
        }, 1000);
    }

}