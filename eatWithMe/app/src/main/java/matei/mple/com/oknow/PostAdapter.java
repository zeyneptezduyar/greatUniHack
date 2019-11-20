package matei.mple.com.oknow;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Matei on 11/30/2017.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Postache> mPostList;

    Context ctx;

    public PostAdapter(List<Postache> mPostList, Context ctx) {
        this.mPostList = mPostList;
        this.ctx = ctx;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gluma_layout, parent, false);
        return new PostViewHolder(v, ctx);
    }


    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public TextView count;
        public DatabaseReference mFavData;
        public FirebaseAuth mAuth;
        public String current_user;
        public String idd;
        public String nname;
        public DatabaseReference mPost;
        public int ccount;
        public DatabaseReference root;
        public Calendar calendar;
        public String f1,f2;
        Context ctx;

        public PostViewHolder(View itemView, Context ctx) {
            super(itemView);
            this.ctx = ctx;
            itemView.setOnClickListener(this);
            name = (TextView) itemView.findViewById(R.id.name_item22);
            count = (TextView) itemView.findViewById(R.id.count_item22);
            calendar= Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat formatter2 = new SimpleDateFormat("HH");
            f1=String.valueOf(formatter.format(calendar.getTime()));
            f2 =String.valueOf(formatter2.format(calendar.getTime()));



        }


        @Override
        public void onClick(View view) {
            final Map map = new HashMap();
            map.put("name", nname);
            map.put("id", idd);

            if (current_user != null) {
                root.child("time").child(f1).child(f2).child(String.valueOf(idd)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final int a = dataSnapshot.child("count").getValue(Integer.class) + 1;
                        root.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                if (!dataSnapshot1.child("added").child(f1).child(f2).child(current_user).hasChild(String.valueOf(idd))) {
                                    mPost.child("count").setValue(a);
                                    mPost.child(("revCount")).setValue(0 - a);
                                    mFavData.child(current_user).child(String.valueOf(idd)).updateChildren(map, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(final DatabaseError databaseError, DatabaseReference databaseReference) {
                                            Toast.makeText(ctx,"Good",Toast.LENGTH_LONG);



                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Intent i = new Intent(ctx, ChatActivity.class);
                String strName = idd;
                i.putExtra("STRING_I_NEED", strName);
                ctx.startActivity(i);

            }
        }
    }


    @Override
    public void onBindViewHolder(PostAdapter.PostViewHolder holder, int position) {

        Postache c = mPostList.get(position);
        holder.nname=c.getName();
        holder.ccount=c.getCount();
        holder.name.setText(mPostList.get(position).getName());
        holder.count.setText(String.valueOf(mPostList.get(position).getCount()));
        holder.mFavData=FirebaseDatabase.getInstance().getReference().child("added").child(holder.f1).child(holder.f2);
        holder.mAuth=FirebaseAuth.getInstance();
        holder.root=FirebaseDatabase.getInstance().getReference();
        if(holder.mAuth!= null) {
            holder.current_user = holder.mAuth.getCurrentUser().getUid().toString();
        }
        holder.idd=c.getRestaurantID();
       // holder.mPost=FirebaseDatabase.getInstance().getReference().child("restaurants").child(String.valueOf(c.getRestaurantID()));
        holder.mPost=FirebaseDatabase.getInstance().getReference().child("time").child(holder.f1).child(holder.f2).child(holder.idd);

    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }
}

