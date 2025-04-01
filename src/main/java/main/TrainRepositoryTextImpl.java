package main;

import javafx.scene.control.Alert;
import main.factory.TrainFactory;
import main.logic.Train;

import java.io.*;
import java.util.List;

public class TrainRepositoryTextImpl implements TrainRepository {
    @Override
    public void outputArray(List<Train> t, File file) {
        try(PrintWriter outputStream = new PrintWriter(file)) {
            t.stream().map(Train::toStringFile).forEach(outputStream::println);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Error while trying to open file");
            alert.showAndWait();
        }
    }

    @Override
    public void outputArray(List<Train> t, String fileName) {
        File file = new File(fileName);
        outputArray(t, file);
    }

    @Override
    public List<Train> readArray(File file) {
        try (BufferedReader inputStream = new BufferedReader(new FileReader(file))) {
            return inputStream.lines().map(line -> line.trim().split(","))
                    .map(sta -> TrainFactory.createTrain(Integer.parseInt(sta[0]), sta[1],
                            Integer.parseInt(sta[2]), sta[3],
                            Integer.parseInt(sta[4]), sta[5],
                            Integer.parseInt(sta[6]))).toList();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Error while trying to open file");
            alert.showAndWait();
            return null;
        }
    }

    @Override
    public List<Train> readArray(String fileName) {
        File file = new File(fileName);
        return readArray(file);
    }
}
