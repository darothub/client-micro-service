package com.darothub.clientmicroservice.utils;

public enum Gender {
    MALE,
    FEMALE;
    static public Gender forNameIgnoreCase(String value){
        for(Gender gender:Gender.values()){
            if(gender.name().equalsIgnoreCase(value)) return gender;
        }
        return null;
    }
}
