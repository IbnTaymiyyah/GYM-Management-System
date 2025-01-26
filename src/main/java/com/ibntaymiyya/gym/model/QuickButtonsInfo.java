package com.ibntaymiyya.gym.model;

public class QuickButtonsInfo {

    private String nameOfQuickBtn1;
    private String formattedDateStart;
    private String formattedDateEnd;
    private int quickMonth;
    private int quickPay;




    // Getter and Setter


    public String getNameOfQuickBtn1(){
        return nameOfQuickBtn1;
    }

    public void setNameOfQuickBtn1(String nameOfQuickBtn1){
        this.nameOfQuickBtn1 = nameOfQuickBtn1;
    }

    public String getFormattedDateStart(){
        return formattedDateStart;
    }
    public void setFormattedDateStart(String formattedDateStart){
        this.formattedDateStart =formattedDateStart;
    }

    public String getFormattedDateEnd(){
        return formattedDateEnd;
    }
    public void setFormattedDateEnd(String formattedDateEnd){
        this.formattedDateEnd =formattedDateEnd;
    }

    public int getQuickMonth(){
        return quickMonth;
    }

    public void setQuickMonth(int quickMonth){
        this.quickMonth =quickMonth;
    }

    public void setQuickPay(int quickPay){this.quickPay = quickPay;}

    public int getQuickPay(){return quickPay;}



}
