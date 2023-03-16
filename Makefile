# When running `make parse_aa program=<program>`, you can add `logging=true` and/or `showAll=true` to 
# view built-in logging in the semantic actions and toggle the `+showAll` option.
SHOWALL = 
ifeq ($(showAll), true)
	SHOWALL := +showAll
endif

# Compile the BitSequencer java backend
buildplugin:
	javac  -d ./build -cp ".:BitSequencer" BitSequencer/*.java BitSequencer/**/*.java
	cd build && jar cvf ./BitSequencer.jar ./BitSequencer && cd ..
	javac  -d ./build -cp ".:./ART/art.jar:./build/BitSequencer.jar" ValueUserPlugin.java
	cd build && jar uf ./BitSequencer.jar *.class && cd ..

# Interpret the !try directives in eSOSRules.art
esos_rules: 
	java -cp ".:./ART/art.jar:./build/BitSequencer.jar" uk.ac.rhul.cs.csle.art.ART eSOSRules.art
	mv artSpecification.* build/

# Create the assignment submission zip
windows_submission: buildplugin
	mkdir -p _BitSequencerSubmission
	cp ART/art.jar _BitSequencerSubmission/
	cp build/BitSequencer.jar _BitSequencerSubmission/
	cp 00README.txt eSOSRules.art informalAndInternal.txt ValueUserPlugin.java _BitSequencerSubmission/
	cp scripts/submission\_scripts/* _BitSequencerSubmission/
	zip -r _BitSequencerSubmission.zip _BitSequencerSubmission/

# == Interpet programs using the eSOS grammar with these == #
# run `make parse_esos program=<program in programs/>` or `make esos program=<program in programs/>`

# Parse a program in programs/ using the eSOS grammar to create term.txt in the build dir
# Note, esos NEEDS +showAll so it is not an option here
parse_esos: buildplugin 
	java -cp ".:./ART/art.jar" uk.ac.rhul.cs.csle.art.v3.ARTV3 grammars/BitSequencerEsos.art
	javac -Xlint -cp ".:./ART/art.jar" ARTGeneratedParser.java ARTGeneratedLexer.java; \
	java -cp ".:./ART/art.jar" ARTTest $2 $3 $4 $5 $6 $7 $8 $9 programs/$(program).str +phaseAG +showAll
	mv ARTGenerated* *.dot term.txt build/
	echo "$$(cat eSOSRulesONLY.art) !try $$(cat build/term.txt), __map" > build/term.art

# Parse a program, create term.text and then interpret it using the eSOS rules
esos: parse_esos
	java -cp ".:./ART/art.jar:./build/BitSequencer.jar" uk.ac.rhul.cs.csle.art.ART build/term.art
# ========================================================= #

# == Interpret programs using the attribute action grammar == #
parse_aa: buildplugin
	java -cp ".:./ART/art.jar" uk.ac.rhul.cs.csle.art.v3.ARTV3 grammars/BitSequencerAttributeAction.art
	javac -Xlint -cp ".:./ART/art.jar" ARTGeneratedParser.java ARTGeneratedLexer.java;
	java -Dlogging=$(logging) -cp ".:./ART/art.jar:./build/BitSequencer.jar" ARTTest $2 $3 $4 $5 $6 $7 $8 $9 programs/$(program).str +phaseAG $(SHOWALL)
	mv ARTGenerated* build/
# =========================================================== #

generate_esos_parser: buildplugin
	mkdir -p package/esos_parser;
	java -cp ".:./ART/art.jar" uk.ac.rhul.cs.csle.art.v3.ARTV3 grammars/BitSequencerEsos.art;
	javac -Xlint -cp ".:./ART/art.jar" ARTGeneratedParser.java ARTGeneratedLexer.java;
	mv ARTGenerated* package/esos_parser;

generate_aa_parser: buildplugin
	mkdir -p package/aa_parser;
	java -cp ".:./ART/art.jar" uk.ac.rhul.cs.csle.art.v3.ARTV3 grammars/BitSequencerAttributeAction.art
	javac -Xlint -cp ".:./ART/art.jar" ARTGeneratedParser.java ARTGeneratedLexer.java;
	mv ARTGenerated* package/aa_parser;

package: generate_esos_parser generate_aa_parser
	./scripts/windowssubmission.sh;


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