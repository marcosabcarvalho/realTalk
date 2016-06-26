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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.parser.ParseException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownServiceException;
import java.util.Locale;

public class LanguageChoiceActivity extends AppCompatActivity {
    private String newString = new String();
    private TextToSpeech textToSpeech;
    private String[] text;
    private Button backBtn;
    private Button playAgainBtn;

    private String temp;

    private TextView dialogueText;
    private TextView translatedDialogueText;
    private ImageButton playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_choice);
        text = getIntent().getExtras().getStringArray("text");

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
                    speak(newString);
                    temp = new String(newString);
                }
                else {
                    speak("I couldn't get that, please say it again");
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
                speak(temp);
            }
        });

    }

    private void speak(String textToSpeak) {
        //textToSpeech.setSpeechRate(100);

        Log.d("tag", "Passed into speak method: " + textToSpeak);
        //TranslatorURLConnection.sendPost("fr", "Hello I am speaking in English");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //textToSpeak = TranslatorURLConnection.sendPost("fr", textToSpeak);
                    newString = TranslatorURLConnection.sendPost("fr", newString);

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
        Locale locale = new Locale("fr");
        textToSpeech.setLanguage(locale);
        textToSpeech.speak(newString, TextToSpeech.QUEUE_FLUSH, null, "unique");
    }
}
