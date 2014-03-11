/*
 * Copyright (c) 2011 Mike Duigou
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights 
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
 * copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.bondolo.tictactoe;

import org.bondolo.tiles.rect.RectTileCoord;
import org.bondolo.tiles.rect.RectTileDimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Mini-demo for tiles.
 */

public final class Main {

    private static final int TILE_SCALES = 3;
    private static final int INITIAL_SCALE = 1;
    private static final int BASE_SCALE_FACTOR = 6;

    /**
     * Singleton
     */
    private Main() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RectTileDimension scales[] = new RectTileDimension[TILE_SCALES];
        for (int scale = 0; scale < scales.length; scale++) {
            scales[scale] = new RectTileDimension(1 << (scale + BASE_SCALE_FACTOR));
            System.out.println(scales[scale]);
        }

        final TicTacMap map = new TicTacMap();
        final TicTacView view = new TicTacView(map, scales, INITIAL_SCALE);

        final JFrame f = new JFrame("Tic Tac Toe");

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // click to play
                view.addMouseListener(new MouseAdapter() {

                    TicTacTile lastClick = null;

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Point2D click = new Point2D.Double((double) e.getX(), (double) e.getY());
                        RectTileCoord coord = view.pointToCoord(click);

                        if (null == coord) {
                            return;
                        }

                        Set<TicTacTile> theWin = map.checkForWin();
                        if (null != theWin) {
                            // clear board and restart
                            view.clearSelection();
                            map.reset();
                            view.repaint();
                            return;
                        }

                        if (null != lastClick) {
                            // clear old highlight
                            view.removeFromSelection(lastClick);
                        }

                        final TicTacTile tile = map.getTile(coord);

                        if (map.play(tile)) {
                            // draw clicked
                            view.addToSelection(tile);
                            lastClick = tile;
                            theWin = map.checkForWin();
                            if (null != theWin) {
                                view.setSelection(theWin);
                            }
                        } else {
                            lastClick = null;
                        }
                    }
                });

                f.getContentPane().add("Center", view);
                f.pack();
                f.setVisible(true);
            }
        });
    }
}
