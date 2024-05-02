import java.awt.event.*;

//escuta enquanto a janela (JFrame) estiver em foco
public class Sender extends KeyAdapter {
   int lastKeyCodePressed;
   
   public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_SPACE)
         Client.out.println("pressedSpace " + Game.you.x + " " + Game.you.y);
      else if (isNewKeyCode(e.getKeyCode()))
         Client.out.println("keyCodePressed " + e.getKeyCode());
   }
      
   public void keyReleased(KeyEvent e) {
      Client.out.println("keyCodeReleased " + e.getKeyCode());
      lastKeyCodePressed = -1; //a próxima tecla sempre será nova
   }
   
   boolean isNewKeyCode(int keyCode) {
      boolean ok = (keyCode != lastKeyCodePressed) ? true : false;
      lastKeyCodePressed = keyCode;
      return ok;
   }
}