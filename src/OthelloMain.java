public class OthelloMain {
    private enum Strategy{
        POSITIONNEL("positionnel"),
        ABSOLU("absolu"),
        MOBILITY("mobility"),
        DEFAULT("default");
        String strategie;
        Strategy(String strategie) {
            this.strategie = strategie;
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Othello!");
        // BLACK = X
        // WHITE = 0
        // Création des joueurs
        Player humanPlayer = new HumanPlayer(DiskColor.BLACK);
        Player computerPlayer = new ComputerPlayer(DiskColor.WHITE);

        Player IA1 = new MinMaxPlayer(DiskColor.BLACK, Strategy.ABSOLU.strategie.toString()); // X
        Player IA2 = new MinMaxAlphaBetaPlayer(DiskColor.WHITE, Strategy.MOBILITY.strategie.toString());
        Player IARandom = new RandomPlayer(DiskColor.WHITE);


        for (int i = 0; i < 1000; i++){
            System.out.println("Partie " + i);
            // Démarrage du jeu
            Game game = new Game(IA1, IARandom);
            game.start();
        }
    }
}
