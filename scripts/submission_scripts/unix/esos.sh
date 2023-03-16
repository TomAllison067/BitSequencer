#!/bin/bash
# Call this script e.g. ./scripts/submission_scripts/aa.sh programs/controlflow.str -Dlogging=true
# (-Dlogging=true is optional)
java -cp ".:./package/art.jar:./package/BitSequencer.jar:./package/esos_parser" ARTTest $2 $3 $4 $5 $6 $7 $8 $9 $1 +phaseAG +showAll
mkdir -p terms/
mv term.txt terms/
echo "$(cat eSOSRulesONLY.art) !try $(cat terms/term.txt), __map" > terms/term.txt
java -cp ".:./package/art.jar:./package/BitSequencer.jar:./package/esos_parser" uk.ac.rhul.cs.csle.art.ART terms/term.txt
mv *.dot artSpecification.art artSpecification.tex terms/