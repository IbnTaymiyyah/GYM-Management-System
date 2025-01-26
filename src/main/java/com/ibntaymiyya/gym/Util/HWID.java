package com.ibntaymiyya.gym.Util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HWID {

    private String getHWID() {

        try {

            // Collect info
            String toEncrypt = System.getenv("COMPUTERNAME") + System.getProperty("user.name") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL");

            // String to Hash
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(toEncrypt.getBytes());
            byte[] byteData = md.digest();


            // Convert Hash to HEX
            StringBuilder hexString = new StringBuilder();
            for (byte b : byteData){
                String hex = Integer.toHexString(0xff & b);
                if (hexString.length() == 1) hexString.append(0);
                hexString.append(hex);
            }


            // Return as String
            return hexString.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }



    }



    public String codeHWID(){
        String hwid = getHWID();

        return hwid;
    }

}
