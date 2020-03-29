g_MAX_FRACT = 10;

function sin(value)
  return math.sin(math.rad(value))
end

function cos(value)
  return math.cos(math.rad(value))
end

function tg(value)
  return math.tan(math.rad(value))
end

function ctg(value)
  return 1/tg(value)
end

function arcsin(value)
  return math.deg(math.asin(value))
end

function arccos(value)
  return math.deg(math.acos(value))
end

function arctg(value)
  return math.deg(math.atan(value))
end

function arcctg(value)
  return 1 / arctg(value)
end

function log(basis, value)
  return lg(value) / lg(basis)
end

function logn(value)
  return log(2,value)
end

function pow(number, powValue)
  return math.pow(number, powValue)
end

function toBaseLua(value, base)
 
  local d = base * value
  local i1 = 0
  local ret = ""
  
  while (i1 < g_MAX_FRACT and d > 0.00001) do
    
    i1 = i1 + 1
    
    ret = ret..math.floor(d);
    
    print(d, math.floor(d), (d - (math.floor(d))))
    
    d = (d - (math.floor(d))) * base
  
   end
  
  return ret
end

function process()
  return (text)
end
