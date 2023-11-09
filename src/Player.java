public abstract class Player {
    public DiskColor color;
    public String evaluationStrategy;
    public long totalTime = 0; // Temps total d'ex�cution de play
    public int playCount = 0; // Nombre d'appels � play
    public int nbNoeud = 0;
    public final int DEPTH = 3;

    public Player(DiskColor color) {
        this.color = color;
    }

    public DiskColor getColor() {
        return color;
    }

    public abstract Move play(Board board);

    // Appel� apr�s la partie pour obtenir la dur�e moyenne de play
    public double getAveragePlayTime() {
        if (playCount > 0) {
            return (double) totalTime / playCount / 1_000_000.0; // Conversion en millisecondes
        } else {
            return 0.0;
        }
    }

    // Appel� apr�s la partie pour obtenir la dur�e moyenne de play
    public double getAverageNbNoeud() {
        if (playCount > 0) {
            return (double) nbNoeud / playCount;
        } else {
            return 0.0;
        }
    }

}
