package com.example.kurt.ugood.firebase;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
    }
}
