package com.javiersvg.logparser.application;

import com.javiersvg.logparser.LogParser;
import com.javiersvg.logparser.LogParserException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class LogParserApplication extends Application {
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");
    private final FileChooser fileChooser = new FileChooser();
    private File file;
    private ExecutorService service = Executors.newFixedThreadPool(1);

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Log parser");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("Log parser");
        sceneTitle.setId("welcome-text");
        grid.add(sceneTitle, 0, 0, 2, 1);

        Label startDateLabel = new Label("Start date:");
        grid.add(startDateLabel, 0, 1);

        DatePicker startDate = new DatePicker();
        grid.add(startDate, 1, 1);

        Label timeLabel = new Label("Time:");
        grid.add(timeLabel, 0, 2);

        Spinner<LocalTime> time = timeSpinner();
        grid.add(time, 1, 2);

        Label durationLabel = new Label("Duration:");
        grid.add(durationLabel, 0, 3);

        ChoiceBox<DurationItem> duration = new ChoiceBox<>(FXCollections.observableArrayList(DurationItem.values()));
        grid.add(duration, 1, 3);

        Label thresholdLabel = new Label("Threshold:");
        grid.add(thresholdLabel, 0, 4);

        Spinner<Integer> threshold = new Spinner<>(1, 1000,    100);
        grid.add(threshold, 1, 4);

        Label fileLabel = new Label("Log file:");
        grid.add(fileLabel, 0, 5);

        TextField filePath = new TextField();
        grid.add(filePath, 1, 5);

        Button logFileChooser = new Button("...");
        grid.add(logFileChooser, 2, 5);

        logFileChooser.setOnAction(e -> {
            file = fileChooser.showOpenDialog(primaryStage);
            filePath.setText(file.getPath());
        });

        Button btn = new Button("Parse");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 6);

        final Text actiontarget = new Text();
        actiontarget.setId("actiontarget");
        grid.add(actiontarget, 1, 7);

        btn.setOnAction(e -> {
            Task task = new Task() {
                @Override
                protected Object call() {
                    LocalDateTime dateTime = LocalDateTime.of(startDate.getValue(), time.getValue());
                    new LogParser(dateTime, duration.getValue().getDuration(), threshold.getValue()).parse();
                    return null;
                }
            };
            task.setOnRunning(event -> actiontarget.setText("Processing log"));
            task.setOnSucceeded(event -> actiontarget.setText("Processing log finished"));
            task.setOnFailed(event -> actiontarget.setText("Processing log failed"));
            service.submit(task);
        });

        Scene scene = new Scene(grid, 300, 300);
        scene.getStylesheets().add(cssFile());
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> service.shutdown());

        primaryStage.show();
    }

    private String cssFile() {
        URL resource = getClass().getClassLoader().getResource("application.css");
        if (resource != null) {
            return resource.toExternalForm();
        } else {
            throw new LogParserException(resourceBundle.getString("error.properties.file"));
        }
    }

    private Spinner<LocalTime> timeSpinner() {
        Spinner<LocalTime> spinner = new Spinner<>(new TimeSpinner());
        spinner.setEditable(true);
        return spinner;
    }
}
