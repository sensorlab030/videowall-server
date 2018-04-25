package nl.sensorlab.videowall.animation.canvasanimations;

import nl.sensorlab.videowall.animation.BaseCanvasAnimation;
import nl.sensorlab.videowall.udp.UDPVideoStreamClient;
import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * Animation class that starts a UDP Client of video streams and display the
 * received images on the wall
 */
public class VideoStreamAnimation extends BaseCanvasAnimation {

    // Images and stream
    private UDPVideoStreamClient udpClient;

    public VideoStreamAnimation(PApplet applet) {
        super(applet, DEFAULT_SCALE, CANVAS_MODE_2D);
        udpClient = new UDPVideoStreamClient();
    }

    @Override
    protected final void drawCanvasAnimationFrame(PGraphics g, double dt) {
        g.image(udpClient.getImage(), 0, 0, g.width, g.height);
    }

    @Override
    public void setData(String data) {
        udpClient.setExpectedSender(data);
    }

    /**
     * Init UDP Video Stream Client
     */
    @Override
    public void isStarting() {
        udpClient.start();
    }

    /**
     * Stop UDP Video Stream Client
     */
    @Override
    public void isStopping() {
        udpClient.stop();
    }
}
