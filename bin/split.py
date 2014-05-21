#!/usr/bin/python

import sys, getopt
from random import shuffle

def main(argv):
	inputfile = ''
	labeled = 0.0
	unlabeled = 0.0
	dev = 0.0
	try:
		opts, args = getopt.getopt(argv,"i:l:u:d:",["input=","labeled-ratio=","unlabeled-ratio=","dev-ratio="])
	except getopt.GetoptError:
		#print 'test.py -i <inputfile> -o <outputfile>'
		sys.exit(2)
	for opt, arg in opts:
		#if opt == '-h':
		#	print 'test.py -i <inputfile> -o <outputfile>'
		#	sys.exit()
		if opt in ("-i", "--input"):
			inputfile = arg
		elif opt in ("-l", "--labeled-ratio"):
			labeled = float(arg)
		elif opt in ("-u", "--unlabeled-ratio"):
			unlabeled = float(arg)
		elif opt in ("-d", "--dev-ratio"):
			dev = float(arg)
	
	with open(inputfile) as inp:
		instances = list()
		inpLines = inp.readlines()
		numLines = len(inpLines)
		i = 0
		while(i < numLines):
			sent = list()
			while(i < numLines and len(inpLines[i]) > 1):
				sent.append(inpLines[i])
				i += 1
			i += 1
			instances.append(sent)
			
	numInstances = len(instances)
	print "All the instances are ", numInstances
	#shuffle(instances)
	
	labeledStartIndex = 0
	labeledEndIndex = int(round(numInstances * labeled))
	unlabeledStartIndex = labeledEndIndex 
	unlabeledEndIndex = unlabeledStartIndex + int(round(numInstances * unlabeled)) 
	devStartIndex = unlabeledEndIndex 
	devEndIndex = numInstances
	
	#print labeledStartIndex, labeledEndIndex, unlabeledStartIndex, unlabeledEndIndex, devStartIndex, devEndIndex	
	labeled_list = instances[labeledStartIndex:labeledEndIndex]
	unlabeled_list = instances[unlabeledStartIndex:unlabeledEndIndex]
	dev_list = instances[devStartIndex:devEndIndex]
	
	#print len(labeled_list)+len(unlabeled_list)+len(dev_list)
	#print labeled_list[len(labeled_list)-1],unlabeled_list[0]
	out = ""
	for l in labeled_list:
		for s in l:
			out += s
		out += "\n"
	with open(inputfile + ".labeled","w") as ow:
		ow.write(out)
	
	out = ""
	for l in unlabeled_list:
		for s in l:
			out += s
		out += "\n"
	with open(inputfile + ".unlabeled","w") as ow:
		ow.write(out)
		
	out = ""
	for l in dev_list:
		for s in l:
			out += s
		out += "\n"
	with open(inputfile + ".dev","w") as ow:
		ow.write(out)
	
	
	
if __name__ == "__main__":
	main(sys.argv[1:])
