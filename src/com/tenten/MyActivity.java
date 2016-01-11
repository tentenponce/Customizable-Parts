package com.tenten;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.*;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MyActivity extends PreferenceActivity {

    Context c = this;
    ListPreference tensize, tenstyle;
    Preference tentext1, tentext2, tencolor, tencolor1, tencolor2, tencolor3;
    CheckBoxPreference tenanim, tenrandom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.labelopt);


        PreferenceScreen prefSet = getPreferenceScreen();
        tenrandom = (CheckBoxPreference) prefSet.findPreference("tenrandom");
        tenrandom.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Settings.System.putInt(c.getContentResolver(), "tenrandom", tenrandom.isChecked() ? 1:0 );
                setActive();
                return true;
            }
        });
        tenanim = (CheckBoxPreference) prefSet.findPreference("tenanim");
        tenanim.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Settings.System.putInt(c.getContentResolver(), "tenanim", tenanim.isChecked() ? 1:0 );
                if(tenanim.isChecked()) {
                    disableall();
                }else {
                    enableall();
                }
                return true;
            }
        });
        tentext1 = prefSet.findPreference("tentext1");
        tentext2 = prefSet.findPreference("tentext2");

        tencolor = prefSet.findPreference("tencolor");
        tencolor.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ColorPickerDialogs cp = new ColorPickerDialogs(c, LabelTextColorListener, getLabelColor());
                cp.show();
                return true;
            }
        });

        tencolor1 = prefSet.findPreference("tencolor1");
        tencolor1.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ColorPickerDialogs cp = new ColorPickerDialogs(c, color1, c1());
                cp.show();
                return true;
            }
        });

        tencolor2 = prefSet.findPreference("tencolor2");
        tencolor2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ColorPickerDialogs cp = new ColorPickerDialogs(c, color2, c2());
                cp.show();
                return true;
            }
        });

        tencolor3 = prefSet.findPreference("tencolor3");
        tencolor3.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ColorPickerDialogs cp = new ColorPickerDialogs(c, color3, c3());
                cp.show();
                return true;
            }
        });

        tensize = (ListPreference) prefSet.findPreference("tensize");
        tensize.setValue(String.valueOf(Settings.System.getInt(getContentResolver(), "tensize", 0)));
        tensize.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                Settings.System.putInt(getContentResolver(), "tensize", Integer.valueOf((String)o).intValue());
                return true;
            }
        });

        tenstyle = (ListPreference) prefSet.findPreference("tenstyle");
        tenstyle.setValue(String.valueOf(Settings.System.getInt(getContentResolver(), "tenstyle", 3)));
        tenstyle.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                Settings.System.putInt(getContentResolver(), "tenstyle", Integer.valueOf((String)o).intValue());
                return true;
            }
        });
        tentext1.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final Dialog dialog = new Dialog(c);
                dialog.setContentView(R.layout.lenoxlabel);
                dialog.setTitle("Set Lenox Label");
                Button setlabelbutton = (Button) dialog.findViewById(R.id.setlabelbutton);
                final EditText setlenoxlabel = (EditText) dialog.findViewById(R.id.setlenoxlabel);
                setlabelbutton.setText("Set First Label");
                setlenoxlabel.setHint("Put First Label");
                setlenoxlabel.setText(Settings.System.getString(getContentResolver(), "tentext1"));
                setlabelbutton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String faklabel = setlenoxlabel.getText().toString();
                        Settings.System.putString(getContentResolver(), "tentext1", faklabel);
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return true;
            }
        });

        tentext2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final Dialog dialog = new Dialog(c);
                dialog.setContentView(R.layout.lenoxlabel);
                dialog.setTitle("Set Lenox Label");
                Button setlabelbutton = (Button) dialog.findViewById(R.id.setlabelbutton);
                final EditText setlenoxlabel = (EditText) dialog.findViewById(R.id.setlenoxlabel);
                setlabelbutton.setText("Set Second Label");
                setlenoxlabel.setHint("Put Second Label");
                setlenoxlabel.setText(Settings.System.getString(getContentResolver(), "tentext2"));
                setlabelbutton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String faklabel = setlenoxlabel.getText().toString();
                        Settings.System.putString(getContentResolver(), "tentext2", faklabel);
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return true;
            }
        });

        setActive();
        if(tenanim.isChecked()) {
            disableall();
        }else {
            enableall();
        }
    }

    ColorPickerDialogs.OnColorChangedListener LabelTextColorListener =
            new ColorPickerDialogs.OnColorChangedListener() {
                public void colorChanged(int color) {
                    Settings.System.putInt(getContentResolver(), "tencolor", color);
                    tencolor.setSummary(Integer.toHexString(color));
                }
                public void colorUpdate(int color) {
                }
            };

    private int getLabelColor() {
        try {
            return Settings.System.getInt(getContentResolver(),
                    "tencolor");
        } catch (Settings.SettingNotFoundException e) {
            return 0xffffffff;
        }
    }

    ColorPickerDialogs.OnColorChangedListener color1 =
            new ColorPickerDialogs.OnColorChangedListener() {
                public void colorChanged(int color) {
                    Settings.System.putInt(getContentResolver(), "tencolor1", color);
                    tencolor1.setSummary(Integer.toHexString(color));
                }
                public void colorUpdate(int color) {
                }
            };

    ColorPickerDialogs.OnColorChangedListener color2 =
            new ColorPickerDialogs.OnColorChangedListener() {
                public void colorChanged(int color) {
                    Settings.System.putInt(getContentResolver(), "tencolor2", color);
                    tencolor2.setSummary(Integer.toHexString(color));
                }
                public void colorUpdate(int color) {
                }
            };

    ColorPickerDialogs.OnColorChangedListener color3 =
            new ColorPickerDialogs.OnColorChangedListener() {
                public void colorChanged(int color) {
                    Settings.System.putInt(getContentResolver(), "tencolor3", color);
                    tencolor3.setSummary(Integer.toHexString(color));
                }
                public void colorUpdate(int color) {
                }
            };

    private int c1() {
        try {
            return Settings.System.getInt(getContentResolver(),
                    "tencolor1");
        } catch (Settings.SettingNotFoundException e) {
            return 0xffffffff;
        }
    }

    private int c2() {
        try {
            return Settings.System.getInt(getContentResolver(),
                    "tencolor2");
        } catch (Settings.SettingNotFoundException e) {
            return 0xffffffff;
        }
    }

    private int c3() {
        try {
            return Settings.System.getInt(getContentResolver(),
                    "tencolor3");
        } catch (Settings.SettingNotFoundException e) {
            return 0xffffffff;
        }
    }
    public void setActive(){
        if(!tenrandom.isChecked()){
            disable();
        }else{
            enable();
        }
    }

    public void disable(){
        tencolor1.setEnabled(false);
        tencolor2.setEnabled(false);
        tencolor3.setEnabled(false);
    }

    public void enable(){
        tencolor1.setEnabled(true);
        tencolor2.setEnabled(true);
        tencolor3.setEnabled(true);
    }

    public void disableall(){
        tencolor1.setEnabled(false);
        tencolor2.setEnabled(false);
        tencolor3.setEnabled(false);
        tentext1.setEnabled(false);
        tentext2.setEnabled(false);
        tensize.setEnabled(false);
        tenstyle.setEnabled(false);
        tencolor.setEnabled(false);
        tenrandom.setEnabled(false);
    }

    public void enableall(){
        tencolor1.setEnabled(true);
        tencolor2.setEnabled(true);
        tencolor3.setEnabled(true);
        tentext1.setEnabled(true);
        tentext2.setEnabled(true);
        tensize.setEnabled(true);
        tenstyle.setEnabled(true);
        tencolor.setEnabled(true);
        tenrandom.setEnabled(true);
    }
}
