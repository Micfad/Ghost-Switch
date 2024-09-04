package com.example.ghostswitch.otherClass;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.ghostswitch.DatabaseClasses.HomeIpAddressManager;

public class MyViewModelFactory implements ViewModelProvider.Factory {

    private final HomeIpAddressManager homeIpAddressManager;

    public MyViewModelFactory(HomeIpAddressManager homeIpAddressManager) {
        this.homeIpAddressManager = homeIpAddressManager;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MyViewModel.class)) {
            return (T) new MyViewModel(homeIpAddressManager);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
