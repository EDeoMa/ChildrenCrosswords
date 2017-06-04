package com.dbkudryavtsev.childrencrosswords.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.dbkudryavtsev.childrencrosswords.R;

import static com.dbkudryavtsev.childrencrosswords.utilities.LocalCrosswordsRepository.deleteAnswers;
import static com.dbkudryavtsev.childrencrosswords.utilities.ResourcesBuilder.downloadCrosswords;

public final class SettingsActivity extends Activity {

    private static final String[] menuStrings = {"Обновить кроссворды", "Сбросить прогресс", "Фон кроссвордов"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ListView settingsList = (ListView) findViewById(R.id.settings_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                menuStrings);
        settingsList.setAdapter(adapter);
        settingsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch ((int) id){
                    case 0: {
                        downloadCrosswords(getBaseContext());
                        return;
                    }
                    case 1: {
                        if(!deleteAnswers(SettingsActivity.this))
                        Toast.makeText(SettingsActivity.this,
                                "Проблема с удалением файлов.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    default:{

                    }
                }
            }
        });
    }
}
