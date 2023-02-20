#!/bin/bash
javac  -cp ".:MidiPlayer" MidiPlayer/MiniMusicPlayer.java
javac --module-path /usr/share/openjfx/lib --add-modules javafx.controls -cp ".:art.jar:MidiPlayer" ValueUserPlugin.java
