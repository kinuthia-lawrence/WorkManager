package com.larrykin343.workmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.BaseMenuPresenter;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //you have all functionality of job scheduler by using work manager

        Data data = new Data.Builder()
                .putInt("number", 10)
                .build();
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(true)
                .build();
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(SampleWorker.class)
                .setInputData(data)
//                .setConstraints(constraints)
//                .setInitialDelay(5, TimeUnit.HOURS)
                .addTag("download")
                .build();
        WorkManager.getInstance(this).enqueue(request);
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(SampleWorker.class, 7, TimeUnit.DAYS)
                .setInputData(data)
                .setConstraints(constraints)
                .addTag("periodic")
                .setInitialDelay(10, TimeUnit.HOURS)
                .build();
//        WorkManager.getInstance(this).enqueue(periodicWorkRequest);
/*
        WorkManager.getInstance(this).getWorkInfosByTagLiveData("download").observe(this,
                new Observer<List<WorkInfo>>() {
                    @Override
                    public void onChanged(List<WorkInfo> workInfos) {
                        for (WorkInfo workInfo : workInfos) {
                            Log.d(TAG, "onChanged: WorkInfo.State.SUCCEEDED");
                        }
                    }
                });*/
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(request.getId()).observe(this,
                new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        Log.d(TAG, "onChanged: Work status" + workInfo.getState());
                    }
                });
        WorkManager.getInstance(this).cancelWorkById(request.getId());

        WorkManager.getInstance(this).beginWith(request)
                .then(request)
                .then(request)
                .enqueue();
    }
}