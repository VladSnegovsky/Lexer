package com.company;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class Lexer {
    private String code;
    private State state = State.INITIAL;
    private StringBuilder buffer = new StringBuilder();
    private List<Token> tokens = new LinkedList<>();
    private int position;

    public Lexer(String file) throws IOException {
        readFile(file);
    }

    private String byteToString(byte[] data) {
        String result = "";
        result = new String(data, StandardCharsets.UTF_8).replace("\r", "");
        result += "\n";
        return result;
    }

    private void readFile(String file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(file));
        byte[] arr = fileInputStream.readAllBytes();
        this.code = byteToString(arr);
        fileInputStream.close();
    }

    private void parser() {
        for (int currPos = 0; currPos < this.code.length(); currPos++) {
            char ch = this.code.charAt(currPos);
            if (state == State.INITIAL){
                stateStart(ch);
            } else if (state == State.SINGLE_SLASH) {
                stateSingleSlash(ch);
            } else if (state == State.SINGLE_PLUS) {
                stateSinglePlus(ch);
            } else if (state == State.SINGLE_MINUS) {
                stateSingleMinus(ch);
            } else if (state == State.STAR_IN_MULTI_LINE_COMMENT) {
                stateStarInMultiLineComment(ch);
            }
        }
    }

    private void addCharacterToBuffer(char ch) {
        this.buffer.append(ch);
    }

    private void addCharacterToBuffer(State state, char ch) {
        this.buffer.append(ch);
        this.state = state;
    }

    private void addToken(TokenType tokenType, String value) {
        this.tokens.add(new Token(tokenType, value));
    }

    private void addToken(TokenType tokenType) {
        addToken(tokenType, buffer.toString());
        this.buffer = new StringBuilder();
    }

    private void stateStart(char ch) {
        if (Character.isWhitespace(ch)) {
            addToken(TokenType.WHITE_SPACE, String.valueOf(ch));
        } else if (ch == '0') {
            addCharacterToBuffer(State.SINGLE_ZERO, ch);
        } else if (ch >= '1' && ch <= '9') {
            addCharacterToBuffer(State.DECIMAL_NUMBER, ch);
        } else if (ch == '+') {
            addCharacterToBuffer(State.SINGLE_PLUS, ch);
        } else if (ch == '-') {
            addCharacterToBuffer(State.SINGLE_MINUS, ch);
        } else if (ch == '=' || ch == '%' || ch == '^' || ch == '!' || ch == '*') {
            addCharacterToBuffer(State.SOME_OPERATOR_BEFORE_EQUAL, ch);
        } else if (ch == '<') {
            addCharacterToBuffer(State.SINGLE_LESS, ch);
        } else if (ch == '>') {
            addCharacterToBuffer(State.SINGLE_GREATER, ch);
        } else if (ch == '/') {
            addCharacterToBuffer(State.SINGLE_SLASH, ch);
        } else if (ch == '&') {
            addCharacterToBuffer(State.SINGLE_AMPERSAND, ch);
        } else if (ch == '|') {
            addCharacterToBuffer(State.SINGLE_VERTICAL_BAR, ch);
        } else if (ch == ':') {
            addCharacterToBuffer(State.SINGLE_COLON, ch);
        } else if (Character.isLetter(ch) || ch == '$' || ch == '_') {
            addCharacterToBuffer(State.IDENTIFIER, ch);
        } else if (ch == '.') {
            addCharacterToBuffer(State.SINGLE_DOT, ch);
        } else if (ch == '\'') {
            addCharacterToBuffer(State.SYMBOLIC_CONSTANT, ch);
        } else if (ch == '\"') {
            addCharacterToBuffer(State.LITERAL_CONSTANT, ch);
        } else if (ch == '(' || ch == ')' || ch == '{' || ch == '}' || ch == '[' || ch == ']' || ch == '@' || ch == ',' || ch == ';') {
            addCharacterToBuffer(State.INITIAL, ch);
            addToken(TokenType.PUNCTUATION);
        } else if (ch == '?' || ch == '~') {
            addCharacterToBuffer(State.INITIAL, ch);
            addToken(TokenType.OPERATOR);
        } else {
            addCharacterToBuffer(State.INITIAL, ch);
            addToken(TokenType.ERROR);
        }
    }

    private void stateSingleSlash(char ch) {
        if (ch == '/') {
            addCharacterToBuffer(State.SINGLE_LINE_COMMENT, ch);
        } else if (ch == '*') {
            addCharacterToBuffer(State.MULTI_LINE_COMMENT, ch);
        } else if (ch == '=') {
            addCharacterToBuffer(State.INITIAL, ch);
            addToken(TokenType.OPERATOR);
        } else {
            addToken(TokenType.OPERATOR);
            this.position--;
            state = State.INITIAL;
        }
    }

    private void stateSinglePlus(char ch) {
        if (ch == '+' || ch == '=') {
            addCharacterToBuffer(State.INITIAL, ch);
            addToken(TokenType.OPERATOR);
        } else {
            addToken(TokenType.OPERATOR);
            this.position--;
            state = State.INITIAL;
        }
    }

    private void stateSingleMinus(char ch) {
        if (ch == '-' || ch == '=' || ch == '>') {
            addCharacterToBuffer(State.INITIAL, ch);
            addToken(TokenType.OPERATOR);
        } else {
            addToken(TokenType.OPERATOR);
            this.position--;
            state = State.INITIAL;
        }
    }

    private void stateStarInMultiLineComment(char ch) {
        if (ch == '*') {
            addCharacterToBuffer(ch);
        } else if (ch == '/') {
            addCharacterToBuffer(State.INITIAL, ch);
            addToken(TokenType.MULTI_LINE_COMMENT);
        } else {
            addCharacterToBuffer(State.MULTI_LINE_COMMENT, ch);
        }
    }
}
