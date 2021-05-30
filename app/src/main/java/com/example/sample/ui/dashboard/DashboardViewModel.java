package com.example.sample.ui.dashboard;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sample.model.CollectionsResponse;
import com.example.sample.model.DashboardResponse;

import java.util.ArrayList;

public class DashboardViewModel extends ViewModel {

    MutableLiveData<ArrayList<DashboardResponse.Dash>> stringLiveData;
    ArrayList<DashboardResponse.Dash> stringArrayList;

    public DashboardViewModel() {
        stringLiveData = new MutableLiveData<>();
        stringLiveData.setValue(new ArrayList<>());
    }

    public void updateData(ArrayList<DashboardResponse.Dash> list) {
        stringLiveData.postValue(list);
    }

    public MutableLiveData<ArrayList<DashboardResponse.Dash>> getStringLiveData() {
        return stringLiveData;
    }
}