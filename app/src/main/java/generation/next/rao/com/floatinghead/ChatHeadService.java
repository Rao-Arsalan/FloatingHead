package generation.next.rao.com.floatinghead;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class ChatHeadService extends Service {

    private WindowManager mWindowManager;
    private View mChatHeadView;
    private WindowManager.LayoutParams mLayoutParams;
    private ClipboardManager mClipBoardManger;
    private ClipboardManager.OnPrimaryClipChangedListener onPrimaryClipChangedListener;
    private String TAG = ChatHeadService.class.getSimpleName();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




    @Override
    public void onCreate() {
        super.onCreate();

        //inflate chat head view layout
        mChatHeadView = LayoutInflater.from(this).inflate(R.layout.chat_head_layout,null,false);



        //add view to the window
        mLayoutParams =  new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT

        );

        mLayoutParams.gravity = Gravity.TOP|Gravity.START;
        mLayoutParams.x = 0;
        mLayoutParams.y = 100;

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);


        //clip board
        clipBoard(mWindowManager,mLayoutParams);
    }

    private void clipBoard(WindowManager mWindowManager, WindowManager.LayoutParams mLayoutParams) {

        mClipBoardManger = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        onPrimaryClipChangedListener = new PrimaryClipChangeListener(mWindowManager,mLayoutParams);
        mClipBoardManger.addPrimaryClipChangedListener(onPrimaryClipChangedListener);
    }

    private class PrimaryClipChangeListener implements ClipboardManager.OnPrimaryClipChangedListener{

        WindowManager windowManager;
        WindowManager.LayoutParams params;

        PrimaryClipChangeListener(WindowManager windowManager,WindowManager.LayoutParams params){
            this.windowManager = windowManager;
            this.params = params;
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onPrimaryClipChanged() {

            String copyMsg =  mClipBoardManger.getPrimaryClip().getItemAt(0).getText().toString();
            Toast.makeText(ChatHeadService.this, copyMsg, Toast.LENGTH_LONG).show();



            new CountDownTimer(5000,1000) {
                @Override
                public void onTick(long l) {
                    Log.i(TAG,"seconds Remaing :"+l/1000);
                }

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onFinish() {
                        if(mChatHeadView.isAttachedToWindow()){
                           mWindowManager.removeView(mChatHeadView);
                        }
                }
            }.start();
            if(!mChatHeadView.isAttachedToWindow()){
                mWindowManager.addView(mChatHeadView,params);
            }

        }
    }
}
