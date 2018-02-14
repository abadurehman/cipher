package com.cypherics.palnak.cipher.Service;

import android.app.AppOpsManager;
import android.app.KeyguardManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.cypherics.palnak.cipher.LoginActivty;
import com.cypherics.palnak.cipher.SharedPreference.SharedPreference;
import com.cypherics.palnak.cipher.UserAppLogin;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static android.content.ContentValues.TAG;

public class MyAppService extends Service {
    private Context context;
    private SharedPreference sharedPreference=new SharedPreference();
    List<String> lockedApp;
    private String appName;
    private String topPackageName;
    public static String runningApp = "";
    public static  String previousApp = "test";
    public String myAppName = "cipher";





    public MyAppService() {
    }
    @Override
    public void onCreate() {

        context = this;


    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onTaskRemoved(intent);
        AppListner();

        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent intent = new Intent(getApplicationContext(),this.getClass());
        intent.setPackage(getPackageName());

        startService(intent);
        super.onTaskRemoved(rootIntent);
    }

    public void AppListner(){
        KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if( myKM.inKeyguardRestrictedInputMode()) {
            stopSelf();
        } else{
            lockedApp = sharedPreference.getApp(getApplicationContext());
            UsageStatsManager mUsageStatsManager = (UsageStatsManager)getSystemService(context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000*10, time);


            if(stats != null) {
                SortedMap<Long,UsageStats> mySortedMap = new TreeMap<Long,UsageStats>();
                for (UsageStats usageStats : stats) {
                    mySortedMap.put(usageStats.getLastTimeUsed(),usageStats);
                }
                if(!mySortedMap.isEmpty()) {
                    topPackageName =  mySortedMap.get(mySortedMap.lastKey()).getPackageName();

                    try {
                        appName = (String) getPackageManager().
                                getApplicationLabel(getPackageManager().
                                        getApplicationInfo(topPackageName, PackageManager.GET_META_DATA));
                        runningApp = appName;

                        for (String lockedAppUser : lockedApp) {

                            if (lockedAppUser.equals(appName) && !runningApp.equals(previousApp) ) {
                                if (Build.VERSION.SDK_INT >= 23 ) {



                                    Intent intent = new Intent(this, UserAppLogin.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("package", topPackageName);


                                        startActivity(intent);
                                        previousApp = runningApp;
                                }


                            }else {


                                if (!appName.matches(myAppName) && !previousApp.equals(runningApp)){

                                    previousApp = "test";

                                }
                            }
                        }

                    }catch (PackageManager.NameNotFoundException exception){
                        exception.printStackTrace();
                    }



                }
            }


        }
    }








}
