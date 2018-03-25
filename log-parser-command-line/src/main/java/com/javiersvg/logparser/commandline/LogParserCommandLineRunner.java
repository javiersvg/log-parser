package com.javiersvg.logparser.commandline;

import com.javiersvg.logparser.LogParser;

public class LogParserCommandLineRunner {
    public static void main(String... args) {
        Arguments arguments = new Arguments(args);
        arguments.validate("startDate", "duration", "threshold");
        new LogParser(arguments.dateArgument("startDate"),
                arguments.durationArgument("duration"),
                arguments.intArgument("threshold")).parse();
    }
}
