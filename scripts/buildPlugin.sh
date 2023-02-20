#!/bin/bash
javac  -cp ".:BeatSequencer" BeatSequencer/MiniMusicPlayer.java
javac --module-path /usr/share/openjfx/lib --add-modules javafx.controls -cp ".:./ART/art.jar:MidiPlayer" ValueUserPlugin.java
