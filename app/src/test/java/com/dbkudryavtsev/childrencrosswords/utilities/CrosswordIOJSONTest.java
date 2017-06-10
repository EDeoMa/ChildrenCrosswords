package com.dbkudryavtsev.childrencrosswords.utilities;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class CrosswordIOJSONTest { //TODO VK  // не хватает тестов на краевые случаи. Что будет если подать пустой массив? null? некорректный json?
    @Test
    public void parseAnswersFromJson() throws Exception {
        String jsonString = "{ \"answers\": [{\n" +
                "        \"answer\": \"РАСЧЁСКА\"},\n" +
                "        {\n" +
                "        \"answer\": \"ВОРОБЕЙ\"},\n" +
                "        {\n" +
                "        \"answer\": \"РОБОТ\"},\n" +
                "        {\n" +
                "        \"answer\": \"СОРОКА\"}] }";

        String[] rightAnswers = new String[]{"РАСЧЁСКА", "ВОРОБЕЙ", "РОБОТ", "СОРОКА"};

        String[] result = CrosswordIOJSON.parseAnswersFromJson(jsonString);

        assertEquals(rightAnswers, result);
    }
}
