package nl.sensorlab.videowall.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;

import processing.core.PConstants;
import processing.core.PImage;

public class UDPVideoStreamClient implements Runnable {

    // Misc constants
    private static final int PORT_IN = 10233; // Network in port
    private static final int STREAM_IMAGE_WIDTH = 280;               // Expected height of the video stream image
    private static final int STREAM_IMAGE_HEIGHT = 76;                // Expected width of the video stream image 
    
    /**
     * Length of the data buffer calculated as number of pixels * 3 (3 bytes for 3 colors, RGB) + 3 
     * bytes of the IMG start of the packet
     */
    private final static int BUFFER_LENGTH = STREAM_IMAGE_WIDTH * STREAM_IMAGE_HEIGHT * 3 + 3;

    // Misc
    private DatagramSocket inSocket; // Receive socket
    private Object streamImageLock = new Object(); // Lock for stream image
    
    private InetAddress expectedSender;
    
    private PImage streamImage;

    // Thread status
    private boolean running = false;
    private Thread t;
    
    public UDPVideoStreamClient() {
        streamImage = new PImage(STREAM_IMAGE_WIDTH, STREAM_IMAGE_HEIGHT, PConstants.ARGB);
    }

    /**
     * Stop receiving packet and disconnect the socket
     */
    public void stop() {
        inSocket.disconnect();
        running = false;
    }

    /**
     * Set expected sender host
     * @param senderHost
     */
    public void setExpectedSender(String senderHost) {
        
        // Set expected host
        try {
            expectedSender = InetAddress.getByName(senderHost);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        
    }

    public void start() {

        try {

            // Initialize receiving socket
            inSocket = new DatagramSocket(PORT_IN);
            inSocket.setSoTimeout(100);

            // Start deamon thread for listening
            t = new Thread(this);
            t.setDaemon(true);
            t.start();

            // Enable client
            running = true;
            System.out.println("UDP Video Stream Client / Listening to port: " + PORT_IN
                    + ". Expecting packets from IP : " + expectedSender.getHostAddress());

        } catch (SocketException e) {
            e.printStackTrace();
        } 

    }

    /**
     * Receives image datagram packet and updates the streamImage to new image
     */
    private void receivePacket() {

        byte[] inBuffer = new byte[BUFFER_LENGTH]; // In buffer @TODO: move to class member?

        try {

            // Receive packet on socket
            DatagramPacket packet = new DatagramPacket(inBuffer, inBuffer.length);
            inSocket.receive(packet);
            
            // Confirm sender
            if (!packet.getAddress().equals(expectedSender)) {
                throw new Exception("Invalid sender: " + packet.getAddress().getHostAddress());
            }
            
            // Confirm expected packet size
            if (packet.getLength() != BUFFER_LENGTH) {
                throw new Exception("Invalid packet size");
            }

            // Confirm header
            String header = new String(Arrays.copyOfRange(inBuffer, 0, 3));
            if (!header.equals("IMG")) {
                throw new Exception("Invalid packet header");
            }
            
            // Update buffered image with RGB image data and replace image with tmp image
            int bufferIndex = 3;
            synchronized (streamImageLock) {
                for (int i = 0; i < streamImage.pixels.length; i++) {
                    // int alpha = inBuffer[bufferIndex++] & 0xff; // We don't send alpha values to save bandwidth)
                    int alpha = 255 & 0xff;
                    int red = inBuffer[bufferIndex++] & 0xff;
                    int green = inBuffer[bufferIndex++] & 0xff;
                    int blue = inBuffer[bufferIndex++] & 0xff;
    
                    streamImage.pixels[i] = (alpha << 24) + (red << 16) + (green << 8) + blue;
                    
                }
                streamImage.updatePixels();
            }
            
        } catch (SocketTimeoutException e) {
            // Doesn't matter, had no RX
        } catch (SocketException e) {
            System.err.println(e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Ignoring packet: " + e.getMessage());
        }

    }

    /**
     * Get the last received stream image
     * 
     * @return
     */
    public PImage getImage() {
        return streamImage;
    }

    @Override
    public void run() {

        while (running && !t.isInterrupted()) {
            receivePacket();
        }

        inSocket.close();

        System.out.println("UPD Video Stream Client stopped");

    }
}
