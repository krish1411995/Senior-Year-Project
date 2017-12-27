# Explanation

The init file is uploaded in the NodeMCU which is connected with the Dimmer Circuit. When the Automatic button from the application is pressed then the topic /nodemcu/intensity will have a send "hello" and then after every time interval the tem() function of init.lua file will run, which will check the value of the LDR(attached with nodeMCU), calculate and then automatically set the intensity of the bulb. In the manual mode the topic /nodemcu/intensity will send the values like 10%,20%,...,100% to change the intensity of the bulb manually.

# Dimmer Circuit
![screen shot 2017-12-27 at 12 44 04 am](https://user-images.githubusercontent.com/20076221/34376281-c9aa8dc6-ea9f-11e7-9253-6d3f12b13c87.png)

## Introduction

The board can be used in applications where dimming of 110-220V AC power is required like dimming of bulb or fan. The input can be simple 4 bit high/low signal from microcontroller working at 3V or 5V which is isolated with the use of opto-couplers. Total of 16 levels of power control can be set from totally off(0%) to full on(100%) as per input control levels.

## Features
1. Works on AC power supply.
2. 16 levels of control.
3. Works from any microcontroller input.

## Working

A dimmer switch rapidly turns a light circuit on and off to reduce the energy flowing to a light switch. The central element in this switching circuit is a triode alternating current switch, or triac.A triac is a small semiconductor device, similar to a diode or transistor. Like a transistor, a triac is made up of different layers of semiconductor material. This includes N-type material, which has many free electrons, and P-type material, which has many "holes" where free electrons can go.The triac has two terminals, which are wired into two ends of the circuit. There is always a voltage difference between the two terminals, but it changes with the fluctuation of the alternating current. That is, when current moves one way, the top terminal is positively charged while the bottom terminal is negatively charged, and when the current moves the other way the top terminal is negatively charged while the bottom terminal is positively charged.

## Table
| INPUT LEVEL  | D3 | D2 | D1 | D0 | DIMMER LEVEL |
| ------------- | ------------- |  ------------- | ------------- | ------------- | ------------- |
| 1 | 0  | 0  | 0  | 0  | 100%  |
| 2 | 0  | 0  | 0  | 1  | 86%  |
| 3 | 0  | 0  | 1  | 0  | 80%  |
| 4 | 0  | 0  | 1  | 1  | 75%  |
| 5 | 0  | 1  | 0  | 0  | 60%  |
| 6 | 0  | 1 | 0  | 1  | 65%  |
| 7 | 0  | 1  | 1  | 0  | 60%  |
| 8 | 0  | 1  | 1  | 1  | 50%  |
| 9 | 1  | 0  | 0  | 0  | 40%  |
| 10 | 1  | 0  | 0  | 1  | 30%  |
| 11 | 1  | 0  | 1  | 0  | 25%  |
| 12 | 1  | 0  | 1  | 1  | 20%  |
| 13 | 1  | 1  | 0  | 0  | 15%  |
| 14 | 1  | 1  | 0  | 1  | 10%  |
| 15 | 1  | 1  | 1  | 0  | 5%  |
| 16 | 1  | 1  | 1 | 1  | 0%  |

