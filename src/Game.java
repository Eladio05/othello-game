import java.util.Arrays;
import java.util.List;

public class Game {
    private Board board;
    private List<Player> players;

    private int currentPlayerIndex;

    public Game(Player... players) {
        this.board = new Board();
        this.players = Arrays.asList(players);
        this.currentPlayerIndex = 0; // Le premier joueur commence par défaut
    }

    public void start() {
        System.out.println("Starting game...\n");
        System.out.println(board);

        while (!isGameOver()) {
            playTurn();
            switchPlayer();
        }

        displayWinner();
    }

    public void playTurn() {
        System.out.println("It's " + getCurrentPlayer().getColor() + "'s turn.");
        if (!board.hasValidMoves(getCurrentPlayer().getColor())) {
            System.out.println(getCurrentPlayer().getColor() + " cannot make a move!");
            return;
        }

        Move move;
        do {
            move = getCurrentPlayer().play(board);
        } while (!board.isValidMove(move.getRow(), move.getCol(), getCurrentPlayer().getColor()));

        board.placeDisk(move.getRow(), move.getCol(), getCurrentPlayer().getColor());
        System.out.println(board);
    }


    public void switchPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    private boolean isGameOver() {
        return !board.hasValidMoves(DiskColor.BLACK) && !board.hasValidMoves(DiskColor.WHITE);
    }

    private void displayWinner() {
        int blackCount = 0;
        int whiteCount = 0;
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                Disk disk = board.getCell(i, j).getDisk();
                if (disk != null) {
                    if (disk.getColor() == DiskColor.BLACK) {
                        blackCount++;
                    } else {
                        whiteCount++;
                    }
                }
            }
        }

        System.out.println("Game over!");
        System.out.println("Black disks: " + blackCount);
        System.out.println("White disks: " + whiteCount);

        if (blackCount > whiteCount) {
            System.out.println("BLACK wins!");
        } else if (whiteCount > blackCount) {
            System.out.println("WHITE wins!");
        } else {
            System.out.println("It's a tie!");
        }
    }

    public static void main(String[] args) {
        Player player1 = new ComputerPlayer(DiskColor.BLACK);
        Player player2 = new ComputerPlayer(DiskColor.WHITE);
        Game game = new Game(player1, player2);
        game.start();
    }

    public boolean placeDisk(int row, int col, DiskColor diskColor) {
        return board.placeDisk(row, col, diskColor);
    }

    public Board getBoard() {
        return board;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
}
