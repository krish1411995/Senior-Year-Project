#!/bin/sh
mosquitto_pub -h iot.eclipse.org -u username -P group14  -t "/raspi/security" -m "https://www.dropbox.com/sh/cvmpkqwlk3a12ji/AACXKSMeIGmDKxxafd4t5rzZa?dl=0"
