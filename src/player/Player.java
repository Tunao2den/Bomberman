package player;

import client.Client;
import constants.Const;
import game.Sprite;
import game.Game;

import java.awt.Graphics;
import javax.swing.JPanel;

//tanto para you quanto para enemy
// for both you and enemy
public class Player {
   public int x;
   public int y;
   public String status, color;
   public JPanel panel;
   public boolean alive;

   public StatusChanger sc;

   public Player(int id, JPanel panel) throws InterruptedException {
      this.x = Client.spawn[id].x;
      this.y = Client.spawn[id].y;
      this.color = Sprite.personColors[id];
      this.panel = panel;
      this.alive = Client.alive[id];

      (sc = new StatusChanger(this, "wait")).start();
   }

   public void draw(Graphics g) {
      if (alive)
         g.drawImage(Sprite.ht.get(color + "/" + status), x, y, Const.WIDTH_SPRITE_PLAYER, Const.HEIGHT_SPRITE_PLAYER, null);
   }
}

