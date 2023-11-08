public class NegaMaxAlphaBetaPlayer extends Player{
    final int DEPTH = 3;

    public NegaMaxAlphaBetaPlayer(DiskColor color, String evaluationStrategy) {
        super(color);
        this.evaluationStrategy = evaluationStrategy;
    }

    @Override
    public Move play(Board board) {
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = null;

        for (Move move : board.getValidMoves(color)) {
            Board tempBoard = new Board(board);  // Clone the board to simulate moves
            tempBoard.placeDisk(move.getRow(), move.getCol(), color);

            //NEGAMAX
            int score = negamaxAlphaBeta(tempBoard, DEPTH,Integer.MIN_VALUE,Integer.MAX_VALUE, color);

            if (score >= bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private int negamaxAlphaBeta(Board board, int depth, int alpha, int beta, DiskColor currentColor) {
        if (depth == 0 || !board.hasValidMoves(DiskColor.BLACK) && !board.hasValidMoves(DiskColor.WHITE)) {
            return board.evaluate(currentColor, evaluationStrategy) * (currentColor == color ? 1 : -1);
        }

        int maxEval = Integer.MIN_VALUE;
        DiskColor opponentColor = (currentColor == DiskColor.BLACK) ? DiskColor.WHITE : DiskColor.BLACK;

        for (Move move : board.getValidMoves(currentColor)) {
            Board tempBoard = new Board(board);
            tempBoard.placeDisk(move.getRow(), move.getCol(), currentColor);
            int eval = -negamaxAlphaBeta(tempBoard, depth - 1, -beta, -alpha, opponentColor);
            maxEval = Math.max(maxEval, eval);
            alpha = Math.max(alpha, eval);
            if (alpha >= beta) break;
        }

        return maxEval;
    }
}
