package com.example.ahmedorabi.lightintensity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Ahmed Orabi on 09/03/2017.
 */
public class LDR extends AppCompatActivity {

    String address=null;
    ProgressDialog progressDialog;
    BluetoothAdapter BA;
    BluetoothSocket bluetoothSocket;
    boolean isBTConnected= false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    TextView tv_data;
    Button btn;
    byte [] buffer = new byte[1024];
    String message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ldr);

        Intent intent = getIntent();
        address = intent.getStringExtra("ADDRESS");

        tv_data= (TextView) findViewById(R.id.tv_data);
        btn= (Button) findViewById(R.id.get_data);

        ConnectBT connectBT = new ConnectBT();
        connectBT.execute();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                  int bytes=  bluetoothSocket.getInputStream().read(buffer);
                    message=buffer.toString();

                   String s = new String(buffer,0,bytes);

                    tv_data.setText(s);
                } catch (IOException e) {
                    Toast.makeText(getBaseContext(),"Error in Getting data",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private class ConnectBT extends AsyncTask<Void,Void,Void> {

        boolean connectSuccess =true;

        @Override
        protected void onPreExecute() {
            progressDialog= ProgressDialog.show(LDR.this,"Loading..","Please Wait");
        }



        @Override
        protected Void doInBackground(Void... params) {

            try {
                if (bluetoothSocket == null || !isBTConnected) {

                    BA = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice bluetoothDevice = BA.getRemoteDevice(address);
                    bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    bluetoothSocket.connect();


                }
            }
            catch (Exception e){
                connectSuccess=false;
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(!connectSuccess){
                Toast.makeText(getBaseContext(),"Connection Failed. Is it a SPP Bluetooth? Try again.",Toast.LENGTH_LONG).show();
                finish();

            }else{
                Toast.makeText(getBaseContext(),"Connected",Toast.LENGTH_LONG).show();
                isBTConnected = true;
            }
            progressDialog.dismiss();
        }

    }
}
