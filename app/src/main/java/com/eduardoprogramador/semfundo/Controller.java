package com.eduardoprogramador.semfundo;

    /*
    Copyright 2022. Eduardo Programador
        www.eduardoprogramador.com
        consultoria@eduardoprogramador.com

    Todos os direitos reservados

     */


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.StrictMode;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class Controller {
    private Activity context;
    private AlertDialog alertDialog;
    public static final int PERMISSION_WRITE = 1234;
    public static final int PERMISSION_READ = 1235;

    public Controller(Activity context) {
        this.context = context;
        configPolicies();
    }

    public boolean checkIfNetIsEnabled() {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if(wifiManager.isWifiEnabled() || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected())
            return true;
        else
            return false;
    }

    private void turnOnWifi() {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);
    }

    public boolean doProcessFiles() {
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_WRITE);
        }

        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_READ);
        }

        if(ActivityCompat.checkSelfPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ||
        ActivityCompat.checkSelfPermission(context,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }

    }

    private void configPolicies() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void showInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Sem Internet");
        builder.setMessage("Você precisa ativar a internet para usar o aplicativo. Deseja ativar o Wi-Fi agora?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                turnOnWifi();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Algorithm.setEduardoProgramador();
                    }
                }).start();
                alertDialog.cancel();
                if(alertDialog != null)
                    alertDialog.dismiss();
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "Você não poderá usar o app sem ativar a internet", Toast.LENGTH_LONG).show();
                alertDialog.cancel();
                if(alertDialog != null)
                    alertDialog.dismiss();
            }
        });

        alertDialog = builder.show();
    }

    public void showPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Erro de Permissão");
        builder.setMessage("Você precisa autorizar o app a ler e gravar arquivos em seu dispositivo. Deseja ativar a permissão agora?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                doProcessFiles();
                alertDialog.cancel();
                if(alertDialog != null)
                    alertDialog.dismiss();
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "Você não poderá usar o app sem ativar a permissão de leitura e escrita", Toast.LENGTH_LONG).show();
                alertDialog.cancel();
                if(alertDialog != null)
                    alertDialog.dismiss();
            }
        });

        alertDialog = builder.show();
    }
}
