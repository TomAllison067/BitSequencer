rm -rf ./BitSequencerSubmission
mkdir BitSequencerSubmission

## Package the backend
cp -aR package BitSequencerSubmission
cp ART/art.jar build/BitSequencer.jar BitSequencerSubmission/package

cp 00README.md eSOSRules.art informalAndInternal.txt ValueUserPlugin.java BitSequencerSubmission/
mkdir -p BitSequencerSubmission/scripts
cp -aR scripts/submission\_scripts/* BitSequencerSubmission/scripts

# Copy backend src
mkdir -p BitSequencerSubmission/backend_source_code/
mkdir -p BitSequencerSubmission/backend_source_code/Bindings
cp -aR ./BitSequencer/*.java BitSequencerSubmission/backend_source_code/
cp -aR ./BitSequencer/Bindings/*.java BitSequencerSubmission/backend_source_code/Bindings

# Copy grammars and esos rules
cp -aR grammars/ BitSequencerSubmission/
cp eSOSRulesONLY.art BitSequencerSubmission/

# Copy programs
cp -aR programs BitSequencerSubmission/

cp ./documentation/instrumentlist.txt BitSequencerSubmission/
cp ./documentation/writeup/BitSequencerWriteup.pdf BitSequencerSubmission/