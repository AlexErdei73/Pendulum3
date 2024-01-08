import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

class Pendulum3 extends Canvas implements Runnable {
    double theta = 30.0 / 180.0 * Math.PI;  //Initial angle 30deg
    double damp = 0.5;
    double driveAmp = 0.5;
    double driveFreq = 2.0/3.0;
    //We use double buffering to stop the flickering of the animation
    BufferedImage bf = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
    DoubleScroller driveAmpScroller;
    boolean running = false;

    Pendulum3() {
        setSize(400, 400);  //animation window 400px X 400px
        Frame pictureFrame = new Frame("Driven, Damped, Pendulum");
        pictureFrame.addWindowListener(new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            super.windowClosing(e);
            System.exit(0);
          }
        });
        Panel canvasPanel = new Panel();
        canvasPanel.add(this);
        pictureFrame.add(canvasPanel);
        Panel controlPanel = new Panel();
        this.driveAmpScroller = new DoubleScroller("Drive Amplitude", 0, 2.0, 0.01, 0.5);
        controlPanel.add(this.driveAmpScroller);
        Button startStopButton = new Button("Start");
        startStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                running = !running;
                if (running) startStopButton.setLabel("Stop");
                else startStopButton.setLabel("Start");
            }
        });
        controlPanel.add(startStopButton);
        pictureFrame.add(controlPanel, BorderLayout.SOUTH);
        pictureFrame.pack();
        pictureFrame.setVisible(true);

        Thread simulationThread = new Thread(this);
        simulationThread.start();   //it executes the run method
    }

    public void paint(Graphics g) {
        //coordinates of the end point, rod length: 150px
        int x = (int) Math.round(200 - 150 * Math.sin(this.theta));
        int y = (int) Math.round(200 + 150 * Math.cos(this.theta));
        //We are drawing on the buffer instead of the canvas
        Graphics bg = bf.getGraphics();
        bg.setColor(Color.lightGray);
        bg.fillRect(0, 0, 400, 400);    //background rectangle
        bg.setColor(Color.blue);
        bg.drawOval(195, 195, 10, 10);  //pivot center:(200px,200px) radius:5px
        bg.setColor(Color.black);
        bg.drawLine(200, 200, x, y);    //rod line
        bg.setColor(Color.red);
        bg.drawOval(x - 15, y - 15, 30, 30);    //pendulum body center: (x, y) radius: 15px
        //After drawing we flush the content of the buffer to the canvas
        //This is atomic operation and no screen refreshment is happening during this
        //Hence no more flickering of the animation
        g.drawImage(bf, 0, 0, null);
    }

    double torque(double theta, double omega, double t) {
        return -Math.sin(theta) - this.damp * omega + this.driveAmp * Math.sin(this.driveFreq * t);
    }

    //simulation thread
    public void run() {
        Plot phaseSpacePlot = new Plot("Phase Space Plot", -Math.PI, Math.PI, 0.5, -Math.PI, Math.PI, 0.5);
        phaseSpacePlot.setPointSize(1);
        double dt = 0.002;  //time step
        double thetaMid, omega, omegaMid, alpha, alphaMid, t;
        t = 0;  //time starts from 0
        omega = 0;  //starting angular velocity 0
        while (true) {  //infinite simulation loop
            if (this.running) {
                for (int i = 0; i < 0.1 / dt; i++) {
                    //Euler - Richardson algorithm
                    alpha = torque(theta, omega, t);
                    thetaMid = theta + omega * 0.5 * dt;
                    omegaMid = omega + alpha * 0.5 * dt;
                    alphaMid = torque(thetaMid, omegaMid, t + 0.5 * dt);
                    theta += omegaMid * dt;
                    omega += alphaMid * dt;
                    t += dt;

                    //Draw the phase space diagram
                    double normTheta = theta; //put angle value between -PI and PI
                    while (normTheta < -Math.PI || normTheta > Math.PI) {
                        if (normTheta > Math.PI) normTheta -= 2*Math.PI;
                        if (normTheta < -Math.PI) normTheta += 2*Math.PI;
                    }
                    phaseSpacePlot.addPoint(normTheta, omega);
                }
                //Update animation when the for loop done
                paint(this.getGraphics());
            }
            //Clear the phase space diagram when the drive amplitude has changed
            if (this.driveAmp != this.driveAmpScroller.getValue()) phaseSpacePlot.clearThePlot();
            //Update drive amplitude
            this.driveAmp = this.driveAmpScroller.getValue();
            //Make thread wait for drawing animation
            try { Thread.sleep(10); } catch (InterruptedException e) {}
        }
    }

    public static void main(String[] args) {
        new Pendulum3();
    }
}
