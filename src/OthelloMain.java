public class OthelloMain {
    public static void main(String[] args) {
        System.out.println("Welcome to Othello!");

        // Création des joueurs
        Player humanPlayer = new HumanPlayer(DiskColor.BLACK);
        Player computerPlayer = new ComputerPlayer(DiskColor.WHITE);

        // Démarrage du jeu
        Game game = new Game(humanPlayer, computerPlayer);
        game.start();
    }
}
