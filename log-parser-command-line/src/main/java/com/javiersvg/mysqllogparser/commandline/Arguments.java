package com.javiersvg.mysqllogparser.commandline;

import com.javiersvg.mysqllogparser.MySqlLogParserException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Arguments extends HashMap<String, Argument> {
    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("cmd_messages");

    Arguments(String... args) {
        this.putAll(Arrays.stream(args)
                .map(Argument::new)
                .collect(Collectors.toMap(Argument::name, Function.identity())));
    }

    LocalDateTime dateArgument(String argumentName) {
        return this.get(argumentName).value(s ->LocalDateTime.parse(s.replace( "." , "T" )));
    }

    Duration durationArgument(String argumentName) {
        String value = this.get(argumentName).value(Function.identity());
        switch (value) {
            case "hourly": return Duration.ofHours(1);
            case "daily": return Duration.ofDays(1);
            default: throw new MySqlLogParserException("Improper duration value");
        }
    }

    int intArgument(String argumentName) {
        return this.get(argumentName).value(Integer::valueOf);
    }

    void validate(String... requiredArguments) {
        if (!isEveryArgumentPresent(requiredArguments)) {
            String message = String.format(resourceBundle.getString("error.arguments.required"),Arrays.asList(requiredArguments));
            throw new MySqlLogParserException(message);
        }
    }

    private boolean isEveryArgumentPresent(String... requiredArguments) {
        return Arrays.stream(requiredArguments)
                .allMatch(this::containsKey);
    }
}
