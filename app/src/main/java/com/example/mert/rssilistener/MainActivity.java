package com.example.mert.rssilistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
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

import com.example.mert.rssilistener.datatype_Class.AccessPointData;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends ActionBarActivity {

    //Define
    AccessPointData AP_Data;
    private static ListView list;
    private static EditText editText;
    private static TextView textView;
    private static Button button;
    public List<ScanResult> wifi_list;
    public String loc_Name;
    public int RSSI;
    public String SSID;
    public String MAC;


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

    public static String POST(String url,AccessPointData AP_Data){

        InputStream inputStream = null;
        String post_result="";
        try {
            //create HttpClient
            HttpClient httpClient = new DefaultHttpClient();
            //make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";

            //json build
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("Location_Name:",AP_Data.getLoc_Name_post());
            jsonObject.accumulate("RSSI:",AP_Data.getRSSI_post());
            jsonObject.accumulate("SSID:",AP_Data.getSSID_post());
            jsonObject.accumulate("MAC:",AP_Data.getMAC_post());

            //convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            //set json to StringEntity
            StringEntity se = new StringEntity(json);

            //set httpPost Entity
            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");


            //Execute POST request to the given URL
            HttpResponse httpResponse = httpClient.execute(httpPost);

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            //convert inputstream to string
            if(inputStream != null) {

                post_result = convertInputStreamToString(inputStream);
            }else
                post_result = "did not work!";

        }catch (Exception e){
            Log.d("InputStream",e.getLocalizedMessage());
        }

        return post_result;

    }


    private class  HttpAsyncTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {

            AP_Data = new AccessPointData();
            AP_Data.setLoc_Name_post(loc_Name);
            AP_Data.setRSSI_post(String.valueOf(RSSI));
            AP_Data.setSSID_post(SSID);
            AP_Data.setMAC_post(MAC);

            return POST(urls[0],AP_Data);
        }

        @Override
        protected void onPostExecute(String result) {
           Toast.makeText(getBaseContext(),"data send!",Toast.LENGTH_LONG).show();
        }
    }



    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

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

            RSSI = result.level;
            SSID = result.SSID;
            String  capabilities = result.capabilities;
            MAC = wifi.getConnectionInfo().getMacAddress();

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
