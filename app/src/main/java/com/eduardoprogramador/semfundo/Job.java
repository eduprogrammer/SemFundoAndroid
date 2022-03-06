/*
    Copyright 2022. Eduardo Programador
        www.eduardoprogramador.com
        consultoria@eduardoprogramador.com

    Todos os direitos reservados

     */

package com.eduardoprogramador.semfundo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Job extends AppCompatActivity implements View.OnClickListener {
    private Button btnIn, btnStart;
    private Controller controller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job);

        btnIn = findViewById(R.id.button_in);
        btnStart = findViewById(R.id.button_start);

        controller = new Controller(Job.this);

        if(controller.checkIfNetIsEnabled()) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Algorithm.setEduardoProgramador();
                }
            }).start();


            if(controller.doProcessFiles()) {
                btnStart.setEnabled(true);
            }
        } else {
            controller.showInternetDialog();
        }

        btnIn.setOnClickListener(this);
        btnStart.setOnClickListener(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i = 0; i < permissions.length; i++) {
            if(permissions[i].equalsIgnoreCase(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if(requestCode == Controller.PERMISSION_WRITE) {
                    if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        btnStart.setEnabled(true);
                    } else {
                        btnStart.setEnabled(false);
                        controller.showPermissionDialog();
                    }
                }
            } else if(permissions[i].equalsIgnoreCase(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                if(requestCode == Controller.PERMISSION_READ) {
                    if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        btnStart.setEnabled(true);
                    } else {
                        btnStart.setEnabled(false);
                        controller.showPermissionDialog();
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_in:
                if(controller.doProcessFiles()) {
                    Intent intent = new Intent(Job.this, FileDialog.class);
                    startActivity(intent);
                }
                break;

            case R.id.button_start:
                if(controller.doProcessFiles()) {
                    if (controller.checkIfNetIsEnabled()) {
                        //code to remove background task...
                    } else {
                        controller.showInternetDialog();
                    }
                }

                break;
        }
    }
}
