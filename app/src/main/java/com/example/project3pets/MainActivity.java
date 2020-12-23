package com.example.project3pets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String ERROR = "404!";
    private static final String CNU_KEY = "CNU - Defender";
    private static final String CNU_URL = "https://www.pcs.cnu.edu/~kperkins/pets/pets.json";
    private static final String TENTON_URL = "https://www.tentonsoftware.com/pets";
    private static final String WINSTON_URL = "https://www.pcs.cnu.edu/~kperkins/pets/p0.png";
    private static final String HIGGENS_URL = "https://www.pcs.cnu.edu/~kperkins/pets/p1.png";
    private static final String BROCOLI_URL = "https://www.pcs.cnu.edu/~kperkins/pets/p2.png";
    private static final String DINOSAUR_URL = "https://tipsmake.com/data/images/hack-the-dinosaur-game-of-google-chrome-to-make-your-trex-immortal-and-max-speed-picture-1-Mj2qacw7X.jpg";

    private String myURL = CNU_URL;
    private String JSonKey;

    private String currImgURL;
    JSONArray petList;

    private Spinner spinner;

    private TextView tv1,tv2;
    WebImageView_TP mv;

    SharedPreferences.OnSharedPreferenceChangeListener listener = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences myPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        myPreference.registerOnSharedPreferenceChangeListener(listener);
        getPrefVal(myPreference);
        if (JSonKey.equals(CNU_KEY)){
            myURL = CNU_URL;
        }
        else{
            myURL = TENTON_URL;
        }
        getContentView();

        mv = (WebImageView_TP)findViewById(R.id.imageView);

        tv1 = (TextView)findViewById(R.id.tv1);
        tv2 = (TextView)findViewById(R.id.tv2);

    }

    private void getContentView(){
        if (!checkNetworkConectivity()) {
            setContentView(R.layout.no_network);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        else {
            setContentView(R.layout.activity_main);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            DownloadTask_TP myTask = new DownloadTask_TP(this);
            myTask.setnameValuePair("Name1","Value1");
            myTask.setnameValuePair("Name2","Value2");
            myTask.execute(myURL);

            DownloadTask_TP dt = new DownloadTask_TP(this);
            dt.execute(myURL);
            spinner = (Spinner) findViewById(R.id.spinner);

            ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MainActivity.this,
                    R.layout.spinner_item, getResources().getStringArray(R.array.names));
            myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(myAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    loadImage(spinner.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
    private void loadImage(String img){
        if (img.equals("Winston"))
            currImgURL = WINSTON_URL;
        else if (img.equals("Higgens"))
            currImgURL = HIGGENS_URL;
        else
            currImgURL = BROCOLI_URL;
        mv.setImageUrl(currImgURL);
    }

    private void getPrefVal(SharedPreferences settings){
        JSonKey = settings.getString("json", CNU_KEY);
    }
    private boolean checkNetworkConectivity(){
        ConnectivityCheck myCheck = new ConnectivityCheck(this);
        return myCheck.isNetworkReachable() || myCheck.isWifiReachable();
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
        if (id == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void processJSon(String string) {

        if (string == null){
            if (tv1 != null && tv2 != null) {
                tv1.setText(ERROR);
                tv2.setText(myURL);
            }
            if (spinner != null)
            spinner.setVisibility(View.GONE);
            if (mv != null)
            mv.setImageUrl(DINOSAUR_URL);
        }
        else {
            try {
                tv1.setText(null);
                tv2.setText(null);
                mv.setImageUrl(currImgURL);
                spinner.setVisibility(View.VISIBLE);
                JSONObject jsonobject = new JSONObject(string);

                //*********************************
                //makes JSON indented, easier to read

                // you must know what the data format is, a bit brittle
                petList = jsonobject.getJSONArray("pets");
                // how many entries
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Map<String, ?> all = sharedPreferences.getAll();
        Object value = all.get(key);
        if (String.valueOf(value).equals("CNU - Defender")){
            myURL = CNU_URL;
        }
        else{
            myURL = TENTON_URL;
        }
        DownloadTask_TP dt = new DownloadTask_TP(this);
        dt.execute(myURL);
    }
}
