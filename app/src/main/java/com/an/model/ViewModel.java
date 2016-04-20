package com.an.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrAn on 20-Apr-16.
 */
public class ViewModel {
    private List<Integer> id;
    private Bitmap bpView;

    public ViewModel() {
    }

    public ViewModel(List<Integer> id, Bitmap bpView) {
        this.id = id;
        this.bpView = bpView;
    }

    public List<Integer> getId() {
        if (id==null) {
            id = new ArrayList<>();
        }
        return id;
    }

    public void setId(List<Integer> id) {
        this.id = id;
    }

    public Bitmap getBpView() {
        return bpView;
    }

    public void setBpView(Bitmap bpView) {
        this.bpView = bpView;
    }
}
