package com.dbkudryavtsev.ccw.childrencrosswords.main;

class Cwords {
    private String question;
    private String word;
    private int posX;
    private int posY;

    Cwords(String question, String word, int posX, int posY) {
        this.question = question;
        this.word = word;
        this.posX=posX;
        this.posY=posY;
    }

    String getQuestion() {
        return question;
    }

    String getWord(){
        return word;
    }

    int getPosX() {
        return posX;
    }

    int getPosY() { return posY; }
}