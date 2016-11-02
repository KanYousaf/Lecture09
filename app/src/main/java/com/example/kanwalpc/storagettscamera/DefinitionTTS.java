package com.example.kanwalpc.storagettscamera;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class DefinitionTTS extends AppCompatActivity {
    private TextView display_recorded_voice;
    private Locale currentSpokenLanguage = Locale.US;
    private TextView display_definition;
    private HashMap<String, String> dictionary;
    private TextToSpeech tts;
    private static final int SPEECH = 125;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definition_tts);

        tts=new TextToSpeech(DefinitionTTS.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                convertTextToSpeech();
            }
        });

        display_recorded_voice = (TextView) this.findViewById(R.id.display_recorded_Voice);

        dictionary=new HashMap<>();
        display_definition = (TextView) this.findViewById(R.id.display_gre_definiton);
        readFromRawFile();

        Intent receiver=getIntent();
        String gre_word_received=receiver.getStringExtra("words");

        if(dictionary.containsKey(gre_word_received)){
            display_definition.setText(dictionary.get(gre_word_received));
        }



    }

    public void readFromRawFile(){
        Scanner output=new Scanner(getResources().openRawResource(R.raw.grewords));
        while(output.hasNextLine()){
            String value=output.nextLine();
            String parts[] = value.split("\t");
            if(parts.length>=2) {
                String gre_word = parts[0];
                String gre_definition = parts[1];
                dictionary.put(gre_word, gre_definition);
            }
        }
  }

    public void convertTextToSpeech(){
        tts.setLanguage(currentSpokenLanguage);
        String toSpeak=display_definition.getText().toString();
        tts.speak(toSpeak,TextToSpeech.QUEUE_FLUSH, null);

    }


    public void press_listen_button(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something!");
        try {
            startActivityForResult(intent, SPEECH);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(this, "Your Device Doesn't Support Speech to Text", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SPEECH:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> list = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (list == null || list.isEmpty()) {
                        return;
                    }
                    String spoken_text = list.get(0);
                    display_recorded_voice.setText(spoken_text);
                }
                break;
        }
    }



}
