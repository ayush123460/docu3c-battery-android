package com.example.docu3c_battery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;
import android.os.BatteryManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.content.IntentFilter;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    TextView t;
    Button b;
    IntentFilter i;
    DatabaseReference mdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context ctx = getApplicationContext();
        t = findViewById(R.id.t);
        i = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        b = findViewById(R.id.b);
        mdb = FirebaseDatabase.getInstance().getReference();

//        WorkManager.getInstance(ctx).cancelAllWork();
//        WorkManager.getInstance(ctx).pruneWork();

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constraints c = new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build();
                PeriodicWorkRequest req = new PeriodicWorkRequest.Builder(UploadWorker.class, 1, TimeUnit.HOURS, 5, TimeUnit.MINUTES)
                    .addTag("uw")
                    .setConstraints(c)
                    .build();
                WorkManager.getInstance(ctx).enqueueUniquePeriodicWork("uw", ExistingPeriodicWorkPolicy.REPLACE, req);
            }
        });
    }
}