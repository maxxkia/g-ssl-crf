graph-ssl-crf
=============

# Graph-Based Semi-Supervised CRF for Language Understanding

This package contains all executables necessary for running the Semi-Supervised CRF. It is the implementation of our paper:

  Graph-based semi-supervised conditional random fields for spoken language understanding using unaligned data
  M. Aliannejadi, M. Kiaeeha et al., ALTW 2014

Please cite our work if you use it.

## System Requirements

The following program(s) need to be installed on the host computer before running the application:
- Java Runtime Environment (JRE)

## How To Run
Before running the program you must grant execute premission for all of 
the files in the bin directory, which is done in this way on Linux OS:
sudo chmod +x *.py
sudo chmod +x crfsuite
sudo chmod +x *.pl

Install Junto as described in:
bin\junto-master\README.md

Run the graphcrf script contained in the root directory with the following arguments:
./graphcrf <labeledData.crf> <unlabeledData.crf> <testData.crf> <devData.crf>

a sample input data is provided in ./Data folder, so the program call would be:
./graphcrf Data/atis.labeled Data/atis.unlabeled Data/atis.test

This will run the program on a pre-shuffled set of data. To shuffle the data and
run the program at the same time you should run the ./shufflerun script using
the following arguments:
./shufflerun <dataset.crf> <test.crf> <a float number representing percent of labeled data> <a float number representing percent of unlabeled data> <a float representing percent of dev data>

e.g.
./shufflerun Data/atis Data/atis.test 0.1 0.9 0.1

note: all input data provided for "graphcrf" should be in standard CRF format
