#!/bin/bash
# Call this script e.g. ./scripts/submission_scripts/aa.sh programs/controlflow.str -Dlogging=true
# (-Dlogging=true is optional)
java -cp ".:./package/art.jar:./package/BitSequencer.jar:./package/aa_parser" $2 $3 $4 $5 $6 $7 $8 $9 ARTTest $1 +phaseAG