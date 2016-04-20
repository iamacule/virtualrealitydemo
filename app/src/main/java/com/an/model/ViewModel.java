package com.an.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrAn on 20-Apr-16.
 */
public class ViewModel {
    private String id;
    private Bitmap bpView;

    public ViewModel() {
    }

    public ViewModel(String id, Bitmap bpView) {
        this.id = id;
        this.bpView = bpView;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Bitmap getBpView() {
        return bpView;
    }

    public void setBpView(Bitmap bpView) {
        this.bpView = bpView;
    }
}
