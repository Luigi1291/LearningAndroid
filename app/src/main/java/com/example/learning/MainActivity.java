package com.example.learning;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG,"Estoy en onCreate");
        setupUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"Estoy en onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG,"Estoy en onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"Estoy en onPause");
    }

    @Override
    protected void onStop() {
        Log.i(TAG,"Estoy en onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG,"Estoy en onDestroy");
        super.onDestroy();
    }

    private void setupUI(){
        final TextView mTextView = (TextView) findViewById(R.id.textView);
        mTextView.setText(R.string.app_name);

        Button mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText mEditText = (EditText) findViewById(R.id.editText);
                mTextView.setText(mEditText.getText().toString());
                Log.i( "MainActivity", "content is:" + mTextView.getText().toString());
                Toast.makeText(getApplicationContext(), "HOLA", Toast.LENGTH_LONG).show();
            }
        });

        Button mNewButton = (Button) findViewById(R.id.newButton);
        mNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), NewActivity.class);
                startActivity(intent);
            }
        });
    }

    public void changeText(View v){

    }
}
