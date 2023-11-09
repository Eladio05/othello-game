public class MinMaxPlayer extends Player{
    final int DEPTH = 3;

    public MinMaxPlayer(DiskColor color, String evaluationStrategy) {
        super(color);
        this.evaluationStrategy = evaluationStrategy;
    }

    @Override
    public Move play(Board board) {
        long startTime = System.nanoTime(); // Début de mesure du temps

        int bestScore = Integer.MIN_VALUE;
        Move bestMove = null;

        for (Move move : board.getValidMoves(color)) {
            Board tempBoard = new Board(board);  // Clone the board to simulate moves
            tempBoard.placeDisk(move.getRow(), move.getCol(), color);
            int score = minimax(tempBoard, DEPTH, false); // Depth of 3 for example

            if (score >= bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        long endTime = System.nanoTime(); // Fin de mesure du temps
        totalTime += (endTime - startTime); // Accumulation du temps total
        playCount++; // Incrémentation du nombre d'appels
        return bestMove;
    }



    private int minimax(Board board, int depth, boolean isMaximizingPlayer) {
        nbNoeud++;

        if (depth == 0 || !board.hasValidMoves(DiskColor.BLACK) && !board.hasValidMoves(DiskColor.WHITE)) {
            return board.evaluate(color, evaluationStrategy);
        }

        if (isMaximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : board.getValidMoves(color)) {
                Board tempBoard = new Board(board);
                tempBoard.placeDisk(move.getRow(), move.getCol(), color);
                int eval = minimax(tempBoard, depth - 1, false);
                maxEval = Math.max(maxEval, eval);
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            DiskColor opponentColor = (color == DiskColor.BLACK) ? DiskColor.WHITE : DiskColor.BLACK;
            for (Move move : board.getValidMoves(opponentColor)) {
                Board tempBoard = new Board(board);
                tempBoard.placeDisk(move.getRow(), move.getCol(), opponentColor);
                int eval = minimax(tempBoard, depth - 1, true);
                minEval = Math.min(minEval, eval);
            }
            return minEval;
        }
    }
}
