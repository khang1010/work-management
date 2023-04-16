package com.example.workmanagement.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.workmanagement.utils.dto.TableDetailsDTO;
import com.example.workmanagement.utils.dto.UserInfoDTO;

import java.util.List;

public class BoardViewModel extends ViewModel {

    private MutableLiveData<Long> id = new MutableLiveData<>();

    private MutableLiveData<String> name = new MutableLiveData<>();

    private MutableLiveData<UserInfoDTO> admin = new MutableLiveData<>();

    private MutableLiveData<List<UserInfoDTO>> members = new MutableLiveData<>();

    private MutableLiveData<List<TableDetailsDTO>> tables = new MutableLiveData<>();

    public MutableLiveData<Long> getId() {
        return id;
    }

    public void setId(Long id) {
        this.id.postValue(id);
    }

    public MutableLiveData<String> getName() {
        return name;
    }

    public void setName(String name) {
        this.name.postValue(name);
    }

    public MutableLiveData<UserInfoDTO> getAdmin() {
        return admin;
    }

    public void setAdmin(UserInfoDTO admin) {
        this.admin.postValue(admin);
    }

    public MutableLiveData<List<UserInfoDTO>> getMembers() {
        return members;
    }

    public void setMembers(List<UserInfoDTO> members) {
        this.members.setValue(members);
    }

    public MutableLiveData<List<TableDetailsDTO>> getTables() {
        return tables;
    }

    public void setTables(List<TableDetailsDTO> tables) {
        this.tables.setValue(tables);
    }
}
