

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

class BackgroundPanel extends JPanel implements Serializable{
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        backgroundImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
    
}
