package com.chess.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.pieces.*;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Board extends Component {

    private final List<Tile> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;
    private final WhitePlayer whitePlayer;
    public final Player currentPlayer;
    private final BlackPlayer blackPlayer;
    final Pawn enPassantPawn;




    public Board(final Builder builder) {
        this.gameBoard=createGameBoard(builder);
        this.whitePieces= calculateActivePieces(gameBoard, Alliance.WHITE);
        this.blackPieces= calculateActivePieces(gameBoard, Alliance.BLACK);

        final Collection<Move> whiteStandardLegalMoves= calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackStandardLegalMoves= calculateLegalMoves(this.blackPieces);

        this.enPassantPawn =builder.enPassantPawn;
        this.whitePlayer= new WhitePlayer(this,whiteStandardLegalMoves,blackStandardLegalMoves);
        this.blackPlayer= new BlackPlayer(this,whiteStandardLegalMoves,blackStandardLegalMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer,this.blackPlayer);
    }

    public Pawn getEnPassantPawn() {
        return enPassantPawn;
    }

    public Collection<Piece> getBlackPieces(){
        return this.blackPieces;
    }
    public Collection<Piece> getWhitePieces(){
        return this.whitePieces;
    }

    private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
        final List<Move> legalMov= new ArrayList<>();
        for(final Piece piece:pieces){
            legalMov.addAll(piece.calculateLegalMoves(this));
        }
        return  ImmutableList.copyOf(legalMov);
    }

    private Collection<Piece> calculateActivePieces(List<Tile> gameBoard, final Alliance alliance) {
        final List<Piece> activePieces= new ArrayList<>();
        for (final Tile tile: gameBoard){
            if(tile.isTileOccupied()){
                final Piece piece=tile.getPiece();
                if(piece.getPieceAlliance()== alliance ){
                    activePieces.add(piece);
                }
            }
        }
        return ImmutableList.copyOf(activePieces);
    }

    private List<Tile> createGameBoard(final Builder builder) {
        final Tile[] tiles= new Tile[BoardUtils.NUM_TILES];
        for(int i=0; i< BoardUtils.NUM_TILES;i++){
            tiles[i]= Tile.createTile(i, builder.boardConfig.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }

    public Tile getTile(final int candidateDestinationCoordinate) {
    return gameBoard.get(candidateDestinationCoordinate);
    }

    public static Board createStandardBoard(){
        final Builder builder = new Builder();
        builder.setPiece(new Rook(Alliance.BLACK, 0));
        builder.setPiece(new Knight(Alliance.BLACK, 1));
        builder.setPiece(new Bishop(Alliance.BLACK, 2));
        builder.setPiece(new Queen(Alliance.BLACK, 3));
        builder.setPiece(new King(Alliance.BLACK, 4));
        builder.setPiece(new Bishop(Alliance.BLACK, 5));
        builder.setPiece(new Knight(Alliance.BLACK, 6));
        builder.setPiece(new Rook(Alliance.BLACK, 7));
        builder.setPiece(new Pawn(Alliance.BLACK, 8));
        builder.setPiece(new Pawn(Alliance.BLACK, 9));
        builder.setPiece(new Pawn(Alliance.BLACK, 10));
        builder.setPiece(new Pawn(Alliance.BLACK, 11));
        builder.setPiece(new Pawn(Alliance.BLACK, 12));
        builder.setPiece(new Pawn(Alliance.BLACK, 13));
        builder.setPiece(new Pawn(Alliance.BLACK, 14));
        builder.setPiece(new Pawn(Alliance.BLACK, 15));

        //WHITE

        builder.setPiece(new Pawn(Alliance.WHITE, 48));
        builder.setPiece(new Pawn(Alliance.WHITE, 49));
        builder.setPiece(new Pawn(Alliance.WHITE, 50));
        builder.setPiece(new Pawn(Alliance.WHITE, 51));
        builder.setPiece(new Pawn(Alliance.WHITE, 52));
        builder.setPiece(new Pawn(Alliance.WHITE, 53));
        builder.setPiece(new Pawn(Alliance.WHITE, 54));
        builder.setPiece(new Pawn(Alliance.WHITE, 55));
        builder.setPiece(new Rook(Alliance.WHITE, 56));
        builder.setPiece(new Knight(Alliance.WHITE, 57));
        builder.setPiece(new Bishop(Alliance.WHITE, 58));
        builder.setPiece(new Queen(Alliance.WHITE, 59));
        builder.setPiece(new King(Alliance.WHITE, 60));
        builder.setPiece(new Bishop(Alliance.WHITE, 61));
        builder.setPiece(new Knight(Alliance.WHITE, 62));
        builder.setPiece(new Rook(Alliance.WHITE, 63));
        //MOVE MAKER
        builder.setNextMoveMaker(Alliance.WHITE);
        return builder.build();
    }

    public Player whitePlayer() {
        return this.whitePlayer;
    }

    public Player blackPlayer() {
        return this.blackPlayer;
    }

    public final Player getCurrentPlayer(){
        return currentPlayer;
    }

    public Iterable<Move> getAllLegalMoves() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(), this.blackPlayer.getLegalMoves()));
    }

    public static class Builder{

        Map<Integer, Piece> boardConfig;
        Alliance nextMoveMaker;
        Pawn enPassantPawn;

        public Builder() {
            this.boardConfig= new HashMap<>();
        }

        public Builder setPiece(final Piece piece){
            this.boardConfig.put(piece.getPiecePosition(),piece);
            return this;
        }

        public Builder setNextMoveMaker(Alliance nextMoveMaker) {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public Board build(){
            return new Board(this);
        }

        public void setEnPassantPawn(Pawn enPassantPawn) {
            this.enPassantPawn=enPassantPawn;
        }
        public Pawn getEnPassantPawn() {
            return enPassantPawn;
        }
    }

    @Override
    public String toString() {

        StringBuilder buildd= new StringBuilder();
        for (int i=0; i<BoardUtils.NUM_TILES; i++){
            final String tileText= this.gameBoard.get(i).toString();
            buildd.append(String.format("%3s",tileText));
            if((i+1)% BoardUtils.NUM_TILES_PER_ROW==0){
                buildd.append("\n");
            }
        }

        return buildd.toString();
    }
}
