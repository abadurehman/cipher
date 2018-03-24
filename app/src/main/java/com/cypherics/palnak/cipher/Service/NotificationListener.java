package com.cypherics.palnak.cipher.Service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.cypherics.palnak.cipher.SharedPreference.SharedPreference;
import com.cypherics.palnak.cipher.UserAppLogin;

import java.util.List;

import static com.cypherics.palnak.cipher.Service.MyAppService.runningApp;

/**
 * Created by palnak on 24-3-18.
 */

public class NotificationListener extends NotificationListenerService {

    /*
        These are the package names of the apps. for which we want to
        listen the notifications
     */
    private SharedPreference sharedPreference=new SharedPreference();
    private String appName;
    List<String> lockedApp;



    /*
        These are the return codes we use in the method which intercepts
        the notifications, to decide whether we should do something or not
     */
    public static final class InterceptedNotificationCode {
        public static final int LOCKED_APP_CODE = 1;
        public static final int OTHER_NOTIFICATIONS_CODE = 2; // We ignore all notification with code == 4
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        int notificationCode = matchNotificationCode(sbn);


            Intent intent = new Intent("com.github.chagall.notificationlistenerexample");
            intent.putExtra("Notification Code", notificationCode);
            sendBroadcast(intent);

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn){


    }

    private int matchNotificationCode(StatusBarNotification sbn) {

        lockedApp = sharedPreference.getApp(getApplicationContext());
        try {
            appName = (String) getPackageManager().
                    getApplicationLabel(getPackageManager().
                            getApplicationInfo(sbn.getPackageName(), PackageManager.GET_META_DATA));
        }catch (PackageManager.NameNotFoundException exception){
            exception.printStackTrace();

        }
        for (int i = 0; lockedApp != null && i < lockedApp.size(); i++) {
            if (appName.equals(lockedApp.get(i))) {
                runningApp = lockedApp.get(i);
                Log.e("Notification",runningApp);
                return(InterceptedNotificationCode.LOCKED_APP_CODE);
            }
        }
        return(InterceptedNotificationCode.OTHER_NOTIFICATIONS_CODE);


    }
}
