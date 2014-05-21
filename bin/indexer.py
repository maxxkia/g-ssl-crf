#!/usr/bin/python

import sys, getopt
import operator

def main(argv):
    labeledFile = ''
    unlabeledFile = ''
    devFile = ''
    testFile = ''
    seperator = ' '
    
    try:
        opts, args = getopt.getopt(argv,"hl:u:d:t:s:",["help", "labeled=", "unlabeled=", "dev=", "test=", "seperator="])
    except getopt.GetoptError:
        print 'indexer.py -l <labeledfile> -u <unlabeledfile> -d <devfile> -t <testfile> -seperator <SPACE|TAB>' 
        sys.exit(2)
    for opt, arg in opts:
        if opt in ('-h','--help'):
            print 'indexer.py -l <labeledfile> -u <unlabeledfile> -d <devfile> -t <testfile> -seperator <SPACE|TAB>'
            sys.exit(1)
        elif opt in ('-l', '--labeled'):
            labeledFile = arg
        elif opt in ('-u', '--unlabeled'):
            unlabeledFile = arg
        elif opt in ('-d', '--dev'):
            devFile = arg
        elif opt in ('-t', '--test'):
            testFile = arg
        elif opt in ('-s', '--seperator'):
            if(arg.lower() == "tab"):
                seperator = '\t'
        
    wordID = 1 # 0 is reserved for dummy
    classID = 1 # 0 is reserved for dummy
    labelID = 0
    
    wordDict = dict()
    classDict = dict()
    labelDict = dict()
    labeledLabelDict = dict()
    
    labelOutput = ''
    unlabeledOutput = ''
    devOutput = ''
    testOutput = ''
    
    with open(labeledFile) as inp:        
        inpLines = inp.readlines()        
        for l in inpLines:
            if(len(l) < 2):
                labelOutput += l
            else:
                tokens = l.strip().split(seperator)
                
                if wordDict.has_key(tokens[0]) == False:
                    wordDict[tokens[0]] = wordID
                    labelOutput += str(wordID) + seperator
                    wordID += 1
                else:
                    labelOutput += str(wordDict[tokens[0]]) + seperator
                    
                if classDict.has_key(tokens[1]) == False:
                    classDict[tokens[1]] = classID
                    labelOutput += str(classID) + seperator
                    classID += 1
                else:
                    labelOutput += str(classDict[tokens[1]]) + seperator
                    
                if labelDict.has_key(tokens[2]) == False:
                    labelDict[tokens[2]] = labelID
                    labeledLabelDict[tokens[2]] = labelID
                    labelOutput += str(labelID) + '\n'
                    labelID += 1
                else:
                    labelOutput += str(labelDict[tokens[2]]) + '\n' 
                    
    with open(unlabeledFile) as inp:        
        inpLines = inp.readlines()        
        for l in inpLines:
            if(len(l) < 2):
                unlabeledOutput += l
            else:
                tokens = l.strip().split(seperator)
              
                if wordDict.has_key(tokens[0]) == False:
                    wordDict[tokens[0]] = wordID
                    unlabeledOutput += str(wordID) + seperator
                    wordID += 1
                else:
                    unlabeledOutput += str(wordDict[tokens[0]]) + seperator
                    
                if classDict.has_key(tokens[1]) == False:
                    classDict[tokens[1]] = classID
                    unlabeledOutput += str(classID) + seperator
                    classID += 1
                else:
                    unlabeledOutput += str(classDict[tokens[1]]) + seperator
                if len(tokens) == 3:		       
                    if labelDict.has_key(tokens[2]) == False:
                        labelDict[tokens[2]] = labelID
                        unlabeledOutput += str(labelID) + '\n'
                        labelID += 1
                    else:
                        unlabeledOutput += str(labelDict[tokens[2]]) + '\n' 
		else:
		    unlabeledOutput += '\n'
                    
    with open(devFile) as inp:        
        inpLines = inp.readlines()        
        for l in inpLines:
            if(len(l) < 2):
                devOutput += l
            else:
                tokens = l.strip().split(seperator)
                
                if wordDict.has_key(tokens[0]) == False:
                    wordDict[tokens[0]] = wordID
                    devOutput += str(wordID) + seperator
                    wordID += 1
                else:
                    devOutput += str(wordDict[tokens[0]]) + seperator
                    
                if classDict.has_key(tokens[1]) == False:
                    classDict[tokens[1]] = classID
                    devOutput += str(classID) + seperator
                    classID += 1
                else:
                    devOutput += str(classDict[tokens[1]]) + seperator
                    
                if labelDict.has_key(tokens[2]) == False:
                    labelDict[tokens[2]] = labelID
                    devOutput += str(labelID) + '\n'
                    labelID += 1
                else:
                    devOutput += str(labelDict[tokens[2]]) + '\n'
                    
    with open(testFile) as inp:        
        inpLines = inp.readlines()        
        for l in inpLines:
            if(len(l) < 2):
                testOutput += l
            else:
                tokens = l.strip().split(seperator)
                
                if wordDict.has_key(tokens[0]) == False:
                    wordDict[tokens[0]] = wordID
                    testOutput += str(wordID) + seperator
                    wordID += 1
                else:
                    testOutput += str(wordDict[tokens[0]]) + seperator
                    
                if classDict.has_key(tokens[1]) == False:
                    classDict[tokens[1]] = classID
                    testOutput += str(classID) + seperator
                    classID += 1
                else:
                    testOutput += str(classDict[tokens[1]]) + seperator
                    
                if labelDict.has_key(tokens[2]) == False:
                    labelDict[tokens[2]] = labelID
                    testOutput += str(labelID) + '\n'
                    labelID += 1
                else:
                    testOutput += str(labelDict[tokens[2]]) + '\n' 
                    
                    
                                       
                                      
    with open(labeledFile + '.indexed', 'w') as out:
        out.write(labelOutput)
        
    with open(unlabeledFile + '.indexed', 'w') as out:
        out.write(unlabeledOutput)
        
    with open(devFile + '.indexed', 'w') as out:
        out.write(devOutput)

    with open(testFile + '.indexed', 'w') as out:
        out.write(testOutput)
        
    output = ''
    
    for w,i in sorted(wordDict.items(), key=operator.itemgetter(1)):
        output += str(i) + ' ' + w + '\n'
    
    with open('word.dict', 'w') as out:
        out.write(output)
        
    output = ''
    
    for w,i in sorted(classDict.iteritems(), key=operator.itemgetter(1)):
        output += str(i) + ' ' + w + '\n'
    
    with open('class.dict', 'w') as out:
        out.write(output)
        
    output = ''
    
    for w,i in sorted(labelDict.iteritems(), key=operator.itemgetter(1)):
        output += str(i) + ' ' + w + '\n'
    
    with open('label.dict', 'w') as out:
        out.write(output)

    output = ''
            
    for w,i in sorted(labeledLabelDict.iteritems(), key=operator.itemgetter(1)):
        output += str(i) + ' ' + w + '\n'
    
    with open('labeledLabel.dict', 'w') as out:
        out.write(output)   
    
    print "Done"
                
    
if __name__ == "__main__":
    main(sys.argv[1:])
    
    
    
    
