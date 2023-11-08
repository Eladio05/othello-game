import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class OthelloMain {
    private enum Strategy{
        POSITIONNEL("positional"),
        ABSOLU("absolu"),
        MOBILITY("mobility"),
        MIXTE("mixed")	,      
        DEFAULT("default");
        String strategie;
        Strategy(String strategie) {
            this.strategie = strategie;
        }
    }

    private static void writeLineToCsv(String csvFile, List<String> strings) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile, true))) { // 'true' pour l'append mode
            StringBuilder sb = new StringBuilder();

            for (String str : strings) {
                sb.append(str).append(","); // Ajoutez des guillemets si nécessaire
            }

            // Retirez la dernière virgule
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 1);
            }

            writer.println(sb.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        System.out.println("Welcome to Othello!");
        // BLACK = X
        // WHITE = 0
        // Création des joueurs
        Player humanPlayer = new HumanPlayer(DiskColor.BLACK);
        Player computerPlayer = new ComputerPlayer(DiskColor.WHITE);

        List<Player> players = new ArrayList<Player>();

        players.add(new MinMaxPlayer(DiskColor.WHITE, Strategy.POSITIONNEL.strategie));
        players.add(new MinMaxPlayer(DiskColor.WHITE, Strategy.ABSOLU.strategie.toString()));
        players.add(new MinMaxPlayer(DiskColor.WHITE, Strategy.MOBILITY.strategie));
        players.add(new MinMaxPlayer(DiskColor.WHITE, Strategy.MIXTE.strategie.toString()));
        players.add(new MinMaxPlayer(DiskColor.WHITE, Strategy.DEFAULT.strategie.toString()));

        players.add(new MinMaxAlphaBetaPlayer(DiskColor.WHITE, Strategy.POSITIONNEL.strategie));
        players.add(new MinMaxAlphaBetaPlayer(DiskColor.WHITE, Strategy.ABSOLU.strategie.toString()));
        players.add(new MinMaxAlphaBetaPlayer(DiskColor.WHITE, Strategy.MOBILITY.strategie));
        players.add(new MinMaxAlphaBetaPlayer(DiskColor.WHITE, Strategy.MIXTE.strategie.toString()));
        players.add(new MinMaxAlphaBetaPlayer(DiskColor.WHITE, Strategy.DEFAULT.strategie.toString()));

        players.add(new NegaMaxPlayer(DiskColor.WHITE, Strategy.POSITIONNEL.strategie));
        players.add(new NegaMaxPlayer(DiskColor.WHITE, Strategy.ABSOLU.strategie.toString()));
        players.add(new NegaMaxPlayer(DiskColor.WHITE, Strategy.MOBILITY.strategie));
        players.add(new NegaMaxPlayer(DiskColor.WHITE, Strategy.MIXTE.strategie.toString()));
        players.add(new NegaMaxPlayer(DiskColor.WHITE, Strategy.DEFAULT.strategie.toString()));

        players.add(new NegaMaxAlphaBetaPlayer(DiskColor.WHITE, Strategy.POSITIONNEL.strategie));
        players.add(new NegaMaxAlphaBetaPlayer(DiskColor.WHITE, Strategy.ABSOLU.strategie.toString()));
        players.add(new NegaMaxAlphaBetaPlayer(DiskColor.WHITE, Strategy.MOBILITY.strategie));
        players.add(new NegaMaxAlphaBetaPlayer(DiskColor.WHITE, Strategy.MIXTE.strategie.toString()));
        players.add(new NegaMaxAlphaBetaPlayer(DiskColor.WHITE, Strategy.DEFAULT.strategie.toString()));

        Player IARandom = new RandomPlayer(DiskColor.BLACK);
        for (Player p: players) {
            System.out.println("IA: " + p.getClass().getName());
            for (int i = 0; i < 100; i++) {
                // Démarrage du jeu
                Game game = new Game(p, IARandom);
                game.start();
                writeLineToCsv("results.csv", game.getResult());
            }
        }
        /*for (int i = 0; i < 1000; i++){
            System.out.println("Partie " + i);
            // Démarrage du jeu
            Game game = new Game(IA1, IARandom);
            game.start();
            System.out.println(game.getResult());
            writeLineToCsv("results.csv", game.getResult());
        }*/
    }
}
