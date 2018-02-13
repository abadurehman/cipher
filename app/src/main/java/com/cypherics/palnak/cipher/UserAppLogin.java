package com.cypherics.palnak.cipher;

import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cypherics.palnak.cipher.Helper.FingerprintUiHelper;
import com.cypherics.palnak.cipher.Service.MyAppService;

public class UserAppLogin extends AppCompatActivity implements FingerprintUiHelper.Callback {
    private FingerprintManager.CryptoObject mCryptoObject;
    private FingerprintUiHelper mFingerprintUiHelper;
    private ImageView imageView;
    private TextView textView;
    private Intent intent;
    private String packageName;
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
//        getApplicationContext().sendBroadcast(new Intent("finish"));
        intent = getPackageManager().getLaunchIntentForPackage(packageName);

        startActivity(intent);
        finish();
//        stopService(new Intent(UserAppLogin.this, MyAppService.class));
        mFingerprintUiHelper.stopListening();

    }

    @Override
    public void onError() {

    }
}
