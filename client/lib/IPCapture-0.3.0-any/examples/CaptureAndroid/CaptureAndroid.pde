/* IPCapture sample sketch for Android            *
 *                                                *
 * === IMPORTANT ===                              *
 * Remember to enable INTERNET permissions in the *
 * Android -> Sketch permissions menu             */

import ipcapture.android.*;

IPCapture cam;

void setup() {
  size(640,480);
  cam = new IPCapture(this, "http://212.219.113.227/axis-cgi/mjpg/video.cgi", "", "");
  cam.start();
  
  // this works as well:
  
  // cam = new IPCapture(this);
  // cam.start("url", "username", "password");
  
  // It is possible to change the MJPEG stream by calling stop()
  // on a running camera, and then start() it with the new
  // url, username and password.
}

void draw() {
  if (cam.isAvailable()) {
    cam.read();
    image(cam,0,0);
  }
}

void keyPressed() {
  if (key == ' ') {
    if (cam.isAlive()) cam.stop();
    else cam.start();
  }
}
