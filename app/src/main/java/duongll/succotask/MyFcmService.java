package duongll.succotask;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFcmService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String refreshedToken) {
        Log.d("TAG", "Refreshed token: " + refreshedToken);
    }
}
