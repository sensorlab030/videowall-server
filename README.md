# LED Wall at Sensorlab

## About
This is the software that drives the LED wall in the Sensor Lab event space. It's using WS2812b leds driven by the [OctoWS2811 LED Library](https://www.pjrc.com/teensy/td_libs_OctoWS2811.html) running on two [Teensy 3.2 boards](https://www.pjrc.com/store/teensy32.html) both by [Paul J. Stoffregen](https://www.pjrc.com). This software controls in effect a couple [Processing](https://processing.org/) sketches, and then converts their output to a format that can be sent to the Teensies and is based on PJRC's [VideoDisplay Processing software](https://github.com/PaulStoffregen/OctoWS2811/tree/master/extras/VideoDisplay/Processing).

## Glossary

Throughout the code, several terms are used to define pieces of hardware. Here's a sketch that define what those terms are:

![glossary](https://github.com/sensorlab030/led-wall/blob/master/img/LedWallGlossary.png)

## From a canvas drawing to a LED wall

There are two different ways of creating an animation.

1. Draw on a normal Canvas, and extend your animation from `CanvasAnimation`.
The drawing gets projected on a grid of rows and columns and only a subset of the pixels of your drawing get represented on the LED wall.

2. Draw on a pixel grid, and extend your animation from `Animation`. In that case, the canvas is sized to the number of led strips in width, and the number of led rows in height. Which means that each pixel on your canvas represent an LED on the wall.

![CanvasToLED](https://github.com/sensorlab030/led-wall/blob/master/img/CanvasToAnimationToPreview.png)
