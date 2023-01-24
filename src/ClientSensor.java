import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;

import org.json.simple.JSONObject;


public class ClientSensor {
    public static Format TimeStamp;
    public static double temperature;
    public static double humidity;
    public static JSONObject data;

    public static String SensorDataGenerate() throws IOException {
        data = new JSONObject();
        temperature = 10 + 20 * Math.random();      // temperature varies from 10-30 celsius degree
        humidity = Math.random();                   // humidity varies from 0 to 1 (0% - 100%)
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStamp = timeStampFormat.format(System.currentTimeMillis());
        data.put("temperature",temperature);
        data.put("humidity",humidity);
        data.put("timestamp",timeStamp);
        StringWriter out = new StringWriter();
        data.writeJSONString(out);
        String jsonText = out.toString();

        return jsonText;
    }

    //post request
    public String restCallerPost(String path, String param) {
        //path
        //param json {}
        //interface ip
        String httpip = "http://10.2.1.28:80";

        int responseCode;
        //String urlParam = "?aaa=1&bbb=2";
        String urlParam = "";

        String data = "";

        //url concatenation
        String lasturl = httpip + path + urlParam;
        try {
            URL restURL = new URL(lasturl);
            HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
            conn.setRequestMethod("POST");
            //request head
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setDoOutput(true);

            //input stream
            //OutputStream os = conn.getOutputStream();
            OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            os.write(param);
            os.flush();
            // output response code
            responseCode = conn.getResponseCode();
            // output response
            if(responseCode == 200){
                //os
                //BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
                data = reader.readLine();
            } else {
                data = "false";
            }

            os.close();
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    public static void main(String[] args) throws IOException {
        String dataString = SensorDataGenerate();
        RestCallerUtil rcuPost = new RestCallerUtil();
        String resultDataPost = rcuPost.restCallerPost(pathPost, paramPost);
        System.out.println(resultDataPost);
    }
}