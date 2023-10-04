public class ComputerPlayer extends Player {

    public ComputerPlayer(DiskColor color) {
        super(color);
    }

    @Override
    public Move play(Board board) {
        for (Move move : board.getValidMoves(color)) {
            return move; // Choisissez simplement le premier mouvement valide
        }
        return null; // Aucun mouvement valide trouvé
    }
}
