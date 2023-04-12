package com.example.workmanagement.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BoardViewModel extends ViewModel {

    private MutableLiveData<Long> id = new MutableLiveData<>();

    public MutableLiveData<Long> getId() {
        return id;
    }

    public void setId(Long id) {
        this.id.postValue(id);
    }
}
