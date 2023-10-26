import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Alert.AlertType;

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

        HBox buttonBox = new HBox(10);  // Espace de 10 pixels entre les boutons
        buttonBox.setAlignment(Pos.CENTER_RIGHT); // Aligner les boutons à droite

        root.getChildren().add(buttonBox);  // Ajoutez la HBox à la VBox principale après le GridPane

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
                        playComputerTurn();
                        updateUI();
                    }

                    resetTimer();  // Restart the timer for the human player
                }
            }
        }));
        timeline.playFromStart();
    }

    private void playComputerTurn() {
        System.out.println("Tour du bot");
        if (game.isGameOver()) {
            showGameOverPopup();
            return;
        }
        do {
            Move move = game.getCurrentPlayer().play(game.getBoard());
            if (move != null) {

                game.getBoard().placeDisk(move.getRow(), move.getCol(), game.getCurrentPlayer().getColor());
                System.out.println("The bot has played again");
                System.out.println(game.getBoard().getValidMoves(game.getCurrentPlayer().getColor()));
            }
            game.switchPlayer();

        } while (game.getBoard().hasValidMoves(DiskColor.WHITE) && !game.getBoard().hasValidMoves(DiskColor.BLACK));
    }

    private void resetTimer() {
        timeSeconds = 20;
        timerLabel.setText("Time left: " + timeSeconds + " seconds");
        timeline.playFromStart();
    }

    private void showGameOverPopup() {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Fin de la partie");
            alert.setHeaderText(null);
            alert.setContentText("La partie est termin�e!");

            alert.showAndWait();
        });
    }

    private void restartGame() {
        // Remettez votre jeu � son �tat initial ici
        // Par exemple, si vous avez une m�thode pour initialiser le jeu, appelez-la ici.
        // Sinon, vous pouvez simplement fermer et rouvrir votre fen�tre pour red�marrer le jeu.
        createGame();
        updateUI();
        resetTimer();
    }
}
