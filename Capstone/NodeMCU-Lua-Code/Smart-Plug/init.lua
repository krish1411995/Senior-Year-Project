wifi.setmode(wifi.STATION)
wifi.sta.config("KRISH","krishmehta")
function ConnStatus(x)
    print("Waiting for IP ...")
    status = wifi.sta.status()
    if (status == 5) then
        print('Connected as '..wifi.sta.getip())
        gpio.mode(0, gpio.OUTPUT)
        gpio.write(0, gpio.LOW)
        m = mqtt.Client("nodemcu", 0, "username", "group14")
        print("started")
        m:on("offline", function(client) print ("offline") ConnStatus(60) end)

       

-- Connect to MQTT broker
       m:connect("iot.eclipse.org", 1883, 0, 
        function(client)
        client:subscribe("/nodemcu/led",0, function(client) print("subscribe success") end)
        print("connected")
        end)
       -- On publish message receive event
        m:on("message", function(client, topic, data) 
            if topic == "/nodemcu/led" then
                if data == "1" then
                   -- print("0")
                    gpio.write(0, gpio.HIGH)
                else
                   -- print("1")
                    gpio.write(0, gpio.LOW)
                end
            end
            if data ~= nil then
            print(topic .. ":" .. data)
            else
                print(topic .. " without data")
            end
       end)
    else
        if x > 0 then
            -- Keep trying
            tmr.alarm(0, 1000, 0, function() ConnStatus(x - 1) end)
        else
            print("Connection failed")
        end
    end
end

-- Wait wifi to connect for 60 seconds
ConnStatus(60)