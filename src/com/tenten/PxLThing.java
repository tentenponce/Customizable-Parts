package com.tenten;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.*;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import java.io.*;

public class PxLThing extends PreferenceActivity {

    Context c = this;
    Preference pxlwbhbg, pxlbgnotif;
    CheckBoxPreference hidecarrier;
    ListPreference pxlhandlescale, pxlhandle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pxlthing);

        PreferenceScreen prefSet = getPreferenceScreen();

        pxlhandlescale = (ListPreference) getPreferenceScreen().findPreference("pxlhandlescale");
        pxlhandlescale.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                Settings.System.putInt(getContentResolver(), "pxlhandlescale", Integer.valueOf((String)o).intValue());
                return true;
            }
        });

        pxlhandle = (ListPreference) prefSet.findPreference("pxlhandle");
        pxlhandle.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                if(Integer.valueOf((String)o).intValue() == 2) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 0);
                }
                else{
                    Settings.System.putInt(getContentResolver(), "pxlhandle", Integer.valueOf((String)o).intValue());
                }
                return true;
            }
        });
        hidecarrier = (CheckBoxPreference) prefSet.findPreference("hidecarrier");
        hidecarrier.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Settings.System.putInt(c.getContentResolver(), "hidecarrier", hidecarrier.isChecked() ? 1:0 );
                return true;
            }
        });

        pxlwbhbg = prefSet.findPreference("pxlwbhbg");
        pxlwbhbg.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ColorPickerDialogs cp = new ColorPickerDialogs(c, wbh, wbhgetcolor());
                cp.show();
                return true;
            }
        });

        pxlbgnotif = prefSet.findPreference("pxlbgnotif");
        pxlbgnotif.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ColorPickerDialogs cp = new ColorPickerDialogs(c, notifbg, nbgetcolor());
                cp.show();
                return true;
            }
        });

    }

    ColorPickerDialogs.OnColorChangedListener wbh =
            new ColorPickerDialogs.OnColorChangedListener() {
                public void colorChanged(int color) {
                    Settings.System.putInt(getContentResolver(), "pxlwbhbg", color);
                    pxlwbhbg.setSummary(Integer.toHexString(color));
                }
                public void colorUpdate(int color) {
                }
            };

    private int wbhgetcolor() {
        try {
            return Settings.System.getInt(getContentResolver(),
                    "pxlwbhbg");
        } catch (Settings.SettingNotFoundException e) {
            return 0xffffffff;
        }
    }

    ColorPickerDialogs.OnColorChangedListener notifbg =
            new ColorPickerDialogs.OnColorChangedListener() {
                public void colorChanged(int color) {
                    Settings.System.putInt(getContentResolver(), "pxlbgnotif", color);
                    pxlbgnotif.setSummary(Integer.toHexString(color));
                }
                public void colorUpdate(int color) {
                }
            };

    private int nbgetcolor() {
        try {
            return Settings.System.getInt(getContentResolver(),
                    "pxlbgnotif");
        } catch (Settings.SettingNotFoundException e) {
            return 0xffffffff;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                Uri uri = data.getData();
                String i = getDir(uri);
                String dir = getFilesDir() + "/handle/";
                moveFile(i, dir);
                String bm = dir + "pxl.png";
                Settings.System.putString(getContentResolver(), "pxlhandlepng", bm);
                Settings.System.putInt(getContentResolver(), "pxlhandle", 2);
            }
        }
    }

    private String getDir(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getBaseContext(), contentUri,
                proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void moveFile(String inputPath, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {
            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
                dir.setReadable(true, false);
                dir.setWritable(true, false);
                dir.setExecutable(true, false);

            } else {
                dir.setReadable(true, false);
                dir.setWritable(true, false);
                dir.setExecutable(true, false);;
            }

            in = new FileInputStream(inputPath);
            int n = (int) System.currentTimeMillis();
            out = new FileOutputStream(outputPath + "pxl.png");

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
            File f = new File(outputPath + "pxl.png");
            if (f.exists()) {
                f.setReadable(true, false);
                f.setWritable(true, false);
                f.setExecutable(true, false);
            }

        }
        catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }
}
