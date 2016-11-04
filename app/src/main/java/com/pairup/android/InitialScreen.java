package com.pairup.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.pairup.android.utils.di.Injector;
import com.pairup.android.utils.storage.UserSettings;

import javax.inject.Inject;

/**
 * Created by Ilya Eremin on 22.08.2016.
 */
public class InitialScreen extends AppCompatActivity {

    @Inject UserSettings userSettings;

    private boolean canStart;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_splash);

        Injector.inject(this);

        canStart = true;

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(500);
                    if (canStart) {
                        if (userSettings.userHasToken()) {
                            Navigator.chat(InitialScreen.this);
                        } else {
                            Navigator.welcome(InitialScreen.this);
                        }
                    }
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    finish();
                }
            }
        };
        timerThread.start();
    }

    @Override protected void onPause() {
        super.onPause();
        canStart = false;
        finish();
    }
}
