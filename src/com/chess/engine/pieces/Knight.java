package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.board.BoardUtils;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class Knight extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES={-17,-15,-10,-6,6,10,15,17};
    public Knight(final Alliance pieceAlliance,final int piecePosition) {
        super(PieceType.KNIGHT,piecePosition, pieceAlliance,true);
    }
    public Knight(final Alliance pieceAlliance,final int piecePosition,final boolean isFirstMove) {
        super(PieceType.KNIGHT,piecePosition, pieceAlliance,isFirstMove);
    }
    public Knight movePiece(Move move) {
        return new Knight(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }
    @Override
    public Collection<Move> calculateLegalMoves(Board board) {

        final  List<Move> legalMoves= new ArrayList<>();
        for (final int currentCandidateOffset: CANDIDATE_MOVE_COORDINATES){
            final int candidateDestinationCoordinate=this.piecePosition+currentCandidateOffset;

            if(BoardUtils.isValidTileCordinate(candidateDestinationCoordinate)){
                final Tile candidateDestinationTile=board.getTile(candidateDestinationCoordinate);

                if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset)||
                        isSecondColumnExclusion(this.piecePosition, currentCandidateOffset)||
                        isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset)||
                        isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)){
                    continue;
                }

                if(!candidateDestinationTile.isTileOccupied()){
                    legalMoves.add(new MajorMoves(board,this,candidateDestinationCoordinate));
                }
                else {
                    final Piece pieceAtThatLocation= candidateDestinationTile.getPiece();
                    final  Alliance pieceAlliance2=pieceAtThatLocation.getPieceAlliance();

                    if(this.pieceAlliance != pieceAlliance2){
                        legalMoves.add(new MajorAttackMoves(board,this,candidateDestinationCoordinate,pieceAtThatLocation));
                    }

                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }
    private static boolean isFirstColumnExclusion (final int currentPos , final int candindateOffset){
        return BoardUtils.FIST_COLUMN[currentPos] && ((candindateOffset == -17)||(candindateOffset == -10)||(candindateOffset == 6)||(candindateOffset == 15));
    }

    private static boolean isSecondColumnExclusion (final int currentPos , final int candindateOffset){
        return BoardUtils.SECOND_COLUMN[currentPos] && ((candindateOffset == -10)||(candindateOffset == 6));
    }


    private static boolean isSeventhColumnExclusion (final int currentPos , final int candindateOffset){
        return BoardUtils.SEVENTH_COLUMN[currentPos] && ((candindateOffset == 10)||(candindateOffset == -6));
    }

    private static boolean isEighthColumnExclusion (final int currentPos , final int candindateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPos] && ((candindateOffset == 17)||(candindateOffset == 10)||(candindateOffset == -6)||(candindateOffset == -15));
    }
    @Override
    public String toString(){
        return PieceType.KNIGHT.toString();
    }
}
