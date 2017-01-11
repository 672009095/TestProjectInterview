package com.testprojectinterview.app.testprojectinterview.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.testprojectinterview.app.testprojectinterview.Holder.ActiveApplicationHolder;
import com.testprojectinterview.app.testprojectinterview.Mapping.ApplicationActive;
import com.testprojectinterview.app.testprojectinterview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by skyshi on 12/01/17.
 */

public class ActiveApplicationAdapter extends RecyclerView.Adapter {
    public AppClickedListener appClickedListener;
    public Context context;
    private List<ApplicationActive> mAppList = new ArrayList<>();
    public ActiveApplicationAdapter(AppClickedListener appClickedListener, Context context){
        this.appClickedListener = appClickedListener;
        this.context = context;
    }
    public interface AppClickedListener{
        void onAppClicked(ActiveApplicationHolder holder,ApplicationActive applicationActive,int position);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_activity,parent,false);
        vh = new ActiveApplicationHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ApplicationActive applicationActive = mAppList.get(position);
        final ActiveApplicationHolder vh = (ActiveApplicationHolder)holder;
        Log.d("listBind",applicationActive.getApplicationName());
        Log.d("listBind",applicationActive.getApplicationIcon().toString());
        vh.applicationName.setText(applicationActive.getApplicationName());
        vh.applicationIcon.setImageDrawable(applicationActive.getApplicationIcon());
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appClickedListener.onAppClicked(vh,applicationActive,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAppList.size();
    }

    public void append(List<ApplicationActive>data){
        //if(mAppList == null){
            mAppList.addAll(data);
        //}
    }
}
