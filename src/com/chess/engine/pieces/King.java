package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class King extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = {-9,-8,-7,-1,1,7,8,9};
    public King(final Alliance pieceAlliance,final int piecePosition) {
        super(PieceType.KING,piecePosition, pieceAlliance,true);
    }
    public King(final Alliance pieceAlliance,final int piecePosition,final boolean isFirstMove) {
        super(PieceType.KING,piecePosition, pieceAlliance,isFirstMove);
    }
    public King movePiece(Move move) {
        return new King(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }
    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

            if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset)||
                    isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)){
                continue;
            }

            if (BoardUtils.isValidTileCordinate(candidateDestinationCoordinate)) {
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                if (!candidateDestinationTile.isTileOccupied()) {
                    legalMoves.add(new MajorMoves(board, this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceAtThatLocation = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance2 = pieceAtThatLocation.getPieceAlliance();

                    if (this.pieceAlliance != pieceAlliance2) {
                        legalMoves.add(new MajorAttackMoves(board, this, candidateDestinationCoordinate, pieceAtThatLocation));
                    }

                }


            }
        }
            return ImmutableList.copyOf(legalMoves);

    }
        private static boolean isFirstColumnExclusion (final int currentPos , final int candindateOffset){
            return BoardUtils.FIST_COLUMN[currentPos] && ((candindateOffset == -9)||(candindateOffset == -1)||(candindateOffset == 7));
        }

        private static boolean isEighthColumnExclusion (final int currentPos , final int candindateOffset){
            return BoardUtils.EIGHTH_COLUMN[currentPos] && ((candindateOffset == 9)||(candindateOffset == 1)||(candindateOffset == -7));
        }
    @Override
    public String toString(){
        return PieceType.KING.toString();
    }
}
