import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class NotesAppGUI extends Application {

    private TextArea textArea;
    private ArrayList<String> notes = new ArrayList<>();
    private final String FILE_NAME = "notes.txt";

    @Override
    public void start(Stage stage) {
        stage.setTitle("📒 Notes App");

        TextField titleField = new TextField();
        titleField.setPromptText("Enter note title");

        textArea = new TextArea();
        textArea.setPromptText("Write your note here...");

        Button saveButton = new Button("Save Note");
        Button viewButton = new Button("View Notes");

        TextArea displayArea = new TextArea();
        displayArea.setEditable(false);

        loadNotes();

        saveButton.setOnAction(e -> {
            String note = titleField.getText() + ": " + textArea.getText();
            notes.add(note);
            saveNotes();
            titleField.clear();
            textArea.clear();
        });

        viewButton.setOnAction(e -> {
            displayArea.clear();
            for (String note : notes) {
                displayArea.appendText(note + "\n\n");
            }
        });

        VBox layout = new VBox(10, titleField, textArea, saveButton, viewButton, displayArea);

        Scene scene = new Scene(layout, 400, 500);
        stage.setScene(scene);
        stage.show();
    }

    private void saveNotes() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String note : notes) {
                writer.write(note);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadNotes() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                notes.add(line);
            }
        } catch (IOException ignored) {}
    }

    public static void main(String[] args) {
        launch(args);
    }
}