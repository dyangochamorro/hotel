
package com.shine.hotels.ui.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.shine.hotels.R;

public class ExitDialog extends Dialog {

    public Activity activity;

    public Dialog dialog;

    public Button yes, no;

    public ExitDialog(Context context) {
        super(context);
        
        activity = (Activity)context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_layout);
        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

        yes = (Button)findViewById(R.id.confirm);
        yes.setOnClickListener(new android.view.View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        no = (Button)findViewById(R.id.cancel);
        no.setOnClickListener(new android.view.View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
