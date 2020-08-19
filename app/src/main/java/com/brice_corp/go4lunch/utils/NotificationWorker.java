package com.brice_corp.go4lunch.utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.brice_corp.go4lunch.di.MyApplication;
import com.brice_corp.go4lunch.repository.FirestoreUserRepository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Created by <NIATEL Brice> on <31/07/2020>.
 */
public class NotificationWorker extends Worker {
    private static final String TAG = "NotificationWorker";
    private static final String RNAME = "restaurant_name";
    private static final String RID = "restaurant_id";
    private static final String RADDRESS = "restaurant_address";

    private FirestoreUserRepository mFirestoreUserRepository;

    private NotificationUtils notificationUtils;
    private String mName;
    private String mAddress;
    private String mId;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        notificationUtils = new NotificationUtils(context);

        mFirestoreUserRepository = ((MyApplication) context).getContainerDependencies().getFirestoreUserRepository();

        mName = getInputData().getString(RNAME);
        mAddress = getInputData().getString(RADDRESS);
        mId = getInputData().getString(RID);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(TAG, "doWork: start");
        try {
            getWormates();
        } catch (Exception e) {
            Log.e(TAG, "doWork: error", e);
            return Result.retry();
        }
        return Result.success();
    }

    //Get all the workmates who eat in your restaurant today and build the notification
    private void getWormates() {
        mFirestoreUserRepository.getUsersDocuments().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<String> workmates = new ArrayList<>();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    if (document != null) {
                        if (document.get("eatToday") != null) {
                            if (Objects.equals(document.get("eatToday"), mId)) {
                                if (!Objects.equals(document.get("name"), mFirestoreUserRepository.getUser().getName()))
                                    workmates.add(String.valueOf(document.get("name")));
                            }
                        } else {
                            Log.d(TAG, "onSuccess: document.get(\"eatToday\") = null");
                        }
                    } else {
                        Log.d(TAG, "onSuccess: document = null");
                    }
                }
                //Build notification
                Log.i(TAG, "onSuccess: notification : " + "Restaurant name : " + mName + "Address : " + mAddress + "List workmates : " + workmates);
                notificationUtils.sendNotification(mName, mAddress, workmates);
            }
        });
    }
}