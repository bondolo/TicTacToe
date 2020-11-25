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

import org.bondolo.tiles.rect.RectTile;
import org.bondolo.tiles.rect.RectTileCoord;
import org.bondolo.tiles.rect.RectTileDimension;
import java.awt.BasicStroke;
import static java.awt.BasicStroke.CAP_ROUND;
import static java.awt.BasicStroke.JOIN_ROUND;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import static java.awt.geom.Path2D.WIND_NON_ZERO;
import java.awt.geom.Point2D;
import javax.swing.UIManager;
import static org.bondolo.tictactoe.TicTacTile.TileState.BLANK;

/**
 *  Handles state and drawing for a single tile in a tic tac toe game.
 */
public class TicTacTile extends RectTile {

    /**
     * Possible tile states.
     */
    public enum TileState {

        BLANK,
        X,
        O;
    };
    /**
     * The current state of this tile.
     */
    private TileState state = BLANK;
    /**
     * Our drawing mark for an "O"
     */
    private final static Shape O = new Ellipse2D.Double(0.0, 0.0, 1.0, 1.0);
    /**
     * Our drawing mark for an "X".
     */
    private final static Shape X;

    static {
        var p = new GeneralPath(WIND_NON_ZERO, 6);
        p.moveTo(0, 0);
        p.lineTo(1, 1);
        p.moveTo(1, 0);
        p.lineTo(0, 1);
        X = p;
    }

    /**
     * The drawing stroke we use for drawing the mark on a tile.
     */
    private final static Stroke MARK_STROKE = new BasicStroke((float) 0.15, CAP_ROUND, JOIN_ROUND);

    public TicTacTile(RectTileCoord coord) {
        super(coord);
    }

    @Override
    public void draw(final Graphics2D g, final Point2D origin, final RectTileDimension dim, final boolean highlight) {
        final var mark = switch (getState()) {
            case X -> X;
            case O -> O;
            default -> null;
        };

        if (null != mark) {
            // draw the mark
            final var insetx = dim.getWidth() / 6;
            final var insety = dim.getHeight() / 6;
            final var x = origin.getX() + insetx;
            final var y = origin.getY() + insety;
            final var w = dim.getWidth() - 2 * insetx;
            final var h = dim.getHeight() - 2 * insety;
            final var currentTransform = g.getTransform();
            final var currentStroke = g.getStroke();
            final var currentColor = g.getColor();

            g.translate(x, y);
            g.scale(w, h);

            g.setStroke(MARK_STROKE);
            g.setColor(UIManager.getColor(highlight ? "TextArea.inactiveForeground" : "TextArea.foreground"));

            g.draw(mark);

            g.setTransform(currentTransform);
            g.setStroke(currentStroke);
            g.setColor(currentColor);
        }
    }

    /**
     * Returns the tile state.
     *
     * @return the state
     */
    public TileState getState() {
        return state;
    }

    /**
     * Sets the tile state.
     *
     * @param state the state to set
     */
    public void setState(TileState state) {
        this.state = state;
    }
}
