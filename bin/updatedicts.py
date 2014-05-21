#!/usr/bin/python
import sys

ref_dict = sys.argv[1]
classes_dict = sys.argv[2]
prepositions_dict = sys.argv[3]

classes_output = ""
preps_output = ""

ref = dict()

with open(ref_dict) as r:
    for l in r:
        ref[l.split()[1].strip()] = l.split()[0].strip()

with open(classes_dict) as c:
    for l in c:
        classes_output += ref[l.split()[1].strip()] + ' ' + l.split()[1].strip() + '\n'

with open(prepositions_dict) as c:
    for l in c:
        preps_output += ref[l.split()[1].strip()] + ' ' + l.split()[1].strip() + '\n'

with open(classes_dict,'w') as c:
    c.write(classes_output)

with open(prepositions_dict,'w') as c:
    c.write(preps_output)
                