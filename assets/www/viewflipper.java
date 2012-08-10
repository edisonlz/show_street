package slidenews.liuzheng.com;

import slidenews.liuzheng.com.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.graphics.Bitmap;
import android.os.Handler; 
import android.util.Log;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.KeyEvent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.net.NetworkInfo;
import java.util.Hashtable;

import android.widget.ImageButton;
import android.widget.ViewFlipper;
import android.view.GestureDetector;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.MotionEvent;
import android.view.animation.TranslateAnimation;

public class Slide_newsActivity extends Activity {
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private WebView new1WebView;
    private WebView new2WebView;

    ViewFlipper flipper;
    GestureDetector gestureDetector;
    
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        flipper = (ViewFlipper) findViewById(R.id.flipper);
        gestureDetector = new GestureDetector(new MyGestureDetector());

    }
    
    
    public void setWebView(WebView mWebView,Boolean event_add){

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY) ;
        
        mWebView.setWebViewClient(new WebViewClient() {
           @Override
           public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {            
               //loadingFinished = false;
               view.loadUrl(urlNewString);
               return true;
           }

           @Override
           public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //pb.setVisibility(View.VISIBLE);
           }

           @Override
           public void onPageFinished(WebView view, String url) {
               //pb.setVisibility(View.GONE);
            }
        });
        
        
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event))
                return true;
        else
                return false;
    }

    private Animation animateInFrom(int fromDirection) {

        Animation inFrom = null;

        switch (fromDirection) {
        case LEFT:
                inFrom = new TranslateAnimation(
                                Animation.RELATIVE_TO_PARENT, -1.0f, 
                                Animation.RELATIVE_TO_PARENT, 0.0f,
                                Animation.RELATIVE_TO_PARENT, 0.0f,
                                Animation.RELATIVE_TO_PARENT, 0.0f);
                break;
        case RIGHT:
                inFrom = new TranslateAnimation(
                                Animation.RELATIVE_TO_PARENT, +1.0f, 
                                Animation.RELATIVE_TO_PARENT, 0.0f,
                                Animation.RELATIVE_TO_PARENT, 0.0f,
                                Animation.RELATIVE_TO_PARENT, 0.0f);
                break;
        }

        inFrom.setDuration(250);
        inFrom.setInterpolator(new AccelerateInterpolator());
        return inFrom;
    }

    private Animation animateOutTo(int toDirection) {

        Animation outTo = null;

        switch (toDirection) {
        case LEFT:
                outTo = new TranslateAnimation(
                                Animation.RELATIVE_TO_PARENT, 0.0f,
                                Animation.RELATIVE_TO_PARENT, -1.0f,
                                Animation.RELATIVE_TO_PARENT, 0.0f,
                                Animation.RELATIVE_TO_PARENT, 0.0f);
                break;
        case RIGHT:
                outTo = new TranslateAnimation(
                                Animation.RELATIVE_TO_PARENT, 0.0f,
                                Animation.RELATIVE_TO_PARENT, +1.0f,
                                Animation.RELATIVE_TO_PARENT, 0.0f,
                                Animation.RELATIVE_TO_PARENT, 0.0f);
                break;
        }

        outTo.setDuration(250);
        outTo.setInterpolator(new AccelerateInterpolator());
        return outTo;
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        // http://www.codeshogun.com/blog/2009/04/16/how-to-implement-swipe-action-in-android/

        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 250;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                try {
                        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                                return false;
                        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                // right to left swipe
                                flipper.setInAnimation(animateInFrom(RIGHT));
                                flipper.setOutAnimation(animateOutTo(LEFT));
                                flipper.showNext();
                        } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                // left to right swipe
                                flipper.setInAnimation(animateInFrom(LEFT));
                                flipper.setOutAnimation(animateOutTo(RIGHT));
                                flipper.showPrevious();
                        }
                } catch (Exception e) {}
                return false;
        }
    }
    
    public class MyWebView extends WebView {
    
        float downXValue;
        long downTime;
        private ViewFlipper flipper;
    
        private float lastTouchX, lastTouchY;
        private boolean hasMoved = false;
    
        public MyWebView(Context context, ViewFlipper flipper) {
                super(context);
                this.flipper = flipper;
        }
    
        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            boolean consumed = super.onTouchEvent(evt);
            if (isClickable()) {
                switch (evt.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastTouchX = evt.getX();
                    lastTouchY = evt.getY();
                    downXValue = evt.getX();
                    downTime = evt.getEventTime();
                    hasMoved = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    hasMoved = moved(evt);
                    break;
                case MotionEvent.ACTION_UP:
                    float currentX = evt.getX();
                    long currentTime = evt.getEventTime();
                    float difference = Math.abs(downXValue - currentX);
                    long time = currentTime - downTime;
                    
                    /** X轴滑动距离大于100，并且时间小于220ms,并且向X轴右方向滑动   && (time < 220) */
                    if ((downXValue < currentX) && (difference > 100 && (time < 220))) {
                        /** 跳到上一页 */
                        this.flipper.setInAnimation(AnimationUtils.loadAnimation(
                                this.getContext(), R.anim.push_right_in));
                        this.flipper.setOutAnimation(AnimationUtils.loadAnimation(
                                this.getContext(), R.anim.push_right_out));
                        flipper.showPrevious();
                    }
                    /** X轴滑动距离大于100，并且时间小于220ms,并且向X轴左方向滑动 */
                    if ((downXValue > currentX) && (difference > 100) && (time < 220)) {
                        /** 跳到下一页 */
                        this.flipper.setInAnimation(AnimationUtils.loadAnimation(
                                this.getContext(), R.anim.push_left_in));
                        this.flipper.setOutAnimation(AnimationUtils.loadAnimation(
                                this.getContext(), R.anim.push_left_out));
                        flipper.showNext();
                    }
                    break;
                }
            }
            return consumed || isClickable();
        }
    
        private boolean moved(MotionEvent evt) {
            return hasMoved || Math.abs(evt.getX() - lastTouchX) > 10.0
                    || Math.abs(evt.getY() - lastTouchY) > 10.0;
        }
    
    }
}