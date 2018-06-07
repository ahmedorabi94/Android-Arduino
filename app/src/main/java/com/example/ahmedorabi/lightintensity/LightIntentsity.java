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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Ahmed Orabi on 06/03/2017.
 */
public class LightIntentsity extends AppCompatActivity {

    Button on ,off,disconnect;
    SeekBar seekBar;
    String address=null;
    ProgressDialog progressDialog;
    BluetoothAdapter BA;
    BluetoothSocket bluetoothSocket;
    boolean isBTConnected= false;
   static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    TextView lumn;
   // String id = UUID.randomUUID().toString();


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.light);

        Intent intent = getIntent();
        address = intent.getStringExtra("ADDRESS");

        on= (Button) findViewById(R.id.btn_on);
        off= (Button) findViewById(R.id.btn_off);
        disconnect= (Button) findViewById(R.id.btn_disconnect);
        seekBar= (SeekBar) findViewById(R.id.seekbar);

        lumn= (TextView) findViewById(R.id.lumn);

        ConnectBT connectBT = new ConnectBT();
        connectBT.execute();

        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluetoothSocket!=null){
                    try {
                        bluetoothSocket.getOutputStream().write("on".toString().getBytes());
                    } catch (IOException e) {
                        Toast.makeText(getBaseContext(),"Error",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluetoothSocket!=null){
                    try {
                        bluetoothSocket.getOutputStream().write("off".toString().getBytes());
                      //  byte [] buffer = new byte[1024];
                       // bluetoothSocket.getInputStream().read(buffer);
                       // buffer.toString()

                    } catch (IOException e) {
                        Toast.makeText(getBaseContext(),"Error",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluetoothSocket!=null){
                    try {
                        bluetoothSocket.close();
                    } catch (IOException e) {
                        Toast.makeText(getBaseContext(),"Error",Toast.LENGTH_LONG).show();
                    }
                }
                finish();

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(fromUser==true){
                    try {
                        bluetoothSocket.getOutputStream().write(String.valueOf(progress).getBytes());
                        lumn.setText(String.valueOf(progress));
                    } catch (Exception e) {
                        Toast.makeText(getBaseContext(),"Error",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private class ConnectBT extends AsyncTask<Void,Void,Void>{

        boolean connectSuccess =true;

        @Override
        protected void onPreExecute() {
           progressDialog=ProgressDialog.show(LightIntentsity.this,"Loading..","Please Wait");
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
