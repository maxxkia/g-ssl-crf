#!/usr/bin/python

import sys, getopt

def main(argv):
    
    inputfile = ''
    testfile = ''
    outpred = ''
    outref = ''

    try:
        opts, args = getopt.getopt(argv,"hi:t:p:r:",["help", "prediction=", "testfile=", "tblprediction=", "tblreference="])
    except getopt.GetoptError:
        print 'CRFSuiteAligned2TBLFormat.py -i <crfsuiteprediction> -t <testfile> -p <tblprediction> -r <tblreference>'
        sys.exit(2)
    for opt, arg in opts:
        if opt in ("-h", "--help"):
            print 'CRFSuiteAligned2TBLFormat.py -i <crfsuiteprediction> -t <testfile> -p <tblprediction> -r <tblreference>'
            sys.exit()
        if opt in ("-i", "--prediction"):
            inputfile = arg
        elif opt in ("-t", "--testfile"):
            testfile = arg
        elif opt in ("-p", "--tblprediction"):
            outpred = arg
        elif opt in ("-r", "--tblreference"):
            outref = arg

    output_real = ""
    output_pred = ""

    with open(inputfile) as r:
        with open(testfile) as ws:
            lines = r.readlines()
            wslines = ws.readlines()
            i = 0;
            while i < len(lines):
                output_real += "aa <=> flight("
                output_pred += "aa <=> flight("
                first = True
                pFirst = True
                while len(lines[i]) > 1 and i < len(lines):        
                    words = lines[i].strip()
                    testtoks = wslines[i].split()
                    if testtoks[2] != "null":
                        if first == False:
                            output_real += ", "
                        output_real += testtoks[2] + '="' + testtoks[0].strip() + '"'
                        first = False
                    if words.find('*') != -1:
                        if pFirst == False:
                            output_pred += ", "
                        output_pred += words[1:].lower() + '="' + testtoks[0].strip() + '"'
                        pFirst = False
                    i += 1
                output_real += ")\n"
                output_pred += ")\n"
                i += 1
    print "Writing..."
    with open(outpred,"w") as p:
        p.write(output_pred)
    with open(outref,"w") as r:
        r.write(output_real)


if __name__ == "__main__":
    main(sys.argv[1:])