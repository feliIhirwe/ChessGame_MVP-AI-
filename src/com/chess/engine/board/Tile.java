package com.chess.engine.board;
/*
@Author Felicien
@26 Dec 2019

 */
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;



public abstract class Tile {

    protected final int tileCordinate;
    private static final Map<Integer,EmptyTile> EMPTY_TILE_CACHE= createAllPossibleTiles();

    private static Map<Integer, EmptyTile> createAllPossibleTiles() {

        final Map<Integer,EmptyTile> emptyTileMap= new HashMap<>();

        for (int i=0; i<BoardUtils.NUM_TILES; i++){
            emptyTileMap.put(i, new EmptyTile(i));
        }
        return ImmutableMap.copyOf(emptyTileMap);
    }

    public static Tile createTile( final int tileCordinate, final Piece piece){
        return piece != null ? new OccupiedTile(tileCordinate,piece) : EMPTY_TILE_CACHE.get(tileCordinate);
    }
    private Tile(int tile){
        tileCordinate=tile;
    }

    public abstract  boolean isTileOccupied();
    public abstract Piece getPiece();

    public int getTileCoordinate() {
        return tileCordinate;
    }

    public static final  class EmptyTile extends Tile{
        private EmptyTile(final int cordinate)  {
            super(cordinate);
        }

        @Override
        public boolean isTileOccupied() {
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }

        @Override
        public String toString() {
            return "_";
        }
    }


    public static final  class OccupiedTile extends Tile{

        private final Piece pieceOnTile;

        private OccupiedTile(int cordinate, Piece pieceOnTile)  {
            super(cordinate);
            this.pieceOnTile=pieceOnTile;
        }

        @Override
        public boolean isTileOccupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return pieceOnTile;
        }

        @Override
        public String toString() {
            return getPiece().getPieceAlliance().isBlack()? getPiece().toString().toLowerCase():
                    getPiece().toString();
        }
    }
}
