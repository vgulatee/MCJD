import os
import re

path = "C:\Users\Nikhil\Documents\Nikhil\UNIVERSITY\Second Year\Term 2\SFWR 2XB3\Final Project\Terminology Data\ACTUAL DATA"

path = os.getcwd() + "\Terminology Data\ACTUAL DATA"
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
    if (folder == path+"\\BAD"):
        continue
    print folder
    for files in os.listdir(folder):
        print files
        if files=='BAD':
            continue
        f = open(folder + "\\" + files, "r")
        w = open(folder + "\\" + files[0:-4]+"ed.txt", "a")
        #f = open(path + folder + ".csv", "r")
        data = f.readlines()
        for line in data:            
            parameters = line.split("||")
##            print len(parameters)
##            if parameters[-1]=="" and parameters[-2]=="":
##               print parameters
            try:
                if len(parameters)<=2 and parameters[1][-1]=="\n":
                    print parameters
                else:
                    w.write(line)
            except IndexError:
                print "Blah"

##print len(ss)
datasize = 0
#for each in size:
    #print each
##    datasize += each[2]
##
##print test
##print datasize
##print maxlen
##print longest
##print loc
#print minlen, shortest, minloc
#print maxSA
#print maxSUB
#print maxFILE
#print maxSublen
#print maxSubstring
