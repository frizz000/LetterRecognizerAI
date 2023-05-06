import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class DrawingGUI {

    private final int width = 280;
    private final int height = 280;
    private final JFrame frame;
    private final DrawingPanel drawingPanel;
    private final JButton recognizeButton;
    private final JButton clearButton;
    private final MLP mlp;
    private final String[] labels;

    public DrawingGUI(MLP mlp, String[] labels) {
        this.mlp = mlp;
        this.labels = labels;

        frame = new JFrame("Letter Recognition");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLayout(new BorderLayout());

        drawingPanel = new DrawingPanel();
        frame.add(drawingPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        recognizeButton = new JButton("Recognize");
        clearButton = new JButton("Clear");
        buttonPanel.add(recognizeButton);
        buttonPanel.add(clearButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);
        registerListeners();
        frame.setVisible(true);
    }

    private void registerListeners() {
        recognizeButton.addActionListener(e -> {
            BufferedImage drawnImage = drawingPanel.getImage();
            double[] features = ImageFeatureExtractor.extractFeatures(drawnImage);
            int bestLabelIndex = mlp.predictLabel(features);

            String predictedLabel = labels[bestLabelIndex];
            JOptionPane.showMessageDialog(frame, "Predicted label: " + predictedLabel);
        });

        clearButton.addActionListener(e -> drawingPanel.clear());
    }

    class DrawingPanel extends JPanel {

        private final BufferedImage image;
        private final Graphics2D graphics;

        public DrawingPanel() {
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            graphics = image.createGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(Color.BLACK);
            graphics.setStroke(new BasicStroke(10));

            MouseAdapter mouseAdapter = new MouseAdapter() {
                private int prevX, prevY;

                @Override
                public void mousePressed(MouseEvent e) {
                    prevX = e.getX();
                    prevY = e.getY();
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    int x = e.getX();
                    int y = e.getY();
                    graphics.drawLine(prevX, prevY, x, y);
                    prevX = x;
                    prevY = y;
                    repaint();
                }
            };
            addMouseListener(mouseAdapter);
            addMouseMotionListener(mouseAdapter);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, null);
        }

        public BufferedImage getImage() {
            return image;
        }

        public void clear() {
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(Color.BLACK);
            repaint();
        }
    }
}
