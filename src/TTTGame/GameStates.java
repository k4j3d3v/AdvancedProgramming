/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TTTGame;

/**
 *
 * @author luigi
 */
 public enum GameStates{
        GAME_START("Start playing!"),
        X_TURN("Next move: X"),
        O_TURN("Next move: O"),
        GAME_END("Game over"),
        WIN_X("Winner is X"),
        WIN_O("Winner is O");

        private final String text;

        /**
         * @param text
         */
        GameStates(final String text) {
            this.text = text;
        }

        /* (non-Javadoc)
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return text;
        }
    }