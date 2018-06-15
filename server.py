import RPi.GPIO as GPIO
import socket
import os
import sys
import threading
from time import sleep
from picamera import PiCamera
#with picamera.Piamera() as camera

pin = 18
GPIO.setmode(GPIO.BCM)
GPIO.setup(pin, GPIO.OUT)
p = GPIO.PWM(pin, 20)
p.start(0)
cnt = 0

HOST = "192.168.0.18"
PORT = 8885
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print ('Socket created')
s.bind((HOST, PORT))
print ('Socket bind complete')
s.listen(1)
print ('Socket now listening')
#os.system('./exec')
print ('streaming server on')
#from picamera import PiCamera
camera = PiCamera()
def exec1(time):
	camera.start_recording("video.h264")
	sleep(time);

def do_some_stuffs_with_input(input_string):
#	with picamera.PiCamera() as camera:
		if input_string == "start":
			input_string = "start recording"
#			camera.start_preview()
#			camera.start_recording('video.h264')
#			sleep(1)
#			camera.stop_recording()
		elif input_string == "end":
#			camera.stop_preview()
			input_string = "end recording"
#			sleep(1)
#			camera.stop_recording()
#			camera.close()
		elif input_string == "capture":
			input_string = "do capture"
#			camera.start_preview()
			camera.capture('image.jpg')
#			del picamera
#			camera.stop_preview()
		elif input_string == "left":
			p.ChangeDutyCycle(1)
			sleep(0.1)
			p.ChangeDutyCycle(0)
			input_string="servo motor left"
		elif input_string == "right":
                        p.ChangeDutyCycle(8)
			sleep(0.1)
			p.ChangeDutyCycle(0)
			input_string="servo motor right"
#		elif input_string == "import":
		#	from picamera import PiCamera
		#	camera = PiCamera()

		else :
			input_string = input_string + " no order"
		return input_string

#t1=threading.Thread(target=exec1)
#t2=threading.Thread(target=do_some_stuffs_with_input
#t1.start()
try:
	while True:
		
		conn, addr = s.accept()
		print("Connected by ", addr)	
	
		
		data = conn.recv(1024)
		data = data.decode("utf8").strip()
		if not data: break
		print("Received: " + data)

		if data == "start":
			camera.start_recording("video.h264")
	#	while(True):
	#		sleep(1)
	#		data = conn.recv(1024)
	#		if(data == "end"):
	#			camera.stop_recording()
	#			camera.close()
	#			break
		if data == "end":
			camera.stop_recording()
		res = do_some_stuffs_with_input(data)
		print("pi movement :" + res)
		conn.sendall(res.encode("utf-8"))
	
		conn.close()
	s.close()
except KeyboardInterrupt:
    p.stop()
GPIO.cleanup()

