/*
    Copyright 2022. Eduardo Programador
        www.eduardoprogramador.com
        consultoria@eduardoprogramador.com

    Todos os direitos reservados

     */

package com.eduardoprogramador.semfundo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;

public class Process extends AppCompatActivity implements View.OnClickListener {

    private TextView txtResult;
    private ProgressBar progressBarFile;
    private String path;
    private ImageView imgRemoved;
    private Button btnGo, btnShare;
    private String complete;

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_renew) {
            Intent intent = new Intent(Process.this,Job.class);
            startActivity(intent);
        } else if(view.getId() == R.id.button_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/png");
            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(Process.this,BuildConfig.APPLICATION_ID + ".provider",new File(complete)));
            startActivity(Intent.createChooser(intent,"Compartilhar Imagem"));
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.process);

        btnGo = findViewById(R.id.button_renew);
        btnShare = findViewById(R.id.button_share);
        txtResult = findViewById(R.id.text_result);
        progressBarFile = findViewById(R.id.progress_file);
        imgRemoved = findViewById(R.id.image_removed);
        Intent intent = getIntent();
        if(intent != null)
            path = intent.getStringExtra("path_intent");

        imgRemoved.setVisibility(View.INVISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {

                complete = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + Algorithm.getNameRandom();

                if(Algorithm.removeBackground(path, complete)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBarFile.setVisibility(View.INVISIBLE);
                            txtResult.setText("Fundo Removido Com Sucesso");
                            imgRemoved.setVisibility(View.VISIBLE);
                            imgRemoved.setImageURI(Uri.parse("file://" + complete));

                            btnGo.setVisibility(View.VISIBLE);
                            btnShare.setVisibility(View.VISIBLE);
                        }
                    });



                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBarFile.setVisibility(View.INVISIBLE);
                            txtResult.setText("Falha ao remover fundo de imagem: ");
                            imgRemoved.setVisibility(View.INVISIBLE);

                            btnGo.setVisibility(View.INVISIBLE);
                            btnShare.setVisibility(View.INVISIBLE);

                        }
                    });
                }
            }
        }).start();

        btnShare.setOnClickListener(this);
        btnGo.setOnClickListener(this);
    }

}
