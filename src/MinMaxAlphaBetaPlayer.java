public class MinMaxAlphaBetaPlayer extends Player{

    public MinMaxAlphaBetaPlayer(DiskColor color, String evaluationStrategy) {
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
            int score = minimaxAlphaBeta(tempBoard, DEPTH,Integer.MIN_VALUE,Integer.MAX_VALUE, false); // Depth of 3 for example

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

    // Fonction MinMax avec �lagage alpha-b�ta
    private int minimaxAlphaBeta(Board board, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
        nbNoeud++;
        // Conditions d'arr�t : profondeur atteinte ou pas de coups possibles
        if (depth == 0 || !board.hasValidMoves(DiskColor.BLACK) && !board.hasValidMoves(DiskColor.WHITE)) {
            return board.evaluate(color, evaluationStrategy);
        }
        if (isMaximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : board.getValidMoves(color)) {
                Board tempBoard = new Board(board);
                tempBoard.placeDisk(move.getRow(), move.getCol(), color);
                int eval = minimaxAlphaBeta(tempBoard, depth - 1, alpha, beta, false);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval); // Met � jour la valeur alpha
                // Si beta est inf�rieur ou �gal � alpha, on �laguer les autres branches (coupure beta)
                if (beta <= alpha) break;
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            DiskColor opponentColor = (color == DiskColor.BLACK) ? DiskColor.WHITE : DiskColor.BLACK;
            for (Move move : board.getValidMoves(opponentColor)) {
                Board tempBoard = new Board(board);
                tempBoard.placeDisk(move.getRow(), move.getCol(), opponentColor);
                int eval = minimaxAlphaBeta(tempBoard, depth - 1, alpha, beta, true);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval); // Met � jour la valeur beta
                // Si beta est inf�rieur ou �gal � alpha, on �laguer les autres branches (coupure alpha)
                if (beta <= alpha) break;
            }
            return minEval;
        }
    }
}
