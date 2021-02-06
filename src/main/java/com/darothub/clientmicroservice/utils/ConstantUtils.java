package com.darothub.clientmicroservice.utils;

public class ConstantUtils {
    public static final String GENDER_PATTERN = "Male|male|Female|female";
    public static final String CHAR_PATTERN = "[a-zA-z\\s]+";
    public static final String NUMBER_PATTERN = "\\d{13}";
    public static final String PASSWORD = "^ (?=.*\\\\d) (?=\\\\S+$) (?=.* [@#$%^&+=]) (?=.* [a-z]) (?=.* [A-Z]). {8,10}$";
}
