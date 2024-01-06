import java.awt.*;
class Pendulum3 extends Canvas {
  double theta = 30.0 / 180.0 * Math.PI;
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
    int x = 200 - (int) Math.round(150 * Math.sin(this.theta));
    int y = 200 + (int) Math.round(150 * Math.cos(this.theta));
    g.setColor(Color.blue);
    g.drawOval(195, 195, 10, 10);
    g.setColor(Color.black);
    g.drawLine(200, 200, x, y);
    g.setColor(Color.red);
    g.drawOval(x - 15, y - 15, 30, 30);
  }
  public static void main(String[] args) {
    new Pendulum3();
  }
}
