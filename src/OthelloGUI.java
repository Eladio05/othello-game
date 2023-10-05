import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class OthelloGUI extends Application {
    private Game game;
    private Button[][] buttons = new Button[Board.SIZE][Board.SIZE];

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


        updateUI();

        primaryStage.setScene(new Scene(grid));
        primaryStage.setTitle("Othello");
        primaryStage.show();
    }

    private void handleButtonClick(int row, int col) {
        if (game.placeDisk(row, col, DiskColor.BLACK)) {
            game.switchPlayer();
            updateUI();

            if (!game.getBoard().hasValidMoves(game.getCurrentPlayer().getColor())) {
                System.out.println(game.getCurrentPlayer().getColor() + " cannot make a move!");
                return;
            }

            // Pause de 1 seconde (ou la durée que vous souhaitez) avant que l'IA ne joue
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(e -> {
                Move move;
                do {
                    move = game.getCurrentPlayer().play(game.getBoard());
                } while (!game.getBoard().isValidMove(move.getRow(), move.getCol(), game.getCurrentPlayer().getColor()));

                game.getBoard().placeDisk(move.getRow(), move.getCol(), game.getCurrentPlayer().getColor());
                game.switchPlayer();
                updateUI();
            });
            pause.play();
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
}
