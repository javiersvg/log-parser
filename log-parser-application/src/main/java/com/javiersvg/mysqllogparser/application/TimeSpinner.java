package com.javiersvg.mysqllogparser.application;

import javafx.scene.control.SpinnerValueFactory;
import javafx.util.converter.LocalTimeStringConverter;

import java.time.LocalTime;
import java.time.format.FormatStyle;

public class TimeSpinner extends SpinnerValueFactory<LocalTime> {

    TimeSpinner(){
        setConverter(new LocalTimeStringConverter(FormatStyle.MEDIUM));
    }

    @Override
    public void decrement(int steps) {
        if (getValue() == null)
            setValue(LocalTime.now());
        else {
            LocalTime time = getValue();
            setValue(time.minusMinutes(steps));
        }
    }

    @Override
    public void increment(int steps) {
        if (this.getValue() == null)
            setValue(LocalTime.now());
        else {
            LocalTime time = getValue();
            setValue(time.plusMinutes(steps));
        }
    }
}
