package com.goldbach.aut.main;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeParser;

public class ObjectWithJodaTimeReferences {

    private DateTime jodaDatTime;


    public static String convertDateFormat(String inputStrDate, String inputFormat, String outputFormat) {
        //some parser to do something...
        DateTimeParser parser = DateTimeFormat.forPattern(inputFormat).getParser();

        return "some String";
    }
}
