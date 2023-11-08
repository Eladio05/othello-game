public abstract class Player {
    protected DiskColor color;
    public String evaluationStrategy;

    public Player(DiskColor color) {
        this.color = color;
    }

    public DiskColor getColor() {
        return color;
    }

    public abstract Move play(Board board);


}
