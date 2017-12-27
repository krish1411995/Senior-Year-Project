# import the necessary packages
from picamera.array import PiRGBArray
from picamera import PiCamera
import time
import threading
import dropbox
import cv2
import argparse
import datetime
import imutils
import numpy as np
import subprocess
import smtplib
from email.MIMEMultipart import MIMEMultipart
from email.MIMEText import MIMEText
from email.MIMEBase import MIMEBase
from email import encoders
camera = PiCamera()
camera.resolution = (640, 480)
camera.framerate = 32
rawCapture = PiRGBArray(camera, size=(640, 480))
        
refPt = []
class myThread (threading.Thread):
    def __init__(self, threadID, name, counter):
        threading.Thread.__init__(self)
        self.threadID = threadID
        self.name = name
        self.counter = counter
    def run(self):
        print "Starting " + self.name
        # Get lock to synchronize threads
        threadLock.acquire()
        print_time(self.name, self.counter, 3)
        # Free lock to release next thread
        threadLock.release()

def print_time(threadName, delay, counter):
    count = 0
    timestr = time.strftime("%Y%m%d-%H%M%S")
    camera.capture('/home/pi/Desktop/image'+timestr+'.jpg') 
    while count < 1 :
        client = dropbox.client.DropboxClient('1_dV_kMexIAAAAAAAAAADjHVh0rMmCRbsTuiim6RW5SzVnqeKlcf_8-hdhVxd_SB')
        print 'linked account:'
        f=open('/home/pi/Desktop/image'+timestr+'.jpg','rb')
        response = client.put_file('/Alert...!!!/image'+timestr+'.jpg',f)
        count = 1
        print "uploaded:"

threadLock = threading.Lock()
threads = []
cropping = False 
def click_and_crop(event, x, y, flags, param):
	# grab references to the global variables
	global refPt, cropping
 
	# if the left mouse button was clicked, record the starting
	# (x, y) coordinates and indicate that cropping is being
	# performed
	if event == cv2.EVENT_LBUTTONDOWN:
		refPt = [(x, y)]
		print (refPt)
		cropping = True
 
	# check to see if the left mouse button was released
	elif event == cv2.EVENT_LBUTTONUP:
		# record the ending (x, y) coordinates and indicate that
		# the cropping operation is finished
		refPt.append((x, y))
		print (refPt)
		cropping = False
 
		# draw a rectangle around the region of interest
		cv2.rectangle(image, refPt[0], refPt[1], (0, 255, 0), 2)
		cv2.imshow("image", image)
##############################################
		
def run_video():
        
        time.sleep(0.1)
        avg = None
        #motionCounter = 0
        current = datetime.datetime.now()

        for f in camera.capture_continuous(rawCapture, format="bgr", use_video_port=True):

                frame = f.array
                timestamp = datetime.datetime.now()
                text = "Safe"
 
	
                frame = imutils.resize(frame, width=640,height=480)
                cv2.rectangle(frame, refPt[0], refPt[1], (0, 255, 0), 1)
                gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
                gray = cv2.GaussianBlur(gray, (21, 21), 0)
 
	
                if avg is None:
                        print "[INFO] starting background model..."
                        avg = gray.copy().astype("float")
                        rawCapture.truncate(0)
                        continue
 
	
                cv2.accumulateWeighted(gray, avg, 0.5)
                frameDelta = cv2.absdiff(gray, cv2.convertScaleAbs(avg))
		
                thresh = cv2.threshold(frameDelta, 20, 255,cv2.THRESH_BINARY)[1]
                thresh = cv2.dilate(thresh, None, iterations=2)
                (_, cnts, _) = cv2.findContours(thresh.copy(), cv2.RETR_EXTERNAL,
                        cv2.CHAIN_APPROX_SIMPLE)
 
	
                for c in cnts:
		
                        if cv2.contourArea(c) < 800:
                                continue
                        M = cv2.moments(c)
                        cX = int(M["m10"] / M["m00"])
                        cY = int(M["m01"] / M["m00"])
                        if cY < refPt[0][1]:
                            continue
                        if cY > refPt[1][1]:
                            continue
                        if cX < refPt[0][0]:
                            continue
                        if cX > refPt[1][0]:
                            continue
		
                        (x, y, w, h) = cv2.boundingRect(c)
                        cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 255, 0), 1)
                        text = "Invaded"
 
                # draw the text and timestamp on the frame
	
                cv2.putText(frame, "Room Status: {}".format(text), (10, 20),cv2.FONT_HERSHEY_SIMPLEX, 0.5, (245, 123, 255), 2)
                #ts2 = timestamp.strftime("%A %d %B %Y %I:%M:%S%p")
                #cv2.putText(frame, ts, (10, frame.shape[0] - 10), cv2.FONT_HERSHEY_SIMPLEX,0.35, (0, 0, 255), 1)
                if text == "Invaded":
                        lasttime = datetime.datetime.now()
                        cv2.rectangle(frame, refPt[0], refPt[1], (0, 0, 255), 2)
                        diff = lasttime-current
                        print diff.total_seconds()
                        if diff.total_seconds()>15:
                                # Create new threads
                                print("before creating new thread")
                                thread1 = myThread(1, "Thread-1", 1)
                                # Start new Threads
                                thread1.start()
                                print("main thread is on")
                                # Add threads to thread list
                                threads.append(thread1)
                                print("after the thread is appended")
                                # Wait for all threads to complete
                                for t in threads:
                                    t.join()
                                print "Exiting Main Thread"
                                current=lasttime
                                subprocess.call(['/home/pi/Desktop/security.sh'])
 
                # otherwise, the room is not occupied

                
                cv2.imshow("Security Feed", frame)
        #       cv2.imshow("Thresh", thresh)
                #cv2.imshow("Frame Delta", frameDelta)
	
                #cv2.imshow("Security Feed", frame)
                key = cv2.waitKey(1) & 0xFF
                # if the `q` key is pressed,     break from the lop
        	if key == ord("q"):
                    	break
 
	
        	rawCapture.truncate(0)
##############################################################################
# load the image, clone it, and setup the mouse callback function
camera.capture('/home/pi/Desktop/initial.jpg')
image = cv2.imread('/home/pi/Desktop/initial.jpg',0)
#image=cv2.resize(image,(640,480))
clone = image.copy()
cv2.namedWindow("image")
image = imutils.resize(image, width=640,height=480)
cv2.putText(image, "Press 'y' to confirm" , (10, 20),cv2.FONT_HERSHEY_SIMPLEX, 0.5, (245, 123, 255), 2)
cv2.putText(image, "Press 'r' to retry" , (10, 40),cv2.FONT_HERSHEY_SIMPLEX, 0.5, (245, 123, 255), 2)
cv2.setMouseCallback("image", click_and_crop)
 
# keep looping until the 'q' key is pressed
while True:
	# display the image and wait for a keypress
	cv2.imshow("image", image)
	key = cv2.waitKey(1) & 0xFF
 
	# if the 'r' key is pressed, reset the cropping region
	if key == ord("r"):
		image = clone.copy()
 
	# if the 'c' key is pressed, break from the loop
	elif key == ord("y"):
                run_video()
                cv2.destroyWindow(image)
		
	elif key == ord("q"):
                break
 
# if there are two reference points, then crop the region of interest
# from teh image and display it
if len(refPt) == 2:
	roi = clone[refPt[0][1]:refPt[1][1], refPt[0][0]:refPt[1][0]]
	cv2.imshow("ROI", roi)
	cv2.waitKey(0)
 
# close all open windows
cv2.destroyAllWindows()
