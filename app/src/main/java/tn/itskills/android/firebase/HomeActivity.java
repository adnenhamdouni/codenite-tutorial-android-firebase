package tn.itskills.android.firebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "HomeActivity";

    FirebaseDatabase mDatabase;
    private DatabaseReference mMyRef;

    private EditText mSendMessageField;
    private TextView mReadMessageField;
    private Button mSendMessageButton;
    private Button mReadMessageButton;
    private Button mSignOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
        initDatabase();
    }



    private void initView() {

        // Views
        mSendMessageField = (EditText) findViewById(R.id.field_send_message);
        mReadMessageField = (TextView) findViewById(R.id.field_read_message);
        mSendMessageButton = (Button) findViewById(R.id.button_send_message);
        mReadMessageButton = (Button) findViewById(R.id.button_read_message);
        mSignOutButton = (Button) findViewById(R.id.button_sign_out);

        // Click listeners
        mSendMessageButton.setOnClickListener(this);
        mReadMessageButton.setOnClickListener(this);
        mSignOutButton.setOnClickListener(this);
    }


    private void initDatabase() {

        mDatabase = FirebaseDatabase.getInstance();
        mMyRef = mDatabase.getReference("message");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_send_message:
                sendMessage();
                break;
            case R.id.button_read_message:
                readMessage();
                break;
            case R.id.button_sign_out:
                signOut();
                break;
        }
    }

    private void sendMessage() {
        // Write a message to the database

        mMyRef.setValue(mSendMessageField.getText().toString());
    }

    private void readMessage() {
        // Read from the database
        mMyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                mReadMessageField.setText("Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
                mReadMessageField.setText("Failed to read value.");
            }
        });
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, MainActivity.class));
    }
}
