import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;

public class Gradebook extends Application {

    private static Map<String, Student> studentDatabase = new HashMap<>();

    @Override
    public void start(Stage stage) {
        generateStudents();

        Label title = new Label("Electronic Gradebook System");
        title.getStyleClass().add("title");

        TextField username = new TextField();
        username.setPromptText("Username");

        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        Button loginBtn = new Button("Login");
        Label message = new Label();

        VBox loginBox = new VBox(10, title, username, password, loginBtn, message);
        loginBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(loginBox, 400, 300);
        scene.getStylesheets().add("style.css");

        loginBtn.setOnAction(e -> {
            if (username.getText().equals("teacher") && password.getText().equals("admin")) {
                showTeacherView(stage);
            } else if (studentDatabase.containsKey(username.getText())
                    && password.getText().equals("student")) {
                showStudentView(stage, studentDatabase.get(username.getText()));
            } else {
                message.setText("Invalid login.");
            }
        });

        stage.setTitle("Gradebook Login");
        stage.setScene(scene);
        stage.show();
    }

    // ---------- Teacher View ----------
   private void showTeacherView(Stage stage) {

    TextArea area = new TextArea();
    area.setEditable(false);

    ComboBox<Student> studentBox = new ComboBox<>();
    studentBox.getItems().addAll(studentDatabase.values());

    TextField assignmentField = new TextField();
    assignmentField.setPromptText("Assignment Name");

    TextField gradeField = new TextField();
    gradeField.setPromptText("Grade");

    Button addGradeBtn = new Button("Add Assignment & Grade");

    addGradeBtn.setOnAction (e -> {
        Student selected = studentBox.getValue();
        String assignment = assignmentField.getText();
        String gradeText = gradeField.getText();

        if (selected != null && !assignment.isEmpty() && !gradeText.isEmpty()) {
            try {
                double grade = Double.parseDouble(gradeText);
                selected.addAssignment(assignment, grade);

                // Refresh display
                area.clear();
                for (Student s : studentDatabase.values()) {
                    area.appendText(s.toString() + "\n");
                }

                assignmentField.clear();
                gradeField.clear();

            } catch (NumberFormatException ex) {
                area.appendText("Invalid grade format.\n");
            }
        }
    });

    Button logout = new Button("Logout");
    logout.setOnAction(e -> start(stage));

    VBox root = new VBox(
            10,
            new Label("Teacher Dashboard"),
            studentBox,
            assignmentField,
            gradeField,
            addGradeBtn,
            area,
            logout
    );

    root.setPadding(new Insets(15));

    Scene scene = new Scene(root, 600, 500);
    scene.getStylesheets().add("style.css");
    stage.setScene(scene);
    }
    // ---------- Student View ----------
    private void showStudentView(Stage stage, Student student) {
        Label info = new Label(student.toString());
        Button logout = new Button("Logout");

        logout.setOnAction(e -> start(stage));

        VBox root = new VBox(15,
                new Label("Student Grade Report"),
                info,
                logout);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 400, 300);
        scene.getStylesheets().add("style.css");
        stage.setScene(scene);
    }

    // ---------- Data Generation ----------
    private void generateStudents() {
        Random rand = new Random();
        for (int i = 1001; i <= 1150; i++) {
            studentDatabase.put(
                    String.valueOf(i),
                    new Student(i, "Student " + i,
                            60 + rand.nextInt(41),
                            60 + rand.nextInt(41),
                            65 + rand.nextInt(36))
            );
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

// ---------- Student Class ----------
// ---------- Student Class ----------
class Student {
    int id;
    String name;
    int homework, quiz, project;

    public Student(int id, String name, int hw, int qz, int pr) {
        this.id = id;
        this.name = name;
        this.homework = hw;
        this.quiz = qz;
        this.project = pr;
    }

    // 🔧 Added method to fix compile error
    public void addAssignment(String assignment, double grade) {
        switch (assignment.toLowerCase()) {
            case "homework":
                homework = (int) grade;
                break;
            case "quiz":
                quiz = (int) grade;
                break;
            case "project":
                project = (int) grade;
                break;
            default:
                // Ignore invalid assignment names
                break;
        }
    }

    public double average() {
        return (homework + quiz + project) / 3.0;
    }

    @Override
    public String toString() {
        return name + " (ID: " + id + ")\n" +
                "Homework: " + homework + "\n" +
                "Quiz: " + quiz + "\n" +
                "Project: " + project + "\n" +
                "Average: " + String.format("%.2f", average()) + "%\n";
    }
}