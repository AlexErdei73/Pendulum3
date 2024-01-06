import java.awt.*;
class Pendulum3 extends Canvas {
  Pendulum3() {
    setSize(400, 400);
    Frame pictureFrame = new Frame("Driven, Damped, Pendulum");
    Panel canvasPanel = new Panel();
    canvasPanel.add(this);
    pictureFrame.add(canvasPanel);
    pictureFrame.pack();
    pictureFrame.setVisible(true);
  }
  public void paint(Graphics g) {
    g.drawString("Hello World!", 160, 200);
  }
  public static void main(String[] args) {
    new Pendulum3();
  }
}
