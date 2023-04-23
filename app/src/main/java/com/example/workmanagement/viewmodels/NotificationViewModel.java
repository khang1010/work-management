package com.example.workmanagement.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.workmanagement.utils.dto.NotificationDTO;

import java.util.List;

public class NotificationViewModel extends ViewModel {

    private MutableLiveData<List<NotificationDTO>> notifications = new MutableLiveData<>();

    public MutableLiveData<List<NotificationDTO>> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationDTO> notifications) {
        this.notifications.setValue(notifications);
    }
}
