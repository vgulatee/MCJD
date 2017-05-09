import os
import re

size = []
sum = 0
loc = ""
minloc = ""
maxlen = 0
minlen = 200
longest = ""
shortest = ""
subject = ""
ss = []
subarraysize = 0
maxSA = -1
maxSublen = 0

f = open("test.txt", "r")
data = f.readlines()
for line in data:            
    parameters = line.split("||")
    if parameters[0] == subject:
        subarraysize += 1
    if parameters[0] != subject:
        if maxSublen < len(parameters[0]):
            maxSublen = len(parameters[0])
            maxSubstring = parameters[0]
        if subarraysize > maxSA and subarraysize < 48000:
            print parameters[0]            
            maxSA = subarraysize
            maxSUB = parameters[0]                  
        subarraysize = 1
        subject = parameters[0]
        ss.append(subject)    
sum += len(data)
sum = 0

f.close()


datasize = 0
for each in ss:
    print each
