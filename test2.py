import requests
url='http://222.107.4.162:3824/upload'
files={'file': open('image.jpg','rb')}
values={'id' : 'file'}
r=requests.post(url,files=files,data=values)
a= r.content
print a
