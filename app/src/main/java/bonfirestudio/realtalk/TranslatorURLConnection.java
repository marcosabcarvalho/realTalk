package bonfirestudio.realtalk;

import android.util.Log;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by indig_000 on 2016-06-25.
 */
public class TranslatorURLConnection {
    private static final String USER_AGENT = "Mozilla/5.0";

    private TranslatorURLConnection() {};

    // HTTP POST request
    public static String sendPost(String language, String text) throws Exception {

        String url = "https://translate.yandex.net/api/v1.5/tr.json/translate?";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        Log.d("tag", "Opened HTTP URL Connection");

        //add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "lang="+ language +"&text='"+ text +"'&key=trnsl.1.1.20160625T173147Z.970d09e8322d1eca.7410911983e269b1b50b0d89744990ece5bd3269";

        Log.d("tag", "Set POST request");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream()); //WHAT THE FUCK?
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        Log.d("tag", "Finished writing");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        JSONParser parser = new JSONParser();
        return ((JSONObject) parser.parse(response.toString())).get("text").toString();
    }

}
