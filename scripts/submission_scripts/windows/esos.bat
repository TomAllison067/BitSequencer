@echo off
REM Call this script e.g. scripts\submission_scripts\esos.bat programs\controlflow.str -Dlogging=true
REM (-Dlogging=true is optional)

java -cp ".;./package/art.jar;./package/BitSequencer.jar;./package/esos_parser" ARTTest %2 %3 %4 %5 %6 %7 %8 %9 %1 +phaseAG +showAll

mkdir terms

move term.txt terms\

echo "$(type eSOSRulesONLY.art) !try $(type terms\term.txt), __map" > terms\term.txt

java -cp ".;./package/art.jar;./package/BitSequencer.jar;./package/esos_parser" uk.ac.rhul.cs.csle.art.ART terms\term.txt

move *.dot artSpecification.art artSpecification.tex terms\
