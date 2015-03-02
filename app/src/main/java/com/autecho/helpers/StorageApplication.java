package com.autecho.helpers;

import android.app.Application;

/**
 * Created by Santosh on 2/21/15.
 */
public class StorageApplication extends Application {

    private static StorageService mStorageService;

    private static StorageApplication mstorageApplication;

    public static StorageApplication getStorageApplication(){
        if(mstorageApplication==null)
            mstorageApplication = new StorageApplication();
        return mstorageApplication;
    }

    public StorageService getStorageService() {
        if (mStorageService == null) {
            mStorageService = new StorageService(this);
        }
        return mStorageService;
    }
}
