package se.miun.mova1701.dt031g.dialer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadActivity extends AppCompatActivity {
    private WebView webView;
    private WebViewClient webViewClient;
    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 123;
    private String DownloadURL;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    try {
                        File folder = new File(getResources().getString(R.string.externalStorageDirectory));
                        if (!folder.exists()) {
                            folder.mkdir();
                        }
                        String fileFolder = folder.toString();
                        downloadFile(DownloadURL, fileFolder);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        setContentView(R.layout.activity_download);

        webView = (WebView) findViewById(R.id.activity_download_webview);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String Url) {

                if(extras != null) {
                    view.loadUrl(Url);
                }
                return true;
            }

        });
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                File folder = new File(getResources().getString(R.string.externalStorageDirectory));
                DownloadURL = url;
                if (!folder.exists()) {
                    folder.mkdir();
                }
                String fileFolder = folder.toString();
                if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DownloadActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
                else {
                    try {
                        downloadFile(url, fileFolder);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                /*Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);*/
            }
        });
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            View layout = findViewById(R.id.dialpad);
            Snackbar mySnackbar = Snackbar.make(layout, R.string.externalStorageNoSDCardError, 3000);
            mySnackbar.show();
        } else {
            String externalStorageDirectory = android.os.Environment.getExternalStorageDirectory().getPath() + "/Dialer/Voices/";
            webView.loadUrl(extras.getString("url"));
        }
    }






    private static final int BUFFER_SIZE = 4096;

    /**
     * Downloads a file from a URL
     * @param fileURL HTTP URL of the file to be downloaded
     * @param saveDir path of the directory to save the file
     * @throws IOException
     */
    public void downloadFile(String fileURL, String saveDir)
            throws IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        ProgressDialog pd = new ProgressDialog(DownloadActivity.this);
        pd.setMessage("Downloading");
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(false);
        pd.setMax(100);
        pd.setCancelable(true);

        pd.show();
        Thread thread = new Thread() {
            @Override
            public void run() {

                try {
                    URL url = new URL(fileURL);
                    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                    int responseCode = httpConn.getResponseCode();

                    // always check HTTP response code first
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        String fileName = "";
                        String disposition = httpConn.getHeaderField("Content-Disposition");
                        String contentType = httpConn.getContentType();
                        int contentLength = httpConn.getContentLength();

                        if (disposition != null) {
                            // extracts file name from header field
                            int index = disposition.indexOf("filename=");
                            if (index > 0) {
                                fileName = disposition.substring(index + 10,
                                        disposition.length() - 1);
                            }
                        } else {
                            // extracts file name from URL
                            fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                                    fileURL.length());
                        }

                        System.out.println("Content-Type = " + contentType);
                        System.out.println("Content-Disposition = " + disposition);
                        System.out.println("Content-Length = " + contentLength);
                        System.out.println("fileName = " + fileName);

                        // opens input stream from the HTTP connection
                        InputStream inputStream = httpConn.getInputStream();
                        String saveFilePath = saveDir + File.separator + fileName;

                        // opens an output stream to save into file
                        FileOutputStream outputStream = new FileOutputStream(saveFilePath);
                        int bytesRead = -1;
                        byte[] buffer = new byte[BUFFER_SIZE];
                        int bytesWritten = 0;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                            bytesWritten+=bytesRead;
                            System.out.println("bytesWritten: " + bytesWritten + "\n" + contentLength + "\n");
                            pd.setProgress((100*bytesWritten)/contentLength);
                            System.out.println((100*bytesWritten)/contentLength);

                        }

                        outputStream.close();
                        inputStream.close();
                        pd.dismiss();
                        System.out.println("File downloaded");
                        File file = new File(saveDir);
                        if(!file.isDirectory()) {
                            file.mkdirs();
                        }
                        ZIP.decompress(saveFilePath, saveDir);

                    } else {
                        System.out.println("No file to download. Server replied HTTP code: " + responseCode);
                    }
                    httpConn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };

        thread.start();
    }
}
