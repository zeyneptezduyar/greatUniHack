package matei.mple.com.oknow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {
    private EditText txt;
    private Button btn;
    private int count;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserData;
    private DatabaseReference mPostData;
    private DatabaseReference mCategorii;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        txt=(EditText)findViewById(R.id.addRest);
        btn=(Button)findViewById(R.id.addBtnRes);
        mPostData =FirebaseDatabase.getInstance().getReference().child("restaurants");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = txt.getText().toString();


                        DatabaseReference push = mPostData.push();

                        long unixTime = System.currentTimeMillis();
                        final String push_id = push.getKey();
                        count=0;
                        final Map map = new HashMap();
                        map.put("name", name);
                        map.put("count",count);
                        map.put("revCount",0-count);
                        map.put("restaurantID",push_id);
                        map.put("date",unixTime);
                        mPostData.child(push_id).updateChildren(map, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                if (databaseError == null) {
                                    Toast.makeText(AddActivity.this, "restaurant added", Toast.LENGTH_LONG).show();
                                    Intent mainIntent = new Intent(AddActivity.this, MainActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(mainIntent);

                                } else if (databaseError != null) {
                                    Toast.makeText(AddActivity.this, databaseError.toString(), Toast.LENGTH_LONG).show();
                                }


                            }
                        });



                }


        });

    }
}
