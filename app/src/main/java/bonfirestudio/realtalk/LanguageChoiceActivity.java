package bonfirestudio.realtalk;


import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.parser.ParseException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownServiceException;
import java.util.Locale;
import java.util.Map;

public class LanguageChoiceActivity extends AppCompatActivity {
    private String newString = new String();
    private TextToSpeech textToSpeech;
    private String[] text;
    private Button backBtn;
    private Button playAgainBtn;
    private Spinner langSpinner;

    private String keyCode;

    private Map<String, String> langKeyMap;

    private TextView dialogueText;
    private TextView translatedDialogueText;
    private ImageButton playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_choice);
        text = getIntent().getExtras().getStringArray("text");

        langSpinner = (Spinner)findViewById(R.id.langSpinner);
        String[] values = {"English", "French", "Spanish", "Japanese", "Chinese", "Hindi", "Arabic", "Korean", "German"};
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, values);
        langSpinner.setAdapter(stringArrayAdapter);

        langKeyMap = new HashMap<>();
        langKeyMap.put("English", "en_US");
        langKeyMap.put("French","fr_FR");
        langKeyMap.put("Spanish","es_ES");
        langKeyMap.put("Japanese","ja_JP");
        langKeyMap.put("Chinese","zh_CN");
        langKeyMap.put("Hindi","hi_IN");
        langKeyMap.put("Arabic","ar_KR");
        langKeyMap.put("Korean","ko_DE");
        langKeyMap.put("German","de_EG");


        langSpinner.setOnItemSelectedListener((new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  keyCode = langKeyMap.get(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }));


        dialogueText = (TextView) findViewById(R.id.dialogueText);


        Toast.makeText(LanguageChoiceActivity.this, text.toString(), Toast.LENGTH_LONG);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    StringBuilder sb = new StringBuilder();
                    for(String s: text) {
                        sb.append(s);
                        sb.append("");
                        break;
                    }
                    //String newString = new String();
                    newString = sb.toString();
                    dialogueText.setText(newString);
                    if(sb.toString().contains("*")) {
                        newString = sb.toString().replace("*", "");
                    }
                    speak();
                }
                else {
                    speak();
                }
            }
        });

        backBtn = (Button)findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LanguageChoiceActivity.this, MainActivity.class));
            }
        });


        playAgainBtn = (Button)findViewById(R.id.playAgainBtn);
        playAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });

    }

    private void speak() {
        //textToSpeech.setSpeechRate(100);

        //Log.d("tag", "Passed into speak method: " + textToSpeak);
        //TranslatorURLConnection.sendPost("fr", "Hello I am speaking in English");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    newString = TranslatorURLConnection.sendPost(keyCode, newString);

                    int length = newString.length();

                    newString = newString.replace("[", "").replace("]", "").replace("\"", "").replace("\\", "").replace("'", "");
                    translatedDialogueText = (TextView) findViewById(R.id.translatedDialogueText);
                    translatedDialogueText.setText(newString);
                }
                catch (NetworkOnMainThreadException e){
                    Log.d("Exception", "NetworkOnMainThreadException caughtttttt");
                }
                catch (Exception e) {
                    Log.e("tag", "Exception caught " + e.getMessage() + " " + e.getStackTrace());
                    System.out.println(e.getMessage() + " " + e.getStackTrace() + e.getCause());
                }
            }
        });
        thread.start();

        try {
            //thread.sleep(5000);
            thread.join();
        }catch (Exception e){
            Log.d("tag", "Why");
        }
        Locale locale = new Locale(keyCode);
        textToSpeech.setLanguage(locale);
        textToSpeech.speak(newString, TextToSpeech.QUEUE_FLUSH, null, "unique");
    }
}
