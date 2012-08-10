package childs_l.liuzheng.com;

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




public class Child_layoutActivity extends Activity {
    /** Called when the activity is first created. */
	
	private ImageButton childButton;
	private ImageButton girlButton;
	private ImageButton backButton;
	private ImageButton homeButton;
	
	private ImageView titleBackView;

	
	boolean loadingFinished = true;
	private Hashtable webViewHt;
	private Boolean open_child_view = true;
	private Boolean open_girl_view = true;
	private Boolean open_home_view = true;
	
	private WebView current_view;
	private WebView childWebView;
	private WebView grilWebView;
	private WebView infoWebView;
	private WebView homeWebView;
	
	private ProgressBar pb; 
	
	private Handler mHandler = new Handler();
	
	private String current_poster_id;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	final Context context = this;

    	webViewHt = new Hashtable();
    	
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		homeButton = (ImageButton) findViewById(R.id.HomeButton);
		homeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				onHomeButtonClick(view);
			}
		});
		
		childButton = (ImageButton) findViewById(R.id.childViewButton);
		childButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				onChildButtonClick(view);
			}
		});
		
		girlButton = (ImageButton) findViewById(R.id.girlViewButton);
		girlButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				onGirlButtonClick(view);
			}
		});
		
		backButton = (ImageButton) findViewById(R.id.backButton);
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				onBackButtonClick(view);
			}
		});
		
		backButton.setVisibility(View.GONE);
		
		
		pb = (ProgressBar) findViewById(R.id.loader);
		pb.setVisibility(View.GONE);
		
		
		homeWebView = (WebView) findViewById(R.id.HomeWebView);
		setWebView(homeWebView,true);
		
		childWebView = (WebView) findViewById(R.id.ChildWebView);
		setWebView(childWebView,true);
		
		grilWebView = (WebView) findViewById(R.id.GirlWebView);
		setWebView(grilWebView,true);
		
		infoWebView = (WebView) findViewById(R.id.InfoWebView);
		setWebView(infoWebView,false);
		
		unvisibleWebView();
		homeWebView.loadUrl("file:///android_asset/www/home.html");
		homeWebView.setVisibility(View.VISIBLE);
		open_home_view = false;
		current_view = homeWebView;
    }
    
    
    public void setWebView(WebView mWebView,Boolean event_add){

    	mWebView.getSettings().setJavaScriptEnabled(true);
    	mWebView.getSettings().setSupportZoom(false);
    	mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY) ;
    	
    	mWebView.setWebViewClient(new WebViewClient() {
    	   @Override
    	   public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {    	       
	    	   loadingFinished = false;
	    	   view.loadUrl(urlNewString);
	    	   return true;
    	   }

    	   @Override
    	   public void onPageStarted(WebView view, String url, Bitmap favicon) {
    	        loadingFinished = false;
     		   	pb.setVisibility(View.VISIBLE);
    	   }

    	   @Override
    	   public void onPageFinished(WebView view, String url) {
    		   loadingFinished = true;
    		   pb.setVisibility(View.GONE);
    	    }
    	});
    	
    	if(event_add){
	    	mWebView.addJavascriptInterface(new Object() {
	            public void showInfo(String poster_id) {
	            	current_poster_id = poster_id;
	            	mHandler.post(new Runnable() {    
	                    public void run() {
	                    	Log.d("debug poster_id", current_poster_id);
	    	            	infoWebView.loadUrl("file:///android_asset/www/info.html?poster_id="+current_poster_id);
	    	            	unvisibleWebView();
	    	            	infoWebView.setVisibility(View.VISIBLE);
	    	            	backButton.setVisibility(View.VISIBLE);
	                    }
	                });
	            }
	        }, "info");
	    	  
    	};
    }
    
    
    public void unvisibleWebView(){
    	childWebView.setVisibility(View.GONE);
    	grilWebView.setVisibility(View.GONE);
    	homeWebView.setVisibility(View.GONE);
    	
    	infoWebView.setVisibility(View.GONE);
    	backButton.setVisibility(View.GONE);
    	pb.setVisibility(View.GONE);
    }
    
    
    public void onGirlButtonClick(View view){
		
    	current_view = grilWebView;
		unvisibleWebView();
		grilWebView.setVisibility(View.VISIBLE);
		infoWebView.loadData("<html><body></body></html>","text/html","UTF-8");
		
		if(open_girl_view){
			grilWebView.loadUrl("file:///android_asset/www/index.html?channel_id=9");
			open_girl_view= false;
		}
    }
    
    public void onHomeButtonClick(View view){
		current_view = homeWebView;
		unvisibleWebView();
		homeWebView.setVisibility(View.VISIBLE);
		infoWebView.loadData("<html><body></body></html>","text/html","UTF-8");
		
		if(open_home_view){
			grilWebView.loadUrl("file:///android_asset/www/home.html");
			open_home_view = false;
		}
    }
    
    public void onChildButtonClick(View view){
    	
    	unvisibleWebView();
    	childWebView.setVisibility(View.VISIBLE);
    	infoWebView.loadData("<html><body></body></html>","text/html","UTF-8");
    	
		current_view = childWebView;
		
		if(open_child_view){
			childWebView.loadUrl("file:///android_asset/www/index.html?channel_id=2");
			open_child_view = false;
		}
    }
    
    public void onBackButtonClick(View view){
    	
    	
    	unvisibleWebView();
    	current_view.setVisibility(View.VISIBLE);
    	infoWebView.loadData("<html><body></body></html>","text/html","UTF-8");
    	
    	ProgressBar pb = (ProgressBar) findViewById(R.id.loader);
		pb.setVisibility(View.GONE);
		backButton.setVisibility(View.GONE);
    }
    
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			dialog();
			return true;
		}
		return true;
	}
    
    
    protected void dialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(Child_layoutActivity.this);
		builder.setMessage("亲，你确定要退出么：）");
		builder.setTitle("提示");
		builder.setPositiveButton("恩",
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					Child_layoutActivity.this.finish();
					//System.exit(1);
					android.os.Process.killProcess(android.os.Process.myPid());
				}
		});
		builder.setNegativeButton("待会",
		new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
    
    
    public boolean isConnecting()
    {
        ConnectivityManager mConnectivity = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mTelephony = (TelephonyManager)this.getSystemService(TELEPHONY_SERVICE);

        NetworkInfo info = mConnectivity.getActiveNetworkInfo();

        if(info == null || !mConnectivity.getBackgroundDataSetting())
        {
         return false;
        }
       
        int netType = info.getType();    
        int netSubtype = info.getSubtype();

        if(netType == ConnectivityManager.TYPE_WIFI)
        {       
         return info.isConnected();    
        }
        else if(netType == ConnectivityManager.TYPE_MOBILE && netSubtype ==TelephonyManager.NETWORK_TYPE_UMTS && !mTelephony.isNetworkRoaming())
        {       
         return info.isConnected();    
        }
        else
        {       
         return false;    
        }
    }

}