import java.util.Scanner;

public class HumanPlayer extends Player {
    private Scanner scanner;

    public HumanPlayer(DiskColor color) {
        super(color);
        scanner = new Scanner(System.in);
    }

    @Override
    public Move play(Board board) {
        System.out.println("Player " + color + ", enter your move (row col):");
        int row = scanner.nextInt();
        int col = scanner.nextInt();
        return new Move(row, col);
    }
}
