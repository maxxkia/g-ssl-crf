#!/usr/bin/python

import sys, getopt

def main(argv):
    inputFile = ''
    wordFile = ''
    classFile = ''
    labelFile = ''
    outputFile = ''
    seperator = ' ' #default is SPACE
    
    wordDict = dict()
    classDict = dict()
    labelDict = dict()
    
    try:
        opts, args = getopt.getopt(argv,"hi:w:c:l:o:s:",["help", "input=", "worddict=", "classdict=", "labeldict=", "output=", "seperator="])
    except getopt.GetoptError:
        print 'test.py -i <inputfile> -w <worddict> -c <classdict> -l <labeldict> -o <outputfile> -s <TAB|SPACE>' 
        sys.exit(2)
    for opt, arg in opts:
        if opt in ('-h','--help'):
            print 'test.py -i <inputfile> -w <worddict> -c <classdict> -l <labeldict> -o <outputfile> -s <TAB|SPACE>'
            sys.exit(1)
        elif opt in ('-i', '--input'):
            inputFile = arg
        elif opt in ('-w', '--worddict'):
            wordFile = arg
        elif opt in ('-c', '--classdict'):            
            classFile = arg
        elif opt in ('-l', '--labeldict'):
            labelFile = arg
        elif opt in ('-o', '--outputfile'):
            outputFile = arg    
        elif opt in ('-s', '--seperator'):
            if(arg.lower() == "tab"):
                seperator = '\t'
    
    output = ''
    
    with open(wordFile) as inp:        
        for l in inp.readlines():
            lparts = l.strip().split()
            wordDict[lparts[0]] = lparts[1]
    
    with open(classFile) as inp:        
        for l in inp.readlines():
            lparts = l.strip().split()
            classDict[lparts[0]] = lparts[1]
            
    with open(labelFile) as inp:        
        for l in inp.readlines():
            lparts = l.strip().split()
            labelDict[lparts[0]] = lparts[1]
    
    
    with open(inputFile) as inp:       
        for l in inp.readlines():
            if len(l) < 2:
                output += l
            else:
                lparts = l.strip().split(seperator)
                output += wordDict[lparts[0]] + seperator + classDict[lparts[1]] + seperator + labelDict[lparts[2]] + '\n'
        
            
                    
    with open(outputFile, 'w') as out:
        out.write(output)
        
    
    print "Done"
                
    
if __name__ == "__main__":
    main(sys.argv[1:])
    
    
    
    