package com.scnu.easygo.ui.taxi;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TaxiViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TaxiViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is taxi fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}