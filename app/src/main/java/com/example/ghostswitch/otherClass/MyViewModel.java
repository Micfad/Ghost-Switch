package com.example.ghostswitch.otherClass;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ghostswitch.DatabaseClasses.HomeIpAddressManager;
import com.example.ghostswitch.data_models.SwitchesSinglesDataModel;
import com.example.ghostswitch.network.RelayStateTask;

import java.util.ArrayList;
import java.util.List;

public class MyViewModel extends ViewModel {

    private MutableLiveData<List<SwitchesSinglesDataModel>> switchData = new MutableLiveData<>();
    private HomeIpAddressManager homeIpAddressManager;

    // Constructor for MyViewModel
    public MyViewModel(HomeIpAddressManager homeIpAddressManager) {
        this.homeIpAddressManager = homeIpAddressManager;
        loadSwitchData(); // Initial load of data
    }

    // Expose LiveData to observers
    public LiveData<List<SwitchesSinglesDataModel>> getSwitchData() {
        return switchData;
    }

    // Method to update switch data using RelayStateTask
    public void loadSwitchData() {
        new RelayStateTask(homeIpAddressManager) {
            @Override
            protected void onPostExecute(List<SwitchesSinglesDataModel> result) {
                if (result != null && !result.isEmpty()) {
                    // Update LiveData only if the data has changed
                    List<SwitchesSinglesDataModel> currentData = switchData.getValue();
                    if (currentData == null || !currentData.equals(result)) {
                        switchData.setValue(result);
                    }
                }
            }
        }.execute();
    }
}
