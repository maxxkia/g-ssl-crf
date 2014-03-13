#!/usr/bin/python
"""
Injects the labels of the input file into the file with format of CRFSuite.
Note that the script assumes that all the sequences are of the same length in both files.
And it assumes that the CRFSuite file already has its own labels and it replaces them.

"""
import sys, getopt

def main(argv):
    
    labelfile = ''
    crffile = ''
    outfile = ''    

    try:
        opts, args = getopt.getopt(argv,"hl:c:o:",["help", "labels=", "crffile=", "output="])
    except getopt.GetoptError:
        print 'labelInject.py -l <labels> -c <crfsuitefile> -o <outputfile>'
        sys.exit(2)
    for opt, arg in opts:
        if opt in ("-h", "--help"):
            print 'labelInject.py -l <labels> -c <crfsuitefile> -o <outputfile>'
            sys.exit()
        if opt in ("-l", "--labels"):
            labelfile = arg
        elif opt in ("-c", "--crffile"):
            crffile = arg
        elif opt in ("-o", "--output"):
            outfile = arg        
    
    output = ''

    with open(labelfile) as r:
        with open(crffile) as ws:
            labellines = r.readlines()
            crflines = ws.readlines()
            for cl,ll in zip(crflines, labellines):
                cls = cl.strip().split('\t')
                cls[0] = ll.strip()
                output += '\t'.join(cls) + '\n'
            
            
    print "Writing..."
    with open(outfile,"w") as p:
        p.write(output)
    

if __name__ == "__main__":
    main(sys.argv[1:])