package com.tenten;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.provider.Settings;

public class LenoxPicOpt extends PreferenceActivity {


    CheckBoxPreference picanim, pichide;
    Preference picanim1, picanim2;
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        addPreferencesFromResource(R.xml.picopt);
        picanim = (CheckBoxPreference) getPreferenceScreen().findPreference("picanim");
        pichide = (CheckBoxPreference) getPreferenceScreen().findPreference("pichide");
        picanim1 = getPreferenceScreen().findPreference("picanim1");
        picanim2 = getPreferenceScreen().findPreference("picanim2");

        pichide.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Settings.System.putInt(getContentResolver(), "pichide", pichide.isChecked() ? 1:0 );
                if(pichide.isChecked()){
                    picanim1.setEnabled(false);
                    picanim2.setEnabled(false);
                    picanim.setEnabled(false);
                } else{
                    picanim1.setEnabled(true);
                    picanim2.setEnabled(true);
                    picanim.setEnabled(true);
                }
                return true;
            }
        });
        picanim.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Settings.System.putInt(getContentResolver(), "picanim", picanim.isChecked() ? 1:0 );
                if(picanim.isChecked()){
                    picanim1.setEnabled(false);
                    picanim2.setEnabled(false);
                    pichide.setEnabled(false);
                } else{
                    picanim1.setEnabled(true);
                    picanim2.setEnabled(true);
                    pichide.setEnabled(true);
                }
                return true;
            }
        });
        picanim1.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
                return true;
            }
        });

        picanim2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
                return true;
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                Uri uri = Uri.parse(data.getDataString());
                Settings.System.putString(getContentResolver(), "picanim1", uri.toString());
            } else if(requestCode == 1){
                Uri uri = Uri.parse(data.getDataString());
                Settings.System.putString(getContentResolver(), "picanim2", uri.toString());
            }
        }
    }
}
