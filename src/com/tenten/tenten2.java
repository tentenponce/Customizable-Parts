package com.tenten;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class tenten2 extends Activity {

    EditText idname, packagename;
    Button ok;
    RadioButton visible, invisible, gone;
    TextView tv;

    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.super_custom);

        idname = (EditText) findViewById(R.id.idname);
        packagename = (EditText) findViewById(R.id.packagename);

        ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    tv = (TextView) findViewById(getLayout(idname.getText().toString(), "id", packagename.getText().toString()));
                    checkvisibility();
                }catch(Exception e){
                    Toast.makeText(tenten2.this, "Wrong package or ID", Toast.LENGTH_SHORT).show();
                }

            }
        });

        visible = (RadioButton) findViewById(R.id.visible);
        visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visibility(1);
            }
        });

        invisible = (RadioButton) findViewById(R.id.invisible);
        invisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visibility(2);
            }
        });

        gone = (RadioButton) findViewById(R.id.gone);
        gone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visibility(3);
            }
        });

    }


    public int getLayout(String mDrawableName, String typeName, String packName){
        int ResID = 0;
        try {
            PackageManager manager = getPackageManager();
            Resources mApk1Resources = manager.getResourcesForApplication(packName);

            ResID = mApk1Resources.getIdentifier(mDrawableName, typeName, packName);
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return ResID;
    }

    public void visibility(int a){
        if(a == 1){
            invisible.setChecked(false);
            gone.setChecked(false);
            visible.setChecked(true);
        }else if(a == 2){
            invisible.setChecked(true);
            gone.setChecked(false);
            visible.setChecked(false);
        }else if(a == 3){
            invisible.setChecked(false);
            gone.setChecked(true);
            visible.setChecked(false);
        }
    }

    public void checkvisibility(){
        if(visible.isChecked()){
            tv.setVisibility(View.VISIBLE);
        }else if(invisible.isChecked()){
            tv.setVisibility(View.INVISIBLE);
        }else if(gone.isChecked()){
            tv.setVisibility(View.GONE);
        }
    }
}
