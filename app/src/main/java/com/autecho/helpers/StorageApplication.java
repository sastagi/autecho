package com.autecho.helpers;

import android.app.Application;

/**
 * Created by Santosh on 2/21/15.
 */
public class StorageApplication extends Application {

    private StorageService mStorageService;

    public StorageApplication() {}

    public StorageService getStorageService() {
        if (mStorageService == null) {
            mStorageService = new StorageService(this);
        }
        return mStorageService;
    }
}
