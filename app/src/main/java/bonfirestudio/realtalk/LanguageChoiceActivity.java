package bonfirestudio.realtalk;


import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
public class LanguageChoiceActivity extends AppCompatActivity {
    private TextToSpeech textToSpeech;
    private  String[] text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_choice);
        Bundle b = getIntent().getExtras();
        text = b.getStringArray("text");
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

    }


    private void speak(String textToSpeak) {
        textToSpeech.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, "unique");
    }


}
