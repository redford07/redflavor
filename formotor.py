import socket
import os
import sys
import threading
from time import sleep
HOST = "192.168.1.25"
PORT = 8886
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print ('Socket created')
s.bind((HOST, PORT))
print ('Socket bind complete')
s.listen(1)
print ('Socket now listening')

def do_some_stuffs_with_input(input_string):
		if input_string == "start":
			input_string = "start recording"
		elif input_string == "end":
			input_string = "end recording"
		elif input_string == "capture":
			input_string = "do capture"
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
		else :
			input_string = input_string + " no order"
		return input_string
try:
	while True:
		conn, addr = s.accept()
		print("Connected by ", addr)
		data = conn.recv(1024)
		data = data.decode("utf8").strip()
		if not data: break
		print("Received: " + data)
		res = do_some_stuffs_with_input(data)
		print("pi movement :" + res)
		conn.sendall(res.encode("utf-8"))
	
		conn.close()
	s.close()
except KeyboardInterrupt:
    p.stop()
GPIO.cleanup()

