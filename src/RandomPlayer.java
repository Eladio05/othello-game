import java.util.List;
import java.util.Random;
public class RandomPlayer extends Player{
    Random random = new Random();

    public RandomPlayer(DiskColor color) {
        super(color);
    }

    @Override
    public Move play(Board board) {
        //System.out.println("RandomPlay");
        List<Move> moves = board.getValidMoves(color);
        if(moves != null || !moves.isEmpty()){
            return moves.get(random.nextInt(moves.size()));
        }
        return null; // Aucun mouvement valide trouvé
    }
}
