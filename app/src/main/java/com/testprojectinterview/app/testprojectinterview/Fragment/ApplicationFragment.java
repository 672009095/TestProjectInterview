package com.testprojectinterview.app.testprojectinterview.Fragment;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.testprojectinterview.app.testprojectinterview.Adapter.ActiveApplicationAdapter;
import com.testprojectinterview.app.testprojectinterview.Holder.ActiveApplicationHolder;
import com.testprojectinterview.app.testprojectinterview.Mapping.ApplicationActive;
import com.testprojectinterview.app.testprojectinterview.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by skyshi on 12/01/17.
 */

public class ApplicationFragment extends Fragment implements ActiveApplicationAdapter.AppClickedListener{
    RecyclerView recyclerView;
    public List<ApplicationActive> data = new ArrayList<>();
    public ApplicationActive applicationActive;
    public ActiveApplicationAdapter adapter;
    PackageManager pm;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_application,container,false);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActivityManager am = (ActivityManager)getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        getActiveApplication(am);

        adapter = new ActiveApplicationAdapter(this,getContext());
        setListActiveApplication();
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
    }
    private void getActiveApplication(ActivityManager am){
        List l = am.getRunningAppProcesses();
        Iterator iter = l.iterator();
        pm = getActivity().getPackageManager();
        while(iter.hasNext()){
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo)(iter.next());
            try {
                CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                Drawable d = pm.getApplicationIcon(pm.getApplicationInfo(info.processName,PackageManager.GET_META_DATA));
                PackageInfo s = pm.getPackageInfo(info.processName,PackageManager.GET_ACTIVITIES);
                //Log.d("APPACTIVE", c.toString());
                //Log.d("APPACTIVE", d.toString());
                //Log.d("APPACTIVE", "package name"+s.packageName.toString());

                applicationActive = new ApplicationActive();
                applicationActive.setApplicationName(c.toString());
                applicationActive.setApplicationIcon(d);
                applicationActive.setApplicationPackage(s.packageName.toString());
                data.add(applicationActive);
            }catch(Exception e) {
                //Name Not FOund Exception
            }
        }
    }
    public void setListActiveApplication(){
        adapter.append(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAppClicked(ActiveApplicationHolder holder, ApplicationActive applicationActive, int position) {
        Log.d("packageClicked",applicationActive.getApplicationPackage());
        Intent launchAPP = pm.getLaunchIntentForPackage(applicationActive.getApplicationPackage());
        if(launchAPP!=null){
            startActivity(launchAPP);
        }
    }
}
