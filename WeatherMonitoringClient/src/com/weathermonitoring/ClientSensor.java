package com.weathermonitoring;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;


// 	• {"temperature": XXX, "humidity":xxx, "timestamp":"xx yy mm"}
public class ClientSensor {
    public static Format TimeStamp;
    public static double temperature;
    public static double humidity;
    //public static JSONObject data;// unmodifiable map view to the JSON object name/value mappings
//JSON change to data
    public static void main(String[] args) throws IOException {
            String dataString = SensorDataGenerate();
            String postResponse = restCallerPost("", dataString);
            System.out.println("hello,world!");
            System.out.println(postResponse);
        }


    public static String SensorDataGenerate() throws IOException {
        temperature = 10 + 20 * Math.random();      // temperature varies from 10-30 celsius degree
        humidity = Math.random();                   // humidity varies from 0 to 1 (0% - 100%)
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStamp = timeStampFormat.format(System.currentTimeMillis());
        /*data.put("temperature",temperature);
        data.put("humidity",humidity);
        data.put("timestamp",timeStamp);*/
        String jsonText = "temperature:" + temperature+"humidity:"+humidity+"timeStamp:"+timeStamp;
        System.out.println(jsonText);
        return jsonText;// Finally give back a string!!!
    }

    //post request
    public static String restCallerPost(String path, String param) {
        //path
        //param json {}
        //interface ip
//        String httpip = "http://10.2.1.28:80";
        String httpip = "http://127.0.0.1:8081";
        int responseCode;
        //String urlParam = "?aaa=1&bbb=2";
        String urlParam = "";

        String data = "";

        //url concatenation
        String lasturl = httpip + path + urlParam;
        System.out.println(lasturl);
        try {
            System.out.println("hello,try!");
            URL restURL = new URL(lasturl);//ip -> lasturl -> restURL
            //System.out.println("restURL:",restURL);
            HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
            conn.setRequestMethod("POST");// 设置发送请求的方法。
            //request head
            // 设置使用标准编码格式编码参数的名-值对
            System.out.println("hello,1!");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            System.out.println("2");
            conn.setDoOutput(true);//set up as we can output to HttpURLConnection

            System.out.println("3");
            //input stream
            //OutputStream os = conn.getOutputStream();
            OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            os.write(param);
            os.flush();
            System.out.println("4");
            // output response code
            responseCode = conn.getResponseCode();
            System.out.println("response code: "+responseCode);
            // output response
            if(responseCode == 200){
                System.out.println("200");
                //os
                //BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
                data = reader.readLine();
            } else {
                System.out.println("else");
                data = "false";
            }

            System.out.println("4");
            os.close();
            conn.disconnect();

        } catch (Exception e) {//what is this 'e'???
            System.out.println("hello,catch!");
            e.printStackTrace();
        }

        System.out.println("data:"+data);
        return data;
    }
}
