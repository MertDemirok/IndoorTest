package com.example.mert.rssilistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    //Define
    private static ListView list;
    private static EditText editText;
    private static TextView textView;
    private static Button button;
    public List<ScanResult> wifi_list;
    public String loc_Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = (ListView) findViewById(R.id.listView);
        editText = (EditText) findViewById(R.id.text_name_loc);
        button = (Button) findViewById(R.id.button_save);
        textView = (TextView) findViewById(R.id.textView2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loc_Name = editText.getText().toString();
                textView.setText(loc_Name);
                Toast.makeText(MainActivity.this, loc_Name, Toast.LENGTH_SHORT).show();
            }

        });

        initializeWiFiListener();

        //Wifi scan result
        ArrayAdapter<ScanResult> adapter = new  ArrayAdapter<ScanResult>(this , android.R.layout.simple_list_item_1,
                android.R.id.text1,wifi_list);
        list.setAdapter(adapter);

    }


    //Wifi listener
    private void initializeWiFiListener(){

        //WifiManager open
        String connectivity_context = Context.WIFI_SERVICE;
        final WifiManager wifi = (WifiManager) getSystemService(connectivity_context);

        //Wifi is enable
        if(!wifi.isWifiEnabled()){
            if(wifi.getWifiState() != WifiManager.WIFI_STATE_ENABLING){
                wifi.setWifiEnabled(true);
            }
        }


         int maxLevel = 5;
         wifi_list = wifi.getScanResults();

        for (ScanResult result : wifi_list){

            //düşün
            //int level = wifi.calculateSignalLevel(result.level,maxLevel);
            //System.out.print(result.level + " test");

            int RSSI = result.level;
            String SSID = result.SSID;
            String  capabilities = result.capabilities;
            String MAC = wifi.getConnectionInfo().getMacAddress();

            //System.out.println( wifi_list.toString()+ " test1");
            System.out.println( " Location Name: "+loc_Name + " RSSI result: "+ RSSI + " SSID result: "+ SSID + " MAC result: " + MAC +" test2");
        }


        registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                WifiInfo info = wifi.getConnectionInfo();

            }
        }, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));

    }
}
