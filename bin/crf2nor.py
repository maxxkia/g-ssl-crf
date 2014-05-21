#!/usr/bin/python

import sys, getopt

def main(argv):
    inputFile = ''
    outputFile = ''
    
    try:
        opts, args = getopt.getopt(argv,"hi:o:",["help", "input=", "output="])
    except getopt.GetoptError:
        print 'crf2nor.py -i <inputfile> -o <outputfile>' 
        sys.exit(2)
        
    for opt, arg in opts:
        if opt in ('-h','--help'):
            print 'crf2nor.py -i <inputfile> -o <outputfile>'
            sys.exit(1)
        elif opt in ('-i', '--input'):
            inputFile = arg
        elif opt in ('-o', '--output'):
            outputFile = arg
    
    output = ""    
    with open(inputFile) as r:
        lines = r.readlines()
        i = 0
        while i < len(lines):
            j = 0
            output += '0 '
            while len(lines[i]) > 1 and i < len(lines):        
                words = lines[i].split()                            
                output +=  words[1] + ' '                           
                i += 1
                j += 1
            output += ' 0\n'
            i += 1
    #print "Writing..."
    with open(outputFile,'w') as p:
        p.write(output)
    
    
    
if __name__ == "__main__":
    main(sys.argv[1:])