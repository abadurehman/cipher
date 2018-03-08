package com.cypherics.palnak.cipher;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cypherics.palnak.cipher.Adapter.AddCartListAdapter;
import com.cypherics.palnak.cipher.Helper.RecyclerItemTouchHelper;
import com.cypherics.palnak.cipher.Listner.RecyclerTouchListener;
import com.cypherics.palnak.cipher.Manager.PolicyManager;
import com.cypherics.palnak.cipher.Service.MyAppService;
import com.cypherics.palnak.cipher.SharedPreference.SharedPreference;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public RecyclerView recyclerView;
    public CoordinatorLayout coordinatorLayout;
    private List<AvalaibleApps> cartList;
    private AddCartListAdapter mAdapter;
    private List<ApplicationInfo> installedPackages;
    private SharedPreference sharedPreference =new SharedPreference();
    private PolicyManager policyManager;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        policyManager = new PolicyManager(this);



        recyclerView = findViewById(R.id.recycle_view);
        coordinatorLayout = findViewById(R.id.coordinator_layout);

        cartList = new ArrayList<>();
        mAdapter = new AddCartListAdapter(this, cartList);

        if (!policyManager.isAdminActive()) {
            Intent activateDeviceAdmin = new Intent(
                    DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            activateDeviceAdmin.putExtra(
                    DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                    policyManager.getAdminComponent());
            activateDeviceAdmin
                    .putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                            "After activating admin, you will be able to block application uninstallation.");
            startActivityForResult(activateDeviceAdmin,
                    PolicyManager.DPM_ACTIVATION_REQUEST_CODE);
        }


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.addItemDecoration(new com.cypherics.palnak.cipher.DividerItemDecoration(getApplicationContext()));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                AvalaibleApps cartListAdapter = cartList.get(position);
                Toast.makeText(getApplicationContext(), cartListAdapter.getName() + " is selected!", Toast.LENGTH_SHORT).show();
                String name = cartListAdapter.getName() ;
                Log.e("Adding-",name);
                sharedPreference.addApp(getApplicationContext(),name);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, new RecyclerItemTouchHelper.RecyclerItemTouchHelperListener() {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

            }
        });
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        prepareCart();

        if(!isAccessGranted()){
            Log.e(TAG, "Permission denied");
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }

    }

    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (NullPointerException| PackageManager.NameNotFoundException e
                ) {
            return false;
        }
    }

    private List<ApplicationInfo> getInstalledPackages(){

        int flags = PackageManager.GET_META_DATA |
                PackageManager.GET_SHARED_LIBRARY_FILES |
                PackageManager.GET_UNINSTALLED_PACKAGES;

        PackageManager pm = getApplicationContext().getPackageManager();




        installedPackages = pm.getInstalledApplications(flags);
        return installedPackages;

    }

    private  void  prepareCart(){
        installedPackages=getInstalledPackages();
        for (ApplicationInfo packageInfo : installedPackages){

//            cartList.clear();
            if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
//                Log.e("k1",packageInfo.loadLabel(context.getPackageManager()).toString());

                if (packageInfo.loadLabel(getApplicationContext().getPackageManager()).toString().equals("WhatsApp")){
                    AvalaibleApps avalaibleApps=new AvalaibleApps(packageInfo.loadLabel(getApplicationContext().getPackageManager()).toString(),packageInfo.loadIcon(getApplicationContext().getPackageManager()));
                    cartList.add(avalaibleApps);
                }
                if (packageInfo.loadLabel(getApplicationContext().getPackageManager()).toString().equals("Settings")){
                    AvalaibleApps avalaibleApps=new AvalaibleApps(packageInfo.loadLabel(getApplicationContext().getPackageManager()).toString(),packageInfo.loadIcon(getApplicationContext().getPackageManager()));
                    cartList.add(avalaibleApps);
                }
                // System application
            } else {
                // Installed by user
                AvalaibleApps avalaibleApps=new AvalaibleApps(packageInfo.loadLabel(getApplicationContext().getPackageManager()).toString(),packageInfo.loadIcon(getApplicationContext().getPackageManager()));
                cartList.add(avalaibleApps);
            }



        }

        mAdapter.notifyDataSetChanged();

        // refreshing recycler view

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == Activity.RESULT_OK
                && requestCode == PolicyManager.DPM_ACTIVATION_REQUEST_CODE) {
            sharedPreference.addApp(getApplicationContext(),"Settings");

            Log.e("succes","admin");
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}