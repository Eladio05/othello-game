import java.util.List;
import java.util.Random;
public class RandomPlayer extends Player{
    Random random = new Random();

    public RandomPlayer(DiskColor color) {
        super(color);
    }

    @Override
    public Move play(Board board) {
        long startTime = System.nanoTime(); // Début de mesure du temps
        //System.out.println("RandomPlay");
        List<Move> moves = board.getValidMoves(color);
        if(moves != null || !moves.isEmpty()){
            return moves.get(random.nextInt(moves.size()));
        }
        long endTime = System.nanoTime(); // Fin de mesure du temps
        totalTime += (endTime - startTime); // Accumulation du temps total
        playCount++; // Incrémentation du nombre d'appels
        return null; // Aucun mouvement valide trouvé
    }
}
