package player;

import constants.Const;
import game.Game;
import game.Sprite;

public class StatusChanger extends Thread {
    Player p;
    String status;
    int index;
    boolean playerInMotion;

    StatusChanger(Player p, String initialStatus) {
        this.p = p;
        this.status = initialStatus;
        index = 0;
        playerInMotion = true;
    }

    public void run() {
        while (true) {
            p.status = status + "-" + index;
            if (playerInMotion) {
                index = (++index) % Sprite.maxLoopStatus.get(status);
                p.panel.repaint();
            }

            try {
                Thread.sleep(Const.RATE_PLAYER_STATUS_UPDATE);
            } catch (InterruptedException e) {
            }

            if (p.status.equals("dead-4")) {
                p.alive = false;
                if (Game.you == p)
                    System.exit(1);
            }
        }
    }

    public void setLoopStatus(String status) {
        this.status = status;
        index = 1;
        playerInMotion = true;
    }

    public void stopLoopStatus() {
        playerInMotion = false;
        index = 0;
    }
}
