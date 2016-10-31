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
    private PrintStream output;
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
                String gre_word_to_pass=gre_words_array_list.get(position);
                Intent goToTTSActivity=new Intent(DataStorage.this, DefinitionTTS.class);
                goToTTSActivity.putExtra("word",gre_word_to_pass);
                startActivity(goToTTSActivity);
            }
        });



    }

    public void readFromFile(){
        Scanner scan=new Scanner(getResources().openRawResource(R.raw.grewords));
        while(scan.hasNextLine()){
            String line=scan.nextLine();
            String parts[]=line.split("\t");
            if(parts.length>=2) {
                String gre_word = parts[0];
                gre_words_array_list.add(gre_word);
            }
        }
        if(scan!=null){
            scan.close();
        }
    }

    public void press_enter_button(View view) {
        new_word=(EditText)this.findViewById(R.id.add_own_word);
        new_definition=(EditText)this.findViewById(R.id.add_own_word_definition);

        if(new_word.getText().toString().equals("") || new_definition.getText().toString().equals("")){
            Toast.makeText(this,"Enter both", Toast.LENGTH_SHORT).show();
        }else{
            try {
                output=new PrintStream(openFileOutput("own_gre_dictionary", Context.MODE_APPEND));
                output.println(new_word.getText().toString());
                output.println("\t");
                output.println(new_definition.getText().toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if(output!=null){
                output.close();
            }
            Toast.makeText(DataStorage.this,"Data Entered Successfully "+getFilesDir(), Toast.LENGTH_SHORT).show();
        }
        new_word.setText("");
        new_definition.setText("");
    }

    public void press_view_button(View view) {
        Intent open_detail_activity=new Intent(DataStorage.this, DetailActivity.class);
        startActivity(open_detail_activity);
    }
}
