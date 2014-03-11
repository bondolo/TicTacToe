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
import org.bondolo.tiles.rect.RectTileMap;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Implements the game logic for Tic Tac Toe game.
 */
public class TicTacMap extends RectTileMap {

    private static final int BOARD_SIZE = 3;

    /**
     * Who's turn is it currently?
     */
    private TicTacTile.TileState turn = TicTacTile.TileState.X;

    public TicTacMap() {
        super(createBoard());
    }

    private static TicTacTile[][] createBoard() {
        TicTacTile[][] board = new TicTacTile[BOARD_SIZE][BOARD_SIZE];

        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                board[x][y] = new TicTacTile(new RectTileCoord(x, y));
            }
        }

        return board;
    }

    @Override
    public TicTacTile getTile(int x, int y) {
        return (TicTacTile) super.getTile(x, y);
    }

    @Override
    public TicTacTile getTile(RectTileCoord forLoc) {
        return (TicTacTile) super.getTile(forLoc);
    }

    public boolean play(TicTacTile tile) {
        if (TicTacTile.TileState.BLANK != tile.getState()) {
            return false;
        }

        tile.setState(turn);

        // it is now the other player's turn.
        turn = (TicTacTile.TileState.X == turn) ? TicTacTile.TileState.O : TicTacTile.TileState.X;

        return true;
    }

    /**
     *
     * @return a set containing the tiles of the winning combination, an empty
     * set if the game is a stalemate or {@code null} if valid play remains.
     */
    public Set<TicTacTile> checkForWin() {
        TicTacTile first = null;
        TicTacTile second = null;
        TicTacTile third = null;
        boolean win = false;
        boolean playable = false;

        // columns
        for (int x = 0; !win && x < getXSize(); x++) {
            first = getTile(x, 0);
            if (TicTacTile.TileState.BLANK == first.getState()) {
                playable = true;
                continue;
            }

            second = getTile(x, 1);
            third = getTile(x, 2);

            if ((first.getState() == second.getState()) &&
                    (first.getState() == third.getState())) {
                win = true;
            } else {
                playable |= (TicTacTile.TileState.BLANK == second.getState());
                playable |= (TicTacTile.TileState.BLANK == third.getState());
            }
        }

        // rows
        for (int y = 0; !win && y < getYSize(); y++) {
            first = (getTile(0, y));
            if (TicTacTile.TileState.BLANK == first.getState()) {
                playable = true;
                continue;
            }

            second = getTile(1, y);
            third = getTile(2, y);

            if ((first.getState() == second.getState()) &&
                    (first.getState() == third.getState())) {
                win = true;
            } else {
                playable |= (TicTacTile.TileState.BLANK == second.getState());
                playable |= (TicTacTile.TileState.BLANK == third.getState());
            }
        }

        // diagonals
        if (!win) {
            first = getTile(1, 1);
            if (TicTacTile.TileState.BLANK != first.getState()) {
                second = getTile(2, 0);
                third = getTile(0, 2);

                if ((first.getState() == second.getState()) &&
                        (first.getState() == third.getState())) {
                    win = true;
                } else {
                    playable |= (TicTacTile.TileState.BLANK == second.getState());
                    playable |= (TicTacTile.TileState.BLANK == third.getState());
                    second = getTile(0, 0);
                    third = getTile(2, 2);

                    if ((first.getState() == second.getState()) &&
                            (first.getState() == third.getState())) {
                        win = true;
                    } else {
                        playable |= (TicTacTile.TileState.BLANK == second.getState());
                        playable |= (TicTacTile.TileState.BLANK == third.getState());
                    }
                }
            } else {
                playable = true;
            }
        }

        // Return the winning configuration
        final Set<TicTacTile> theWin;
        if (win) {
            // We have a winner
            theWin = new HashSet<TicTacTile>();
            theWin.add(first);
            theWin.add(second);
            theWin.add(third);
        } else if (!playable) {
            // stalemate
            theWin = Collections.emptySet();
        } else {
            // no win.
            theWin = null;
        }

        return theWin;
    }

    /**
     * reset the board to begin a new game.
     */
    public void reset() {
        for (int x = 0; x < getXSize(); x++) {
            for (int y = 0; y < getYSize(); y++) {
                getTile(x, y).setState(TicTacTile.TileState.BLANK);
            }
        }

        turn = TicTacTile.TileState.X;
    }
}
