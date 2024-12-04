package org.agonzalez.bankin.msvc.transactions.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Utils {

    public static String generateRandomNum(int length) {
        StringBuilder accountId = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomNum = (int) (Math.random() * 9);
            accountId.append(randomNum);
        }
        return (accountId.toString());
    }

    public static Date dateFormater() {
        return (Utils.dateFormater(0, 0));
    }

    public static Date dateFormater(int yearsToAdd, int hoursToAdd) {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.YEAR, yearsToAdd);
        calendar.add(Calendar.HOUR_OF_DAY, hoursToAdd);
        return (calendar.getTime());
    }

}
