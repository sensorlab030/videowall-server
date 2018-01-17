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

The wall hardware consists of 26 WS2812b  strips of 81 leds each, distributed over
13 panels (one strip on each side of the panels). The data for the leds is supplied
by two [Teensy 3.2 boards](https://www.pjrc.com/store/teensy32.html), each being 
responsible for about half of the wall. The data is supplied via CAT5e cables through 
OctoWS2811 adapter boards.

![Video wall data distribution](docs/img/data.png "Video wall data distribution")

Power is supplied by two 400W power supplies, each again bing responsible for about\
half of the wall. Power is supplied to the led strips both from the top and bottom
by 20 AWG power cable.

![Video wall power distribution](docs/img/power.png "Video wall power distribution")

## Pixel mapping

Creating led data from a rasterized image the size of the wall is not straightforward
as the video wall does not have a regular grid of led (pixels) like a normal display
does. To facilitate creating animations, we apply pixel mapping to a rasterized image
that has the same proportions as the physical wall, then fetch the colors of the pixels
of that image on the coordinates that the leds are, and use that as the data for the 
leds, removing all the physical empty space that is between the leds.

The resulting pixel image needs to be rotated as the hardware on the wall is installed
90° CCW (like a monitor laying on it's side), which made the physical installation
much easier. 

Then it needs to be split between the two Teensies, which both expect an
image that required a row count that is a multiple of eight. We only have 14 or 12 rows 
(depending on which half of the wall), so we introduce 'phantom' rows that remain black
and are ignored by the hardware (there's just no led strips attached to show those rows).

![Video wall pixel mapping](docs/img/pixel-mapping.png "Video wall pixel mapping")


