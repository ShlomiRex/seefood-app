package com.shlomirex.seefood;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EvaluatingActivity extends AppCompatActivity {

    private String apiServerHost = "192.168.1.107";
    private int apiServerPort = 5000;

    private String fileName = "file";
    private final String boundary =  "*****";
    private final String crlf = "\r\n";
    private final String twoHyphens = "--";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluating);

        if (getIntent().getExtras() == null) {
            Log.e("EvaluatingActivity", "No extras in intent");
            Toast.makeText(this, "Internal error", Toast.LENGTH_SHORT).show();
            return;
        } else if (getIntent().getExtras().get("photo") != null) {
            Log.d("EvaluatingActivity", "Got photo");
        }

        Bitmap photo = (Bitmap) getIntent().getExtras().get("photo");

        View overlay = findViewById(R.id.overlayView);
        View progressBar = findViewById(R.id.progressBar);
        View txtView_eval = findViewById(R.id.textView_evaluating);
        View hotdog = findViewById(R.id.hotdog);
        View not_hotdog = findViewById(R.id.not_hotdog);

        hotdog.setVisibility(View.INVISIBLE);
        not_hotdog.setVisibility(View.INVISIBLE);

        // Change the background to the photo
        findViewById(R.id.evaluatingActivity).setBackground(new BitmapDrawable(getResources(), photo));

        // Make network calls - call the API
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {

                //Background work here
                boolean is_hotdog = predictPhoto(photo);

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here
                        overlay.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        txtView_eval.setVisibility(View.INVISIBLE);

                        if (is_hotdog)
                            hotdog.setVisibility(View.VISIBLE);
                        else
                            not_hotdog.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    /**
     *
     * @param photo
     * @return True if hotdog. False if not hotdog.
     */
    private boolean predictPhoto(Bitmap photo) {
        String file = "api";

        try {
            //TODO: Change URL to the server's URL
            URL url = new URL("http", apiServerHost, apiServerPort, "/api/");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            Log.i("sendAPIRequest", "Opened connection to " + apiServerHost + ":" + apiServerPort + "/" + file);

            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            con.setRequestMethod("POST");
//            con.setRequestProperty("Content-Type", "image/jpeg");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Cache-Control", "no-cache");
            String boundary = "*****";
            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            OutputStream outputStream = con.getOutputStream();
            DataOutputStream request = new DataOutputStream(outputStream);
            request.writeBytes(this.twoHyphens + this.boundary + this.crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"" +
                    fileName + "\";filename=\"" +
                    fileName + "\"" + this.crlf);
            request.writeBytes(this.crlf);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] file_bytes = stream.toByteArray();

            request.write(file_bytes);
            request.writeBytes(this.crlf);
            request.writeBytes(this.twoHyphens + this.boundary +
                    this.twoHyphens + this.crlf);

            request.flush();
            request.close();

            // Send request
            con.connect();
            Log.i("sendAPIRequest", "Sent request to API server");

            // Get response
            int code = con.getResponseCode();
            Log.d("sendAPIRequest", "Response code: " + code);
            Log.d("sendAPIRequest", "Response message: " + con.getResponseMessage());

            if (code != 200) {
                Log.e("sendAPIRequest", "API server returned code " + code);
                //Toast.makeText(context, "Internal error", Toast.LENGTH_LONG).show();
                throw new RuntimeException("API server returned code " + code);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = reader.readLine();

            reader.close();
            con.disconnect();

            // Parse response - single line of JSON
            if (line.startsWith("{\"is_hotdog\":")) {
                Log.d("sendAPIRequest", "API server returned valid response");
                if (line.contains("true")) {
                    Log.i("sendAPIRequest", "The image is hot dog");
                    return true;
                } else {
                    Log.i("sendAPIRequest", "The image is not hot dog");
                    return false;
                }
            } else {
                Log.e("sendAPIRequest", "API server returned invalid response");
                throw new RuntimeException("API server returned invalid response");
            }
        } catch (MalformedURLException e) {
            Log.e("sendAPIRequest", "Malformed URL, host: " + apiServerHost + ", port: " + apiServerPort + ", file: " + file);
            Toast.makeText(this, "Internal error", Toast.LENGTH_LONG).show();
            throw new RuntimeException(e);
        } catch (IOException e) {
            Log.e("sendAPIRequest", "Failed to open connection to " + apiServerHost + ":" + apiServerPort + "/" + file);
            Toast.makeText(this, "Could not connect to API server", Toast.LENGTH_LONG).show();
            throw new RuntimeException(e);
        }
    }
}
