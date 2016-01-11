package com.tenten;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;


public class LenoxPicture extends ImageView {

    public Handler mHandler, h;
    Uri uri1, uri2;
    Context c;
    int whatpic, picanim, pichide;
    Thread thread;
    Boolean animm;

    public LenoxPicture(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        c = context;
        mHandler = new Handler();
        h = new Handler();
        SettingsObserver settingsObserver = new SettingsObserver(mHandler);
        settingsObserver.observe();
        animm = true;
        updatepic();

        restartthread();

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(Settings.System.getInt(c.getContentResolver(), "picanim", 0) == 1)) {
                    if (whatpic == 0) {
                        Settings.System.putInt(c.getContentResolver(), "picmagic", 1);
                    } else {
                        Settings.System.putInt(c.getContentResolver(), "picmagic", 0);
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
                    "picanim"), false, this);
            resolver.registerContentObserver(Settings.System.getUriFor(
                    "picanim1"), false, this);
            resolver.registerContentObserver(Settings.System.getUriFor(
                    "picanim2"), false, this);
            resolver.registerContentObserver(Settings.System.getUriFor(
                    "picmagic"), false, this);
            resolver.registerContentObserver(Settings.System.getUriFor(
                    "pichide"), false, this);
        }

        public void onChange(boolean selfChange) {
            updatepic();
            anim();
        }
    }

    public void updatepic(){

        try{
            uri1 = Uri.parse(Settings.System.getString(c.getContentResolver(), "picanim1"));
            uri2 = Uri.parse(Settings.System.getString(c.getContentResolver(), "picanim2"));
        }catch(Exception ignored){}

        whatpic = Settings.System.getInt(c.getContentResolver(), "picmagic", 0);
        pichide = Settings.System.getInt(c.getContentResolver(), "pichide", 0);

        if(pichide == 1){
           setVisibility(GONE);
        }else{
            setVisibility(VISIBLE);
            try{
                if(whatpic == 0)
                    setImageURI(uri2);
                else
                    setImageURI(uri1);
            }catch(Exception ignored){}
        }
    }

    public void anim(){
        picanim = Settings.System.getInt(c.getContentResolver(), "picanim", 0);

        if(picanim == 1) {
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
            if(animm)
                setImageURI(uri2);
            else
                setImageURI(uri1);

            animm = !animm;
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
}
