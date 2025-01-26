package com.ibntaymiyya.gym;

import com.ibntaymiyya.gym.Util.SaveFileManager;

public class Launcher {

    public static void main(final String[] args) {
        Main.launch(Main.class,args);
        clearLoginData();
    }

    private static void clearLoginData(){

        SaveFileManager.setText("currentuser","");
        SaveFileManager.setText("currentuserpassword","");

        for (int id = 0 ; id < 5 ; id++){
            SaveFileManager.setText("perm"+(id+1),"");
        }
    }

}
