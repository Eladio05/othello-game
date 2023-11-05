import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class Game {
    private Board board;
    private List<Player> players;
    private int currentPlayerIndex;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public Game(Player... players) {
        this.board = new Board();
        this.players = Arrays.asList(players);
        this.currentPlayerIndex = 0;
    }

    public void start() {
        //System.out.println("Starting game...\n");
        //System.out.println(board);

        while (!isGameOver()) {
            playTurn();
            //System.out.println(board);
            switchPlayer();
        }

        //displayWinner();
        shutdownExecutor();
    }

    public void playTurn() {
        //System.out.println("It's " + getCurrentPlayer().getColor() + "'s turn.");
        if (!board.hasValidMoves(getCurrentPlayer().getColor())) {
            //System.out.println(getCurrentPlayer().getColor() + " cannot make a move!");
            return;
        }

        Future<Move> future = executor.submit(() -> getCurrentPlayer().play(board));

        Move move = null;
        try {
            move = future.get(15, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            //System.out.println("Time's up for " + getCurrentPlayer().getColor() + "!");
            future.cancel(true);
        }

        if (move != null && board.isValidMove(move.getRow(), move.getCol(), getCurrentPlayer().getColor())) {
            board.placeDisk(move.getRow(), move.getCol(), getCurrentPlayer().getColor());
            //System.out.println(board);
        } else {
            //System.out.println(getCurrentPlayer().getColor() + " missed their turn!");
        }
    }

    public void switchPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public boolean isGameOver() {
        // a         System.out.println("La game est fini: " + (!board.hasValidMoves(DiskColor.BLACK) && !board.hasValidMoves(DiskColor.WHITE) ));
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

    private void shutdownExecutor() {
        executor.shutdown();
    }

    public static void main(String[] args) {
        Player player1 = new HumanPlayer(DiskColor.BLACK);
        Player player2 = new ComputerPlayer(DiskColor.WHITE);
        Game game = new Game(player1, player2);
        game.start();
    }

    public List<String> getResult(){
        List<String> res = new ArrayList<String>();
        String str1 = players.get(0).getClass().getName();
        String str2 = players.get(1).getClass().getName();

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

        String winner = String.valueOf(whiteCount - blackCount);
        res.add(str1);
        res.add(str2);
        res.add(winner);
        return res;
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
