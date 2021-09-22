package com.bteam.blocal.ui.shared.connection_info;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConnectionInfoViewModel extends ViewModel {
    private MutableLiveData<String> connectionInfo;

    public ConnectionInfoViewModel() {
        this.connectionInfo = new MutableLiveData<>();
    }

    public LiveData<String> getConnectionInfo() {
        return connectionInfo;
    }

    public void setConnectionInfo(String connectionInfo) {
        this.connectionInfo.setValue(connectionInfo);
    }
}
