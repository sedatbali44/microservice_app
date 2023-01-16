package com.sample.orderservice.Log;

import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ReflectionUtils {

    private final static ToStringStyle LOGGER_STYLE = new RecursiveToStringStyle();

    public static String objectToString(Object o) {

        try {

            if (o.getClass().getMethod("toString").getDeclaringClass() == o.getClass()) {
                return o.toString();
            } else {
                return ReflectionToStringBuilder.toString(o, LOGGER_STYLE, true, true);
            }

            // Ignore Exception
        } catch (NoSuchMethodException e) {
            return "No Result";
        } catch (SecurityException e) {
            return "Not Accessible";
        }

    }

    public static String convertArgumentArrayToString(Object[] arguments) {

        String result = "";

        if (arguments != null) {

            result = Arrays.stream(arguments)
                    .map(argument -> String.format("%s: %s", argument != null ? argument.getClass() : "null",
                            argument != null ? objectToString(argument) : "null"))
                    .collect(Collectors.joining("\n"));

        }

        return result;
    }

}
