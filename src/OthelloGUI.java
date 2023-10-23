import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class OthelloGUI extends Application {
    private Game game;
    private Button[][] buttons = new Button[Board.SIZE][Board.SIZE];
    private Label timerLabel = new Label();
    private Timeline timeline;
    private int timeSeconds = 20; // Durée du timer

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
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
        root.getChildren().addAll(timerLabel, grid);

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

            // Jouer le tour de l'IA
            if (game.getCurrentPlayer() instanceof ComputerPlayer) {
                Move move;
                do {
                    move = game.getCurrentPlayer().play(game.getBoard());
                } while (!game.getBoard().isValidMove(move.getRow(), move.getCol(), game.getCurrentPlayer().getColor()));

                game.getBoard().placeDisk(move.getRow(), move.getCol(), game.getCurrentPlayer().getColor());
                game.switchPlayer();
                updateUI();
            }

            resetTimer();
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
                game.switchPlayer(); // Passer le tour du joueur humain
                
                // Jouer le tour de l'IA
                if (game.getCurrentPlayer() instanceof ComputerPlayer) {
                    Move move;
                    do {
                        move = game.getCurrentPlayer().play(game.getBoard());
                    } while (!game.getBoard().isValidMove(move.getRow(), move.getCol(), game.getCurrentPlayer().getColor()));

                    game.getBoard().placeDisk(move.getRow(), move.getCol(), game.getCurrentPlayer().getColor());
                    game.switchPlayer();
                    updateUI();
                }
                
                resetTimer(); // Réinitialiser le timer pour le tour suivant du joueur humain
            }
        }));
        timeline.playFromStart();
    }

    private void resetTimer() {
        timeSeconds = 20;
        timerLabel.setText("Time left: " + timeSeconds + " seconds");
        timeline.playFromStart();
    }
}
