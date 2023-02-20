Welcome to BeatSequencer!

The backend compiles on the university linux server with `scripts/buildplugin.sh` (and other unix systems) - I haven't managed to make it work on Windows (the javac syntax is slightly different and I'm useless at .bat files)

== Introduction ==
BeatSequencer is a music DSL that allows users to program both monophonic and polyphonic patterns and play them
back using the Java MIDI subsystem.

A pattern is represented by a string, and takes the following form:
"[C4:4, E4:4, G4:4, .:4, {G4/B4/D4}:4, .:4, {C4/E4/G4}:4, .:4]"

A phrase begins and ends with opening/closing square brackets, and inside the brackets is a comma-delimited list of notes/measures.

The first part of a single note, before the colon, is the MIDI note name. The second part, after the colon, represents the subdivision of the note. For example,
"C4:4" represents the pitch C4 (middle C, midi note 60) played for one crotchet (quarter note). A quaver/eighth note would be represented by "C4:8".

Chords are written by enclosing notes delimited by a `/` symbol inside curly braces, and the subdivision of the chord is added with a colon and number after 
the last curly brace. So, "{G4/B4/D4}:4" plays a crotchet-length (quarter note) chord consisting of notes G4, B4 and D4 (a G major chord).

== How to run the eSOS rules ==
From the root directory of the project, run `scripts/build.sh` to compile the Java backend classes and `scripts/art.sh eSOSRules.art` to interpret the !try
directives.

All the !try directives should work as expected, but they've all been commented out aside from the last three (which are the most interesting):
* One plays an excerpt of Sweet Dreams by Eurythmics with three instruments by playing three patterns concurrently.
* One plays a rhythmic scale, repeating notes starting with breves, semibreves, and dividing into eventually into 64th notes. This is to test that note durations
  have been properly implemented.
* One plays all of the MIDI notes available to BeatSequencer, ranging from 21 (A0) to 127 (G9), repeating notes where sharps and flats overlap.
