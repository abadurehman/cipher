package com.cypherics.palnak.cipher.Receiver;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DeviceAdminService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by palnak on 8-3-18.
 */

public class SampleDeviceAdminReceiver extends DeviceAdminReceiver {


    @Override
    public void onDisabled(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Toast.makeText(context, "disabled dpm", Toast.LENGTH_SHORT).show();
        super.onDisabled(context, intent);
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Toast.makeText(context, "enabled dpm", Toast.LENGTH_SHORT).show();
        super.onEnabled(context, intent);
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Toast.makeText(context, "disable dpm request", Toast.LENGTH_SHORT).show();
        return super.onDisableRequested(context, intent);
    }
}
