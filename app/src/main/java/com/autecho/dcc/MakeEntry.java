package com.autecho.dcc;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.autecho.helpers.GetCurrentLocation;
import com.autecho.helpers.Mood;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


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
    public static String profilename_value="";
    public static String profilename_id="";

    private FrameLayout main;
    //private AutechoModel autechoModel;
    private SeekBar mSeekBar;
    private View face, lips;
    private ProgressDialog progressDialog;
    private String userid,profileid;
    private String location="no";
    LocationManager locationManager;
    private Location currentloc;
    LocationListener listener;
    private Float latitude, longitude;

    private OnFragmentInteractionListener mListener;

    private GetCurrentLocation mListen;

    private ImageView photoImage = null;
    private View imageLayout;
    private Button imageButton;

    private static final String TAG = "CallCamera";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQ = 0;
    final int REQUEST_TAKE_PHOTO = 1;

    private Uri fileUri = null;

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
        Button mLocation = (Button)view.findViewById(R.id.location);
        mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocaction();
            }
        });
        photoImage = (ImageView) view.findViewById(R.id.photo_image);

        imageLayout = view.findViewById(R.id.imageLayout);

        imageButton = (Button) view.findViewById(R.id.addImage);
        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //dispatchTakePictureIntent();
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //File file = getOutputPhotoFile();
                fileUri = Uri.fromFile(getOutputPhotoFile());
                i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQ );
            }
        });
        return view;
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQ) {
            if (resultCode == Activity.RESULT_OK) {
                Uri photoUri = null;
                if (data == null) {
                    // A known bug here! The image should have saved in fileUri
                    Log.d("IMAGE RESULT","SUCCESSFUL IMAGE");
                    photoUri = fileUri;
                } else {
                    photoUri = data.getData();
                    Log.d("IMAGE RESULT","SUCCESSFUL IMAGE");
                    //Toast.makeText(this, "Image saved successfully in: " + data.getData(),Toast.LENGTH_LONG).show();
                }
                showPhoto(photoUri);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d("IMAGE RESULT","CANCELLED");
                //Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("IMAGE RESULT","Callout for image capture failed!");
                //Toast.makeText(this, "Callout for image capture failed!",Toast.LENGTH_LONG).show();
            }
        }
       /* Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        photoImage.setImageBitmap(imageBitmap);*/
    }
    private void showPhoto(Uri photoUri) {
        File imageFile = new File(photoUri.getPath());
        if (imageFile.exists()){
            Bitmap bitmap;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;
            Log.d("Details regarding image:", "Height: "+imageHeight+"Width: "+imageWidth);
            if(imageWidth > imageHeight) {
                options.inSampleSize = calculateInSampleSize(options,512,256);//if landscape
            } else{
                options.inSampleSize = calculateInSampleSize(options,256,512);//if portrait
            }
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(),options);
            bitmap = RotateBitmap(bitmap, 90);
            Log.d("Details regarding image:", "Height: "+imageHeight+"Width: "+imageWidth);
            BitmapDrawable drawable = new BitmapDrawable(this.getResources(), bitmap);
            photoImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            photoImage.setImageDrawable(drawable);
            imageLayout.setVisibility(View.VISIBLE);
            imageButton.setVisibility(View.GONE);
            Log.d("IMAGEFILE CONTENT IS AT::::::",imageFile.toString());
        }
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


    public void getLocaction(){
        mListen = new GetCurrentLocation(getActivity());

        mListen.startGettingLocation(new GetCurrentLocation.getLocation() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("LOCATION IS:", String.valueOf(location.getLatitude())+String.valueOf(location.getLongitude()));
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
