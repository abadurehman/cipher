package com.cypherics.palnak.cipher;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.widget.ImageView;
import android.widget.TextView;

import com.cypherics.palnak.cipher.Helper.FingerprintUiHelper;
import com.cypherics.palnak.cipher.SharedPreference.SharedPreference;


public class UserAppLogin extends AppCompatActivity implements FingerprintUiHelper.Callback {
    private FingerprintManager.CryptoObject mCryptoObject;
    private FingerprintUiHelper mFingerprintUiHelper;
    private ImageView imageView;
    private TextView textView;
    private Intent intent;
    private String packageName;
    private String runningApp;
    private SharedPreference sharedPreference=new SharedPreference();


    @Override
    public void onBackPressed() {

        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        try {

            am.killBackgroundProcesses(packageName);

        }catch (NullPointerException exception){
            exception.printStackTrace();
        }

        Log.e("Killing",packageName);
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
        startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        finish();
//        mActivityManager.killBackgroundProcesses(packageName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_app_login);
        imageView = findViewById(R.id.fingerprint_icon);
        textView = findViewById(R.id.fingerprinterror);
        packageName = getIntent().getStringExtra("package");



        mFingerprintUiHelper = new FingerprintUiHelper(
                getBaseContext().getSystemService(FingerprintManager.class),
                textView, this);
        mFingerprintUiHelper.startListening(mCryptoObject);


    }

    @Override
    public void onAuthenticated() {
        Log.e("userapplogin",packageName);

        intent = getPackageManager().getLaunchIntentForPackage(packageName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();


    }

    @Override
    public void onError() {

    }
}
