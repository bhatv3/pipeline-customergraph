package com.intuit.sbg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

/**
 * Created by vikasbhat on 2/28/18.
 */
public class Utils {

    public static void writeToLocalLog(String from, String msg) {
        write("/tmp/cust_stream_proc.log", from, msg);
    }

    public static void write(String fileName, String from, String msg) {
        try {
            Files.write(Paths.get(fileName), Arrays.asList(from + " -> " + msg), UTF_8, APPEND, CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
}
