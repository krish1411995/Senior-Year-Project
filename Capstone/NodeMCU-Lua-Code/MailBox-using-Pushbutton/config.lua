local module = {}

module.SSID = {}  
--module.SSID["anuj"] = "1234567890"
--module.SSID["Devil"] = "55555555"
--module.SSID["Wifi"] = "rcshah1405"
--module.SSID["co_staff"] = "routeco123"
module.SSID["DLINK"] = "loveubaby"
module.HOST = "iot.eclipse.org"  
module.PORT = 1883  
module.ID = node.chipid()

module.ENDPOINT = "nodemcu/"  
return module 
