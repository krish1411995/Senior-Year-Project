local module = {}
m = nil
-- Sends a simple ping to the broker
local function send_ping()
    --print("Pinging")
    ip = wifi.sta.getip()
    m:publish(config.ENDPOINT .. "ping",wifi.sta.getmac() .. ";" .. ip .. ";1",0,0)
end

local function mqtt_start()
    m = mqtt.Client(config.ID, 0,"username","group14")
    print("Client Started")
    print("After sleep")
    -- register message callback beforehand
    m:connect(config.HOST, config.PORT, 0, 0, function(con)
        print("Connected. Before pinging")
        tmr.alarm(6, 50000, 1, send_ping)
    end)
      mailbox.start()   
end
function module.start()
  mqtt_start()
end

return module
