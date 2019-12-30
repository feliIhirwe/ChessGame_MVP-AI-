package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;
import com.google.common.primitives.Ints;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static com.chess.gui.Table.*;

public class TakenPiecePanel extends JPanel {


    private static final Dimension TAKEN_PIECE_DIMENSION = new Dimension(40,80);
    private final JPanel northPanel;
    private final JPanel southPanel;
    private static final Color PANEL_COLOR = Color.red;
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);

    public TakenPiecePanel(){
        super(new BorderLayout());
        setBackground(Color.lightGray);
        setBorder(PANEL_BORDER);

        northPanel = new JPanel(new GridLayout(8,2));
        southPanel = new JPanel(new GridLayout(8,2));
        northPanel.setBackground(PANEL_COLOR);
        southPanel.setBackground(PANEL_COLOR);
        add(this.northPanel, BorderLayout.NORTH);
        add(this.southPanel, BorderLayout.SOUTH);
        setPreferredSize(TAKEN_PIECE_DIMENSION);
    }

    public void redo(MoveLog moveLog) {
        southPanel.removeAll();
        southPanel.removeAll();
        final List<Piece> whiteTakenPieces= new ArrayList<>();
        final List<Piece> blackTakenPieces= new ArrayList<>();

        for (final Move move:moveLog.getMoves()){
            if(move.IsAttack()){
                final  Piece takenPi= move.getAttackedPiece();
                if(takenPi.getPieceAlliance().isWhite()){
                    whiteTakenPieces.add(takenPi);
                }
                else if(takenPi.getPieceAlliance().isBlack()){
                    blackTakenPieces.add(takenPi);
                }
                else {
                    throw new RuntimeException("Should not reach here!!");
                }
            }
        }

        Collections.sort(whiteTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
            }
        });

        Collections.sort(blackTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
            }
        });

        for (Piece takenPiece:whiteTakenPieces){
            try{
                final BufferedImage image= ImageIO.read(new File("art/piece/plair"
                        + takenPiece.getPieceAlliance().toString().substring(0,1)+""+takenPiece.toString()));
                final ImageIcon icon= new ImageIcon(image);
                final JLabel imageLabel= new JLabel();
                this.southPanel.add(imageLabel);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        for (Piece takenPiece:blackTakenPieces){
            try{
                final BufferedImage image= ImageIO.read(new File("art/fancy2"
                        + takenPiece.getPieceAlliance().toString().substring(0,1)+""+takenPiece.toString()));
                final ImageIcon icon= new ImageIcon(image);
                final JLabel imageLabel= new JLabel();
                this.southPanel.add(imageLabel);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        validate();
    }
}
