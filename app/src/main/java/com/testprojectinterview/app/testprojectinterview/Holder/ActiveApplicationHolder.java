package com.testprojectinterview.app.testprojectinterview.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.testprojectinterview.app.testprojectinterview.R;

/**
 * Created by skyshi on 12/01/17.
 */

public class ActiveApplicationHolder extends RecyclerView.ViewHolder {
    public TextView applicationName;
    public ImageView applicationIcon;
    public ActiveApplicationHolder(View itemView) {
        super(itemView);
        applicationName = (TextView)itemView.findViewById(R.id.text_application_name);
        applicationIcon = (ImageView) itemView.findViewById(R.id.image_application_icon);
    }
}
