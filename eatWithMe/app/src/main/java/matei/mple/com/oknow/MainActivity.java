package matei.mple.com.oknow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FloatingActionButton btn1;
    private final List<Postache> postaches= new ArrayList<>();
    private LinearLayoutManager mLinearLayout;
    private PostAdapter mAdapter;
    private RecyclerView postList;
    private DatabaseReference postData;
    private Handler mHandler = new Handler();
    private FloatingActionButton addButt;
    private FloatingActionButton newBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        postList=(RecyclerView)findViewById(R.id.post_list);
        newBtn=(FloatingActionButton)findViewById(R.id.btn41);
        mLinearLayout = new LinearLayoutManager(MainActivity.this);
        mAdapter = new PostAdapter(postaches,MainActivity.this);
        addButt=(FloatingActionButton)findViewById(R.id.btn31);
        addButt.setEnabled(false);
        mAuth =FirebaseAuth.getInstance();
        postList.setHasFixedSize(true);
        postList.setLayoutManager(mLinearLayout);
        postList.setAdapter(mAdapter);
        long unixTime = System.currentTimeMillis();
        long round =unixTime- (unixTime%3600);
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat formatter2 = new SimpleDateFormat("HH");
        String f1=String.valueOf(formatter.format(calendar.getTime()));
        String f2 =String.valueOf(formatter2.format(calendar.getTime()));
        postData= FirebaseDatabase.getInstance().getReference().child("time").child(f1).child(f2);
        btn1=(FloatingActionButton)findViewById(R.id.btn21) ;
        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIn= new Intent(MainActivity.this,NewActivity.class);
                startActivity(newIn);
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent= new Intent(MainActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Intent loginIntent = new Intent(MainActivity.this,StartActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }
            }
        };
        addButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });



    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        postData.orderByChild("revCount").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Postache postache = dataSnapshot.getValue(Postache.class);
                postaches.add(postache);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        postaches.clear();
    }
}
