package com.example.wifidolgozat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.Formatter;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.Format;

public class MainActivity extends AppCompatActivity {
    private TextView textview;
    private BottomNavigationView nav;

    private WifiManager wifiManager;
    private WifiInfo wifiInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        nav.setOnNavigationItemReselectedListener(
                new BottomNavigationView.OnNavigationItemReselectedListener() {
                    @Override
                    public void onNavigationItemReselected(@NonNull MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.wifiOn:
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                {
                                    textview.setText("Nincs jogosultság a wifi állapot módosítására.");
                                    Intent panel = new Intent(Settings.Panel.ACTION_WIFI);
                                    startActivityForResult(panel,0);
                                    return;
                                }
                                wifiManager.setWifiEnabled(true);
                                textview.setText("Wifi bekapcsolva");
                                break;
                            case R.id.wifiOff:
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                {
                                    textview.setText("Nincs jogosultság a wifi állapot módosítására.");
                                    Intent panel = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
                                    startActivityForResult(panel,0);
                                    return;
                                }
                                wifiManager.setWifiEnabled(false);
                                textview.setText("Wifi kikapcsolva");
                                break;
                            case R.id.wifiInfo:
                                ConnectivityManager connManager =
                                        (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo netInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                                if (netInfo.isConnected()) {
                                    int ip_int = wifiInfo.getIpAddress();
                                    String ip = Formatter.formatIpAddress(ip_int);
                                    textview.setText("IP cím: "+ip);
                                }
                                else
                                {
                                    textview.setText("Nem csatlakozik wifi hálózathoz");
                                }
                                break;
                        }
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0)
        {
            if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLING
                    || wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED)
            {textview.setText("Wifi kikapcsolva");}
            else if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING
                    || wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED)
            {textview.setText("Wifi bekapcsolva");}
        }
    }

    private void init() {
        textview = findViewById(R.id.textInfo);
        nav = findViewById(R.id.navrow);
        wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
    }
}