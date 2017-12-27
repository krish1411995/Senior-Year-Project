local module = {}  
gpio.mode(1,gpio.INT,gpio.PULLUP)

local function tem()    
            m:publish("/sensors/temp","Mail Received",0,1)

end

function module.start()  
gpio.trig(1,"up",tem)
end

return module
