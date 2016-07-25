package com.ap.lockscreen;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button lockButton;
    private Button disableButton;
    private Button enableButton;
    private Button helpButton;
    static final int RESULT_ENABLE = 1;

    DevicePolicyManager deviceManger;
    ActivityManager activityManager;
    ComponentName compName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceManger = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        compName = new ComponentName(this, MyAdmin.class);

        lockButton =(Button)findViewById(R.id.lockButton);
        disableButton = (Button)findViewById(R.id.disableButton);
        enableButton =(Button)findViewById(R.id.enableButton);
        helpButton =(Button)findViewById(R.id.helpButton);

        lockButton.setOnClickListener(this);
        disableButton.setOnClickListener(this);
        enableButton.setOnClickListener(this);
        helpButton.setOnClickListener(this);

        updateButtonStates();
    }

    private boolean isAdmin() {
        return deviceManger.isAdminActive(compName);
    }

    @Override
    public void onClick(View v) {

        if(v == lockButton){

            boolean active = isAdmin();
            if (active) {
                deviceManger.lockNow();
            } else {
                showMessage(getString(R.string.ask_for_admin));
            }

        } else if(v == enableButton){

            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getString(R.string.admin_privileges_extra_explanation));
            startActivityForResult(intent, RESULT_ENABLE);

        } else if(v == disableButton) {

            deviceManger.removeActiveAdmin(compName);
            //here it is needed as admin state changes after a little so updateButtonStates can't be called immediately
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateButtonStates();
                }
            }, 500);


        } else if(v == helpButton) {

            showMessage(getString(R.string.help_text));
        }
    }

    private void updateButtonStates() {

        boolean active = isAdmin();

        enableButton.setEnabled(!active);
        disableButton.setEnabled(active);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_ENABLE:
                if (resultCode == Activity.RESULT_OK) {
                    L.i("Admin enabled!");
                } else {
                    L.i("Admin enabling FAILED!");
                }
        }
        updateButtonStates();

    }

    private void showMessage(String message) {

        final TextView messageView = new TextView(this);
        final SpannableString s = new SpannableString(message);
        Linkify.addLinks(s, Linkify.WEB_URLS);
        messageView.setText(s);
        messageView.setMovementMethod(LinkMovementMethod.getInstance());
        messageView.setPadding(10,10,10,10);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(messageView)
                .setTitle(getString(R.string.dialog_title))
        .setNegativeButton(getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,int id) {
                dialog.cancel();
            }
        })
        .show();
    }

}
