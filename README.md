# Sensor Lab Video wall

## About
This is the code that drives the video wall in the Sensor Lab event space. It's 
using WS2812b leds driven by the [OctoWS2811 LED Library](https://www.pjrc.com/teensy/td_libs_OctoWS2811.html) 
running on two [Teensy 3.2 boards](https://www.pjrc.com/store/teensy32.html) both by [Paul J. Stoffregen](https://www.pjrc.com). 

The project is divided in the two firmwares for the two Teensies (in (/firmware)),
which is the same Arduino program, just with different settings. Then there is the
client software (in (/client)) that provides the Teensies with data to send to the leds. This software
is a [Processing](https://processing.org/) application (built using Eclipse), that 
contains several PApplet sketches, and then converts their output to a format that 
can be sent to the Teensies. This client software is based on PJRC's 
[VideoDisplay Processing software](https://github.com/PaulStoffregen/OctoWS2811/tree/master/extras/VideoDisplay/Processing).


## Wall hardware
TODO

## Converting Processing sketches into led data
TODO

