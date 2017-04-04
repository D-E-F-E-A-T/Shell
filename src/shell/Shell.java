package shell;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Javier Riveros <walter.riveros@unillanos.edu.co>
 */
public class Shell {
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    java.awt.EventQueue.invokeLater(() -> {
      try {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) 
        if ("Nimbus".equals(info.getName())) {
          UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {}
      
      new GUI().setVisible(true);
    });
  }
}
