#!/usr/bin/perl

# SemScore.pl semtst semref
# - score semtst against semref
# both files have format   sent <=> dact
#

# BUG FIX: Francois 21/05/2008
# 1. allow requesting for multiple attirbutes
# 2. fix error that assign an item match even if value is not there

# MODIFICATION Filip 10/09/2008
# added data output only - it is easier to parse

use strict;

use Getopt::Std;

my %cmdlineopts;
getopts('evd', \%cmdlineopts);
my $verbose=$cmdlineopts{"v"};
my $perrors=$cmdlineopts{"e"};
my $data_only=$cmdlineopts{"d"};
my $nargs = scalar @ARGV;

# keep track of the most frequent causes of error
my %itemPrecisionError;
my %itemRecallError;
my %daPrecisionError;
my %daRecallError;
my %refActs;
my %testActs;
my %refItems;
my %testItems;

if ($nargs<2){
    print "usage: SemScore.pl [-ev] semtst semref\n";
    print "       -e  print all non=matching pairs\n";
    print "       -v  verbose\n";
    print "       -d  data only\n";
    exit;
}

# ------------- pos arg constraints ---------------
#  0 = NoArgsAllowed, 1 = Arg1fixed, 2 = FreeOrder
my %acttypes;
$acttypes{hello}=2;
$acttypes{silence}=0;
$acttypes{thankyou}=0;
$acttypes{ack}=0;
$acttypes{bye}=0;
$acttypes{hangup}=0;
$acttypes{inform}=2;
$acttypes{request}=1;
$acttypes{reqalts}=2;
$acttypes{reqmore}=2;
$acttypes{confirm}=2;
$acttypes{select}=2;
$acttypes{affirm}=2;
$acttypes{negate}=2;
$acttypes{deny}=1;
$acttypes{repeat}=0;
$acttypes{help}=0;
$acttypes{restart}=0;
$acttypes{null}=0;

# Read semtst and semdef files and compare entries
my $semtst = $ARGV[0];
my $semdef = $ARGV[1];
open (ST, $semtst) || die("SemScore.pl: cannot open semtst file $semtst");
open (SD, $semdef) || die("SemScore.pl: cannot open semdef file $semdef");

my ($rline,$tline,$rutt,$tutt,$ract,$tact);
my $Na=0; my $Ha=0; my $Ni=0; my $Ri=0; my $Hi=0;
my $nerrs=0;
my %refhash1;  # ref attr/val pairs
my %refhash2;
my %refneg;   # true if attr was != form

sub eqval {
        my ($a,$b,$na,$nb) = @_;
        # print "$a EQUAL $b? ".($a eq $b)."\n";
        # Added by Francois: case insensitive
        if (defined $a) { $a = lc $a; }
        if (defined $b) { $b = lc $b; }
        $a =~ s/"//g;
        $b =~ s/"//g;
        return ($a eq $b) && ($na == $nb);
}



# remove venue prefixes
# order items alphabetically, unbounded items first
sub normaliseItemList {


    my ($list) = @_;
    if ($list eq "") { return $list; }
    else{
       
        my @s = split(/\s*,\s*/, $list);
        my @unbound;
        my @bound;
        foreach my $item (@s) {
            # remove prefix
            $item =~ s/^venue\.//;
            # separate
            if ($item =~ /=/) { push(@bound, $item); } else { push(@unbound, $item); }
        }
       
        # sort
        my @sortedItems = (sort @unbound, sort @bound);
        return join(",", @sortedItems);
    }    

}

