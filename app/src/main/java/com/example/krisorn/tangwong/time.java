package com.example.krisorn.tangwong;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.lang.Integer.parseInt;

public class time extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String id="1";
    public DatabaseReference nameCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_time);

    }

    public void click(View view) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        nameCard =database.getReference();

        nameCard.addValueEventListener (new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.child ("room").child (id).child ("time_noti").child ("status") .getValue (String.class).equals ("1"))
            {
                long num = dataSnapshot.child ("room").child (id).child ("people_live").getChildrenCount ();
                for(long i=0;i<num;i++)
                {
                    if(dataSnapshot.child ("room").child (id).child("people_live").child (Long.toString (i)).child ("uid").getValue (String.class)!=null)
                    {
                        String tempUid =  dataSnapshot.child ("room").child (id).child("people_live").child (Long.toString (i)).child ("uid").getValue (String.class);
                        String text = dataSnapshot.child ("room").child (id).child ("time_noti").child ("text") .getValue (String.class);
                        mDatabase.child ("user").child (tempUid).child ("time").child ("status") .setValue ("1");
                        mDatabase.child ("user").child (tempUid).child ("time").child ("text") .setValue (text);
                    }

                }
                mDatabase.child ("room").child (id).child ("time_noti").child ("status") .setValue ("0");
            }







            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        if(view.getId()==R.id.set){
            EditText hr = findViewById(R.id.hr);
            EditText mi = findViewById(R.id.min);
            EditText se = findViewById(R.id.sec);


            long a = Integer.parseInt (hr.getText ().toString ());
             a=a*3600;

            long b = Integer.parseInt (mi.getText ().toString ());
            b=b*60;

            long c = Integer.parseInt (se.getText ().toString ());

            int asd = Integer.parseInt(String.valueOf (a+b+c))*1000;
            new CountDownTimer (asd, 1000) {
                EditText text = findViewById(R.id.text);

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {

                    mDatabase.child ("room").child (id).child ("time_noti").child ("status") .setValue ("1");
                    mDatabase.child ("room").child (id).child ("time_noti").child ("text") .setValue (text.getText ().toString ());

                }
            }.start();

            Intent i = new Intent(this,UsersActivity.class);
            startActivity(i);




        }
    }

    private void showNotification(String text) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://devahoy.com/posts/android-notification/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("การเเจ้งเตือน")
                        .setContentText(text)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setDefaults(Notification.DEFAULT_SOUND)

                        .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1000, notification);


    }

}
