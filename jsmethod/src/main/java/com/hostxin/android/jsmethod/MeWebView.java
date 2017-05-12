package com.hostxin.android.jsmethod;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;


import com.hostxin.android.util.Dbg;
import com.hostxin.android.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MeWebView extends WebView {

	private static final int FILECHOOSER_RESULTCODE = 0;

	private String initJs = null;

	private JsMethodManager manager;

	private ValueCallback<Uri> mUploadMessage;

	private WebViewClientListener webViewClientListener;

	public static class WebViewClientListener {
		public void onProgressChanged(WebView view, int newProgress) {};
		public void onReceivedTitle(WebView view, String t) {};
		public void shouldOverrideUrlLoading(WebView view, String url) {};
		public void onPageStarted(WebView view, String url, Bitmap favicon) {};
		public void onPageFinished(WebView view, String url) {};
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {}
	}

	public MeWebView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@SuppressLint("SetJavaScriptEnabled")
	public void init(BaseActivityBrowser activity) {

		manager = new JsMethodManager(activity, this);
		setScrollBarStyle(ScrollView.SCROLLBARS_OUTSIDE_OVERLAY);

		WebSettings webSettings = getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(false);
		webSettings.setBuiltInZoomControls(false);
		setInitialScale(100);// 防止被放大
		webSettings.setUseWideViewPort(true);

		webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setAllowUniversalAccessFromFileURLs(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setMediaPlaybackRequiresUserGesture(false);

		setWebChromeClient(new WebChromeClient() {

			public void openFileChooser(ValueCallback<Uri> uploadMsg,
					String acceptType, String capture) {
				openFileChooser(uploadMsg, "");
			}

			@Override
			public boolean onJsConfirm(WebView view, String url,
					String message, JsResult result) {
				// TODO Auto-generated method stub
				return super.onJsConfirm(view, url, message, result);
			}

			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					final JsResult result) {
				return false;
			}

			// For Android 3.0+
			public void openFileChooser(ValueCallback<Uri> uploadMsg,
					String acceptType) {
				if (mUploadMessage != null)
					return;
				mUploadMessage = uploadMsg;
				manager.getActivity().startActivityForResult(
						createDefaultOpenableIntent(), FILECHOOSER_RESULTCODE);
			}

			// For Android < 3.0
			public void openFileChooser(ValueCallback<Uri> uploadMsg) {
				openFileChooser(uploadMsg, "");
			}

			@Override
			public void onReceivedTitle(WebView view, String t) {
				final WebViewClientListener listener = getWebViewClientListener();
				if (listener != null) {
					listener.onReceivedTitle(view, t);
				}
				super.onReceivedTitle(view, t);
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				final WebViewClientListener listener = getWebViewClientListener();
				if (listener != null) {
					listener.onProgressChanged(view, newProgress);
				}
				super.onProgressChanged(view, newProgress);
			}

			@Override
			public void onConsoleMessage(String message, int lineNumber,
					String sourceID) {
			}

		});
		setWebViewClient(new WebViewClient() {

			@Override
			public void onLoadResource(WebView view, String url) {
				// DebugUtil.e("MeWebView onLoadResource " + url);
				super.onLoadResource(view, url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {

				if (errorCode == -2) {
					super.onReceivedError(view, errorCode, description,
							failingUrl);
					manager.getActivity().showToast(manager.getActivity().getString(R.string.error_code_no_net));
				} else if (errorCode == -10) {
					super.onReceivedError(view, errorCode, description,
							failingUrl);
				} else {
					super.onReceivedError(view, errorCode, description,
							failingUrl);
				}
			}

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// loadUrl("javascript:" + getInitJs());
				if (!StringUtils.isBlank(url)
						&& (url.startsWith("http") || url.startsWith("https") || url.startsWith("file"))) {
					loadUrl(url);
					final WebViewClientListener listener = getWebViewClientListener();
					if (listener != null) {
						listener.shouldOverrideUrlLoading(view, url);
					}

				}
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				final WebViewClientListener listener = getWebViewClientListener();
				if (listener != null) {
					listener.onPageStarted(view, url, favicon);
				}

				loadUrl("javascript:" + getInitJs());
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				manager.cleanCallback();
				final WebViewClientListener listener = getWebViewClientListener();
				loadUrl("javascript:" + getInitJs());

				if (listener != null) {
					listener.onPageFinished(view, url);
				}
				super.onPageFinished(view, url);
			}

			@Override
			public void onFormResubmission(WebView view, Message dontResend,
					Message resend) {
				super.onFormResubmission(view, dontResend, resend);
			}

			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				Dbg.d("_web", "onReceivedSslError");
				final WebViewClientListener listener = getWebViewClientListener();
				if (listener != null) {
					listener.onReceivedSslError(view, handler, error);
				}
			}
		});
		setDownloadListener(new DownloadListener() {

			@Override
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype,
					long contentLength) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri content_uri_browsers = Uri.parse(url);
				intent.setData(content_uri_browsers);
				getContext().startActivity(intent);
			}
		});
		addJavascriptInterface(new MeJsApi(manager), "_nativeMe");
	}
	
	public JsMethodManager getManager() {
		return manager;
	}

	private String getInitJs() {
		if (initJs != null) {
			return initJs;
		}
		try {
			InputStream is = getContext().getAssets().open("init.js");
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			StringBuffer buffer = new StringBuffer();
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				buffer.append(tempString);
			}
			reader.close();
			initJs = buffer.toString();
			return initJs;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "void()";
	}

	public void nativeCallback(final String callbackId, Object data) {
		final String cd = data.toString();

		post(new Runnable() {
			@Override
			public void run() {
				loadUrl("javascript:nativeCallback('" + callbackId + "'," + cd
						+ ");");
			}
		});

	}

	public void nativeCallError(final String callbackId, final String error) {

		post(new Runnable() {
			@Override
			public void run() {
				loadUrl("javascript:nativeError('" + callbackId + "','" + error
						+ "');");
			}
		});
	}

	public JsMethodManager getJsMethodManager() {
		return manager;
	}

	public WebViewClientListener getWebViewClientListener() {
		return webViewClientListener;
	}

	public void setWebViewClientListener(
			WebViewClientListener webViewClientListener) {
		this.webViewClientListener = webViewClientListener;
	}

	public void handleActivityResult(int requestCode, int resultCode,
			Intent data) {
		if (requestCode == FILECHOOSER_RESULTCODE) {
			if (null == mUploadMessage)
				return;
			Uri result = data == null || resultCode != Activity.RESULT_OK ? null
					: data.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
			return;
		}
		getJsMethodManager()
				.handleActivityResult(requestCode, resultCode, data);

	}

	private Intent createDefaultOpenableIntent() {
		Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		i.addCategory(Intent.CATEGORY_OPENABLE);
		i.setType("*/*");

		Intent chooser = createChooserIntent(createCameraIntent(),
				createCamcorderIntent(), createSoundRecorderIntent());
		chooser.putExtra(Intent.EXTRA_INTENT, i);
		return chooser;
	}

	private Intent createChooserIntent(Intent... intents) {
		Intent chooser = new Intent(Intent.ACTION_CHOOSER);
		chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);
		chooser.putExtra(Intent.EXTRA_TITLE, "File Chooser");
		return chooser;
	}

	private Intent createCameraIntent() {
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File externalDataDir = Environment.getExternalStorageDirectory();
		File cameraDataDir = new File(externalDataDir.getAbsolutePath()
				+ File.separator + "browser-photos");
		cameraDataDir.mkdirs();
		String mCameraFilePath = cameraDataDir.getAbsolutePath()
				+ File.separator + System.currentTimeMillis() + ".jpg";
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(mCameraFilePath)));
		return cameraIntent;
	}

	private Intent createCamcorderIntent() {
		return new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
	}

	private Intent createSoundRecorderIntent() {
		return new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
	}

	@Override
	protected void onSizeChanged(int w, int h, int ow, int oh) {
		super.onSizeChanged(w, h, ow, oh);

	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
	}

}
