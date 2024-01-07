import java.awt.*;
class Pendulum3 extends Canvas {
  double theta = 30.0 / 180.0 * Math.PI;
  double damp = 0.5;
  double driveAmp = 0.5;
  double driveFreq = 0.5;
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

  double torque(double theta, double omega, double t) {
    return - Math.sin(theta) - this.damp * omega + this.driveAmp * Math.sin(this.driveFreq * t);
  }
  public void run() {
    double dt = 0.0002;
    double thetaMid, omega, omegaMid, alpha, alphaMid, t;
    t = 0;
    this.damp = 0.5;
    this.driveAmp = 0.5;
    this.driveFreq = 0.5;
    omega = 0;
    while(true) {
      for (int i = 0; i < 0.01/dt; i++) {
        //Euler - Richardson algorithm
        alpha = torque(theta, omega, t);
        thetaMid = theta + omega * 0.5 * dt;
        omegaMid = omega + alpha * 0.5 * dt;
        alphaMid = torque(thetaMid, omegaMid, t + 0.5 * dt);
        theta += omegaMid * dt;
        omega += alphaMid * dt;
        t += dt;
      }
      repaint();
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {

      }
    }
  }
  public static void main(String[] args) {
    Pendulum3 pendulum = new Pendulum3();
    pendulum.run();
    System.out.println("Hello Animation!");
  }
}
