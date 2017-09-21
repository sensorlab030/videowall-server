This is the software that drives the LED wall in the Sensor Lab event space. It's using WS2812b leds driven by the [OctoWS2811 LED Library](https://www.pjrc.com/teensy/td_libs_OctoWS2811.html) running on two [Teensy 3.2 boards](https://www.pjrc.com/store/teensy32.html) both by [Paul J. Stoffregen](https://www.pjrc.com). This software controls in effect a couple [Processing](https://processing.org/) sketches, and then converts their output to a format that can be sent to the Teensies and is based on PJRC's [VideoDisplay Processing software](https://github.com/PaulStoffregen/OctoWS2811/tree/master/extras/VideoDisplay/Processing). 