local module = {}  
gpio.mode(1,gpio.INT,gpio.PULLUP)

local function tem()    
            m:publish("/sensors/temp","Mail Received",0,1)

end

function module.start()  
print("DHT call complete")
gpio.trig(1,"up",tem)
--tmr.alarm( 3, 10000, 1, tem)
end

return module
