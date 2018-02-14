package com.cypherics.palnak.cipher;

import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cypherics.palnak.cipher.Helper.FingerprintUiHelper;
import com.cypherics.palnak.cipher.Service.MyAppService;
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
        intent = getPackageManager().getLaunchIntentForPackage(packageName);

        startActivity(intent);
        finish();


    }

    @Override
    public void onError() {

    }
}
