/*
    Copyright 2022. Eduardo Programador
        www.eduardoprogramador.com
        consultoria@eduardoprogramador.com

    Todos os direitos reservados

     */

package com.eduardoprogramador.semfundo;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class Itens extends ArrayAdapter {
    private ArrayList<Integer> images;
    private ArrayList<String> names, paths, sizes;
    private Activity context;

    public Itens(@NonNull Activity context, int resource, ArrayList<Integer> images, ArrayList<String> names, ArrayList<String> paths, ArrayList<String> sizes) {
        super(context, R.layout.dialog_itens, paths);
        this.images = images;
        this.names = names;
        this.paths = paths;
        this.sizes = sizes;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_itens,null,true);

        TextView txtName = view.findViewById(R.id.text_photo_name);
        TextView txtPath = view.findViewById(R.id.text_photo_path);
        TextView txtSize = view.findViewById(R.id.text_photo_size);
        ImageView img = view.findViewById(R.id.image_photo);

        txtName.setText("Nome:" + names.get(position));
        txtPath.setText("Caminho: " + paths.get(position));
        if(!sizes.get(position).equalsIgnoreCase(""))
            txtSize.setText("Tamanho: " + sizes.get(position));

        if(images.get(position) == 0) {

            img.setImageURI(Uri.parse("file://" + paths.get(position)));
        } else if(images.get(position) == -1) {

            img.setImageResource(R.drawable.ic_folder);
        }

        return view;
    }
}
