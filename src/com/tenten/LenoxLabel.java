package com.tenten;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.graphics.Typeface;
import android.os.Handler;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class LenoxLabel extends TextView{

    String text1, text2;
    int color, size, style, whattext, anim;
    public Handler mHandler;
    Context c;
    Handler h;
    Thread thread;

    public LenoxLabel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        c = context;

        mHandler = new Handler();
        h = new Handler();
        SettingsObserver settingsObserver = new SettingsObserver(mHandler);
        settingsObserver.observe();

        updatelabel();
        updatetext();

        restartthread();

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(Settings.System.getInt(c.getContentResolver(), "tenanim", 0) == 1)) {
                    if (whattext == 0) {
                        Settings.System.putInt(c.getContentResolver(), "magic", 1);
                        dosomemagic();
                    } else {
                        Settings.System.putInt(c.getContentResolver(), "magic", 0);
                        dosomemagic();
                    }
                }
            }
        });

    }

    class SettingsObserver extends ContentObserver {
        SettingsObserver(Handler handler) {
            super(handler);
        }

        void observe() {
            ContentResolver resolver = getContext().getContentResolver();
            resolver.registerContentObserver(Settings.System.getUriFor(
                    "tentext1"), false, this);
            resolver.registerContentObserver(Settings.System.getUriFor(
                    "tentext2"), false, this);
            resolver.registerContentObserver(Settings.System.getUriFor(
                    "tencolor"), false, this);
            resolver.registerContentObserver(Settings.System.getUriFor(
                    "tencolor1"), false, this);
            resolver.registerContentObserver(Settings.System.getUriFor(
                    "tencolor2"), false, this);
            resolver.registerContentObserver(Settings.System.getUriFor(
                    "tencolor3"), false, this);
            resolver.registerContentObserver(Settings.System.getUriFor(
                    "tensize"), false, this);
            resolver.registerContentObserver(Settings.System.getUriFor(
                    "tenstyle"), false, this);
            resolver.registerContentObserver(Settings.System.getUriFor(
                    "magic"), false, this);
            resolver.registerContentObserver(Settings.System.getUriFor(
                    "tenanim"), false, this);
            resolver.registerContentObserver(Settings.System.getUriFor(
                    "tenrandom"), false, this);
        }

        public void onChange(boolean selfChange) {
            updatelabel();
            updatetext();
            anim();
        }
    }

    public void updatelabel(){

        color = Settings.System.getInt(c.getContentResolver(), "tencolor", 0);
        size = Settings.System.getInt(c.getContentResolver(), "tensize", 14);
        style = Settings.System.getInt(c.getContentResolver(), "tenstyle", 0);

        setTextColor(color);
        setTextSize(size);


        if (style == 0)
            setTypeface(null, Typeface.BOLD);
        else if (style == 1)
            setTypeface(null, Typeface.ITALIC);
        else if (style == 2)
            setTypeface(null, Typeface.BOLD_ITALIC);
        else if (style == 3)
            setTypeface(null, Typeface.NORMAL);
    }

    public void updatetext(){

        text1 = Settings.System.getString(c.getContentResolver(), "tentext1");
        text2 = Settings.System.getString(c.getContentResolver(), "tentext2");
        whattext = Settings.System.getInt(c.getContentResolver(), "magic", 0);

        if(whattext == 0)
            setText(text2);
        else
            setText(text1);
    }

    public void anim(){
        anim = Settings.System.getInt(c.getContentResolver(), "tenanim", 0);

        if(anim == 1) {
            try{
                thread.start();
            }catch(Exception ex){
                restartthread();
                thread.start();
            }
        }

        else {
            thread.interrupt();
        }
    }

    public Runnable r = new Runnable() {
        @Override
        public void run() {
            if(text1 == getText())
                setText(text2);
            else
                setText(text1);
            dosomemagic();
        }
    };

    public void restartthread(){
        thread = new Thread()
        {
            @Override
            public void run() {
                try {
                    while(true) {
                        sleep(1000);
                        h.postDelayed(r, 0);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void dosomemagic(){
        int color1 = Settings.System.getInt(c.getContentResolver(), "tencolor1", 0);
        int color2 = Settings.System.getInt(c.getContentResolver(), "tencolor2", 0);
        int color3 = Settings.System.getInt(c.getContentResolver(), "tencolor3", 0);

        if(Settings.System.getInt(c.getContentResolver(), "tenrandom", 0) == 1){
            Random r = new Random();
            int ran = r.nextInt(3);
            switch(ran){
                case 0:
                    if(getCurrentTextColor() == color1)
                        setTextColor(color2);
                    else
                        setTextColor(color1);
                    break;
                case 1:
                    if(getCurrentTextColor() == color2)
                        setTextColor(color3);
                    else
                        setTextColor(color2);
                    break;
                case 2:
                    if(getCurrentTextColor() == color3)
                        setTextColor(color1);
                    else
                        setTextColor(color3);
                    break;
                default:
                    setTextColor(Settings.System.getInt(c.getContentResolver(), "tencolor", 0));
            }
        }
    }
}
