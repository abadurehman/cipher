package com.cypherics.palnak.cipher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.cypherics.palnak.cipher.Helper.FingerprintUiHelper;

public class LoginActivty extends AppCompatActivity implements  FingerprintUiHelper.Callback{
    private FingerprintManager.CryptoObject mCryptoObject;
    private FingerprintUiHelper mFingerprintUiHelper;

    private InputMethodManager mInputMethodManager;
    private SharedPreferences mSharedPreferences;
    private ImageView imageView;
    private TextView textView;
    private LoginActivty mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activty);
        imageView = findViewById(R.id.fingerprint_icon);
        textView = findViewById(R.id.fingerprinterror);
        mFingerprintUiHelper = new FingerprintUiHelper(
                getBaseContext().getSystemService(FingerprintManager.class),
                 textView, this);



    }

    @Override
    public void onResume() {
        super.onResume();
        mFingerprintUiHelper.startListening(mCryptoObject);

    }


    @Override
    public void onPause() {
        super.onPause();
        mFingerprintUiHelper.stopListening();
    }

    @Override
    public void onAuthenticated() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();


    }

    @Override
    public void onError() {

    }


}
