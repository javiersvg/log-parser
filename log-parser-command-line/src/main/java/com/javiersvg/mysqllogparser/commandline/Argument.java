package com.javiersvg.mysqllogparser.commandline;

import com.javiersvg.mysqllogparser.MySqlLogParserException;

import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Argument {
    private static final String ERROR_ARGUMENT_FORMAT_KEY = "error.argument.format";
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("cmd_messages");
    private static final Pattern ARGUMENT_PATTERN = Pattern.compile("--(.*)=(.*)");
    private String value;

    Argument(String value) {
        this.value = value;
    }

    String name() {
        Matcher matcher = ARGUMENT_PATTERN.matcher(value);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw  new MySqlLogParserException(String.format(resourceBundle.getString(ERROR_ARGUMENT_FORMAT_KEY), value));
        }
    }

    <T> T value(Function<String, T> function) {
        Matcher matcher = ARGUMENT_PATTERN.matcher(value);
        if (matcher.find()) {
            return function.apply(matcher.group(2));
        } else {
            throw  new MySqlLogParserException(String.format(resourceBundle.getString(ERROR_ARGUMENT_FORMAT_KEY), value));
        }
    }
}
