package com.example.ahmedorabi.lightintensity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    Button paired;
    ListView list;

    BluetoothAdapter BA;
    Set<BluetoothDevice> PairedDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        paired = (Button) findViewById(R.id.btn_showPaired);
        list= (ListView) findViewById(R.id.lv);

        BA = BluetoothAdapter.getDefaultAdapter();

        if(BA==null){
            Toast.makeText(getBaseContext(),"Bluetooth Device Not Available",Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            if(BA.isEnabled()){}
            else{
                Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnBTon,1);
            }
        }

        paired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPairedDevicesList();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


               String info = (String) parent.getItemAtPosition(position);


                String Address = info.substring(info.length()-17);
                Toast.makeText(getBaseContext(),Address,Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this,LDR.class);
                intent.putExtra("ADDRESS",Address);
                startActivity(intent);


            }
        });



    }

    private void showPairedDevicesList() {

        PairedDevices = BA.getBondedDevices();
        ArrayList li = new ArrayList();


        for(BluetoothDevice b : PairedDevices){
            li.add(b.getName() + "\n" + b.getAddress());
        }

        ArrayAdapter adapter = new ArrayAdapter(getBaseContext(),R.layout.list_item,li);

        list.setAdapter(adapter);



    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
