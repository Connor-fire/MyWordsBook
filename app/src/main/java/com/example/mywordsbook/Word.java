package com.example.mywordsbook;

public class Word {
    String CHN;
    String ENG;
    String SEN;

    public Word(String ENG,String CHN,String SEN){
        this.CHN=CHN;
        this.ENG=ENG;
        this.SEN=SEN;
    }

    public String getCHN() {
        return CHN;
    }

    public String getENG() {
        return ENG;
    }

    public void setCHN(String CHN) {
        this.CHN = CHN;
    }

    public void setENG(String ENG) {
        this.ENG = ENG;
    }

    public String getSEN() {
        return SEN;
    }

    public void setSEN(String SEN) {
        this.SEN = SEN;
    }
}
