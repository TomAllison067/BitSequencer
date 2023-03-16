Welcome to BeatSequencer!

The backend classes, including ValueUserPlugin.class, are all compiled into BitSequencer.jar.

# Introduction ==
BeatSequencer is a music DSL that allows users to program both monophonic and polyphonic patterns and play them
back using the Java MIDI subsystem.

A pattern is represented by a string, and takes the following form:
"[C4:4, E4:4, G4:4, .:4, {G4/B4/D4}:4, .:4, {C4/E4/G4}:4, .:4]"

A phrase begins and ends with opening/closing square brackets, and inside the brackets is a comma-delimited list of notes/measures.

The first part of a single note, before the colon, is the MIDI note name. The second part, after the colon, represents the subdivision of the note. For example,
"C4:4" represents the pitch C4 (middle C, midi note 60) played for one crotchet (quarter note). A quaver/eighth note would be represented by "C4:8".

Chords are written by enclosing notes delimited by a `/` symbol inside curly braces, and the subdivision of the chord is added with a colon and number after 
the last curly brace. So, "{G4/B4/D4}:4" plays a crotchet-length (quarter note) chord consisting of notes G4, B4 and D4 (a G major chord).

The best program to look at is "jump.str" to see every feature in action.

# HOW TO RUN ==
I've tried to make everything easy to use with a few scripts and by packaging the backend and ART together with separate parsers for the eSOS and attribute grammars.

You *should* just be able to do one of the following:
* `$ ./scripts/unix/aa.sh <program>` (to use the attribute action parser) or `$ ./scripts/unix/esos.sh <program>` (to use the eSOS parser) on Unix
  * e.g. `$ ./scripts/unix/aa.sh programs/jump.str`
* And likewise for Windows `$ ./scripts/windows/aa.bat programs/jump.str` .etc

For the attribute action scripts, you can stick `-Dlogging=true` on the end of the call to enable some simple internal logging that prints the symbol table out
at the end of the parse e.g. to check variable values (where eSOS of course prints the symbol table out at the end anyway), but this isn't very interesting.
