
package com.ibntaymiyya.gym.Util;
import java.io.*;
import java.util.Properties;

public class SaveFileManager {


    private static final String externalSaveFilePath = "save/Text_Values.properties";


    private static final String DEFAULT_PROPERTIES = """
        # Default properties
        appName=GYM
        btnMonth1=1
        btnMonth2=3
        btnMonth3=6
        btnPay1=250
        btnPay2=600
        btnPay3=1000
        currentuser=
        currentuserpassword=
        perm1=
        perm2=
        perm3=
        perm4=
        perm5=
        """;



   /*Create Folder and File if NOT Exist   */

    private static void createDefaultPropertiesFile() {
        File file = new File(externalSaveFilePath);


        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();


                try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                    fileOutputStream.write(DEFAULT_PROPERTIES.getBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* Get TEXT into Properties File */

    public static String getText(String key) {
        Properties properties = new Properties();
        File file = new File(externalSaveFilePath);

        createDefaultPropertiesFile();

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties.getProperty(key);
    }

    /* SET TEXT into Properties File */


    public static void setText(String key, String value) {
        Properties properties = new Properties();
        File file = new File(externalSaveFilePath);


        createDefaultPropertiesFile();


        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }


        properties.setProperty(key, value);

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            properties.store(fileOutputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*SET MIX (int value) into Properties File */


    public static void setMix(String key, int value) {
        Properties properties = new Properties();
        File file = new File(externalSaveFilePath);


        createDefaultPropertiesFile();


        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }


        properties.setProperty(key, String.valueOf(value));


        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            properties.store(fileOutputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


