package com.tenten;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.*;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class tenten extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    final Context context = this;
    ListPreference mBehavior;
    CheckBoxPreference proximity, mHideTrackName, mHideAlbumArt, mAdvanceClear;
    Preference labelopt, picopt, pxlthing, supercustom;

    protected void onCreate(Bundle b) {
        super.onCreate(b);
        addPreferencesFromResource(R.xml.main);

        labelopt = getPreferenceScreen().findPreference("labelopt");
        labelopt.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(labelopt.getIntent());
                return true;
            }
        });

        picopt = getPreferenceScreen().findPreference("picopt");
        picopt.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(picopt.getIntent());
                return true;
            }
        });

        pxlthing = getPreferenceScreen().findPreference("pxlthing");
        pxlthing.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(pxlthing.getIntent());
                return true;
            }
        });

        supercustom = getPreferenceScreen().findPreference("supercustom");
        supercustom.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(supercustom.getIntent());
                return true;
            }
        });

        mBehavior = (ListPreference) getPreferenceScreen().findPreference("Behavior");
        mHideTrackName = (CheckBoxPreference) getPreferenceScreen().findPreference("hidetrackname");
        mHideAlbumArt = (CheckBoxPreference) getPreferenceScreen().findPreference("hidealbumart");
        proximity = (CheckBoxPreference) getPreferenceScreen().findPreference("proximity");
        mAdvanceClear = (CheckBoxPreference) getPreferenceScreen().findPreference("advance");
        mBehavior.setOnPreferenceChangeListener(this);
        findPreference("custom_platlogo").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
                return true;
            }
        });

        findPreference("custom_toast").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.customplatlogodialog);
                dialog.setTitle("Custom Toast Text");
                Button setlabelbutton = (Button) dialog.findViewById(R.id.setlabelbutton);
                final EditText setlenoxlabel = (EditText) dialog.findViewById(R.id.setlabel);
                setlabelbutton.setText("Set Toast Text");
                setlenoxlabel.setHint("Put Toast Text");
                setlenoxlabel.setText(Settings.System.getString(getContentResolver(), "custom_toast"));
                setlabelbutton.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        String toasttext = setlenoxlabel.getText().toString();
                        Settings.System.putString(getContentResolver(), "custom_toast", toasttext);
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return true;
            }
        });
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        if (preference == mBehavior)
            Settings.System.putInt(getContentResolver(), "mBehavior", Integer.valueOf((String)o).intValue());
        return true;
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;

        if(preference == mHideTrackName){
            value = mHideTrackName.isChecked();
            Settings.System.putInt(getContentResolver(), "hidetrackname",value ? 1 : 0);
        }

        if(preference == mHideAlbumArt){
            value = mHideAlbumArt.isChecked();
            Settings.System.putInt(getContentResolver(), "hidealbumart",value ? 1 : 0);
        }

        if(preference == mAdvanceClear){
            value = mAdvanceClear.isChecked();
            if(value)
                mAdvanceClear.setSummary("Go home then clear background apps");
            else
                mAdvanceClear.setSummary("Clear only background apps");

            Settings.System.putInt(getContentResolver(), "advance",value ? 1 : 0);
        }

        if(preference == proximity){
            value = proximity.isChecked();
            Settings.System.putInt(getContentResolver(), "disablelock",value ? 1 : 0);
        }
        return true;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                Uri uri = Uri.parse(data.getDataString());
                Settings.System.putString(getContentResolver(), "custom_platlogo", uri.toString());
            }
        }
    }
}