while(<SD>) {
        my $isok=1;
       
        ++$Na;
        $rline = $_; chomp $rline;
        $tline = <ST>; chomp $tline;
        $rline =~ s/^\s*//;
        $tline =~ s/^\s*//;

       
        # extract ref and test lines
        ($rutt,$ract) = split(/\s*<=>\s*/,$rline);
        ($tutt,$tact) = split(/\s*<=>\s*/,$tline);

        if ($verbose){
                print "$Na Ref: $ract\n";
                print "$Na Tst: $tact\n";
        }
        # check that utts are identical
        if ($rutt ne $tutt) {
              if (not $data_only){
                print "ERROR: utts differ at line $Na\n";
                print "  Ref Utt: $rutt\n";
                print "  Tst Utt: $tutt\n";
                ++$nerrs; $isok=0;
                next;
          }
        }
        # compare dialog acts
        $ract =~ m/(.*)\((.*)\)/;
        my $rtype = $1;
        my $ritemlist = &normaliseItemList($2);
        $tact =~ m/(.*)\((.*)\)/;
        my $ttype = $1;
        my $titemlist = &normaliseItemList($2);

       
        if ($ttype eq $rtype){
                ++$Ha;
                print "     acts match;  " if $verbose;
        }else{
            $daRecallError{$rtype}++;
            $daPrecisionError{$ttype}++;
            print "     acts do not match;  " if $verbose;
            $isok=0;
        }
       
        $refActs{$rtype}++;
        $testActs{$ttype}++;

   # extract items
   my $nhits=0;
   my @ritems = split(/\s*,\s*/,$ritemlist);
   my $numritems = $#ritems+1;
   $Ni += $numritems;
        my @titems = split(/\s*,\s*/,$titemlist);
   my $numtitems = $#titems+1;
   $Ri += $numtitems;
   $isok=0 if $numritems != $numtitems;
   # if either has no items then nothing more to do
   if ($numritems==0 && $numtitems==0){
        if ($perrors && $isok==0){
                        printf("%3d: %s\n   Ref: %s\n   Tst: %s\n",$Na,$rutt,$ract,$tact);
                }
        next;
   }
   
   if ($numritems==0 || $numtitems==0){
        if ($verbose){
                print "      tst has inserted items\n" if $numritems==0;
                print "      tst has deleted items\n" if $numtitems==0;
                }elsif ($perrors){
                        printf("%3d: %s\n   Ref: %s\n   Tst: %s\n",$Na,$rutt,$ract,$tact);
                }
                #next;
        }
   
   my ($ratt,$rval,$tatt,$tval);  
   # compare 1st args for position dependent act items
   my $arg=0;
   if ($acttypes{$rtype}==1) {
        ($ratt,$rval) = split(/\s*!?=\s*/,$ritems[0]);
        ($tatt,$tval) = split(/\s*!?=\s*/,$titems[$0]);
        my $tneg = ($titems[0] =~ m/!=/);
        my $rneg = ($ritems[0] =~ m/!=/);
        ++$arg;
                if ($ratt eq $tatt && eqval($rval,$tval,$rneg,$tneg)) {
                        ++$Hi; ++$nhits;
                }else{
                    if (defined $tval ) { $itemPrecisionError{"$tatt=$tval"}++;
                    } else {
                        $itemPrecisionError{"$tatt"}++;
                    }
                    if (defined $rval) { $itemRecallError{"$ratt=$rval"}++; }
                    else {  $itemRecallError{"$ratt"}++; }
                    $isok=0;
                }

        if (defined $tval) { $testItems{"$tatt=$tval"}++; } else { $testItems{"$tatt"}++; }
        if (defined $rval) { $refItems{"$ratt=$rval"}++;  } else { $refItems{"$ratt"}++; }
   }
   
   # remaining items can be in any order so hash the ref items
   %refhash1 = ();
   %refhash2 = ();
   %refneg = ();
   my $i = $arg;
   while ($i<$numritems) {
        ($ratt,$rval) = split(/\s*!?=\s*/,$ritems[$i]);
        # why two hashes? Because it allows for two identical labels (e.g. select())
        if (defined $refhash1{$ratt}){
                $refhash2{$ratt}=$rval;
                }else{
                        $refhash1{$ratt}=$rval;
                }
      $refneg{$ratt} = ($ritems[$i] =~ m/!=/);
      ++$i;
   }
   my $i = $arg;
   while ($i<$numtitems) {
        ($tatt,$tval) = split(/\s*!?=\s*/,$titems[$i]);
        $testItems{"$tatt=$tval"}++;
       
        my $rv1 = $refhash1{$tatt};
        my $rv2 = $refhash2{$tatt};
        # Modified by Francois
        #if (defined $rv1) {
        if (exists $refhash1{$tatt}) {
                my $tneg = ($titems[$i] =~ m/!=/);
              if (eqval($rv1,$tval,$refneg{$tatt},$tneg)){
                ++$Hi; ++$nhits; $refhash1{$tatt} = "----";
                $refItems{"$tatt=$rv1"}++;
                # Modified by Francois
                # } elsif ($eqval($rv2,$tval,$refneg{$tatt},$tneg)){
              } elsif (exists $refhash2{$tatt} && eqval($rv2,$tval,$refneg{$tatt},$tneg)){
                ++$Hi; ++$nhits; $refhash2{$tatt} = "----";
                $refItems{"$tatt=$rv2"}++;
              }else{

                  # $itemPrecisionError{"$tatt=$tval"}++;
                  if ($refhash1{$tatt} ne "----" && !(eqval($rv1,$tval,$refneg{$tatt},$tneg))) {
                      $itemRecallError{"$tatt=$rv1"}++;
                      $refItems{"$tatt=$rv1"}++;
                  }
                  elsif (exists $refhash2{$tatt}&& $refhash2{$tatt} ne "----" &&  !(eqval($rv2,$tval,$refneg{$tatt},$tneg))) {
                      $itemRecallError{"$tatt=$rv2"}++;
                       $refItems{"$tatt=$rv2"}++;
                  }
                  $isok=0;
              }
      }else{
          if (defined $tval) { $itemPrecisionError{"$tatt=$tval"}++; } else {  $itemPrecisionError{"$tatt"}++; }
          if (defined $tval) { $testItems{"$tatt=$tval"}++; } else {  $testItems{"$tatt"}++; }
        $isok = 0;
      }
                ++$i;
        }
        if ($verbose){
           print "      $nhits item hits\n";
        }elsif ($perrors && $isok==0){
                printf("%3d: %s\n   Ref: %s\n   Tst: %s\n",$Na,$rutt,$ract,$tact);
        }
        # if ($Hi ne $Ri) {
        #       print "Hi=$Hi Ri=$Ri\n$rline\n$tline\n"; exit
        # }
}
if ($Na==0){
  if (not $data_only){
        print "ERROR: no acts processed!\n";
        ++$nerrs;
  }
}

