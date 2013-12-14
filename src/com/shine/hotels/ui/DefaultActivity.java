
package com.shine.hotels.ui;

import com.shine.hotels.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class DefaultActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_layout);
        
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            
            @Override
            public void run() {
                Intent intent = new Intent(DefaultActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }
        }, 2000);
    }
}
