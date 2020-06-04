package com.scnu.easygo.ui.route;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RouterViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RouterViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is route fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}