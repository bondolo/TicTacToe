/*
 * Copyright © 2011, 2020 Mike Duigou
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

import org.bondolo.tiles.rect.RectTileDimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.stream.IntStream;
import javax.swing.JFrame;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * Mini-demo for tiles.
 */
public final class Main {

    /**
     * Tile size scales available
     */
    private static final int TILE_SCALES = 3;
    /**
     * Initial tile scale used
     */
    private static final int INITIAL_SCALE = 1;
    /**
     * base scaling factor for tile size
     */
    private static final int BASE_SCALE_FACTOR = 6;

    /**
     * Tile scales, powers of 2 starting at {@link #BASE_SCALE_FACTOR}
     */
    private final static RectTileDimension[] TILE_SCALE_DIMENSIONS
            = IntStream.range(0, TILE_SCALES)
                    .map(scale -> 1 << (scale + BASE_SCALE_FACTOR))
                    .mapToObj(RectTileDimension::new)
                    .peek(dim -> System.out.println(dim))
                    .toArray(RectTileDimension[]::new);

    /**
     * No instances
     */
    private Main() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        var map = new TicTacMap();
        var view = new TicTacView(map, TILE_SCALE_DIMENSIONS, INITIAL_SCALE);

        // click to play
        view.addMouseListener(new MouseAdapter() {

            TicTacTile lastClick = null;

            @Override
            public void mouseClicked(MouseEvent e) {
                Point2D click = new Point2D.Double((double) e.getX(), (double) e.getY());
                var coord = view.pointToCoord(click);

                if (null == coord) {
                    return;
                }

                var theWin = map.checkForWin();
                if (theWin.isPresent()) {
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

                final var tile = map.getTile(coord);

                if (map.play(tile)) {
                    // draw clicked
                    view.addToSelection(tile);
                    lastClick = tile;
                    theWin = map.checkForWin();
                    theWin.ifPresent(view::setSelection);
                } else {
                    lastClick = null;
                }
            }
        });

        var f = new JFrame("Tic Tac Toe");
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);
        f.getContentPane().add("Center", view);

        java.awt.EventQueue.invokeLater(() -> {
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
