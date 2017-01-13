package com.testprojectinterview.app.testprojectinterview.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.format.DateFormat;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.FirebaseDatabase;
import com.testprojectinterview.app.testprojectinterview.LoginCustomActivity;
import com.testprojectinterview.app.testprojectinterview.Mapping.ChatMessage;
import com.testprojectinterview.app.testprojectinterview.R;
import com.testprojectinterview.app.testprojectinterview.TabActivityMenu;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

/**
 * Created by skyshi on 12/01/17.
 */

public class ChatFragment  extends Fragment implements View.OnClickListener{
    ImageView imageSend,imageGetLocation;
    EditText inputMessage;
    ListView listMessage;
    private static int PLACE_PICKER_REQUEST = 1;
    private FirebaseAuth mAuth;
    private String username;
    private FirebaseListAdapter<ChatMessage> adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chat,container,false);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.i("AuthStateChanged", "User is signed in with uid: " + user.getUid()+" || "+user.getEmail());
                    if(user.getEmail().isEmpty()||user.getEmail()==null){
                        username = "Anonymous";
                    }else {
                        username = user.getEmail();
                    }
                } else {
                    Log.i("AuthStateChanged", "No user is signed in.");
                }
            }
        });
        imageSend = (ImageView)view.findViewById(R.id.image_send_chat);
        imageSend.setOnClickListener(this);
        imageGetLocation = (ImageView)view.findViewById(R.id.image_location);
        imageGetLocation.setOnClickListener(this);
        inputMessage = (EditText)view.findViewById(R.id.inputMessage);

        adapter = new FirebaseListAdapter<ChatMessage>(getActivity(), ChatMessage.class,
                R.layout.message_list, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);
                // Set their text
                String text = model.getMessage();
                if(model.getMessage().contains("lat/lng")){
                    text = text.substring(text.indexOf("(")+1);
                    text = "http://maps.google.com/?daddr="+text.substring(0,text.indexOf(")"));
                }
                messageText.setText(text);
                Linkify.addLinks(messageText, Linkify.WEB_URLS);
                messageUser.setText(model.getUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy",
                        Long.parseLong(model.getTime())));
            }
        };
        listMessage = (ListView)view.findViewById(R.id.list_of_messages);
        listMessage.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.image_send_chat){
            FirebaseDatabase.getInstance()
                    .getReference()
                    .push()
                    .setValue(new ChatMessage(inputMessage.getText().toString(),
                            username));

            inputMessage.setText("");
        }else if(id == R.id.image_location){
            Log.d("Picker","clicked");
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            try {
                startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getActivity());
                String toastMsg = String.format(place.getLatLng().toString());
                //String latlng = String.format(place.getLatLng())
                Log.d("Picker",toastMsg);
                if(toastMsg.contains("lat/lng")){
                    toastMsg = toastMsg.substring(toastMsg.indexOf("(")+1);
                    toastMsg= "http://maps.google.com/?daddr="+toastMsg.substring(0,toastMsg.indexOf(")"));
                }
                inputMessage.setText(toastMsg);
                //Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
            mAuth.signOut();
            Intent a = new Intent(getActivity(),LoginCustomActivity.class);
            startActivity(a);
    }
}
