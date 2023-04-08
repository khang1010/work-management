package com.example.workmanagement.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class User extends ViewModel {

    private MutableLiveData<Long> id = new MutableLiveData<>();

    private MutableLiveData<String> email = new MutableLiveData<>();

    private MutableLiveData<String> displayName = new MutableLiveData<>();

    private MutableLiveData<String> token = new MutableLiveData<>();

    private MutableLiveData<String> photoUrl = new MutableLiveData<>();

    public MutableLiveData<Long> getId() {
        return id;
    }

    public void setId(long id) {
        this.id.postValue(id);
    }

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email.postValue(email);
    }

    public MutableLiveData<String> getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName.postValue(displayName);
    }

    public MutableLiveData<String> getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token.postValue(token);
    }

    public MutableLiveData<String> getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl.postValue(photoUrl);
    }
}
