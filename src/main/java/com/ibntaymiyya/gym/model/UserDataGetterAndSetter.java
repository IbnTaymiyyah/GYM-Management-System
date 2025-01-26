package com.ibntaymiyya.gym.model;

import java.util.List;
import java.util.Map;

public class UserDataGetterAndSetter {

        private String password;
        private Map<Integer, Boolean> permissionsMap;

        // Getters and Setters
        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Map<Integer, Boolean> getPermissionsMap() {
            return permissionsMap;
        }

        public void setPermissionsMap(Map<Integer, Boolean> permissionsMap) {
            this.permissionsMap = permissionsMap;
        }




}