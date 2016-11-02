package com.example.kanwalpc.storagettscamera;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class DataStorage extends AppCompatActivity {
    private PrintStream stream;
    private ListView display_gre_words;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> gre_words_array_list;
    private EditText new_word, new_definition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_storage);

        display_gre_words=(ListView)this.findViewById(R.id.display_list_words);
        gre_words_array_list=new ArrayList<>();
        readFromFile();
        adapter=new ArrayAdapter<String>(DataStorage.this, android.R.layout.simple_list_item_1,
                        gre_words_array_list);
        display_gre_words.setAdapter(adapter);
        display_gre_words.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent move_to_definition=new Intent(DataStorage.this, DefinitionTTS.class);
                move_to_definition.putExtra("words", gre_words_array_list.get(position));
                startActivity(move_to_definition);
            }
        });
    }

   public void readFromFile(){
       Scanner output=new Scanner(getResources().openRawResource(R.raw.grewords));
       while(output.hasNextLine()) {
           String value = output.nextLine();
           String parts[] = value.split("\t");
           if (parts.length >= 2) {
               gre_words_array_list.add(parts[0]);
           }
       }
       if(output!=null) {
           output.close();
       }
   }

    public void press_enter_button(View view) {
        new_word = (EditText) this.findViewById(R.id.add_own_word);
        new_definition = (EditText) this.findViewById(R.id.add_own_word_definition);

        if (new_word.getText().toString().equals("") || new_definition.getText().toString().equals("")) {
            Toast.makeText(this, "Enter both", Toast.LENGTH_SHORT).show();
        } else {
            writeIntoInternalFile();
            Toast.makeText(this, "Data Stored at " + getFilesDir(), Toast.LENGTH_SHORT).show();
        }
        new_word.setText("");
        new_definition.setText("");
    }

    public void writeIntoInternalFile(){
        try {
            stream=new PrintStream(openFileOutput("gre_own_words.txt",Context.MODE_APPEND));
            stream.print(new_word.getText().toString());
            stream.println("\t");
            stream.print(new_definition.getText().toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(stream!=null){
            stream.close();
        }
    }

    public void press_view_button(View view) {
        Intent send_to_view_dictionary_act=new Intent(DataStorage.this, DetailActivity.class);
        startActivity(send_to_view_dictionary_act);
    }
}
