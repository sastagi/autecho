package com.autecho.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.shapes.RectShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.autecho.dcc.R;
import com.autecho.helpers.MyShapeDrawable;
import com.autecho.helpers.MySingleton;
import com.autecho.model.StatusList;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Santosh on 3/1/15.
 */
public class StatusAdapter extends ArrayAdapter<StatusList> {

    /**
     * Adapter context
     */
    Context mContext;

    /**
     * Adapter View layout
     */
    int mLayoutResourceId;

    ImageLoader mImageLoader;

    public StatusAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);

        mContext = context;
        mLayoutResourceId = layoutResourceId;
    }

    /**
     * Returns the view for a specific item on the list
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        final StatusList currentItem = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
        }

        row.setTag(currentItem);
        final TextView status = (TextView) row.findViewById(R.id.status);
        status.setText(currentItem.getStatus());

        final TextView poster = (TextView) row.findViewById(R.id.poster);
        poster.setText(currentItem.getFullname());

        final TextView postedField = (TextView) row.findViewById(R.id.posted);
        String timevalue="";
        long currenttime = System.currentTimeMillis()/1000;
        long posted = currentItem.getCreatedAt().getTime()/1000;//Long.parseLong(timestamp[position]);
        if(posted>0){
            if (currenttime<(posted+60))
                timevalue = "A few seconds...";// Work on providing accurate numbers
            else if (currenttime<(posted+120)) timevalue = "1 min";
            else if (currenttime<(posted+(60*60))){
                long time = (currenttime-posted)/60;
                timevalue = time+" mins";
            }
            else if (currenttime<(posted+(60*120))) timevalue = "1 hr";
            else if (currenttime<(posted+(60*60*24))){
                long time = (currenttime-posted)/(60*60);
                timevalue = time + " hr";
            }
            else{
                SimpleDateFormat sf = new SimpleDateFormat("dd MMM");
                Date date = new Date(posted*1000);
                timevalue = sf.format(date);
            }
        }
        postedField.setText(timevalue);

        final ImageView icon = (ImageView) row.findViewById(R.id.icon);
        MyShapeDrawable icon1 = new MyShapeDrawable(new RectShape());
        icon1.getInflection(currentItem.getMood());
        icon1.setIntrinsicHeight(100);
        icon1.setIntrinsicWidth(100);
        icon.setImageDrawable(icon1);

        // Get the NetworkImageView that will display the image.
        final NetworkImageView mNetworkImageView = (NetworkImageView) row.findViewById(R.id.photo);

        if(currentItem.getBloburl().equals("no")){
            mNetworkImageView.setVisibility(View.GONE);
        }else {
            mNetworkImageView.setVisibility(View.VISIBLE);
            // Get the ImageLoader through your singleton class.
            mImageLoader = MySingleton.getInstance(getContext()).getImageLoader();

            // Set the URL of the image that should be loaded into this view, and
            // specify the ImageLoader that will be used to make the request.
            String[] imageUrl = currentItem.getBloburl().split("\\?");
            //Log.d("The image url is", imageUrl[0]);
            mNetworkImageView.setImageUrl(imageUrl[0], mImageLoader);
        }
        final TextView location = (TextView) row.findViewById(R.id.location);
        final TextView locationwithimage = (TextView) row.findViewById(R.id.locationwithimage);

        if(currentItem.getBloburl().equals("no")){
            location.setVisibility(View.VISIBLE);
            locationwithimage.setVisibility(View.GONE);
            setLocationData(currentItem, location);
        } else{
            location.setVisibility(View.GONE);
            locationwithimage.setVisibility(View.VISIBLE);
            setLocationData(currentItem, locationwithimage);
        }



        return row;
    }

    private void setLocationData(StatusList currentItem, TextView location){
        if(!currentItem.getLocation().equals("no")){

            if(currentItem.getAddress()!=null)
                location.setText("at "+currentItem.getAddress());
            else
                location.setText("");
        }else{
            location.setText("");
        }
    }
}
