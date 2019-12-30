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

public class Rook extends Piece {

    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-8, 8, 1, -1};


    public Rook(final Alliance pieceAlliance,final int piecePosition) {
        super(PieceType.ROOK,piecePosition, pieceAlliance,true);
    }

    public Rook(final Alliance pieceAlliance,final int piecePosition, final boolean isFirstMove) {
        super(PieceType.ROOK,piecePosition, pieceAlliance,isFirstMove);
    }

    public Rook movePiece(Move move) {
        return new Rook(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }
    @Override
    public Collection<Move> calculateLegalMoves(Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final int candidateCordOffset : CANDIDATE_MOVE_VECTOR_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition;

            while (BoardUtils.isValidTileCordinate(candidateDestinationCoordinate)) {
                if(isFirstColumnExclusion(candidateDestinationCoordinate,candidateCordOffset)||
                        isEighthColumnExclusion(candidateDestinationCoordinate,candidateCordOffset)){
                    break;
                }


                candidateDestinationCoordinate += candidateCordOffset;


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
                        break;
                    }

                }


            }

        }
        return ImmutableList.copyOf(legalMoves);

    }


    private static boolean isFirstColumnExclusion (final int currentPos , final int candidateOffset){
        return BoardUtils.FIST_COLUMN[currentPos] && ((candidateOffset == -1));
    }
    private static boolean isEighthColumnExclusion (final int currentPos , final int candindateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPos] && ((candindateOffset == 1));
    }

    @Override
    public String toString(){
        return PieceType.ROOK.toString();
    }
}
