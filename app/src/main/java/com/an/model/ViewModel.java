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
    private String modelName;
    private boolean isDynamic;
    private List<Bitmap> listBpView;

    public ViewModel() {
    }

    public boolean isDynamic() {
        return isDynamic;
    }

    public void setDynamic(boolean dynamic) {
        isDynamic = dynamic;
    }

    public List<Bitmap> getListBpView() {
        return listBpView;
    }

    public void setListBpView(List<Bitmap> listBpView) {
        this.listBpView = listBpView;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
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
