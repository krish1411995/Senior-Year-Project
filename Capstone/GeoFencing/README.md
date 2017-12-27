# Explanation
In this module, we'll alert the user as soon as someone enters the room (which should not be compromised) along with the photo of the person entering and without making the person entering know about it. In this module, we will try to data fence a certain area using OpenCV.
The other thing which we have added in the application is that the user can switch on/off the notification.

# Working
The uploadthread_1.py file is runned in the background and whenever there is a change in the Fenced area it automatically clicks the photo, gives a notification on the mocile phone with a link of the uploaded file on the Drop-Box with the time-stamp. The security.sh file is on the raspberry pi which is runned to send the notification of the drop-box link.
