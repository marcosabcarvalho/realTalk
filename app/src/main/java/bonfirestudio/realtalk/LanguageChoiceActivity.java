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
import android.widget.ImageView;
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
    private String translatedText;
    private TextToSpeech textToSpeech;
    private String[] text;
    private Button backBtn;
    private Button playAgainBtn;
    private Spinner langSpinner;

    private String[] keyCode;
    private String originalText;
    private Map<String, String[]> langKeyMap;

    private TextView dialogueText;
    private TextView translatedDialogueText;
    private ImageButton playButton;

    private ImageView meme;

    private boolean isDankMeme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_choice);
        isDankMeme = false;
        text = getIntent().getExtras().getStringArray("text");

        langSpinner = (Spinner)findViewById(R.id.langSpinner);
        String[] values = {"English", "French", "Spanish", "Japanese", "Chinese", "Hindi", "Russian", "Korean", "Dank Memez"};

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, values);
        langSpinner.setAdapter(stringArrayAdapter);

        translatedDialogueText = (TextView) findViewById(R.id.translatedDialogueText);

        langKeyMap = new HashMap<>();
        langKeyMap.put("English", new String[]{"en_US", "en"});
        langKeyMap.put("French",new String[]{"fr_FR", "fr"});
        langKeyMap.put("Spanish", new String[]{"es_ES","es"});
        langKeyMap.put("Japanese", new String[]{"ja_JP", "ja"});
        langKeyMap.put("Chinese", new String[]{"zh_CN", "zh"});
        langKeyMap.put("Hindi", new String[]{"hi_IN", "hi"});
        langKeyMap.put("Russian", new String[]{"ru_", "ru"});
        langKeyMap.put("Korean", new String[]{"ko_DE", "ko"});
        langKeyMap.put("Dank Memez", new String[]{"de_EG", "de"});


        keyCode = langKeyMap.get("English");

        langSpinner.setOnItemSelectedListener((new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  keyCode = langKeyMap.get(parent.getItemAtPosition(position).toString());
                  if(position == 8) {
                      isDankMeme = true;
                  }
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
                    for (String s : text) {
                            sb.append(s);
                            sb.append("");
                            break;
                    }

                    originalText = sb.toString();
                    dialogueText.setText(originalText);





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

                    translatedText = TranslatorURLConnection.sendPost(keyCode[1], originalText);

                    translatedText = translatedText.replace("[", "").replace("]", "").replace("\"", "").replace("\\", "").replace("'", "");
                } catch (NetworkOnMainThreadException e) {
                    Log.d("Exception", "NetworkOnMainThreadException caughtttttt");
                } catch (Exception e) {
                    Log.e("tag", "Exception caught " + e.getMessage() + " " + e.getStackTrace());
                    System.out.println(e.getMessage() + " " + e.getStackTrace() + e.getCause());
                }
            }
        });
        thread.start();

        try {
            //thread.sleep(5000);
            thread.join();
        } catch (Exception e) {
            Log.d("tag", "Why");
        }

        if (isDankMeme){
            translatedDialogueText.setText("Translated Dank Meme:");

            int memeRand = (int) ((Math.random()*5));
            meme = (ImageView) findViewById(R.id.imageView);

            if (memeRand == 0)
                meme.setImageResource(R.drawable.datboi);
            else if (memeRand == 1)
                meme.setImageResource(R.drawable.dt);
            else if (memeRand == 2)
                meme.setImageResource(R.drawable.krabs);
            else if (memeRand == 3)
                meme.setImageResource(R.drawable.pepe1);
            else if (memeRand == 4)
                meme.setImageResource(R.drawable.pepe2);
            else if (memeRand == 5)
                meme.setImageResource(R.drawable.spongegar);

            isDankMeme = false;

        }
        else{

            translatedDialogueText.setText(translatedText);
            Locale locale = new Locale(keyCode[0]);
            textToSpeech.setLanguage(locale);
            textToSpeech.speak(translatedText, TextToSpeech.QUEUE_FLUSH, null, "unique");
        }
    }
}
