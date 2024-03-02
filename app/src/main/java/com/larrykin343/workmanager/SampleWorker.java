package com.larrykin343.workmanager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SampleWorker extends Worker {
    private static final String TAG = "SampleWorker";
    public SampleWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data inputData = getInputData();
        int number = inputData.getInt("number", -1);
        Log.d(TAG, "doWork: number = " + number);

        for (int i = number; i < 10; i++) {
            Log.d(TAG, "doWork: i = " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return Result.failure();
            }
        }

//        the parent output data will be the child input data
        Data outputData = new Data.Builder()
                .putInt("number", 15)
                .build();
        return Result.success(outputData);
    }
}