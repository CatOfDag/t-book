package com.tbook.Utils;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class NetCom implements C {

    private static final String UrlPath = "http://192.168.0.103:8080/";
    private String innerPath;
    private String data;//json data
    private String token;
    private Tips tips = new Tips();


    public NetCom(String innerPath, String data, String token){
        this.innerPath = innerPath;
        this.data = data;
        this.token = token;
    }

    public Tips CommunicationFun(){
        try {
            URL url = new URL(UrlPath + innerPath);

            HttpURLConnection conn=(HttpURLConnection) url.openConnection(); //开启连接
            conn.setConnectTimeout(5000);

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("ser-Agent", "Fiddler");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("token", token);

            if (data != null){
                OutputStream os=conn.getOutputStream();
                os.write(data.getBytes()); //字符串写进二进流
                os.close();
            }

            if(conn.getResponseCode()==200){   //与后台交互成功返回 200
                //读取返回的token
                InputStream inputStream=conn.getInputStream();
                Gson gson = new Gson();
                tips = gson.fromJson(inputStreamToString.STS(inputStream),Tips.class);
            }else{
                tips.setCode(CODE_NET_CONTACT_FALSE);
                tips.setInfo("连接超时,请检查网络");
            }
        }catch (SocketTimeoutException | SocketException e){
            System.out.println(e);
            tips.setCode(CODE_NET_CONTACT_FALSE);
            tips.setInfo("连接超时,请检查网络");

        } catch (IOException e) {
            System.out.println(e);
            tips.setCode(CODE_INTERNAL_EXCEPTION);
            tips.setInfo("系统内部异常");
        }
        return tips;
    }
}
