import os
import re

path = "C:\Users\Nikhil\Documents\Nikhil\UNIVERSITY\Second Year\Term 2\SFWR 2XB3\Final Project\Terminology Data\Data"

path = os.getcwd() + "\Terminology Data\Data"
#print path

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

for folder in (x[0] for x in os.walk(path)):
    if (folder == path):
        continue
    for files in os.listdir(folder):
        f = open(folder + "\\" + files, "r")
        #f = open(path + folder + ".csv", "r")
        data = f.readlines()
        for line in data:            
            parameters = line.split(",")
            if parameters[0] == subject:
                subarraysize += 1
            if parameters[0] != subject:
                if maxSublen < len(parameters[0]):
                    maxSublen = len(parameters[0])
                    maxSubstring = parameters[0]
                if subarraysize > maxSA and subarraysize < 48000:
                    print parameters[0]
                    print files
                    maxSA = subarraysize
                    maxSUB = parameters[0]
                    maxFILE = files                    
                subarraysize = 1
                subject = parameters[0]
                ss.append(subject)
            if (len(parameters[1]) > maxlen):
                maxlen = len(parameters[1])
                test = parameters[0]
                longest = parameters[1]
                loc = files
            if (len(parameters[1]) < minlen):
                minlen = len(parameters[1])
                shortest = parameters[1]
                minloc = files
        sum += len(data)
    name = (folder.replace(path + "\\", "Subject: ",1))
    size.append((name, "size:", sum))
    sum = 0

##print len(ss)
datasize = 0
for each in size:
    print each
##    datasize += each[2]
##
##print test
##print datasize
##print maxlen
##print longest
##print loc
#print minlen, shortest, minloc
print maxSA
print maxSUB
print maxFILE
print maxSublen
print maxSubstring
