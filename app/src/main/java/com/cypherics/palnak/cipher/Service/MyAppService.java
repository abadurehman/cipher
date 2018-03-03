package com.cypherics.palnak.cipher.Service;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.KeyguardManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;

import com.cypherics.palnak.cipher.LoginActivty;
import com.cypherics.palnak.cipher.MainActivity;
import com.cypherics.palnak.cipher.SharedPreference.SharedPreference;
import com.cypherics.palnak.cipher.UserAppLogin;

import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import static android.content.ContentValues.TAG;

public class MyAppService extends Service {
    private Context context;
    private SharedPreference sharedPreference=new SharedPreference();
    List<String> lockedApp;
    private String appName;
    private String mpackageName;
    public static String runningApp = "test1";

    public static  String previousApp = "test";
    public String myAppName = "cipher";
    private Timer timer;
    private Thread dThread;



    public MyAppService() {
    }
    @Override
    public void onCreate() {

        context = this;
        lockedApp = sharedPreference.getApp(getApplicationContext());
        timer = new Timer("AppCheckServices");
        timer.schedule(updateTask, 1L, 1L);


    }

    private TimerTask updateTask = new TimerTask() {
        @Override
        public void run() {

            if (isConcernedAppIsInForeground()){
                Log.e("con","true");

                if (!runningApp.matches(previousApp)){
                    previousApp = runningApp;
                    Log.e("on_the_way","activity_launch");
                    try {
                        Thread.sleep(1000);

                    }catch (InterruptedException exception){
                        exception.printStackTrace();

                    }
                    Intent intent = new Intent(getApplicationContext(), UserAppLogin.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("package", mpackageName);


                    startActivity(intent);


                }
            }else{
                if (!appName.equals(myAppName)){
                    Log.e("Sett",previousApp);
                    previousApp = "";
                }

            }

        }
    };
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }





    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onTaskRemoved(intent);
//        AppListner();

        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent intent = new Intent(getApplicationContext(),this.getClass());
        intent.setPackage(getPackageName());

        startService(intent);
        super.onTaskRemoved(rootIntent);
    }





    public boolean isConcernedAppIsInForeground() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            UsageStatsManager usage = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> stats = usage.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
            if (stats != null) {
                SortedMap<Long, UsageStats> runningTask = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : stats) {
                    runningTask.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (runningTask.isEmpty()) {
                    Log.d(TAG,"isEmpty Yes");
                    mpackageName = "";
                    appName = "";
                }else {
                    mpackageName = runningTask.get(runningTask.lastKey()).getPackageName();
                    try {
                        appName = (String) getPackageManager().
                                getApplicationLabel(getPackageManager().
                                        getApplicationInfo(mpackageName, PackageManager.GET_META_DATA));

                    }catch (PackageManager.NameNotFoundException exception){
                        exception.printStackTrace();

                    }


                    Log.e(TAG,"isEmpty No : "+appName);
                }
            }


            for (int i = 0; lockedApp != null && i < lockedApp.size(); i++) {
                if (appName.equals(lockedApp.get(i))) {
                    runningApp = lockedApp.get(i);
                    return true;
                }
            }

        return false;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer = null;

    }



}
