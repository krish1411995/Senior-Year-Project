wifi.setmode(wifi.STATION)
wifi.sta.config("DLINK","loveubaby")
gpio.mode(0, gpio.OUTPUT)
gpio.mode(1, gpio.OUTPUT)
gpio.mode(2, gpio.OUTPUT)
gpio.mode(3, gpio.OUTPUT)
gpio.write(0, gpio.HIGH)
gpio.write(1, gpio.HIGH)
gpio.write(2, gpio.HIGH)
gpio.write(3, gpio.HIGH)

local ans
local per
local val
local hey
local add
local per1

function tem()  
if val=="hello" then 
    hey = adc.readvdd33(0)
    print(hey)
    ans = hey-2756
    per = 100-((ans*100)/1024)
    print(per)
    per1=per
    per = per + add
    if per<10 then
        data0()
    elseif per>=10 and per<20 then
        data10()
    elseif per>=20 and per<30 then
        data20()
    elseif per>=30 and per<40 then
        data30()
    elseif per>=40 and per<50 then
        data40()
    elseif per>=50 and per<60 then
        data50()
    elseif per>=60 and per<70 then
        data60()
    elseif per>=70 and per<80 then
        data70()
    elseif per>=80 and per<90 then
        data80()
    elseif per>=90 and per<100 then
        data90()
    else
        data100()
    end     
    m:publish("/nodemcu/automatic1",per1,0,1)
    end
end
function data0()
    gpio.write(0, gpio.HIGH)
    gpio.write(1, gpio.HIGH)
    gpio.write(2, gpio.HIGH)
    gpio.write(3, gpio.HIGH)
    print("print hey")
end
function data10()
    gpio.write(0, gpio.HIGH)
    gpio.write(1, gpio.LOW)
    gpio.write(2, gpio.HIGH)
    gpio.write(3, gpio.HIGH)
    print("print hey1")
end
function data20()
    gpio.write(0, gpio.HIGH)
    gpio.write(1, gpio.HIGH)
    gpio.write(2, gpio.LOW)
    gpio.write(3, gpio.HIGH)
    print("print hey2")
end
function data30()
    gpio.write(0, gpio.HIGH)
    gpio.write(1, gpio.LOW)
    gpio.write(2, gpio.LOW)
    gpio.write(3, gpio.HIGH)
    print("print hey3")
end
function data40()
    gpio.write(0, gpio.LOW)
    gpio.write(1, gpio.LOW)
    gpio.write(2, gpio.LOW)
    gpio.write(3, gpio.HIGH)
    print("print hey4")
end
function data50()
    gpio.write(0, gpio.HIGH)
    gpio.write(1, gpio.HIGH)
    gpio.write(2, gpio.HIGH)
    gpio.write(3, gpio.LOW)
    print("print hey5")
end
function data60()
    gpio.write(0, gpio.LOW)
    gpio.write(1, gpio.LOW)
    gpio.write(2, gpio.HIGH)
    gpio.write(3, gpio.LOW)
    print("print hey")
end
function data70()
    gpio.write(0, gpio.HIGH)
    gpio.write(1, gpio.HIGH)
    gpio.write(2, gpio.LOW)
    gpio.write(3, gpio.LOW)
    print("print hey")
end
function data80()
    gpio.write(0, gpio.LOW)
    gpio.write(1, gpio.HIGH)
    gpio.write(2, gpio.LOW)
    gpio.write(3, gpio.LOW)
    print("print hey")
end
function data90()
    gpio.write(0, gpio.HIGH)
    gpio.write(1, gpio.LOW)
    gpio.write(2, gpio.LOW)
    gpio.write(3, gpio.LOW)
    print("print hey")
end
function data100()
    gpio.write(0, gpio.LOW)
    gpio.write(1, gpio.LOW)
    gpio.write(2, gpio.LOW)
    gpio.write(3, gpio.LOW)
    print("print hey")
end
function ConnStatus(x)
    print("Waiting for IP ...")
    status = wifi.sta.status()
    if (status == 5) then
        print('Connected as '..wifi.sta.getip())
        m = mqtt.Client("nodemcu", 0, "username", "group14")
        print("started")
        m:on("offline", function(client) print ("offline") ConnStatus(60) end)
       m:connect("iot.eclipse.org", 1883, 0, 
        function(client)
        client:subscribe("/nodemcu/intensity",0, function(client) print("subscribe success") end)
        print("connected")
        end)
       -- On publish message receive event
        m:on("message", function(client, topic, data) 
            if topic == "/nodemcu/intensity" then
                if data == "0%" then
                    val="nothello"
                   data0()
			elseif data == "10%" then
                    val="nothello"
                    data10()
			elseif data == "20%" then
                    val="nothello"
                    data20()
			elseif data == "30%" then
                    val="nothello"
                    data30()
            elseif data == "40%" then
                    val="nothello"
                    data40()
                elseif data == "50%" then
                    val="nothello"
                    data50()
                elseif data == "60%" then
                    val="nothello"
                    data60()
                elseif data == "70%" then
                    val="nothello"
                    data70()
                elseif data == "80%" then
                    val="nothello"
                    data80()
                elseif data == "90%" then
                    val="nothello"
                    data90()
                elseif data == "100%" then
                    val="nothello"
                    data100()
                else
                    add=data
                    val="hello"
                    tmr.alarm( 3, 10000, 1, tem)
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
ConnStatus(60)
