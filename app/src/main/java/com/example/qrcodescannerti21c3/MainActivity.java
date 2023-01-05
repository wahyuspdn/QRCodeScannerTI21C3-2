package com.example.qrcodescannerti21c3;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.jar.Attributes;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //View Object
    private Button buttonScanning;
    private TextView textViewName, textViewClass, textViewId;
    //qr scanning object
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //View Object
        buttonScanning = (Button) findViewById(R.id.buttonScan);
        textViewName = (TextView) findViewById(R.id.nama);
        textViewClass = (TextView) findViewById(R.id.kelas);
        textViewId = (TextView) findViewById(R.id.nim);
        //Inisialisasi Scan Object
        qrScan = new IntentIntegrator(this);
        //Implementasi onclick Listener
        buttonScanning.setOnClickListener(this);
    }
    //Untuk Mendapatkan Hasil Scanning


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if (result !=null) {
            //Jika qrcode tidak ada sama sekali
            if (result.getContents() == null) {
                Toast.makeText(this, "Hasil Scanning tidak ada", Toast.LENGTH_SHORT).show();
            } else if (Patterns.WEB_URL.matcher(result.getContents()).matches()) {
                Intent visitUrl = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getContents()));
                startActivity(visitUrl);
            } else if (Patterns.PHONE.matcher(result.getContents()).matches()) {
                String telp = String.valueOf(result.getContents());
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + telp));
                startActivity(callIntent);
            } else if(Patterns.WEB_URL.matcher(result.getContents()).matches()) {
                    try {
                        Uri uri = Uri.parse(result.getContents());
                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);

                        //Set Package
                        intent.setPackage("com.google.android.apps.maps");

                        //Set Flag
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);
                    } catch (ActivityNotFoundException e){
                        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");

                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
            } else {
                //jika qr code tidak ditemukan datanya
                try {
                    //konversi datanya ke json
                    JSONObject obj = new JSONObject(result.getContents());
                    //diset nilai datanya ke textviews
                    textViewName.setText(obj.getString("nama"));
                    textViewId.setText(obj.getString("kelas"));
                    textViewClass.setText(obj.getString("nim"));
                }catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                }
            }

    } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
}
@Override
    public void onClick(View v) {
        //inisialisasi qrcode scanning
    qrScan.initiateScan();

}
}