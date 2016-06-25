package bonfirestudio.realtalk;


import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
public class LanguageChoiceActivity extends AppCompatActivity {
    private TextToSpeech textToSpeech;
    private String[] text;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_choice);
        text = getIntent().getExtras().getStringArray("text");
        Toast.makeText(LanguageChoiceActivity.this, text.toString(), Toast.LENGTH_LONG);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    speak(text.toString());
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


    }


    private void speak(String textToSpeak) {
        //textToSpeech.setSpeechRate(100);
        textToSpeech.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, "unique");

    }


}
