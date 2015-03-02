package com.autecho.helpers;

;

/**
 * Created by Santosh on 2/26/15.
 */
public class AzureConnection {
    /*private static AzureConnection azureConnection = new AzureConnection();

    public MobileServiceClient getmClient() {
        return mClient;
    }

    private MobileServiceClient mClient;

    public MobileServiceTable<UserList> getmUserList() {
        return mUserList;
    }

    public MobileServiceTable<StatusList> getmStatusList() {
        return mStatusList;
    }

    private MobileServiceTable<UserList> mUserList;
    private MobileServiceTable<StatusList> mStatusList;
    AzureConnection(Context){
        try {
            // Create the Mobile Service Client instance, using the provided
            // Mobile Service URL and key
            mClient = new MobileServiceClient(
                    "https://autecho.azure-mobile.net/",
                    "EJGwalGeNhbONhLArapTkycXhFQXww10",StorageApplication.getStorageApplication());

            //new MobileServiceClient("p","l");

            // Get the Mobile Service Table instance to use
            mUserList = mClient.getTable(UserList.class);
            mStatusList = mClient.getTable(StatusList.class);
        } catch (MalformedURLException e) {
            //createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        }
    }
    public static AzureConnection getAzureConnection(){
        if(azureConnection==null) {
            azureConnection = new AzureConnection();

        }
        return azureConnection;
    }*/
}
