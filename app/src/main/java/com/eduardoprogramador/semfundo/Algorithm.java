package com.eduardoprogramador.semfundo;

/*
 * Copyright 2022. Eduardo Programador
 * www.eduardoprogramador.com
 * consultoria@eduardoprogramador.com
 *
 * Todos os direitos reservados
 * */

import android.util.Base64;
import com.eduardoprogramador.http.Http;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Algorithm {

    public static boolean removeBackground(String imagePath, String outputImagePath) {
        Http http = Http.buildRequest();

        ArrayList<ArrayList> post = new ArrayList<>();

        ArrayList<String> raw = new ArrayList<>();
        raw.add("raw");
        File temp = new File(imagePath);
        byte[] read = null;
        int bytesLen = 0;
        try {
            FileInputStream fileInputStream = new FileInputStream(temp);
            bytesLen = (int) temp.length();
            read = new byte[bytesLen];
            fileInputStream.read(read);
            fileInputStream.close();
        } catch (Exception ex) {
            return false;
        }

        raw.add((Base64.encodeToString(read,Base64.DEFAULT)));
        post.add(raw);

        ArrayList<String> raw_name = new ArrayList<>();
        raw_name.add("raw_name");
        raw_name.add(temp.getName());
        post.add(raw_name);

        ArrayList<String> password = new ArrayList<>();
        password.add("password");
        password.add("clubedoeduardoprogramador");
        post.add(password);

        return http.post("eduardoprogramador.com",true,443,"/php/sem_fundo_api.php",post,null,outputImagePath);
    }

    public static String getNameRandom() {
        String res = "eduardo_programador_";

        Random random = new Random();

        for (int i = 0; i < 20; i++) {
            int rand = random.nextInt(9);
            res += String.valueOf(rand);
        }

        res += ".png";

        return res;
    }

    public static void setEduardoProgramador() {
        Http http = Http.buildRequest();
        ArrayList<ArrayList> posts = new ArrayList<>();

        ArrayList<String> service = new ArrayList<>();
        service.add("service");
        service.add("Sem Fundo");
        posts.add(service);

        ArrayList<String> date = new ArrayList<>();
        date.add("date");
        date.add(new Date().toString());
        posts.add(date);

        ArrayList<String> time = new ArrayList<>();
        time.add("time");
        time.add(new Date().toString());
        posts.add(time);

        ArrayList<String> param = new ArrayList<>();
        param.add("param");
        param.add("Android");
        posts.add(param);

        http.post("eduardoprogramador.com",true,443,"/php/services.php",posts,null);
    }

}
