package com.lmkr.prmscemployeeapp.ui.leaverequest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lmkr.prmscemployeeapp.App;
import com.lmkr.prmscemployeeapp.data.database.models.AttendanceHistory;
import com.lmkr.prmscemployeeapp.data.database.models.LeaveRequest;
import com.lmkr.prmscemployeeapp.data.webservice.api.ApiCalls;
import com.lmkr.prmscemployeeapp.data.webservice.api.Urls;
import com.lmkr.prmscemployeeapp.data.webservice.models.AttendanceHistoryResponse;
import com.lmkr.prmscemployeeapp.data.webservice.models.LeaveRequestResponse;
import com.lmkr.prmscemployeeapp.data.webservice.models.UserData;
import com.lmkr.prmscemployeeapp.databinding.FragmentLeaveRequestBinding;
import com.lmkr.prmscemployeeapp.ui.adapter.AttendanceHistoryRecyclerAdapter;
import com.lmkr.prmscemployeeapp.ui.adapter.LeaveRequestRecyclerAdapter;
import com.lmkr.prmscemployeeapp.ui.adapter.LeaveTypeSpinnerAdapter;
import com.lmkr.prmscemployeeapp.ui.adapter.LeavesRemainingRecyclerAdapter;
import com.lmkr.prmscemployeeapp.ui.customViews.CustomDatePicker;
import com.lmkr.prmscemployeeapp.ui.home.HomeFragment;
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils;
import com.lmkr.prmscemployeeapp.ui.utilities.SharedPreferenceHelper;
import com.lmkr.prmscemployeeapp.viewModel.AttendanceHistoryViewModel;
import com.lmkr.prmscemployeeapp.viewModel.AttendanceHistoryViewModelFactory;
import com.lmkr.prmscemployeeapp.viewModel.LeaveRequestViewModel;
import com.lmkr.prmscemployeeapp.viewModel.LeaveRequestViewModelFactory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LeaveRequestFragment extends Fragment {

    private static List<LeaveRequest> leaveRequests = null;
    private FragmentLeaveRequestBinding binding;
    private LeaveRequestViewModel leaveRequestViewModel;
    private Observer<? super List<LeaveRequest>> leaveRequestObserver = new Observer<List<LeaveRequest>>() {
        @Override
        public void onChanged(List<LeaveRequest> leaveRequests) {
            LeaveRequestFragment.leaveRequests = leaveRequests;
            loadLeaveRequestData();
        }
    };

    private void loadLeaveRequestData() {
        binding.recyclerViewLeaveRequest.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        LeaveRequestRecyclerAdapter adapter = new LeaveRequestRecyclerAdapter(getActivity(), leaveRequests);
        binding.recyclerViewLeaveRequest.setAdapter(adapter);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

//        LeaveRequestViewModel leaveRequestViewModel = new ViewModelProvider(this).get(LeaveRequestViewModel.class);

        binding = FragmentLeaveRequestBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.contentLeaveFragment.textDashboard;
//        leaveRequestViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        leaveRequestViewModel = new ViewModelProvider(this, new LeaveRequestViewModelFactory(App.getInstance(), "%" + AppUtils.getCurrentDate() + "%")).get(LeaveRequestViewModel.class);
        leaveRequestViewModel.getLeaveRequest().observe(getViewLifecycleOwner(), leaveRequestObserver);



        UserData userData = SharedPreferenceHelper.getLoggedinUser(getActivity());
        binding.recyclerviewLeaveProgress.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        LeavesRemainingRecyclerAdapter adapter = new LeavesRemainingRecyclerAdapter(getActivity(),userData.getLeaveCount());
        binding.recyclerviewLeaveProgress.setAdapter(adapter);

        new CustomDatePicker(getActivity(),binding.dateTimeFrom);
        new CustomDatePicker(getActivity(),binding.dateTimeTo);

        binding.spinnerLeaveTypes.setAdapter(new LeaveTypeSpinnerAdapter(SharedPreferenceHelper.getLoggedinUser(getActivity()).getLeaveCount(),getActivity()));

        getLeaveRequest();
    }



    private void getLeaveRequest() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiCalls.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        Urls urls = retrofit.create(Urls.class);

        Call<LeaveRequestResponse> call = urls.getLeaveRequest(AppUtils.getStandardHeaders(SharedPreferenceHelper.getLoggedinUser(getActivity())));

        call.enqueue(new Callback<LeaveRequestResponse>() {
            @Override
            public void onResponse(Call<LeaveRequestResponse> call, Response<LeaveRequestResponse> response) {
                Log.i("response", response.toString());

                if (!response.isSuccessful()) {
//                    tv.setText("Code :" + response.code());
                }

                leaveRequestViewModel.insert(response.body().getLeaveRequest());
            }

            @Override
            public void onFailure(Call<LeaveRequestResponse> call, Throwable t) {
                t.printStackTrace();
                AppUtils.makeNotification(t.toString(), getActivity());
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
}