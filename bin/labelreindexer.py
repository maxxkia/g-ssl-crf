#!/usr/bin/python

import sys, getopt

def main(argv):
    inputFile = ''    
    labelFile = ''
    outputFile = ''    
        
    labelDict = dict()
    
    try:
        opts, args = getopt.getopt(argv,"hi:l:o:",["help", "input=", "labeldict=", "output="])
    except getopt.GetoptError:
        print 'labelreindexer.py -i <inputfile> -l <labeldict> -o <outputfile>' 
        sys.exit(2)
    for opt, arg in opts:
        if opt in ('-h','--help'):
            print 'labelreindexer.py -i <inputfile> -l <labeldict> -o <outputfile>'
            sys.exit(1)
        elif opt in ('-i', '--input'):
            inputFile = arg       
        elif opt in ('-l', '--labeldict'):
            labelFile = arg
        elif opt in ('-o', '--outputfile'):
            outputFile = arg    
                    
    output = ''
        
    with open(labelFile) as inp:        
        for l in inp.readlines():
            lparts = l.strip().split()
            labelDict[lparts[0]] = lparts[1]
    
    
    with open(inputFile) as inp:       
        for l in inp.readlines():
            if len(l) < 2:
                output += l
            else:                
                output += labelDict[l.strip()] + '\n'
        
            
                    
    with open(outputFile, 'w') as out:
        out.write(output)
        
    
    print "Done"
                
    
if __name__ == "__main__":
    main(sys.argv[1:])
    
    
    
    
    
    
