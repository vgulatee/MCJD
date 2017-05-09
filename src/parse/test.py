f1 = open("Administration.txtsorted.txt","r")

f = open("Administration.txt","r")

data1 = f.readlines()
data2 = f1.readlines()

f.close()
f1.close()

found = 0

for line1 in data2:
    found = 0
    for line2 in data1:
        if line1 == line2:
            found += 1            
    if found == 0:
        print line1

