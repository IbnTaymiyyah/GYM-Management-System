package com.ibntaymiyya.gym.controllers;

import com.ibntaymiyya.gym.Main;
import com.ibntaymiyya.gym.model.MainModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

public class AccountInfoTab extends Setting{


    public static final ObservableList<String> permissions;


    private static final String[] upperChar = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z".split(" ");
    private static final String[] lowerChar = "a b c d e f g h i j k l m n o p q r s t u v w x y z".split(" ");
    private static final String[] digits = "0 1 2 3 4 5 6 7 8 9".split(" ");
    private static final String[] specialCharacters = "! @ # & ( ) – [ { } ]: ; ' , ? / * ~ $ ^ + = < > -".split(" ");


    static {

        permissions = FXCollections.observableArrayList("تغيير الاسعار و الشهور","تعديل صلاحيات الحساب","اضافة حساب","حذف قاعدة بيانات البرنامج", "حذف الحسابات") ;


    }




    public AccountInfoTab() {


    }
}

