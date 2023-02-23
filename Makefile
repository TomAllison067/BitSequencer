buildplugin:
	javac  -d ./build -cp ".:BitSequencer" BitSequencer/*.java
	cd build && jar cvf ./BitSequencer.jar ./BitSequencer/* && cd ..
	javac  -d ./build -cp ".:./ART/art.jar:./build/BitSequencer.jar" ValueUserPlugin.java
	cd build && jar uf ./BitSequencer.jar *.class && cd ..

esos: 
	java -cp ".:./ART/art.jar:./build/BitSequencer.jar" uk.ac.rhul.cs.csle.art.ART eSOSRules.art

clean:
	rm -rf term.txt
	rm -rf *.dot
	rm -rf *.dot.pdf
	rm -rf artSpecification.*
	rm -rf *.class
	rm -rf ARTGeneratedLexer.java
	rm -rf ARTGeneratedParser.java
	rm -rf BitSequencer/*.class
	rm -rf ./build/
	rm -rf ./_BitSequencerSubmission/
	rm -rf ./_BitSequencerSubmission.zip

windows_submission: buildplugin
	mkdir -p _BitSequencerSubmission
	cp ART/art.jar _BitSequencerSubmission/
	cp build/BitSequencer.jar _BitSequencerSubmission/
	cp 00README.txt eSOSRules.art informalAndInternal.txt ValueUserPlugin.java _BitSequencerSubmission/
	cp scripts/submission\_scripts/* _BitSequencerSubmission/
	zip -r _BitSequencerSubmission.zip _BitSequencerSubmission/

