package com.chess.engine.board;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public class BoardUtils {


    public static final boolean [] FIST_COLUMN = initColumnn(0) ;
    public static final boolean [] SECOND_COLUMN = initColumnn(1) ; ;
    public static final boolean [] SEVENTH_COLUMN = initColumnn(6) ; ;
    public static final boolean [] EIGHTH_COLUMN = initColumnn(7) ; ;

    public static final boolean [] EIGHTH_RANK = initRow(0);
    public static final boolean [] SEVENTH_RANK = initRow(8);
    public static final boolean [] SIXTH_RANK = initRow(16);
    public static final boolean [] FITH_RANK = initRow(24);
    public static final boolean [] FOURTH_RANK = initRow(32);
    public static final boolean [] THIRD_RANK = initRow(40);
    public static final boolean [] SECOND_RANK = initRow(48);
    public static final boolean [] FIRST_RANK = initRow(56);
    private static final String[] ALGEBRAIC_NOTATION= initializedAlgebraicNotation();
    private static final Map<String,Integer> POSITION_TO_COORDINATE = initializePostionToCordinateMap() ;


    private static Map<String, Integer> initializePostionToCordinateMap() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();
        for (int i = 0; i < NUM_TILES; i++) {
            positionToCoordinate.put(ALGEBRAIC_NOTATION[i], i);
        }
        return ImmutableMap.copyOf(positionToCoordinate);
    }



    private static String[] initializedAlgebraicNotation() {
        String[] a ={"a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8","a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7", "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6", "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5", "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4", "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3", "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2", "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"};
        return a;
    }

    private static boolean[] initRow(int rowNumber) {
        final boolean[] rows= new boolean[NUM_TILES];
        do{
            rows[rowNumber]=true;
            rowNumber++;
        }
        while (rowNumber%NUM_TILES_PER_ROW!=0);

        return rows;
    }

    public static final int NUM_TILES=64;
    public static final int NUM_TILES_PER_ROW=8;

    private static boolean[] initColumnn(int columnNumber) {

        final boolean[] column= new boolean[NUM_TILES];
        do{
            column[columnNumber]=true;
            columnNumber+=NUM_TILES_PER_ROW;
        }
        while (columnNumber<NUM_TILES);

        return column;
    }

    public BoardUtils() {
        throw new RuntimeException("FUCK YOU!! YOU CAN NOT INSTATIATE ME!!");
    }

    public static boolean isValidTileCordinate(final int coordinate) {
        return coordinate>=0 && coordinate <NUM_TILES;
    }


    public static int getCoordinateAtPosition(String position) {
        return POSITION_TO_COORDINATE.get(position);

    }
    public static String getPositionAtCoordinate(int destinationCoordinate) {
        return ALGEBRAIC_NOTATION[destinationCoordinate];
    }
}
