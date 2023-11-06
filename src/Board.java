import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final int SIZE = 8; // Taille standard du plateau Othello
    private Cell[][] cells;

    public Board() {
        cells = new Cell[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cells[i][j] = new Cell();
            }
        }

        // Position initiale des disques
        cells[SIZE / 2 - 1][SIZE / 2 - 1].setDisk(new Disk(DiskColor.WHITE));
        cells[SIZE / 2][SIZE / 2].setDisk(new Disk(DiskColor.WHITE));
        cells[SIZE / 2 - 1][SIZE / 2].setDisk(new Disk(DiskColor.BLACK));
        cells[SIZE / 2][SIZE / 2 - 1].setDisk(new Disk(DiskColor.BLACK));
    }
    
    private final static int[][] WEIGHTS = {
    	    { 500, -150, 30, 10, 10, 30, -150, 500 },
    	    { -150, -250, 0, 0, 0, 0, -250, -150 },
    	    { 30, 0, 1, 2, 2, 1, 0, 30 },
    	    { 10, 0, 2, 16, 16, 2, 0, 10 },
    	    { 10, 0, 2, 16, 16, 2, 0, 10 },
    	    { 30, 0, 1, 2, 2, 1, 0, 30 },
    	    { -150, -250, 0, 0, 0, 0, -250, -150 },
    	    { 500, -150, 30, 10, 10, 30, -150, 500 }
    	};


    public Cell getCell(int row, int col) {
        return cells[row][col];
    }

    public boolean placeDisk(int row, int col, DiskColor color) {
        if (isValidMove(row, col, color)) {
            cells[row][col].setDisk(new Disk(color));
            flipDisks(row, col, color);
            return true;
        }
        //System.out.println("Move impossible");
        return false;
    }
    
    public int countDisks(DiskColor color) {
        int count = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Cell cell = cells[i][j];
                if (cell != null && !cell.isEmpty() && cell.getDisk().getColor() == color) {
                    count++;
                }
            }
        }
        return count;
    }

    private void flipDisks(int row, int col, DiskColor color) {
        DiskColor opponentColor = (color == DiskColor.BLACK) ? DiskColor.WHITE : DiskColor.BLACK;
        int[] dx = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] dy = {-1, 0, 1, 1, 1, 0, -1, -1};

        for (int direction = 0; direction < 8; direction++) {
            int x = row + dx[direction];
            int y = col + dy[direction];
            List<Cell> cellsToFlip = new ArrayList<>();

            while (x >= 0 && x < SIZE && y >= 0 && y < SIZE && !cells[x][y].isEmpty() && cells[x][y].getDisk().getColor() == opponentColor) {
                cellsToFlip.add(cells[x][y]);
                x += dx[direction];
                y += dy[direction];
            }

            if (x >= 0 && x < SIZE && y >= 0 && y < SIZE && !cells[x][y].isEmpty() && cells[x][y].getDisk().getColor() == color) {
                for (Cell cell : cellsToFlip) {
                    cell.getDisk().flip();
                }
            }
        }
    }


    public List<Move> getValidMoves(DiskColor color) {
        List<Move> validMoves = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (isValidMove(i, j, color)) {
                    validMoves.add(new Move(i, j));
                }
            }
        }
        return validMoves;
    }

    public boolean hasValidMoves(DiskColor color) {
        return !getValidMoves(color).isEmpty();
    }

    public boolean isValidMove(int row, int col, DiskColor color) {
        if (!cells[row][col].isEmpty()) {
            return false;
        }

        DiskColor opponentColor = (color == DiskColor.BLACK) ? DiskColor.WHITE : DiskColor.BLACK;
        int[] dx = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] dy = {-1, 0, 1, 1, 1, 0, -1, -1};

        for (int direction = 0; direction < 8; direction++) {
            int x = row + dx[direction];
            int y = col + dy[direction];
            boolean hasOpponentBetween = false;

            while (x >= 0 && x < SIZE && y >= 0 && y < SIZE) {
                if (cells[x][y].isEmpty()) {
                    break;
                }
                if (cells[x][y].getDisk().getColor() == opponentColor) {
                    hasOpponentBetween = true;
                } else if (cells[x][y].getDisk().getColor() == color) {
                    if (hasOpponentBetween) {
                        return true;  // Valid move found in this direction
                    }
                    break;
                }
                x += dx[direction];
                y += dy[direction];
            }
        }
        return false;

    }
    
    private int evaluateMobility(DiskColor color) {
        // Poids pour les coins et la mobilité
        final int cornerWeight = 20;
        final int mobilityWeight = 1;

        // Calcul de la mobilité
        int myMobility = getValidMoves(color).size();
        DiskColor opponentColor = (color == DiskColor.BLACK) ? DiskColor.WHITE : DiskColor.BLACK;
        int opponentMobility = getValidMoves(opponentColor).size();

        // Calcul des coins
        int myCorners = countCorners(color);
        int opponentCorners = countCorners(opponentColor);

        // Évaluation de la mobilité et des coins
        return myMobility * mobilityWeight - opponentMobility * mobilityWeight 
               + myCorners * cornerWeight - opponentCorners * cornerWeight;
    }

    private int countCorners(DiskColor color) {
        int count = 0;
        int[][] corners = {{0, 0}, {0, SIZE - 1}, {SIZE - 1, 0}, {SIZE - 1, SIZE - 1}};
        for (int[] corner : corners) {
            Cell cell = cells[corner[0]][corner[1]];
            if (!cell.isEmpty() && cell.getDisk().getColor() == color) {
                count++;
            }
        }
        return count;
    }


    public int evaluateDefault(DiskColor color) {
        int score = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Disk disk = getCell(i, j).getDisk();
                if (disk != null) {
                    if (disk.getColor() == color) {
                        score++;
                    } else {
                        score--;
                    }
                }
            }
        }
        return score;
    }
    
    public int evaluate(DiskColor color, String strategy) {
        switch (strategy) {
            case "absolu":
                return evaluateAbsolute(color);
            case "mobility":
                return evaluateMobility(color);
            case "positional":
                return evaluatePositional(color);
            case "mixed":
                return evaluateMixed(color);
            // autres cas...
            default:
                return evaluateDefault(color);
        }
    }

    private int evaluatePositional(DiskColor color) {
        int myScore = 0;
        int opponentScore = 0;
        DiskColor opponentColor = (color == DiskColor.BLACK) ? DiskColor.WHITE : DiskColor.BLACK;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Cell cell = cells[i][j];
                if (!cell.isEmpty()) {
                    int weight = WEIGHTS[i][j];
                    if (cell.getDisk().getColor() == color) {
                        myScore += weight;
                    } else if (cell.getDisk().getColor() == opponentColor) {
                        opponentScore += weight;
                    }
                }
            }
        }

        return myScore - opponentScore;
    }
    
    private int evaluateMixed(DiskColor color) {
        int totalDisks = countDisks(DiskColor.BLACK) + countDisks(DiskColor.WHITE);
        int totalMoves = SIZE * SIZE;
        int earlyGameThreshold = 20; // Nombre de coups pour le début de partie
        int lateGameThreshold = totalMoves - 16; // Nombre de coups pour la fin de partie

        if (totalDisks <= earlyGameThreshold) {
            return evaluatePositional(color);
        } else if (totalDisks <= lateGameThreshold) {
            return evaluateMobility(color);
        } else {
            return evaluateAbsolute(color);
        }
    }




    private int evaluateAbsolute(DiskColor color) {
        int myDisks = countDisks(color);
        DiskColor opponentColor = (color == DiskColor.BLACK) ? DiskColor.WHITE : DiskColor.BLACK;
        int opponentDisks = countDisks(opponentColor);
        return myDisks - opponentDisks;
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Ajouter les numÃ©ros de colonne en en-tÃªte
        sb.append("  "); // Espace pour l'alignement
        for (int j = 0; j < SIZE; j++) {
            sb.append(j).append(" ");
        }
        sb.append("\n");

        // Ajouter le contenu des cellules avec les numÃ©ros de ligne
        for (int i = 0; i < SIZE; i++) {
            sb.append(i).append(" "); // NumÃ©ro de ligne
            for (int j = 0; j < SIZE; j++) {
                sb.append(cells[i][j].toString()).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public void testIsValidMove() {
        // Test 1: Configuration initiale du plateau
        System.out.println("Test 1: Configuration initiale du plateau");
        Board board = new Board();
        System.out.println(board);
        testMove(board, 3, 2, DiskColor.BLACK);
        testMove(board, 2, 3, DiskColor.BLACK);
        testMove(board, 5, 4, DiskColor.BLACK);
        testMove(board, 4, 5, DiskColor.BLACK);
        testMove(board, 3, 5, DiskColor.BLACK);
        testMove(board, 4, 2, DiskColor.BLACK);

        System.out.println("------------------------");

        // Test 2: Configuration personnalisÃ©e
        System.out.println("Test 2: Configuration personnalisÃ©e");
        board = new Board();
        board.cells[3][3].setDisk(new Disk(DiskColor.BLACK));
        board.cells[3][4].setDisk(new Disk(DiskColor.BLACK));
        board.cells[3][5].setDisk(new Disk(DiskColor.WHITE));
        System.out.println(board);
        testMove(board, 3, 2, DiskColor.WHITE);
        testMove(board, 3, 6, DiskColor.BLACK);
        System.out.println("------------------------");

        // Test 3: Autre configuration personnalisÃ©e
        System.out.println("Test 3: Autre configuration personnalisÃ©e");
        board = new Board();
        board.cells[3][3].setDisk(new Disk(DiskColor.BLACK));
        board.cells[3][4].setDisk(new Disk(DiskColor.WHITE));
        board.cells[4][4].setDisk(new Disk(DiskColor.BLACK));
        board.cells[4][3].setDisk(new Disk(DiskColor.WHITE));
        System.out.println(board);
        testMove(board, 2, 2, DiskColor.WHITE);
        testMove(board, 5, 5, DiskColor.BLACK);
    }


    void testPlaceDisk() {
        // Test 1: Configuration initiale du plateau
        System.out.println("Test 1: Configuration initiale du plateau");
        Board board = new Board();
        System.out.println(board);
        makeMove(board, 3, 2, DiskColor.BLACK);
        System.out.println(board);
        makeMove(board, 4, 2, DiskColor.WHITE);
        System.out.println(board);
        makeMove(board, 5, 2, DiskColor.BLACK);
        System.out.println(board);
        makeMove(board, 4, 1, DiskColor.WHITE);
        System.out.println(board);
        makeMove(board, 5, 3, DiskColor.BLACK);
        System.out.println(board);
        System.out.println("------------------------");


    }

    private static void makeMove(Board board, int row, int col, DiskColor color) {
        System.out.println("Making move (" + row + ", " + col + ") for " + color);
        board.placeDisk(row, col, color);
    }

    public Board(Board other) {
        cells = new Cell[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cells[i][j] = new Cell(other.cells[i][j]);
            }
        }
    }




    public static void main(String[] args) {
        //(new Board()).testIsValidMove();
    }

    private static void testMove(Board board, int row, int col, DiskColor color) {
        System.out.println("Testing move (" + row + ", " + col + ") for " + color);
        boolean valid = board.isValidMove(row, col, color);
        System.out.println("Is move (" + row + ", " + col + ") valid for " + color + "? " + valid);
    }


}
