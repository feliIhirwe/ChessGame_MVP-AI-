package com.chess.engine.board;

import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Queen;
import com.chess.engine.pieces.Rook;

import static com.chess.engine.board.Board.*;

public abstract class Move {



    protected final Board board;
    protected final Piece movedPiece;
    protected final int destinationCoordinate;
    protected final boolean isFirstMover;

    public static final Move NULL_MOVE=new NullMoves();

    private Move(final Board board,final Piece piece, final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = piece;
        this.destinationCoordinate = destinationCoordinate;
        isFirstMover = movedPiece.isFirstMove();
    }
    private Move(final Board board, final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = null;
        this.destinationCoordinate = destinationCoordinate;
        isFirstMover = false;
    }

    public int getDestinationCoordinate() {
        return destinationCoordinate;
    }

    public Board execute() {
        final Builder builder= new Builder();
        for(final Piece piece:this.board.getCurrentPlayer().getActivePieces()){
            //TODOOOOOOOOOO
            if(!this.movedPiece.equals(piece)){
                builder.setPiece(piece);
            }

        }
        for (final Piece piece:this.board.getCurrentPlayer().getOpponent().getActivePieces()){
            builder.setPiece(piece);
        }
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setNextMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());

        return builder.build();
    }

    public Board getBoard() {
        return board;
    }

    public static final class MajorMoves extends Move {

        public MajorMoves(Board board, Piece piece, int destinationCoordinate) {
            super(board, piece, destinationCoordinate);
        }

        @Override
        public boolean equals(Object other) {
            return this==other || other instanceof MajorMoves && super.equals(other);
        }

//        @Override
//        public String toString() {
//            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.getDestinationCoordinate());
//        }
    }
    public Piece getMovedPiece(){
        return movedPiece;
    }


    @Override
    public int hashCode(){
        final int prime=31;
        int result=1;
        result=prime*result+this.destinationCoordinate;
        result=prime*result+this.movedPiece.hashCode();
        result=prime*result+this.movedPiece.getPiecePosition();
        return result;
    }


    @Override
    public boolean equals(final Object other){

        if(this==other){
            return true;
        }
        if(!(other instanceof Move)){
            return false;
        }
        final Move otherMove= (Move) other;
        return  getDestinationCoordinate()==otherMove.getDestinationCoordinate() &&
                getMovedPiece().equals(otherMove.getMovedPiece()) &&
                getCurrentCoordinate()==otherMove.getDestinationCoordinate();
    }



    public Piece getAttackedPiece(){
        return null;
    }
    public boolean IsAttack(){
        return false;
    }
    public boolean isCastleingMove(){
        return false;
    }


    public static class AttackMoves extends Move {

        final Piece attackedPiece;
        public AttackMoves(Board board, Piece piece, int destinationCoordinate,Piece attackedPiece) {
            super(board, piece, destinationCoordinate);
            this.attackedPiece=attackedPiece;
        }
        @Override
        public boolean IsAttack() {
            return true;
        }

        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }

        @Override
        public boolean equals(final Object other) {
            if(this==other){
                return true;
            }
            if(!(other instanceof AttackMoves)){
                return false;
            }
            final Move otherAttackMoves= (AttackMoves) other;
            return  getDestinationCoordinate()==otherAttackMoves.getDestinationCoordinate() &&
                    getMovedPiece().equals(otherAttackMoves.getMovedPiece());
        }

        @Override
        public int hashCode() {
            return super.hashCode()+this.attackedPiece.hashCode();
        }
    }

    public static final class MajorAttackMoves extends AttackMoves {

        public MajorAttackMoves(Board board, Piece piece, int destinationCoordinate,Piece pieceAttacked) {
            super(board, piece, destinationCoordinate,pieceAttacked);
        }

        @Override
        public boolean equals(Object other) {
            return this==other || other instanceof MajorAttackMoves && super.equals(other);
        }

        @Override
        public String toString() {
            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.getDestinationCoordinate());
        }
    }


    public static final class PawnMove extends Move {

        public PawnMove(Board board, Piece piece, int destinationCoordinate) {
            super(board, piece, destinationCoordinate);
        }
        @Override
        public boolean equals(Object other) {
            return this== other|| other instanceof PawnMove && super.equals(other);
        }
    }
    public static class PawnAttackMove extends AttackMoves {

        public PawnAttackMove(Board board, Piece piece, int destinationCoordinate, final Piece attckPiece) {
            super(board, piece, destinationCoordinate,attckPiece);
        }

        @Override
        public boolean equals(Object other) {
            return this== other|| other instanceof PawnAttackMove && super.equals(other);
        }
    }

    public static final class PawnEnPassantAttackMove extends PawnAttackMove {

        public PawnEnPassantAttackMove(Board board, Piece piece, int destinationCoordinate, final Piece attckPiece) {
            super(board, piece, destinationCoordinate,attckPiece);
        }
        @Override
        public boolean equals(Object other) {
            return this== other|| other instanceof PawnEnPassantAttackMove && super.equals(other);
        }

        @Override
        public Board execute() {
            final Board.Builder builder = new Builder();
            for (final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
                if (!piece.equals(this.getAttackedPiece())) {
                    builder.setPiece(piece);
                }

            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setNextMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            //builder.setMoveTransition(this);
            return builder.build();
        }
    }
    public static class PawnPromotion extends Move{

        final Move decolMove;
        final Pawn promotedPawn;
        public PawnPromotion(final Move move) {
            super(move.getBoard(), move.getMovedPiece(), move.getDestinationCoordinate());
            this.decolMove = move;
            promotedPawn = (Pawn)move.getMovedPiece();;
        }

        @Override
        public int hashCode() {
            return decolMove.hashCode()+(31*promotedPawn.hashCode());
        }
        @Override
        public boolean equals(Object other) {
            return this == other|| other instanceof PawnPromotion && (super.equals(other));
        }
        @Override
        public Board execute() {
            final Board pawnMovedMoard=this.decolMove.execute();
            final Builder builder= new Builder();
            for(final Piece piece:pawnMovedMoard.getCurrentPlayer().getActivePieces()){
                if(!this.promotedPawn.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece:pawnMovedMoard.getCurrentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }

            final Piece promotedPawnn = this.promotedPawn.getPromotedPiece().movePiece(this);
            builder.setPiece(promotedPawnn);
            builder.setNextMoveMaker(pawnMovedMoard.getCurrentPlayer().getOpponent().getAlliance());

            return builder.build();
        }

        @Override
        public boolean IsAttack() {
            return decolMove.IsAttack();
        }

        @Override
        public Piece getAttackedPiece() {
            return decolMove.getAttackedPiece();
        }

        @Override
        public String toString() {
            return " ";
        }
    }
    public static final class PawnJump extends Move {

        public PawnJump(Board board, Piece piece, int destinationCoordinate) {
            super(board, piece, destinationCoordinate);
        }

        @Override
        public Board execute() {

            final Builder builder= new Builder();
            for(final Piece piece:this.board.getCurrentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece:this.board.getCurrentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }

            final Pawn movedPawn= (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setNextMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());

            return builder.build();
        }

    }
    static abstract class CastleMoves extends Move {


        protected final Rook castleRook;
        private final int castleRookStart;
        private final int castleRookDestination;

        public CastleMoves(Board board, Piece piece, int destinationCoordinate, Rook castleRook, int castleRookStart, int castleRookDestination) {
            super(board, piece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        public Rook getCastleRook() {
            return this.castleRook;
        }

        @Override
        public boolean isCastleingMove() {
            return true;
        }

        @Override
        public boolean equals(Object other) {
            if(this== other|| other instanceof CastleMoves){ //&& super.equals(other)){
                return true;
            }
            final CastleMoves otherCastleMove=(CastleMoves) other;
            return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
        }
        @Override
        public int hashCode(){
            final int prime=31;
            int result=super.hashCode();
            result=prime*result+this.castleRook.hashCode();
            result=prime*result+this.castleRookDestination;
            return result;
        }

        @Override
        public Board execute() {
            Builder builder=  new Builder();
            for(final Piece piece:this.board.getCurrentPlayer().getActivePieces()){
                //TODOOOOOOOOOO
                if(!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece:this.board.getCurrentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.castleRook.getPieceAlliance(),this.castleRookDestination));
            builder.setNextMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }
    public static final class KingSideCastleMoves extends CastleMoves {

        public KingSideCastleMoves(Board board,
                                   Piece piece,
                                   int destinationCoordinate,
                                   Rook castleRook,
                                   int castleRookStart,
                                   int castleRookDestination) {
            super(board, piece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }
        @Override
        public boolean equals(Object other) {
            return this== other|| other instanceof KingSideCastleMoves && super.equals(other);
        }
        @Override
        public String toString() {
            return "0--0";
        }
    }
    public static final class QueenSideCastleMoves extends CastleMoves {

        public QueenSideCastleMoves(Board board,
                                    Piece piece,
                                    int destinationCoordinate,Rook castleRook,
                                    int castleRookStart,
                                    int castleRookDestination) {
            super(board, piece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }
        @Override
        public boolean equals(Object other) {
            return this== other|| other instanceof QueenSideCastleMoves && super.equals(other);
        }
        @Override
        public String toString() {
            return "0-0-0";
        }
    }
    public static final class NullMoves extends Move {

        public NullMoves() {
            super(null, -1);
        }

        @Override
        public Board execute() {
            throw  new RuntimeException("CAN NOT EXECUTE NULL MOVE");
        }

//        @Override
//        public int getCurrentCoordinate(){
//            return -1;
//        }
    }
    public static class MoveFactory{
        private MoveFactory(){
            new RuntimeException("Not instantiable");
        }
        public static Move createMove(final Board board,
                                      final int currentCoordinate,
                                      final int destinationCprd){
            for (Move move:board.getAllLegalMoves()){
                if(move.getDestinationCoordinate()==destinationCprd &&
                        move.getCurrentCoordinate()== currentCoordinate){
                    return move;
                }
            }
            return NULL_MOVE;
        }

    }

    private int getCurrentCoordinate() {
        return this.movedPiece.getPiecePosition();
    }
    @Override
    public String toString() {
        return  BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition())+BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
    }
}
