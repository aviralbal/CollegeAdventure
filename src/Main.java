import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Main extends Application {

    private Label fullscreenStatusLabel;
    private Label gameStatusLabel;
    private AtomicInteger score = new AtomicInteger(0);
    private List<String> events = new ArrayList<>();
    private int currentEventIndex = 0;
    private boolean isFullScreen = false;
    private MediaPlayer mediaPlayer;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("UW-Madison Adventure!");

        // Initialize and play background music
        String musicFile = "/Users/aviralbal/IdeaProjects/Game/out/song.mp3"; // Replace with the path to your music file
        Media media = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the music
        mediaPlayer.play();

        StackPane root = new StackPane();
        Scene scene = new Scene(root, 400, 350);
        primaryStage.setScene(scene);

        CornerRadii cornerRadii = new CornerRadii(10.0);
        Insets insets = new Insets(2);
        BackgroundFill backgroundFill = new BackgroundFill(Color.BLACK, cornerRadii, insets);
        Background background = new Background(backgroundFill);
        root.setBackground(background);

        showIntroduction(primaryStage, root);
        primaryStage.show();
    }

    private void showIntroduction(Stage primaryStage, StackPane root) {
        VBox introLayout = new VBox(10);
        introLayout.setAlignment(Pos.CENTER);

        Label introLabel = new Label("Welcome to UW-Madison Adventure!");
        introLabel.setTextFill(Color.WHITE);

        Label introText = new Label("You are about to begin your college journey. Your choices will shape your experience and your success. Are you ready?");
        introText.setTextFill(Color.WHITE);
        introText.setWrapText(true);

        Button btnStartGame = new Button("Start Game");
        btnStartGame.setOnAction(e -> {
            setupUI(primaryStage);
            if (isFullScreen) {
                primaryStage.setFullScreen(true); // Retain fullscreen during the game
            }
        });

        introLayout.getChildren().addAll(introLabel, introText, btnStartGame);
        root.getChildren().add(introLayout);
    }

    private void setupUI(Stage primaryStage) {
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 400, 350);
        primaryStage.setScene(scene);

        VBox buttonLayout = new VBox(10);
        buttonLayout.setAlignment(Pos.CENTER);

        fullscreenStatusLabel = new Label("Full Screen Mode: Disabled");
        fullscreenStatusLabel.setTextFill(Color.WHITE);

        Button btnStartGame = new Button("Begin College Journey");
        btnStartGame.setOnAction(e -> {
            startGame(primaryStage);
            if (isFullScreen) {
                primaryStage.setFullScreen(true); // Retain fullscreen during the game
            }
        });

        Button options = new Button("Options");
        options.setOnAction(event -> options(primaryStage));

        Button exit = new Button("Exit");
        exit.setOnAction(e -> exitConfirmation());

        Button toggleFullscreen = new Button("Toggle Full Screen");
        toggleFullscreen.setOnAction(e -> toggleFullScreen(primaryStage));

        buttonLayout.getChildren().addAll(btnStartGame, options, exit, toggleFullscreen, fullscreenStatusLabel);

        root.getChildren().add(buttonLayout);
        StackPane.setAlignment(buttonLayout, Pos.CENTER);

        initializeEvents();
    }

    private void initializeEvents() {
        // Add meaningful events and choices affecting the player's score, in chronological order
        events.add("Welcome to college! It's the first day, and you're overwhelmed with excitement and nervousness. What do you do?\n1. Attend the orientation to get familiar with the campus and meet new people.\n2. Skip the orientation to explore the campus on your own.");

        events.add("Your roommate invites you to a late-night party, but you have a morning class. What do you do?\n1. Go to the party and try to wake up in time for class.\n2. Skip the party to ensure you're rested for the class.");

        events.add("You notice some classmates forming a study group for an upcoming midterm. What do you do?\n1. Join the study group to prepare together.\n2. Study alone, trusting your own methods.");

        events.add("A professor offers extra office hours for students struggling with the material. What do you do?\n1. Attend the extra office hours to clarify your doubts.\n2. Skip the office hours and try to figure things out on your own.");

        events.add("You’re juggling multiple assignments and feeling stressed. Your friends suggest a weekend getaway. What do you do?\n1. Take the weekend off to relax and recharge.\n2. Stay back and focus on catching up with your assignments.");

        events.add("It's mid-semester, and you've been invited to join a student club that aligns with your interests. What do you do?\n1. Join the club to expand your network and skills.\n2. Decline the offer to keep your focus solely on academics.");

        events.add("You're offered an internship that could give you real-world experience, but it might conflict with your class schedule. What do you do?\n1. Accept the internship and try to balance both.\n2. Decline the internship to focus on your studies.");

        events.add("As graduation approaches, you're faced with a dilemma: pursue a job that offers immediate financial stability or continue with graduate studies for long-term career growth. What do you choose?\n1. Pursue the job and start earning right away.\n2. Continue with graduate studies for more advanced opportunities.");
    }


    public void startGame(Stage stage) {
        // Clear existing children from the game root before adding new ones
        StackPane gameRoot = new StackPane();
        Scene gameScene = new Scene(gameRoot, 400, 350);
        stage.setScene(gameScene);

        gameStatusLabel = new Label("Score: 0");
        gameStatusLabel.setTextFill(Color.WHITE);

        gameRoot.getChildren().clear();
        gameRoot.getChildren().add(gameStatusLabel);
        gameRoot.setStyle("-fx-background-color: black;");

        currentEventIndex = 0;
        score.set(0);
        startGameLoop(stage);
    }

    private void handleNextEvent(Stage stage) {
        if (currentEventIndex < events.size()) {
            String event = events.get(currentEventIndex);
            showEventChoices(event, stage);
        } else {
            endGame(stage); // Automatically end the game when all events are completed
        }
    }

    private void showEventChoices(String eventText, Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Your Journey Choice");
        alert.setHeaderText(null);
        alert.setContentText(eventText);

        ButtonType buttonTypeOne = new ButtonType("Option 1");
        ButtonType buttonTypeTwo = new ButtonType("Option 2");

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

        alert.showAndWait().ifPresent(type -> {
            if (type == buttonTypeOne) {
                handleChoice(1);
            } else if (type == buttonTypeTwo) {
                handleChoice(2);
            }
            currentEventIndex++;
            handleNextEvent(stage); // Automatically proceed to the next event
        });
    }

    private void handleChoice(int choice) {
        switch (currentEventIndex) {
            case 0: // Selecting Dorm
                if (choice == 1) {
                    score.addAndGet(-5); // Party Dorm
                } else if (choice == 2) {
                    score.addAndGet(5); // Study Dorm
                }
                break;
            case 1: // Midterm Preparation
                if (choice == 1) {
                    score.addAndGet(10);
                } else if (choice == 2) {
                    score.addAndGet(-5);
                }
                break;
            case 2: // Attend Lectures
                if (choice == 1) {
                    score.addAndGet(10);
                } else if (choice == 2) {
                    score.addAndGet(-5);
                }
                break;
            case 3: // Free Time
                if (choice == 1) {
                    score.addAndGet(5);
                } else if (choice == 2) {
                    score.addAndGet(-5);
                }
                break;
            case 4: // Finals Week
                if (choice == 1) {
                    score.addAndGet(15);
                } else if (choice == 2) {
                    score.addAndGet(-10);
                }
                break;
            case 5: // Internship Offer
                if (choice == 1) {
                    score.addAndGet(20);
                } else if (choice == 2) {
                    score.addAndGet(-10);
                }
                break;
            case 6: // Major Project
                if (choice == 1) {
                    score.addAndGet(15);
                } else if (choice == 2) {
                    score.addAndGet(-10);
                }
                break;
            case 7: // Graduation Preparation
                if (choice == 1) {
                    score.addAndGet(20);
                } else if (choice == 2) {
                    score.addAndGet(-10);
                }
                break;
        }

        gameStatusLabel.setText("Score: " + score.get());
    }

    private void startGameLoop(Stage stage) {
        AnimationTimer gameLoop = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 1_000_000_000) { // Update every second
                    updateGame();
                    lastUpdate = now;
                }
            }
        };
        gameLoop.start();
        handleNextEvent(stage); // Start handling events right away
    }

    private void updateGame() {
        gameStatusLabel.setText("Score: " + score.get());
    }

    private void endGame(Stage stage) {
        int finalScore = score.get();
        String message = "Congratulations! Your final score is: " + finalScore + "\n";

        if (finalScore <= 20) {
            message += "It's time to pull up your pants and focus more on your studies!";
        } else if (finalScore <= 50) {
            message += "Well done! Your choices indicate you are on the right track.";
        } else {
            message += "Bro, college is about experience! Study, but have fun as well.";
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

        // Reset game and return to main screen
        start(stage);
    }

    private void exitConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit the game?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Exit Confirmation");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            Platform.exit();
        }
    }

    private void toggleFullScreen(Stage primaryStage) {
        isFullScreen = !isFullScreen;
        primaryStage.setFullScreen(isFullScreen);
        fullscreenStatusLabel.setText("Full Screen Mode: " + (isFullScreen ? "Enabled" : "Disabled"));
    }

    public void options(Stage secondStage) {
        StackPane root2 = new StackPane();
        BackgroundFill backgroundFill = new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY);
        Background bgOptions = new Background(backgroundFill);
        root2.setBackground(bgOptions);

        Button AudioSettings = new Button("Settings");
        AudioSettings.setOnAction(e -> {
            settings(secondStage);
        });

        Button back = new Button("Back");
        back.setOnAction(e -> {
            try {
                start(secondStage);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20;");
        layout.setBackground(bgOptions);
        layout.getChildren().addAll(AudioSettings, back);
        Scene scene2 = new Scene(layout, 400, 350);
        secondStage.setTitle("Options");
        secondStage.setScene(scene2);
        secondStage.show();
    }

    public void settings(Stage primaryStage) {

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20;");

        Button audioSettingsButton = new Button("Audio Settings");
        audioSettingsButton.setOnAction(e -> openAudioSettings());

        Button videoSettingsButton = new Button("Video Settings");
        videoSettingsButton.setOnAction(e -> openVideoSettings(primaryStage));

        Button exitButton = new Button("Close");
        exitButton.setOnAction(e -> options(primaryStage));

        layout.getChildren().addAll(audioSettingsButton, videoSettingsButton, exitButton);

        Scene scene = new Scene(layout, 400, 350);
        Stage settingsStage = new Stage();
        settingsStage.setTitle("Settings");
        settingsStage.setScene(scene);
        settingsStage.show();
    }

    private void openAudioSettings() {
        Stage audioStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Button muteButton = new Button("Toggle Sound");
        muteButton.setOnAction(e -> {
            if (mediaPlayer.isMute()) {
                mediaPlayer.setMute(false);
                System.out.println("Sound unmuted.");
            } else {
                mediaPlayer.setMute(true);
                System.out.println("Sound muted.");
            }
        });

        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> audioStage.close());

        layout.getChildren().addAll(new Label("Sound Settings"), muteButton, closeBtn);

        Scene scene = new Scene(layout, 200, 100);
        audioStage.setTitle("Audio Settings");
        audioStage.setScene(scene);
        audioStage.show();
    }

    private void openVideoSettings(Stage primaryStage) {
        Stage videoStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        // Toggle full-screen button for the primary stage
        Button fullScreenButton = new Button("Toggle Full Screen");
        fullScreenButton.setOnAction(e -> {
            boolean isFullScreen = primaryStage.isFullScreen();
            primaryStage.setFullScreen(!isFullScreen);
        });

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> videoStage.close());

        layout.getChildren().addAll(fullScreenButton, closeButton);

        Scene scene = new Scene(layout, 200, 100);
        videoStage.setTitle("Video Settings");
        videoStage.setScene(scene);
        videoStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
