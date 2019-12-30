package com.chess.engine.pieces;
/*
@Author Felicien
@26 Dec 2019

 */

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;

public abstract class Piece {


    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final PieceType pieceType;
    private final int cachedHashCode;


    protected final boolean isFirstMove;

    Piece(final PieceType pieceType,final int piecePosition, final Alliance pieceAlliance,final boolean isFirstMove){
        this.piecePosition=piecePosition;
        this.pieceAlliance=pieceAlliance;
        this.isFirstMove =isFirstMove;
        this.pieceType=pieceType;
        cachedHashCode = computeHashCode();
    }

    protected  int computeHashCode(){
        int result= pieceType.hashCode();
        result=31*result+pieceAlliance.hashCode();
        result=31+result+piecePosition;
        result=31*result+(isFirstMove? 1:0);
        return result;
    }


    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }

    public  boolean isFirstMove() {
        return isFirstMove;
    }

    public int getPiecePosition(){
        return piecePosition;
    }
    public abstract Piece movePiece(Move move);

    @Override
    public boolean equals(final Object other){

        if(this==other){
            return true;
        }
        if(!(other instanceof Piece)){
            return false;
        }
        final Piece pieceOth= (Piece) other;
        return pieceAlliance ==pieceOth.getPieceAlliance() && pieceType==pieceOth.getPieceType() &&
                piecePosition==pieceOth.getPiecePosition() && isFirstMove== pieceOth.isFirstMove();
    }
    @Override
    public int hashCode(){
     return this.cachedHashCode;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public int getPieceValue() {
        return pieceType.getPieceValue();
    }

    public enum PieceType{
        PAWN("P",100){
            @Override
            public boolean isKing(){
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }

        },
        KNIGHT("N",300) {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP("B",300) {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R",500) {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return true;
            }
        },
        QUEEN("Q",900) {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING("K",10000) {
            @Override
            public boolean isKing() {
                return true;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        };

        private final int pieceValue;
        private String pieceName;
        PieceType(String pieceName,int pieceValue){
            this.pieceValue=pieceValue;
            this.pieceName=pieceName;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

        public abstract boolean isKing();
        public abstract boolean isRook();


        public int getPieceValue(){
            return this.pieceValue;
        }

    }
}
