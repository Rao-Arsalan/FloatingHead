package generation.next.rao.com.floatinghead;

import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Button mStartServiceBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStartServiceBtn = findViewById(R.id.start_service_btn);

        mStartServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Log.i(TAG,"Draw overlay value:"+Settings.canDrawOverlays(MainActivity.this));
                    if(Settings.canDrawOverlays(MainActivity.this)){
                        startService(new Intent(MainActivity.this, ChatHeadService.class));
                        finish();

                    }else{
                        Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        startActivity(myIntent);
                    }
                }else{
                    startService(new Intent(MainActivity.this, ChatHeadService.class));
                    finish();

                }
            }
        });

    }
}
