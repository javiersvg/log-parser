package com.javiersvg.mysqllogparser.commandline;

import com.javiersvg.mysqllogparser.MySqlLogParser;

public class MySqlLogParserCommandLineRunner {
    public static void main(String... args) {
        Arguments arguments = new Arguments(args);
        arguments.validate("startDate", "duration", "threshold");
        new MySqlLogParser(arguments.dateArgument("startDate"),
                arguments.durationArgument("duration"),
                arguments.intArgument("threshold")).parse();
    }
}