if (not $data_only){
# print stats
foreach my $act (sort { $daPrecisionError{$a}/$testActs{$a} <=>  $daPrecisionError{$b}/$testActs{$b} } keys %daPrecisionError) {
    printf  "Precision error per act: %5.2f $act (%i/%i)\n",($daPrecisionError{$act}/$testActs{$act}),$daPrecisionError{$act},$testActs{$act};
}
print "\n";
foreach my $act (sort { $daRecallError{$a}/$refActs{$a} <=>  $daRecallError{$b}/$refActs{$b} } keys %daRecallError) {
    printf  "Recall error per act: %5.2f $act (%i/%i)\n",($daRecallError{$act}/$refActs{$act}),$daRecallError{$act},$refActs{$act};
}
print "\n";
foreach my $item (sort { $itemPrecisionError{$a}/$testItems{$a} <=>  $itemPrecisionError{$b}/$testItems{$b} } keys %itemPrecisionError) {
   printf  "Precision error per item: %5.2f $item (%i/%i)\n",($itemPrecisionError{$item}/$testItems{$item}),$itemPrecisionError{$item},$testItems{$item};
 
}
print "\n";
foreach my $item (sort { $itemRecallError{$a}/$refItems{$a} <=>  $itemRecallError{$b}/$refItems{$b} } keys %itemRecallError) {
    printf  "Recall error per item: %5.2f $item (%i/%i)\n",($itemRecallError{$item}/$refItems{$item}),$itemRecallError{$item},$refItems{$item};
}
print "\n";
}
my $c1; my $c2;
map { $c1 += $refItems{$_}; } keys %refItems;
if (not $data_only){
print "total ref items = $c1\n";
}
map { $c2 += $testItems{$_}; } keys %testItems;
if (not $data_only){
print "total test items = $c2\n";
}
#if ($nerrs>0){
#   print "$nerrs error detected - fix and rerun\n";
#}else{
        my ($acc,$prec,$rec,$f);
       
        if ($Na != 0) {
              $acc = 100*($Ha/$Na);
        }
        else {
          $acc = -100.0
        }
        if ($Ri != 0) {
          $prec = 100*($Hi/$Ri);
        }
        else {
          $prec = -100.0
        }
        if ($Ni != 0) {
          $rec = 100*($Hi/$Ni);
        }
        else {
          $rec = -100.0
        }
         
        $f = 2*$prec*$rec/($rec+$prec);
       
if (not $data_only){
   print "\nSemScore Results: Ref contains $Na acts; $Ni items\n";
   print "---------------------------------------------\n";
   printf "|%10s|%10s|%10s|%10s|\n|%10s|%10s|%10s|%10s|\n",
           "Act Type","Item","Item","Item","Accuracy","Precision","Recall","F-measure";
   print "---------------------------------------------\n";
   printf "|%10.2f|%10.2f|%10.2f|%10.2f|\n",$acc,$prec,$rec,$f;
   print "---------------------------------------------\n";
}
else {
   printf "%10.2f %10.2f %10.2f %10.2f\n",$acc,$prec,$rec,$f;
}
#}
