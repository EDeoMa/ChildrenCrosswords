package com.dbkudryavtsev.childrencrosswords.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.dbkudryavtsev.childrencrosswords.models.Crossword;
import com.dbkudryavtsev.childrencrosswords.models.CrosswordWord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class LocalCrosswordsRepository {

     //TODO переписать логику формирования имени файла на более явную
    private static String loadJSONFromFile(int chosenCrosswordId, String fileName, Context context) {
        String json = "";
        File inputFile = new File(context.getFilesDir(), fileName + chosenCrosswordId + ".json");
        if (inputFile.exists()) {
            try {
                InputStream inputStream = new FileInputStream(inputFile);
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                if(inputStream.read(buffer)<1) throw new IOException();
                inputStream.close();
                json = new String(buffer, "UTF-8");
            } catch (IOException ex) {
                Toast toast = Toast.makeText(context, "Не могу загрузить кроссворды из файла.", Toast.LENGTH_LONG);
                toast.show();
                ex.printStackTrace();
                return null;
            }
        }
        return json;
    }

    // TODO это не Builder!

    public static Crossword getCrossword(int chosenCrosswordId, Context context) {
        // TODO вынести в константы
        String jsonString = loadJSONFromFile(chosenCrosswordId,"crossword", context);
        return parseCrosswordFromJson(context, jsonString);
    }

    //TODO: вынести в отдельный класс, написать на него unit-теста
    //TODO Убрать работу с UI из бизнес-логики
    @NonNull
    private static Crossword parseCrosswordFromJson(Context context, String jsonString) {
        JSONObject jsonObject;
        JSONArray array = new JSONArray();
        CrosswordWord[] cwords = new CrosswordWord[]{};
        int horCount = 0;
        if (jsonString!=null && jsonString.length()>0) {
            try {
                jsonObject = new JSONObject(jsonString);
                array = jsonObject.getJSONArray("crosswordWord");
                cwords = new CrosswordWord[array.length()];
                horCount = (Integer) jsonObject.get("horCount");
            } catch (JSONException ex) {
                Toast toast = Toast.makeText(context, "Не могу создать JSON-объект для получения кроссворда.",
                        Toast.LENGTH_LONG);
                toast.show();
                ex.printStackTrace();
            }
            for (int i = 0; i < cwords.length; i++)
                try {
                    cwords[i] = new CrosswordWord(array.getJSONObject(i).getString("question"),
                            array.getJSONObject(i).getString("word"),
                            array.getJSONObject(i).getInt("posX"),
                            array.getJSONObject(i).getInt("posY"));

                } catch (JSONException ex) {
                    Toast toast = Toast.makeText(context, "Ошибка обращения к JSON-объекту с кроссвордами.",
                            Toast.LENGTH_LONG);
                    toast.show();
                    ex.printStackTrace();
                }
        }
        return new Crossword(cwords, horCount);
    }

    public static String[] getAnswers(int chosenCrosswordId, Context context) {
        String jsonString = loadJSONFromFile(chosenCrosswordId, "answers", context);
        String[] answers = new String[]{};
        JSONObject jsonObject;
        JSONArray array = new JSONArray();
        if (jsonString!=null && jsonString.length()>0) {
            try {
                jsonObject = new JSONObject(jsonString);
                array = jsonObject.getJSONArray("answers");
                answers = new String[array.length()];
            } catch (JSONException ex) {
                Toast toast = Toast.makeText(context, "Не могу создать JSON-объект для получения ответов.",
                        Toast.LENGTH_LONG);
                toast.show();
                ex.printStackTrace();
            }
            for (int i = 0; i < answers.length; i++)
                try {
                    answers[i] = array.getJSONObject(i).getString("answer");
                } catch (JSONException ex) {
                    Toast toast = Toast.makeText(context, "Ошибка обращения к JSON-объекту с ответами.",
                            Toast.LENGTH_LONG);
                    toast.show();
                    ex.printStackTrace();
                }
        }
        else {
            jsonString = loadJSONFromFile(chosenCrosswordId, "crossword", context);
            if (jsonString != null && jsonString.length() > 0) {
                try {
                    jsonObject = new JSONObject(jsonString);
                    array = jsonObject.getJSONArray("crosswordWord");
                    answers = new String[array.length()];
                } catch (JSONException ex) {
                    Toast toast = Toast.makeText(context, "Ошибка чтения длины кроссвордов.",
                            Toast.LENGTH_LONG);
                    toast.show();
                    ex.printStackTrace();
                }
                for (int i = 0; i < answers.length; i++)
                    answers[i] = "";
            }
        }
        return answers;
    }

    public static boolean deleteAnswers(Context context){
        File contentsDirectory = new File(context.getFilesDir().getAbsolutePath());
        for (File file: contentsDirectory.listFiles()){
            if(file.getName().contains("answers"))
                if(!file.delete()) {
                    return true;
                }
        }
        return false;
    }

    public static int getCrosswordsCount(Context context){
        int crosswordsCount = 0;
        File filesDirectory = context.getFilesDir();
        if(filesDirectory.length()!=0) {
            for (File file : filesDirectory.listFiles())
                if (file.getName().contains("crossword")) crosswordsCount++;
        }
        return crosswordsCount;
    }
}