import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class OthelloGUI extends Application {
    private Game game;
    private Button[][] buttons = new Button[Board.SIZE][Board.SIZE];
    private Label timerLabel = new Label();
    private Timeline timeline;
    private int timeSeconds = 20; // Duration of the timer

    Stage primaryStage;
    Text title = new Text("Othello");
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        createGame();
    }

    private void createGame(){
        game = new Game(new HumanPlayer(DiskColor.BLACK), new ComputerPlayer(DiskColor.WHITE));

        GridPane grid = new GridPane();
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                buttons[i][j] = new Button();
                buttons[i][j].setPrefSize(50, 50);

                final int finalI = i;
                final int finalJ = j;
                buttons[i][j].setOnAction(e -> handleButtonClick(finalI, finalJ));

                grid.add(buttons[i][j], j, i);
            }
        }

        VBox root = new VBox();
        root.setSpacing(5);
        root.setAlignment(Pos.CENTER);

        title.setStyle("-fx-font-weight: bold;" +
                "       -fx-font-size: 20;");
        root.getChildren().add(title);




        root.getChildren().addAll(timerLabel, grid);

        Button restartButton = new Button("Recommencer");
        Button exitButton = new Button("Quitter");

        VBox buttonBox = new VBox(10);  // Espace de 10 pixels entre les boutons
        buttonBox.setAlignment(Pos.TOP_LEFT); // Aligner les boutons à droite

        HBox botSide = new HBox();
        botSide.getChildren().add(grid);
        botSide.getChildren().add(buttonBox);
        root.getChildren().add(botSide);  // Ajoutez la HBox à la VBox principale après le GridPane
        buttonBox.getChildren().addAll(restartButton, exitButton);

        restartButton.setOnAction(e -> restartGame());  // Nous allons définir cette méthode plus loin.
        exitButton.setOnAction(e -> primaryStage.close());

        updateUI();
        startTimer();

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Othello");
        primaryStage.show();
    }

    private void handleButtonClick(int row, int col) {
        if (game.placeDisk(row, col, DiskColor.BLACK)) {
            game.switchPlayer();
            updateUI();

            if (game.getCurrentPlayer() instanceof ComputerPlayer) {
                timeline.stop();  // Stop the timer during the computer's turn

                Move move;
                do {
                    move = game.getCurrentPlayer().play(game.getBoard());
                } while (!game.getBoard().isValidMove(move.getRow(), move.getCol(), game.getCurrentPlayer().getColor()));

                game.getBoard().placeDisk(move.getRow(), move.getCol(), game.getCurrentPlayer().getColor());
                game.switchPlayer();
                updateUI();

                resetTimer();  // Restart the timer for the human player
            } else {
                resetTimer();
            }
        }
    }

    private void updateUI() {
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                Disk disk = game.getBoard().getCell(i, j).getDisk();
                if (disk != null) {
                    if (disk.getColor() == DiskColor.BLACK) {
                        buttons[i][j].setText("B");
                        buttons[i][j].setStyle("-fx-background-color: black;");
                    } else {
                        buttons[i][j].setText("W");
                        buttons[i][j].setStyle("-fx-background-color: white;");
                    }
                } else {
                    buttons[i][j].setText("");
                }
            }
        }
    }

    private void startTimer() {
        if (timeline != null) {
            timeline.stop();
        }
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {
            timeSeconds--;
            timerLabel.setText("Time left: " + timeSeconds + " seconds");
            if (timeSeconds <= 0) {
                timeline.stop();
                System.out.println("Time's up! Skipping " + game.getCurrentPlayer().getColor() + "'s turn.");

                if (!(game.getCurrentPlayer() instanceof ComputerPlayer)) {  // Check if it's the human player's turn
                    game.switchPlayer();

                    if (game.getCurrentPlayer() instanceof ComputerPlayer) {
                        Move move;
                        do {
                            move = game.getCurrentPlayer().play(game.getBoard());
                        } while (!game.getBoard().isValidMove(move.getRow(), move.getCol(), game.getCurrentPlayer().getColor()));

                        game.getBoard().placeDisk(move.getRow(), move.getCol(), game.getCurrentPlayer().getColor());
                        game.switchPlayer();
                        updateUI();
                    }

                    resetTimer();  // Restart the timer for the human player
                }
            }
        }));
        timeline.playFromStart();
    }

    private void resetTimer() {
        timeSeconds = 20;
        timerLabel.setText("Time left: " + timeSeconds + " seconds");
        timeline.playFromStart();
    }

    private void restartGame() {
        // Remettez votre jeu à son état initial ici
        // Par exemple, si vous avez une méthode pour initialiser le jeu, appelez-la ici.
        // Sinon, vous pouvez simplement fermer et rouvrir votre fenêtre pour redémarrer le jeu.
        createGame();
        updateUI();
        resetTimer();
    }
}
