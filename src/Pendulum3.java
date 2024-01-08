import java.awt.*;
import java.awt.image.BufferedImage;

class Pendulum3 extends Canvas implements Runnable {
  double theta = 30.0 / 180.0 * Math.PI;
  double damp = 0.5;
  double driveAmp = 0.5;
  double driveFreq = 0.5;
  BufferedImage bf = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
  Pendulum3() {
    setSize(400, 400);
    Frame pictureFrame = new Frame("Driven, Damped, Pendulum");
    Panel canvasPanel = new Panel();
    canvasPanel.add(this);
    pictureFrame.add(canvasPanel);
    Panel controlPanel = new Panel();
    pictureFrame.add(controlPanel,BorderLayout.SOUTH);
    pictureFrame.pack();
    pictureFrame.setVisible(true);
    Thread myThread = new Thread(this);
    myThread.start();
  }
  public void paint(Graphics g) {
    int x = (int) Math.round(200 - 150 * Math.sin(this.theta));
    int y = (int) Math.round(200 + 150 * Math.cos(this.theta));
    Graphics bg = bf.getGraphics();
    bg.setColor(Color.lightGray);
    bg.fillRect(0, 0, 400, 400);
    bg.setColor(Color.blue);
    bg.drawOval(195, 195, 10, 10);
    bg.setColor(Color.black);
    bg.drawLine(200, 200, x, y);
    bg.setColor(Color.red);
    bg.drawOval(x - 15, y - 15, 30, 30);
    g.drawImage(bf, 0, 0, null);
  }

  double torque(double theta, double omega, double t) {
    return - Math.sin(theta) - this.damp * omega + this.driveAmp * Math.sin(this.driveFreq * t);
  }
  public void run() {
    double dt = 0.002;
    double thetaMid, omega, omegaMid, alpha, alphaMid, t;
    t = 0;
    this.damp = 0.5;
    this.driveAmp = 0.5;
    this.driveFreq = 0.5;
    omega = 0;
    while(true) {
      for (int i = 0; i < 0.1/dt; i++) {
        //Euler - Richardson algorithm
        alpha = torque(theta, omega, t);
        thetaMid = theta + omega * 0.5 * dt;
        omegaMid = omega + alpha * 0.5 * dt;
        alphaMid = torque(thetaMid, omegaMid, t + 0.5 * dt);
        theta += omegaMid * dt;
        omega += alphaMid * dt;
        t += dt;
      }
      paint(this.getGraphics());
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {

      }
    }
  }
  public static void main(String[] args) {
    new Pendulum3();
  }
}
