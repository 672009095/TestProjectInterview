package com.testprojectinterview.app.testprojectinterview.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.FirebaseDatabase;
import com.testprojectinterview.app.testprojectinterview.Mapping.ChatMessage;
import com.testprojectinterview.app.testprojectinterview.R;

/**
 * Created by skyshi on 12/01/17.
 */

public class ChatFragment  extends Fragment implements View.OnClickListener{
    ImageView imageSend,imageGetLocation;
    EditText inputMessage;
    ListView listMessage;
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
        inputMessage = (EditText)view.findViewById(R.id.inputMessage);

        adapter = new FirebaseListAdapter<ChatMessage>(getActivity(), ChatMessage.class,
                R.layout.message_list, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);
                // Set their text
                messageText.setText(model.getMessage());
                messageUser.setText(model.getUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
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
        }
    }

}
