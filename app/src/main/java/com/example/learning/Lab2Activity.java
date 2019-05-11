package com.example.learning;

import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Lab2Activity extends AppCompatActivity {

    private static String TAG="Lab2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab2);
        //Run network operations as thread or asynchronous tasks
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setupUI();
    }

    private void setupUI(){
        Button mButton = (Button) findViewById(R.id.downloadButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDownloadUrl();
            }
        });

        Button mAsyncButton= (Button) findViewById(R.id.downloadAsyncButton);
        mAsyncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasInternetAccess()) {
                    String url = ((EditText) findViewById(R.id.txtURL)).getText().toString();
                    DownloadWebPageTask task = new DownloadWebPageTask();
                    task.execute(new String[] { url });
                }
                else
                    Toast.makeText(getApplicationContext(), "No Hay Conexion a Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startDownloadUrl(){
        EditText mUrl = (EditText) findViewById(R.id.txtURL);

        if(hasInternetAccess()) {
            try {
                getURLContent(mUrl.getText().toString());
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error durante la descarga", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        else
            Toast.makeText(getApplicationContext(), "No Hay Conexion a Internet", Toast.LENGTH_SHORT).show();
    }

    private class DownloadWebPageTask extends AsyncTask<String, Void, String>{
        //Download Progress Bar
        private ProgressDialog mProgress;

        @Override
        protected String doInBackground(String... url) {
            OkHttpClient client = new OkHttpClient();
            Request request = null;

            try{
                request = new Request
                        .Builder()
                        .url(url[0])
                        .build();
            } catch (Exception e){
                Log.i(TAG,"error in the URL");
                return "Download failed";
            }

            Response response = null;
            try{
                response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    return response.body().string();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgress = new ProgressDialog(Lab2Activity.this);
            //Evita cancelar el loading
            mProgress.setCancelable(false);

            mProgress.setMessage("Downloading website content");
            mProgress.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            startDownloadUrl();

            mProgress.dismiss();
        }
    }

    public boolean hasInternetAccess(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void getURLContent(String url) throws IOException  {
        String res = "";
        URL urlToFetch = new URL(url);

        HttpURLConnection urlConnection = (HttpURLConnection) urlToFetch.openConnection();
        InputStream stream = urlConnection.getInputStream();

        //Convertir a string los bytes descargados
        res = readStreamEfficiently(stream);
        urlConnection.disconnect();

        //Mostrar contenido descargado
        EditText mContent = (EditText) findViewById(R.id.txtDownloadedContent);
        mContent.setText(res);
    }

    public String readStream(InputStream stream){
        Reader reader = new InputStreamReader(stream);
        BufferedReader buffer = new BufferedReader(reader);

        String response = "";
        String chunkJustRead = "";

        try{
            while((chunkJustRead = buffer.readLine()) != null)
                response += chunkJustRead;
        } catch (IOException ioex){
            ioex.printStackTrace();
        }
        return response;
    }

    private String readStreamEfficiently(InputStream inputStream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder total = new StringBuilder();
        for (String line; (line = r.readLine()) != null; ) {
            total.append(line).append('\n');
        }
        return total.toString();
    }
}