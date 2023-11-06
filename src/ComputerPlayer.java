public class ComputerPlayer extends Player {
    final int DEPTH = 3;
    private String evaluationStrategy = "mixed";
    public ComputerPlayer(DiskColor color) {
        super(color);
    }

    @Override
    public Move play(Board board) {
        System.out.println("IA PLAY");
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = null;

        for (Move move : board.getValidMoves(color)) {
            Board tempBoard = new Board(board);  // Clone the board to simulate moves
            tempBoard.placeDisk(move.getRow(), move.getCol(), color);
            //int score = minimax(tempBoard, DEPTH, false); // Depth of 3 for example
            int score = minimaxAlphaBeta(tempBoard, DEPTH,Integer.MIN_VALUE,Integer.MAX_VALUE, false); // Depth of 3 for example
            
            //NEGAMAX
            //int score = negamax(tempBoard, DEPTH, color); // or negamaxAlphaBeta if you want to test that
            //int score = negamaxAlphaBeta(tempBoard, DEPTH,Integer.MIN_VALUE,Integer.MAX_VALUE, color);

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }
    
    private int minimax(Board board, int depth, boolean isMaximizingPlayer) {
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
    
        // Fonction MinMax avec élagage alpha-bêta
        private int minimaxAlphaBeta(Board board, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
            // Conditions d'arrêt : profondeur atteinte ou pas de coups possibles
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
                    alpha = Math.max(alpha, eval); // Met à jour la valeur alpha
                    // Si beta est inférieur ou égal à alpha, on élaguer les autres branches (coupure beta)
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
                    beta = Math.min(beta, eval); // Met à jour la valeur beta
                    // Si beta est inférieur ou égal à alpha, on élaguer les autres branches (coupure alpha)
                    if (beta <= alpha) break;
                }
                return minEval;
            }
        }
        

        private int negamax(Board board, int depth, DiskColor currentColor) {
            if (depth == 0 || !board.hasValidMoves(DiskColor.BLACK) && !board.hasValidMoves(DiskColor.WHITE)) {
                return board.evaluate(currentColor,evaluationStrategy) * (currentColor == color ? 1 : -1);
            }

            int maxEval = Integer.MIN_VALUE;
            DiskColor opponentColor = (currentColor == DiskColor.BLACK) ? DiskColor.WHITE : DiskColor.BLACK;

            for (Move move : board.getValidMoves(currentColor)) {
                Board tempBoard = new Board(board);
                tempBoard.placeDisk(move.getRow(), move.getCol(), currentColor);
                int eval = -negamax(tempBoard, depth - 1, opponentColor);
                maxEval = Math.max(maxEval, eval);
            }

            return maxEval;
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


