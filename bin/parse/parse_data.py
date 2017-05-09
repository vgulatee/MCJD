import os
import re

path = "C:\Users\Nikhil\Documents\Nikhil\UNIVERSITY\Second Year\Term 2\SFWR 2XB3\Final Project\Terminology Data\Data"
path = "C:\Users\Nikhil\Desktop"

path = os.getcwd() + "\Terminology Data\Data"
#print path

fileList = []
subject = ""
ss = []

for folder in (x[0] for x in os.walk(path)):
    if (folder == path):
        continue
    fileList.append(folder.replace(path + "\\", "",1))
    for files in os.listdir(folder):        
        f = open(folder + "\\" + files, "r")
        #f = open(path + folder + ".csv", "r")
        data = f.readlines()
        for line in data:            
            parameters = line.split(",")
            if parameters[0] != subject:
                subject = parameters[0]
                ss.append(subject)
    name = (folder.replace(path + "\\", "Subject: ",1))


for folder in (x[0] for x in os.walk(path)):
    if (folder == path):
        continue
    name = folder.replace(path + "\\", "",1)
    out = open(folder + "\\" + name + ".csv", "a")
    for files in os.listdir(folder):
        f = open(folder + "\\" + files, "r")
        #f = open(path + folder + ".csv", "r")
        data = f.readlines()
        for line in data:
            out.write(line)

out.close()
