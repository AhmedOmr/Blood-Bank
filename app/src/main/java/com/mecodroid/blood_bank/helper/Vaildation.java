package com.mecodroid.blood_bank.helper;

import android.text.TextUtils;
import android.util.Patterns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Vaildation {

    public static final SimpleDateFormat BIRTHDAY_FORMAT_PARSER = new SimpleDateFormat("yyyy-MM-dd");

    public static final String DASH_STRING = "-";

    // This regex must accept Arabic letters,English letters, spaces and numbers
    public static final String USERNAME_PATERN = "^[\\u0621-\\u064Aa-zA-Z\\d\\-_\\s]{3,25}+$";

    public static boolean isValidName(String name) {

        return name.matches(USERNAME_PATERN);
    }

    public static boolean isValidPhone(String phone) {

        return phone.length() == 11 && TextUtils.isDigitsOnly(phone) && !TextUtils.isEmpty(phone);
    }

    public static boolean isValidPassword(String password) {

        return password.length() >= 3 && !TextUtils.isEmpty(password);
    }

    public static boolean isIdenticalPassword(String passwordConfirm, String fullConfirmPassword) {

        return passwordConfirm.equals(fullConfirmPassword) && !TextUtils.isEmpty(passwordConfirm)
                && !TextUtils.isEmpty(fullConfirmPassword);
    }

    public static boolean isValidEmail(String email) {

        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && !TextUtils.isEmpty(email);
    }


    public static boolean isEmptyField(String text) {

        return !TextUtils.isEmpty(text);
    }


    public static Calendar parseDateString(String date) {
        Calendar calendar = Calendar.getInstance();
        BIRTHDAY_FORMAT_PARSER.setLenient(false);
        try {
            calendar.setTime(BIRTHDAY_FORMAT_PARSER.parse(date));
        } catch (ParseException e) {
        }
        return calendar;
    }

    public static boolean isValidBirthday(String birthday) {
        Calendar calendar = parseDateString(birthday);
        int year = calendar.get(Calendar.YEAR);
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);

        return year >= 1900 && year < thisYear - 9;
    }

    public static boolean isValiddonationday(String donationday) {
        Calendar calendar = parseDateString(donationday);
        int year = calendar.get(Calendar.YEAR);
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int thismonth = Calendar.getInstance().get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int thisday = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        return year >= 1960 && year <= thisYear && month <= thismonth && day <= thisday;
    }

}
