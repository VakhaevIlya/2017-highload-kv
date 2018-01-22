counter = 0
wrk.method = "PUT"

request = function()
    local path = "/v0/entity?id=" .. counter

    local value = ""
    for i = 1, 4096 do
        value = value .. string.char(math.random(48, 122))
    end

    wrk.body = value
    counter = (counter + 1) % 777
    return wrk.format(nil, path)
end