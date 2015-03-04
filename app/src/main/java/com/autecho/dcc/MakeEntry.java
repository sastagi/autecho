package com.autecho.dcc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.autecho.helpers.GetCurrentLocation;
import com.autecho.helpers.Mood;
import com.autecho.helpers.StorageApplication;
import com.autecho.helpers.StorageService;
import com.autecho.model.StatusList;
import com.google.gson.JsonObject;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.autecho.dcc.Autecho.mClient;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MakeEntry.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MakeEntry#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MakeEntry extends Fragment implements SeekBar.OnSeekBarChangeListener {
    public static boolean smallsizeflag;

    private FrameLayout main;
    //private AutechoModel autechoModel;
    private SeekBar mSeekBar;
    private View face, lips;
    private String userLocation = "no";
    private String userAddress = "no";
    private String blobUrl = "no";
    private boolean photoExists;

    private OnFragmentInteractionListener mListener;

    private GetCurrentLocation mListen;

    private ImageView mImageView = null;
    private EditText statusField;
    private Uri mImageCaptureUri = null;
    private Uri fileUri = null;
    private View imageLayout;
    private Button imageButton;
    private static final String TAG = "CallCamera";

    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;

    //Storage variable
    private StorageService mStorageService;
    private Uri currImageURI;

    private MobileServiceTable<StatusList> mStatusList;

    //SeekBar functions
    public void onProgressChanged(SeekBar seekbar, int progress, boolean fromTouch) {
        face = main.getChildAt(0);
        lips = main.getChildAt(1);
        main.removeView(face);
        main.removeView(lips);
        main.addView(new Mood(getActivity(), 220,progress), 0);
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MakeEntry.
     */
    // TODO: Rename and change types and number of parameters
    public static MakeEntry newInstance(String param1, String param2) {
        MakeEntry fragment = new MakeEntry();
        return fragment;
    }
    public void onStartTrackingTouch(SeekBar arg0) {
        // TODO Auto-generated method stub
    }
    public void onStopTrackingTouch(SeekBar arg0) {
        // TODO Auto-generated method stub
    }//Seekbar functions over

    public MakeEntry() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_make_entry, container, false);
        main = ((FrameLayout) view.findViewById(R.id.main_view));
        if (main.getChildAt(0) != null){
            main.removeViewAt(0);
        }
        main.addView(new Mood(getActivity(), 170,50), 0);
        mSeekBar = (SeekBar)view.findViewById(R.id.seek);
        mSeekBar.setProgress(50);
        mSeekBar.setOnSeekBarChangeListener(this);
        statusField = (EditText)view.findViewById(R.id.text);

        final Button mLocation = (Button)view.findViewById(R.id.location);
        mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocaction();
            }
        });

        final String [] items			= new String [] {"Take from camera", "Select from gallery"};
        ArrayAdapter<String> adapter	= new ArrayAdapter<String> (getActivity(), android.R.layout.select_dialog_item,items);
        AlertDialog.Builder builder		= new AlertDialog.Builder(getActivity());

        builder.setTitle("Select Image");
        builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
            public void onClick( DialogInterface dialog, int item ) { //pick from camera
                if (item == 0) {
                    Intent intent 	 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                            "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

                    try {
                        intent.putExtra("return-data", true);

                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else { //pick from file
                    Intent intent = new Intent();

                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
                }
            }
        } );

        final AlertDialog dialog = builder.create();

        mImageView = (ImageView) view.findViewById(R.id.photo_image);

        imageLayout = view.findViewById(R.id.imageLayout);

        imageButton = (Button) view.findViewById(R.id.addImage);
        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.show();
            }
        });

        //Blob stuff
        StorageApplication myApp = (StorageApplication) getActivity().getApplication();
        mStorageService = myApp.getStorageService();
        //Upload data to azure
        final Button mPost = (Button)view.findViewById(R.id.post);
        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(photoExists){
                    //getBlobCount for container;
                    mStorageService.getBlobsForContainer("moodesto",mSeekBar.getProgress()+"-"+statusField.getText().toString());
                    //sendImageToBlob()
                    //mStorageService.getSasForNewBlob("moodesto");
                }else{
                    String status = statusField.getText().toString();//get status message of the user
                    //get user id
                    Context mContext = Autecho.mContext;
                    SharedPreferences sharedPref = mContext.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    String userId = sharedPref.getString(getString(R.string.userid),null);
                    String fullname = sharedPref.getString(getString(R.string.fullname),null);
                    Log.d("AZURE GETS THE FOLLOWING",userId+" XXX "+ fullname +" XXX "+status+" XXX "+mSeekBar.getProgress()+" XXX "+userLocation+" XXX "+userAddress+" XXX "+blobUrl);
                    StatusList statusList = new StatusList(userId, fullname, status, mSeekBar.getProgress(), userLocation, blobUrl, userAddress);
                    // Get the Mobile Service Table instance to use
                    mStatusList = mClient.getTable(StatusList.class);
                    mStatusList.insert(statusList, new TableOperationCallback<StatusList>() {
                        @Override
                        public void onCompleted(StatusList statusList, Exception e, ServiceFilterResponse serviceFilterResponse) {
                            Log.d("INSERTEST", "SUCCESS");
                            FragmentManager fragmentManager = getActivity().getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.container, new FeedFragment())
                                    .commit();
                        }
                    });
                }
            }
        });
        return view;
    }

    /***
     * Register for broadcasts
     */
    @Override
    public void onResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("blobs.loaded");
        filter.addAction("blob.created");
        getActivity().registerReceiver(receiver, filter);
        super.onResume();
    }

    /***
     * Unregister for broadcasts
     */
    @Override
    public void onPause() {
        getActivity().unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;

        switch (requestCode) {
            case PICK_FROM_CAMERA:
                doCrop();

                break;

            case PICK_FROM_FILE:
                mImageCaptureUri = data.getData();
                doCrop();
                break;

            case CROP_FROM_CAMERA:
                File imageFile = new File(fileUri.getPath());
                currImageURI = getImageContentUri(getActivity().getApplicationContext(), imageFile);
                if (imageFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    mImageView.setImageBitmap(myBitmap);
                    imageLayout.setVisibility(View.VISIBLE);
                    imageButton.setVisibility(View.GONE);
                    photoExists=true;
                    //getBlobCount for container;
                    //mStorageService.getBlobsForContainer("moodesto");
                    //sendImageToBlob();
                    //mStorageService.getSasForNewBlob("moodesto");
                }
                break;
        }
    }

    private void doCrop() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities( intent, 0 );

        int size = list.size();

        if (size == 0) {
            //Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();

            return;
        } else {
            fileUri = Uri.fromFile(getOutputPhotoFile());
            intent.setData(mImageCaptureUri);
            intent.putExtra("outputX", 360);
            intent.putExtra("outputY", 360);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            intent.putExtra("output", fileUri);
            intent.putExtra("return-data", false);
            Intent i = new Intent(intent);
            ResolveInfo res	= list.get(0);

            i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

            startActivityForResult(i, CROP_FROM_CAMERA);
        }
    }

    private File getOutputPhotoFile() {
        File directory = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                Log.e(TAG, "Failed to create storage directory.");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(directory.getPath() + File.separator + "IMG_"
                + timeStamp + ".png");
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, android.content.Intent intent) {
            String intentAction = intent.getAction();
            /*if (intentAction.equals("blob.loaded")) {
                String count = intent.getStringExtra("blob.count");
                String blobName = Integer.parseInt(count)+1+"-"+statusField.getText().toString()+"-"+mSeekBar.getProgress();
                Log.d("NEW NAME FOR BLOB IS:",blobName);
                //sendImageToBlob();
                mStorageService.getSasForNewBlob("moodesto", blobName);
            }*/
            if (intentAction.equals("blob.created")) {
                //If a blob has been created, upload the image
                JsonObject blob = mStorageService.getLoadedBlob();
                Log.d("SAAASSSSSSUUUUUUURRRRRRLLLLLLL",blob.toString());
                String sasUrl = blob.getAsJsonPrimitive("sasUrl").toString();
                (new ImageUploaderTask(sasUrl)).execute();
            }
        }
    };
    class ImageUploaderTask extends AsyncTask<Void, Void, Boolean> {
        private String mUrl;
        public ImageUploaderTask(String url) {
            mUrl = url;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                //Get the image data
                Log.d("The image uri is",currImageURI.toString());
                Cursor cursor = getActivity().getContentResolver().query(currImageURI, null,null, null, null);
                cursor.moveToFirst();
                int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                String absoluteFilePath = cursor.getString(index);
                FileInputStream fis = new FileInputStream(absoluteFilePath);
                int bytesRead = 0;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] b = new byte[1024];
                while ((bytesRead = fis.read(b)) != -1) {
                    bos.write(b, 0, bytesRead);
                }
                byte[] bytes = bos.toByteArray();
                // Post our image data (byte array) to the server
                URL url = new URL(mUrl.replace("\"", ""));
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("PUT");
                urlConnection.addRequestProperty("Content-Type", "image/jpeg");
                urlConnection.setRequestProperty("Content-Length", ""+ bytes.length);
                // Write image data to server
                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.write(bytes);
                wr.flush();
                wr.close();
                int response = urlConnection.getResponseCode();
                //If we successfully uploaded, return true
                if (response == 201
                        && urlConnection.getResponseMessage().equals("Created")) {
                    Log.d("Success",urlConnection.getURL().toString());
                    blobUrl = urlConnection.getURL().toString();
                    return true;
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage().toString());
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean uploaded) {
            if (uploaded) {

                //mAlertDialog.cancel();
                //mStorageService.getBlobsForContainer(mContainerName);
                Log.d("SUCCESSSSSSSSSSSS","Check url for image");
                sendMoodDataToAzure();
            }
        }
    }

    public void sendMoodDataToAzure(){
        String status = statusField.getText().toString();//get status message of the user
        //get user id
        Context mContext = Autecho.mContext;
        SharedPreferences sharedPref = mContext.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String userId = sharedPref.getString(getString(R.string.userid),null);
        String fullname = sharedPref.getString(getString(R.string.fullname),null);
        Log.d("AZURE GETS THE FOLLOWING",userId+" XXX "+ fullname +" XXX "+status+" XXX "+mSeekBar.getProgress()+userLocation+" XXX "+userAddress+" XXX "+blobUrl);
        StatusList statusList = new StatusList(userId, fullname, status, mSeekBar.getProgress(), userLocation, blobUrl, userAddress);
        // Get the Mobile Service Table instance to use
        mStatusList = mClient.getTable(StatusList.class);
        mStatusList.insert(statusList, new TableOperationCallback<StatusList>() {
            @Override
            public void onCompleted(StatusList statusList, Exception e, ServiceFilterResponse serviceFilterResponse) {
                Log.d("INSERTEST", "SUCCESS");
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new FeedFragment())
                        .commit();
            }
        });
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            //int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public void getLocaction(){
        mListen = new GetCurrentLocation(getActivity());

        mListen.startGettingLocation(new GetCurrentLocation.getLocation() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("LOCATION IS:", String.valueOf(location.getLatitude())+String.valueOf(location.getLongitude()));
                userLocation = String.valueOf(location.getLatitude())+","+String.valueOf(location.getLongitude());

                RequestQueue queue = Volley.newRequestQueue(Autecho.mContext);
                String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+userLocation+"&sensor=true";

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    //Log.d()
                                    JSONObject jobj = new JSONObject(response);
                                    JSONArray jsonarray = new JSONArray();
                                    jsonarray = jobj.getJSONArray("results");
                                    JSONObject ja = jsonarray.getJSONObject(0);
                                    Log.d("The json object is:",ja.toString());
                                    JSONObject c = jsonarray.getJSONObject(0);

                                    String address = c.getString("formatted_address");
                                    String[] addressparts = address.split(", ");
                                    String[] state = addressparts[2].split(" ");
                                    userAddress = addressparts[1]+", " + state[0];
                                    Log.d("The address is: ",userAddress);
                                    //Toast.makeText(CreateAut.this, location, Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
                mListen.stopGettingLocation();
            }

        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
