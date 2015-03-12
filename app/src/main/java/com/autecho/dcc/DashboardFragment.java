package com.autecho.dcc;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.autecho.model.StatusList;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.QueryOrder;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;

import static com.autecho.dcc.Autecho.mClient;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashboardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private float[] sliceValues = new float[3];
    private MobileServiceTable<StatusList> mStatusTable;

    //Pie chart data
    private PieChartView pieChart;
    private PieChartData pieData;
    //Column chart data
    private ColumnChartData columnData;
    private ColumnChartView columnChart;
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;

    float[][] dataArray = new float[5][4];

    public static String[] dateArray = new String[4];

    private View rootView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public DashboardFragment() {
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
        mStatusTable = mClient.getTable(StatusList.class);
        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        columnChart = (ColumnChartView) rootView.findViewById(R.id.chart);
        pieChart = (PieChartView) rootView.findViewById(R.id.pieChart);
        getData();
        return rootView;
    }

    private void  getData(){

        Context mContext = Autecho.mContext;
        SharedPreferences sharedPref = mContext.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String userId = sharedPref.getString(getString(R.string.userid),null);
        mStatusTable.where().field("userid").eq(userId).orderBy("__createdAt", QueryOrder.Descending).execute(new TableQueryCallback<StatusList>() {

            public void onCompleted(List<StatusList> result, int count, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    int currentCount=3;
                    int moodCount=0;
                    String currentvalue="";
                    for (StatusList item : result) {
                        //Get pie data
                        if(item.getMood()<25){
                            sliceValues[0]=sliceValues[0]+1f;
                        }

                        else if((item.getMood()<50)&&(item.getMood()>25)){
                            sliceValues[1]=sliceValues[1]+1f;
                        }

                        else if((item.getMood()<75)&&(item.getMood()>50)){
                            sliceValues[2]=sliceValues[2]+1f;
                        }
                        //Get chart data
                        String timevalue="";

                        long currenttime = System.currentTimeMillis()/1000;
                        long posted = item.getCreatedAt().getTime()/1000;//Long.parseLong(timestamp[position]);

                        if(posted>0&&currentCount>0){
                            if (currenttime<(posted+(60*60*24))){
                                currentvalue = "Today";
                                dateArray[currentCount] = currentvalue;
                                dataArray[0][currentCount] = (dataArray[0][currentCount]+item.getMood())/++moodCount;
                            }
                            else{
                                SimpleDateFormat sf = new SimpleDateFormat("dd MMM");
                                Date date = new Date(posted*1000);
                                timevalue = sf.format(date);
                                if(!timevalue.equals(currentvalue)){
                                    Log.d("Difference in values",timevalue+"+"+currentvalue);
                                    moodCount=0;
                                    currentCount=currentCount-1;
                                    dateArray[currentCount] = timevalue;
                                    dataArray[0][currentCount] = (dataArray[0][currentCount]+item.getMood())/++moodCount;
                                    currentvalue=timevalue;
                                }
                                else{
                                    dataArray[0][currentCount] = (dataArray[0][currentCount]+item.getMood())/++moodCount;
                                }
                            }
                        }else{
                            dataArray[0][currentCount] = (dataArray[0][currentCount]+item.getMood())/++moodCount;
                        }
                    }
                    if(dateArray[0]!=null) {
                        drawPieData();
                        Log.d("The dates are", dateArray[0] + dateArray[1] + dateArray[2] + dateArray[3]);
                        drawColumnChart();
                    }
                } else {
                    Log.d("ERROR FETCHING ITEMS", "Unable to fetch items from mobile service");
                }
            }
        });
    }

    private void drawPieData() {

        int numValues = 3;
        int color = 0;
        float sliceShare = 0;
        List<SliceValue> values = new ArrayList<SliceValue>();
        for (int i = 0; i < numValues; ++i) {
            switch (i) {
                case 0:
                    color = Color.parseColor("#479bd5");
                    sliceShare = sliceValues[i];
                    break;
                case 1:
                    color = Color.parseColor("#ec4846");
                    sliceShare = sliceValues[i];
                    break;
                case 2:
                    color = Color.parseColor("#fcd206");
                    sliceShare = sliceValues[i];
                    break;
            }
            SliceValue sliceValue = new SliceValue(sliceShare, color);
            values.add(sliceValue);
        }

        pieData = new PieChartData(values);
        pieChart.setPieChartData(pieData);
        preparePieDataAnimation();
        pieChart.startDataAnimation();
    }

    private void preparePieDataAnimation() {
        int count = 0;
        for (SliceValue value : pieData.getValues()) {
            value.setTarget(sliceValues[count++]);
        }
    }

    private void prepareColumnDataAnimation() {
        int count = 0;
        for (Column column : columnData.getColumns()) {
            for (SubcolumnValue value : column.getValues()) {
                value.setTarget(dataArray[0][count++]);
            }
        }
    }

    private void drawColumnChart() {
        columnData = new ColumnChartData(generateColumnData());
        List<AxisValue> axisValuesForColumn = new ArrayList<AxisValue>();

        for (int i = 0; i < dateArray.length; ++i) {
            axisValuesForColumn.add(new AxisValue(i, dateArray[i].toCharArray()));
        }
        if (hasAxes) {
            Axis axisX = new Axis(axisValuesForColumn);
            axisX.setTextColor(Color.parseColor("#6b808d"));
            Axis axisY = new Axis().setHasLines(true);
            axisY.setTextColor(Color.parseColor("#6b808d"));
            if (hasAxesNames) {
                axisX.setName("Date");
                axisY.setName("Average mood no");
            }
            columnData.setAxisXBottom(axisX);
            columnData.setAxisYLeft(axisY);
        } else {
            columnData.setAxisXBottom(null);
            columnData.setAxisYLeft(null);
        }
        columnChart.setColumnChartData(columnData);
        prepareColumnDataAnimation();
        columnChart.startDataAnimation();
    }

    private ColumnChartData generateColumnData() {
        int numSubcolumns = 1;
        int numColumns = 4;
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of the columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue(dataArray[j][i], Color.parseColor("#5cb345")));
            }

            columns.add(new Column(values));
        }
        ColumnChartData columnChartData = new ColumnChartData(columns);
        return columnChartData;
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
