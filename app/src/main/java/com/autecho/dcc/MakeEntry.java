package com.autecho.dcc;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import com.autecho.helpers.GetCurrentLocation;
import com.autecho.helpers.Mood;


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
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
