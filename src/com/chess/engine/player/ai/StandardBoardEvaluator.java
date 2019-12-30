package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.Player;

public final class StandardBoardEvaluator implements BoardEvaluator {

    @Override
    public int evaluate(final Board board, final int depth) {
        return scorePlayer(board, board.whitePlayer(), depth) - scorePlayer( board, board.blackPlayer(), depth);
    }

    private int scorePlayer(Board board, Player player, int depth) {

        return pieceValue(player)+mobility(player)+check(player)+checkMate(player,depth)+castled(player);
    }

    private int castled(Player player) {
        return player.isCastled() ? 60:0;
    }

    private int checkMate(Player player, int depth) {
        return player.getOpponent().isInCheckMate()?10000*depthBonus(depth):0;
    }

    private int depthBonus(int depth) {
        return depth ==0?1:100*depth;
    }

    private int check(Player player) {
        return player.getOpponent().isInCheck()? 100:0;
    }

    private int mobility(Player player) {
        return player.getLegalMoves().size();
    }

    private static int pieceValue(final Player player) {
    int pieceValueScore=0;
    for (final Piece piece:player.getActivePieces()){
        pieceValueScore+=piece.getPieceValue();
        }
    return pieceValueScore;
    }


}
