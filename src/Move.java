public class Move {
    private int row;
    private int col;

    public Move(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Move move = (Move) o;
        return row == move.row && col == move.col;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        return result;
    }
    
    public static void main(String[] args) {
        // Test
        Move move1 = new Move(3, 4);
        Move move2 = new Move(3, 4);
        System.out.println(move1); // (3, 4)
        System.out.println(move1.equals(move2)); // true
    }
}
