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
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class FileDialog extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private File file;
    private StringBuilder stringBuilder;
    private String curPath, lastPath;
    private ListView listFiles;
    private ArrayList<Integer> images;
    private ArrayList<String> names, paths, sizes;
    private ArrayList<Boolean> isDirectory;
    private TextView txtPath;
    private int position;
    private Itens itens;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_dialog);

        Controller controller = new Controller(FileDialog.this);
        ImageView imgBack = findViewById(R.id.image_back);
        ImageView imgForward = findViewById(R.id.image_forward);
        listFiles = findViewById(R.id.list_files);
        txtPath = findViewById(R.id.text_cur_path);

        if(controller.doProcessFiles())
            startFileSearch();

        listFiles.setOnItemClickListener(this);
        imgBack.setOnClickListener(this);
        imgForward.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i = 0; i < permissions.length; i++) {
            if(permissions[i].equalsIgnoreCase(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if(requestCode == Controller.PERMISSION_WRITE) {
                    if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        startFileSearch();
                    } else {
                        Toast.makeText(this, "Ative a permissão para ler arquivos", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            } else if(permissions[i].equalsIgnoreCase(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                if(requestCode == Controller.PERMISSION_READ) {
                    if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        startFileSearch();
                    } else {
                        Toast.makeText(this, "Ative a permissão para ler arquivos", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                goBack();
                break;

            case R.id.image_forward:
                goForward();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

        position = pos;

        if(isDirectory.get(pos)) {
            lastPath = curPath;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    deepIntoFolders(paths.get(position));
                }
            }).start();
        } else {
            Intent intent = new Intent(FileDialog.this, Process.class);
            intent.putExtra("path_intent",paths.get(pos));
            startActivity(intent);
        }
    }

    private void deepIntoFolders(String path) {

        file = new File(path);
        File[] files = null;
        if(file != null) {
            files = file.listFiles();
        }

        images = new ArrayList<>();
        names = new ArrayList<>();
        paths = new ArrayList<>();
        sizes = new ArrayList<>();
        isDirectory = new ArrayList<>();

        if(files == null)
            return;

        for(File unity : files) {
            if(unity.isDirectory()) {
                //directory
                isDirectory.add(true);
                images.add(-1);
                names.add(unity.getName());
                paths.add(unity.getPath());
                sizes.add("");

            } else {
                //not directory
                if(isImage(unity.getName())) {
                    isDirectory.add(false);
                    images.add(0);
                    names.add(unity.getName());
                    paths.add(unity.getPath());
                    sizes.add(getFileSize(unity.length()));
                }
            }
        }

        itens = new Itens(FileDialog.this,R.layout.dialog_itens,images,names,paths,sizes);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listFiles.setAdapter(itens);
                itens.notifyDataSetChanged();
                curPath = file.getPath();
            }
        });


    }

    private void startFileSearch() {


        new Thread(new Runnable() {
            @Override
            public void run() {
                deepIntoFolders(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtPath.setText("Local Atual: " + curPath);
                    }
                });
            }
        }).start();

    }

    private String getFileSize(long bytes) {

        StringBuilder stringBuilder = new StringBuilder();

        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        if(bytes < 1000) {
            stringBuilder.append("" + bytes + " bytes");
        } else if(bytes >= 1000 && bytes < 1000000) {
            stringBuilder.append("" + decimalFormat.format((float) bytes / 1000) + " KB");
        } else if(bytes >= 1000000 && bytes < 1000000000) {
            stringBuilder.append("" + decimalFormat.format((float) bytes / 1000000) + " MB");
        } else if(bytes >= 1000000000) {
            stringBuilder.append("" + decimalFormat.format((float) bytes / 1000000000) + " GB");
        }

        return stringBuilder.toString();
    }

    private boolean isImage(String fileName) {
        String[] parts = fileName.split("\\.");
        if(parts[parts.length - 1].equalsIgnoreCase("png")) {
            return true;
        } else if(parts[parts.length - 1].equalsIgnoreCase("jpg")) {
            return true;
        } else if(parts[parts.length - 1].equalsIgnoreCase("jpeg")) {
            return true;
        } else {
            return false;
        }
    }

    private void goBack() {

        lastPath = curPath;

        stringBuilder = new StringBuilder();
        String[] splitted = curPath.split("/");
        for (int i = 1; i < splitted.length - 1; i++) {
            stringBuilder.append("/" + splitted[i]);
        }

        if(splitted.length > 0) {


            new Thread(new Runnable() {
                @Override
                public void run() {
                    deepIntoFolders(stringBuilder.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtPath.setText("Local Atual: " + curPath);
                        }
                    });
                }
            }).start();
        }


    }

    private void goForward() {
        if(lastPath != null && !lastPath.equalsIgnoreCase(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath())) {


            new Thread(new Runnable() {
                @Override
                public void run() {
                    deepIntoFolders(lastPath);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtPath.setText("Local Atual: " + curPath);
                        }
                    });
                }
            }).start();
        }


    }

}
