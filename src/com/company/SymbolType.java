package com.company;

import java.util.Arrays;
import java.util.HashSet;

public class SymbolType {
    public static final HashSet<String> keywords = new HashSet<>(Arrays.asList("abstract", "assert",
            "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue",
            "default", "do", "double", "else", "enum", "extends", "final", "finally", "float", "for",
            "if", "goto", "implements", "import", "instanceof", "int", "interface", "long",
            "native", "new", "package", "private", "protected", "public", "return",
            "short", "static", "strictfp", "super", "switch", "synchronized",
            "this", "throw", "throws", "transient", "try", "void", "volatile", "while"));

    public static final HashSet<Character> escapeSequences = new HashSet<>(
            Arrays.asList('b', 't', 'n', '\\', '\'', '"', 'r', 'f'));

    public static boolean isKeyword(String word) {
        return keywords.contains(word);
    }

    public static boolean isEscapeSequence(Character character) {
        return escapeSequences.contains(character);
    }
}
