package com.cypherics.palnak.cipher.Helper;

import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.widget.ImageView;
import android.widget.TextView;

import com.cypherics.palnak.cipher.R;


/**
 * Created by palnak on 4-2-18.
 */

public class FingerprintUiHelper extends FingerprintManager.AuthenticationCallback {
    private static final long ERROR_TIMEOUT_MILLIS = 1600;
    private static final long SUCCESS_DELAY_MILLIS = 1300;

    private final FingerprintManager mFingerprintManager;
    private final TextView mErrorTextView;
    private final Callback mCallback;
    private CancellationSignal mCancellationSignal;

    private boolean mSelfCancelled;

    public FingerprintUiHelper(FingerprintManager fingerprintManager, TextView errorTextView, Callback callback) {
        mFingerprintManager = fingerprintManager;
        mErrorTextView = errorTextView;
        mCallback = callback;
    }

    public boolean isFingerprintAuthAvailable() {
        // The line below prevents the false positive inspection from Android Studio
        // noinspection ResourceType
        return mFingerprintManager.isHardwareDetected()
                && mFingerprintManager.hasEnrolledFingerprints();
    }



    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        if (!mSelfCancelled) {
            showError(errString);
//            mIcon.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mCallback.onError();
//                }
//            }, ERROR_TIMEOUT_MILLIS);
        }    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        mErrorTextView.setText(helpString);
        mErrorTextView.setTextColor(
                mErrorTextView.getResources().getColor(R.color.errorText, null));
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        mErrorTextView.removeCallbacks(mResetErrorTextRunnable);
//        mIcon.setImageResource(R.drawable.ic_fingerprint_success);
        mErrorTextView.setTextColor(
                mErrorTextView.getResources().getColor(R.color.colorPrimary1, null));
        mErrorTextView.setText(
                mErrorTextView.getResources().getString(R.string.fingerprint_success));

        mCallback.onAuthenticated();
//        mIcon.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mCallback.onAuthenticated();
//            }
//        }, SUCCESS_DELAY_MILLIS);
    }

    @Override
    public void onAuthenticationFailed() {
        mErrorTextView.setTextColor(
                mErrorTextView.getResources().getColor(R.color.errorText, null));
        mErrorTextView.setText(
                mErrorTextView.getResources().getString(R.string.fingerprint_not_recognized));
    }

    public void startListening(FingerprintManager.CryptoObject cryptoObject) {
        if (!isFingerprintAuthAvailable()) {
            return;
        }
        mCancellationSignal = new CancellationSignal();
        mSelfCancelled = false;
        // The line below prevents the false positive inspection from Android Studio
        // noinspection ResourceType
        mFingerprintManager
                .authenticate(cryptoObject, mCancellationSignal, 0 /* flags */, this, null);
    }

    public void stopListening() {
        if (mCancellationSignal != null) {
            mSelfCancelled = true;
            mCancellationSignal.cancel();
            mCancellationSignal = null;
        }
    }

    public interface Callback {

        void onAuthenticated();

        void onError();
    }

    private void showError(CharSequence error) {
        mErrorTextView.setText(error);
        mErrorTextView.setTextColor(
                mErrorTextView.getResources().getColor(R.color.errorText, null));
        mErrorTextView.removeCallbacks(mResetErrorTextRunnable);
        mErrorTextView.postDelayed(mResetErrorTextRunnable, ERROR_TIMEOUT_MILLIS);
    }

    private Runnable mResetErrorTextRunnable = new Runnable() {
        @Override
        public void run() {
            mErrorTextView.setTextColor(
                    mErrorTextView.getResources().getColor(R.color.errorText, null));
            mErrorTextView.setText(
                    mErrorTextView.getResources().getString(R.string.fingerprint_hint));
        }
    };
}
