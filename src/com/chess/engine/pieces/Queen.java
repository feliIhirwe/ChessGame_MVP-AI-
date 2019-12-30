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

public class Queen extends Piece {

    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9, -7,-8, -1, 8, 1, 7, 9};

    public Queen(final Alliance pieceAlliance,final int piecePosition) {
        super(PieceType.QUEEN,piecePosition, pieceAlliance,true);
    }
    public Queen(final Alliance pieceAlliance,final int piecePosition,final boolean isFirstMove) {
        super(PieceType.QUEEN,piecePosition, pieceAlliance,isFirstMove);
    }
    public Queen movePiece(Move move) {
        return new Queen(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
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
    private static boolean isFirstColumnExclusion (final int currentPos , final int candindateOffset){
        return BoardUtils.FIST_COLUMN[currentPos] && ((candindateOffset == -9)||(candindateOffset == 7)||(candindateOffset == -1));
    }
    private static boolean isEighthColumnExclusion (final int currentPos , final int candindateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPos] && ((candindateOffset == 9)||(candindateOffset == -7)||(candindateOffset == 1));
    }
    @Override
    public String toString(){
        return PieceType.QUEEN.toString();
    }
    }

