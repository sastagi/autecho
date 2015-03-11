package com.autecho.dcc;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.autecho.model.StatusList;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.QueryOrder;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;

import static com.autecho.dcc.Autecho.mClient;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    MapView mMapView;
    private GoogleMap googleMap;
    private View progressWheel;
    private LatLngBounds.Builder builder;
    private MobileServiceTable<StatusList> mStatusTable;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Map.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MapFragment() {
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
        //return inflater.inflate(R.layout.fragment_map, container, false);
        // inflat and return the layout
        View v = inflater.inflate(R.layout.fragment_map, container,
                false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        progressWheel = v.findViewById(R.id.progress_wheel);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        drawMarkers();
        // Get the Mobile Service Table instance to use


        /*googleMap = mMapView.getMap();
        // latitude and longitude
        double latitude = 17.385044;
        double longitude = 78.486671;

        // create marker
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title("Hello Maps");

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

        // adding marker
        googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(17.385044, 78.486671)).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));*/

        // Perform any camera updates here
        return v;
    }

    private void drawMarkers(){
        builder = new LatLngBounds.Builder();
        googleMap = mMapView.getMap();
        mStatusTable = mClient.getTable(StatusList.class);
        Context mContext = Autecho.mContext;
        SharedPreferences sharedPref = mContext.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String userId = sharedPref.getString(getString(R.string.userid),null);
        mStatusTable.where().field("userid").eq(userId).orderBy("__createdAt", QueryOrder.Descending).execute(new TableQueryCallback<StatusList>() {

            public void onCompleted(List<StatusList> result, int count, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {

                    for (StatusList item : result) {
                        //Add Markers
                        // create marker
                        if(!item.getLocation().equals("no")){
                            String[] location = item.getLocation().split(",");
                            double latitude = Double.parseDouble(location[0]);
                            double longitude = Double.parseDouble(location[1]);
                            Log.d("The co-ordinates are",""+latitude+"::"+longitude);
                            MarkerOptions marker = new MarkerOptions().position(
                                    new LatLng(latitude, longitude)).title(item.getStatus());
                            builder.include(marker.getPosition());
                            // Changing marker icon
                            marker.icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_RED));

                            // adding marker
                            googleMap.addMarker(marker);
                        }
                    }
                    LatLngBounds bounds = builder.build();
                    int padding = 50; // offset from edges of the map in pixels
                    CameraUpdate camerUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    googleMap.animateCamera(camerUpdate);
                    progressWheel.setVisibility(View.GONE);
                } else {
                    Log.d("ERROR FETCHING ITEMS", "Unable to fetch items from mobile service");
                }
            }
        });

    }

    private void showMapMarkers(){

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
