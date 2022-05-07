package com.example.docu3c_battery;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UploadWorker extends Worker {
    public UploadWorker(Context ctx, WorkerParameters wParams) {
        super(ctx, wParams);
    }

    double batTemp;
    Date tm;
    DatabaseReference mdb;

    @Override
    public Result doWork() {
        Context ctx = getApplicationContext();
        mdb = FirebaseDatabase.getInstance().getReference();
        Intent battery = ctx.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        batTemp = (double) battery.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10;
        tm = Calendar.getInstance().getTime();
        OkHttpClient ok = new OkHttpClient();
        Request req = new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/weather?lat=23.0747192&lon=76.8575018&appid=5573f132b7ae785cfb94917005ab3085")
                .build();

        try {
            Response res = ok.newCall(req).execute();
            JSONObject weather = new JSONObject(res.body().string()).getJSONObject("main");
            double localTemp = (double) weather.get("temp") - 273.15;
            Map<String, Double> temps = new HashMap<String, Double>();
            temps.put("local", localTemp);
            temps.put("bat", batTemp);
            mdb.child(tm.toString()).setValue(temps);
        } catch (IOException | JSONException e) {
            Log.e("uw", e.toString());
        }

        return Result.success();
    }
}