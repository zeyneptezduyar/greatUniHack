package matei.mple.com.oknow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewActivity extends AppCompatActivity {

    private EditText txt;
    private Button btn1;
    private int count;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserData;
    private DatabaseReference mPostData;
    private DatabaseReference mCategorii;
    private Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        txt=(EditText)findViewById(R.id.addRest2);
        btn1=(Button)findViewById(R.id.addBtnRes2);
        mPostData = FirebaseDatabase.getInstance().getReference().child("time");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = txt.getText().toString();


                DatabaseReference push = mPostData.push();

                long unixTime = System.currentTimeMillis();
                long round =unixTime- (unixTime%1800);
                calendar=Calendar.getInstance();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat formatter2 = new SimpleDateFormat("HH");
                String f1=String.valueOf(formatter.format(calendar.getTime()));
                String f2 =String.valueOf(formatter2.format(calendar.getTime()));

                final String push_id = push.getKey();
                count=0;
                final Map map = new HashMap();
                map.put("name", name);
                map.put("count",count);
                map.put("revCount",0-count);
                map.put("restaurantID",push_id);
                mPostData.child(f1).child(f2).child(push_id).updateChildren(map, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                        if (databaseError == null) {
                            Toast.makeText(NewActivity.this, "restaurant added", Toast.LENGTH_LONG).show();
                            Intent mainIntent = new Intent(NewActivity.this, MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);

                        } else if (databaseError != null) {
                            Toast.makeText(NewActivity.this, databaseError.toString(), Toast.LENGTH_LONG).show();
                        }


                    }
                });



            }


        });



    }
}
