package client;

import constants.Const;
import game.Game;
import game.Sprite;
import sender.Sender;

import javax.swing.*;

public class Window extends JFrame {
    private static final long serialVersionUID = 1L;

    Window() {
        Sprite.loadImages();
        Sprite.setMaxLoopStatus();

        add(new Game(Const.COL * Const.SIZE_SPRITE_MAP, Const.LIN * Const.SIZE_SPRITE_MAP));
        setTitle("bomberman");
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        addKeyListener(new Sender());
    }
}
