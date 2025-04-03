package main.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import main.TrainRepositoryBinaryImpl;
import main.TrainRepositoryTextImpl;
import main.factory.TrainFactory;
import main.logic.Train;
import main.servise.TrainServise;

import java.util.*;

public class MainController {

    private final TrainServise trainServise = new TrainServise();
    @FXML
    private GridPane gridPane;
    List<Train> trains = TrainFactory.createTrains();
    TrainRepositoryBinaryImpl trainRepositoryBinaryImpl = new TrainRepositoryBinaryImpl();
    TrainRepositoryTextImpl trainRepositoryTextImpl = new TrainRepositoryTextImpl();

    public void close() {
        Platform.exit();
    }

    public String getFileName() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter File Name");
        dialog.setHeaderText("Enter File Name");
        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    public void readFileText() {
        trains = trainRepositoryTextImpl.readArray(getFileName());
    }

    public void readFileBinary() {
        trains = trainRepositoryBinaryImpl.readArray(getFileName());
    }

    public void writeInFileText() {
        trainRepositoryTextImpl.outputArray(trains, getFileName());
    }

    public void writeInFileBinary() {
        trainRepositoryBinaryImpl.outputArray(trains, getFileName());
    }

    public void findTrainsThatDepartAfterAGivenTime() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter departure time");
        dialog.setHeaderText("Enter departure time");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(departureTime -> {
            List<Train> task2 = trainServise.findTrainByDepartureTime(trains, departureTime);
            gridPane.getChildren().clear();
            trainServise.setGridTrainFields(gridPane);
            trainServise.setGridTrains(gridPane, task2);
        });
    }

    public void findTrainsByPointOfDestinationAndNumberOfSeats() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter point of destination");
        dialog.setHeaderText("Enter point of destination");
        Optional<String> result = dialog.showAndWait();
        dialog = new TextInputDialog();
        dialog.setTitle("Enter number of seats");
        dialog.setHeaderText("Enter number of seats");
        Optional<String> result1 = dialog.showAndWait();
        result1.ifPresent(numberOfSeats -> result.ifPresent(departureTime -> {
            List<Train> task3 = trainServise.
                    findTrainByPointOfDestinationAndNumberOfSeats(trains, departureTime, Integer.parseInt(numberOfSeats));
            gridPane.getChildren().clear();
            trainServise.setGridTrainFields(gridPane);
            trainServise.setGridTrains(gridPane, task3);
        }));
    }

    public void sortTrainsByNumberOfIntermediateStopsAndWhenMatchedByTrainNumber() {
        List<Train> task4 = trainServise.sortTrainsByNumberOfIntermediateStopsAndTrainNumber(trains);
        gridPane.getChildren().clear();
        trainServise.setGridTrainFields(gridPane);
        trainServise.setGridTrains(gridPane, task4);
    }

    public void forEachPointOfDestinationSortTrainsByTrainNumber() {
        Map<String, List<Train>> trainsMap = trainServise.
                createMapWithKeyPointOfDestinationAndValueListOfTrainsSortedByTrainNumber(trains);
        gridPane.getChildren().clear();
        trainServise.setGridTrainFields(gridPane);
        List<Train> list = new ArrayList<>();
        trainsMap.forEach((key, value) -> list.addAll(value));
        trainServise.setGridTrains(gridPane, list);
    }

    public void forEachPointOfDestinationTrainWithLessNumberOfIntermediateStops() {
        List<Train> trainsList = trainServise.findTrainsWithLessNumberOfIntermediateStopsByPointOfDestination(trains);
        gridPane.getChildren().clear();
        trainServise.setGridTrainFields(gridPane);
        trainServise.setGridTrains(gridPane, trainsList);
    }

    public void showTrains() {
        gridPane.getChildren().clear();
        trainServise.setGridTrainFields(gridPane);
        trainServise.setGridTrains(gridPane, trains);
    }

    public void addTrain() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Add train");
        dialog.setHeaderText("Add train");
        GridPane gridPane = new GridPane();
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
        TextField id = new TextField();
        TextField pointOfDestination = new TextField();
        TextField trainNumber = new TextField();
        TextField departureTime = new TextField();
        TextField numberOfSeats = new TextField();
        TextField travelTime = new TextField();
        TextField numberOfIntermediateStops = new TextField();
        gridPane.add(new Text("id"), 0, 0);
        gridPane.add(id, 0, 1);
        gridPane.add(new Text("point of destination"), 0, 2);
        gridPane.add(pointOfDestination, 0, 3);
        gridPane.add(new Text("train number"), 0, 4);
        gridPane.add(trainNumber, 0, 5);
        gridPane.add(new Text("departure time"), 0, 6);
        gridPane.add(departureTime, 0, 7);
        gridPane.add(new Text("number of seats"), 0, 8);
        gridPane.add(numberOfSeats, 0, 9);
        gridPane.add(new Text("travel time"), 0, 10);
        gridPane.add(travelTime, 0, 11);
        gridPane.add(new Text("number of intermediate stops"), 0, 12);
        gridPane.add(numberOfIntermediateStops, 0, 13);
        dialog.getDialogPane().setContent(gridPane);
        dialog.setResultConverter(button -> {
            if (button == okButtonType) {
                return id.getText() + "," + pointOfDestination.getText() + "," + trainNumber.getText()
                        + "," + departureTime.getText() + "," + numberOfSeats.getText() + "," + travelTime.getText()
                        + "," + numberOfIntermediateStops.getText();
            }
            return null;
        });
        Optional<String> result = dialog.showAndWait();
        result.map(st -> st.split(",")).ifPresent(s ->
                trains.add(TrainFactory.createTrain(Integer.parseInt(s[0]), s[1], Long.parseLong(s[2])
                        , s[3], Integer.parseInt(s[4]), s[5], Integer.parseInt(s[6]))));

    }

    public void findTrainsById() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter train id");
        dialog.setHeaderText("Enter train id");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(id -> {
            List<Train> trainsList = trainServise
                    .findTrainsById(trains, Integer.parseInt(id));
            gridPane.getChildren().clear();
            trainServise.setGridTrainFields(gridPane);
            trainServise.setGridTrains(gridPane, trainsList);
        });
    }

    public void findTrainsByPointOfDestination() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Point of Destination");
        dialog.setHeaderText("Enter Point of Destination");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(pointOfDestination -> {
            List<Train> trainsList = trainServise
                    .findTrainsByPointOfDestination(trains, pointOfDestination);
            gridPane.getChildren().clear();
            trainServise.setGridTrainFields(gridPane);
            trainServise.setGridTrains(gridPane, trainsList);
        });
    }

    public void findTrainsByTrainNumber() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter train number");
        dialog.setHeaderText("Enter train number");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(trainNumber -> {
            List<Train> trainsList = trainServise
                    .findTrainsByTrainNumber(trains, Long.parseLong(trainNumber));
            gridPane.getChildren().clear();
            trainServise.setGridTrainFields(gridPane);
            trainServise.setGridTrains(gridPane, trainsList);
        });
    }

    public void findTrainsByDepartureTime() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter departure time");
        dialog.setHeaderText("Enter departure time");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(departureTime -> {
            List<Train> trainsList = trainServise
                    .findTrainsByDepartureTime(trains, departureTime);
            gridPane.getChildren().clear();
            trainServise.setGridTrainFields(gridPane);
            trainServise.setGridTrains(gridPane, trainsList);
        });
    }

    public void findTrainsByNumberOfSeats() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter number of seats");
        dialog.setHeaderText("Enter number of seats");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(numberOfSeats -> {
            List<Train> trainsList = trainServise
                    .findTrainsByNumberOfSeats(trains, Integer.parseInt(numberOfSeats));
            gridPane.getChildren().clear();
            trainServise.setGridTrainFields(gridPane);
            trainServise.setGridTrains(gridPane, trainsList);
        });
    }

    public void findTrainsByTravelTime() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter travel time");
        dialog.setHeaderText("Enter travel time");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(travelTime -> {
            List<Train> trainsList = trainServise.findTrainsByTravelTime(trains, travelTime);
            gridPane.getChildren().clear();
            trainServise.setGridTrainFields(gridPane);
            trainServise.setGridTrains(gridPane, trainsList);
        });
    }

    public void findTrainsByNumberOfIntermediateStops() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter number of intermediate stops");
        dialog.setHeaderText("Enter number of intermediate stops");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(numberOfIntermediateStops -> {
            List<Train> trainsList = trainServise
                    .findTrainsByNumberOfIntermediateStops(trains, Integer.parseInt(numberOfIntermediateStops));
            gridPane.getChildren().clear();
            trainServise.setGridTrainFields(gridPane);
            trainServise.setGridTrains(gridPane, trainsList);
        });
    }

    public void deleteTrainById() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter train id");
        dialog.setHeaderText("Enter train id");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(id -> {
            if (trainServise.deleteTrainById(trains, Integer.parseInt(id))) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Train deleted");
                alert.setHeaderText("Train deleted successfully");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Train not found");
                alert.showAndWait();
            }
        });
    }
    public void info() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("OOP_lab9");
        alert.setContentText("this program develop for OOP_lab9");
        alert.showAndWait();
    }
}


