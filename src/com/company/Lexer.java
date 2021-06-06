package com.company;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class Lexer {
    private String code;
    private State state = State.INITIAL;
    private StringBuilder buffer = new StringBuilder();
    private final List<Token> tokens = new LinkedList<>();
    private int position;

    public Lexer(String file) throws IOException {
        readFile(file);
        parser();
        Output.showTokens(this.tokens);
    }

    private String byteToString(byte[] data) {
        String result = "";
        result = new String(data, StandardCharsets.UTF_8).replace("\r", "");
        result += "\n";
        return result;
    }

    private void readFile(String file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] arr = fileInputStream.readAllBytes();
        this.code = byteToString(arr);
        fileInputStream.close();
    }

    private void parser() {
        for (this.position = 0; this.position < this.code.length(); this.position++) {
            char ch = this.code.charAt(this.position);
            if (state == State.INITIAL){
                stateStart(ch);
            } else if (state == State.SINGLE_SLASH) {//
                stateSingleSlash(ch);
            } else if (state == State.SINGLE_PLUS) {
                stateSinglePlus(ch);
            } else if (state == State.SINGLE_MINUS) {
                stateSingleMinus(ch);
            } else if (state == State.MULTI_LINE_COMMENT_AND_STAR) {
                stateStarInMultiLineComment(ch);
            } else if (state == State.SINGLE_LINE_COMMENT) {
                stateSingleLineComment(ch);
            } else if (state == State.MULTI_LINE_COMMENT) {
                stateMultiLineComment(ch);
            } else if (state == State.SOME_OPERATOR_BEFORE_EQUAL) {
                stateSomeOperatorBeforeEqual(ch);
            } else if (state == State.SINGLE_LESS) {
                stateSingleLess(ch);
            } else if (state == State.SINGLE_GREATER) {
                stateSingleGreater(ch);
            } else if (state == State.DOUBLE_GREATER) {
                stateDoubleGreater(ch);
            } else if (state == State.SINGLE_COLON) {
                stateSingleColon(ch);
            } else if (state == State.SINGLE_AMPERSAND) {
                stateSingleAmpersand(ch);
            } else if (state == State.SINGLE_VERTICAL_BAR) {
                stateSingleVerticalBar(ch);
            } else if (state == State.SINGLE_DOT) {
                stateSingleDot(ch);
            } else if (state == State.DOUBLE_DOT) {
                stateDoubleDot(ch);
            } else if (state == State.IDENTIFIER) {
                stateIdentifier(ch);
            } else if (state == State.DECIMAL_NUMBER) {
                stateDecimalNumber(ch);
            } else if (state == State.OCTAL_NUMBER) {
                stateOctalNumber(ch);
            } else if (state == State.HEX_NUMBER) {
                stateHexNumber(ch);
            } else if (state == State.FLOATING_POINT_NUMBER) {
                stateFloatingPointNumber(ch);
            } else if (state == State.SINGLE_ZERO) {
                stateSingleZero(ch);
            } else if (state == State.BINARY_NUMBER) {
                stateBinaryNumber(ch);
            } else if (state == State.SYMBOLIC_CONSTANT) {
                stateSymbolicConstant(ch);
            } else if (state == State.BACKSLASH_IN_SYMBOLIC_CONSTANT) {
                stateBackslashInSymbolicConstant(ch);
            } else if (state == State.ONE_OCTAL_DIGIT_AFTER_BACKSLASH_IN_SYMBOLIC_CONSTANT) {
                stateOneOctalDigitAfterBackslashInSymbolicConstant(ch);
            } else if (state == State.TWO_OCTAL_DIGIT_AFTER_BACKSLASH_IN_SYMBOLIC_CONSTANT) {
                stateTwoOctalDigitAfterBackslashInSymbolicConstant(ch);
            } else if (state == State.END_OF_SYMBOLIC_CONSTANT) {
                stateEndOfSymbolicConstant(ch);
            } else if (state == State.ERROR_READ_SYMBOLIC_CONSTANT) {
                stateErrorReadSymbolicConstant(ch);
            } else if (state == State.BACKSLASH_INSIDE_ERROR_READ_SYMBOLIC_CONSTANT) {
                stateBackslashInsideErrorReadSymbolicConstant(ch);
            } else if (state == State.LITERAL_CONSTANT) {
                stateLiteralConstant(ch);
            } else if (state == State.BACKSLASH_IN_LITERAL_CONSTANT) {
                stateBackslashInLiteralConstant(ch);
            } else if (state == State.ERROR_READ_LITERAL_CONSTANT) {
                stateErrorReadLiteralConstant(ch);
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
            System.out.println(String.valueOf(ch) + " in INITIAL");
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
        // //
        if (ch == '/') {
            addCharacterToBuffer(State.SINGLE_LINE_COMMENT, ch);
        } else if (ch == '*') {
            // /*
            addCharacterToBuffer(State.MULTI_LINE_COMMENT, ch);
        } else if (ch == '=') {
            // /=
            addCharacterToBuffer(State.INITIAL, ch);
            addToken(TokenType.OPERATOR);
        } else {
            // /
            addToken(TokenType.OPERATOR);
            this.position--;
            state = State.INITIAL;
        }
    }

    private void stateSinglePlus(char ch) {
        // ++ or +=
        if (ch == '+' || ch == '=') {
            addCharacterToBuffer(State.INITIAL, ch);
            addToken(TokenType.OPERATOR);
        } else {
            // +
            addToken(TokenType.OPERATOR);
            this.position--;
            state = State.INITIAL;
        }
    }

    private void stateSingleMinus(char ch) {
        // -- or -= or ->
        if (ch == '-' || ch == '=' || ch == '>') {
            addCharacterToBuffer(State.INITIAL, ch);
            addToken(TokenType.OPERATOR);
        } else {
            // -
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

    private void stateSingleLineComment(char ch) {
        if (ch == '\n') {
            addToken(TokenType.SINGLE_LINE_COMMENT);
            addToken(TokenType.WHITE_SPACE, String.valueOf(ch));
            state = State.INITIAL;
        } else {
            addCharacterToBuffer(ch);
        }
    }

    private void stateMultiLineComment(char ch) {
        if (ch == '*') {
            addCharacterToBuffer(State.MULTI_LINE_COMMENT_AND_STAR, ch);
        } else {
            addCharacterToBuffer(ch);
        }
    }

    private void stateSomeOperatorBeforeEqual(char ch) {
        // ==
        if (ch == '=') {
            addCharacterToBuffer(State.INITIAL, ch);
            addToken(TokenType.OPERATOR);
        } else {
            // =
            addToken(TokenType.OPERATOR);
            this.position--;
            state = State.INITIAL;
        }
    }

    private void stateSingleLess(char ch) {
        if (ch == '<') {
            // <<
            addCharacterToBuffer(State.SOME_OPERATOR_BEFORE_EQUAL, ch); //double
        } else if (ch == '=') {
            // <=
            addCharacterToBuffer(State.INITIAL, ch);
            addToken(TokenType.OPERATOR);
        } else {
            // <
            addToken(TokenType.OPERATOR);
            this.position--;
            state = State.INITIAL;
        }
    }

    private void stateSingleGreater(char ch) {
        // >>
        if (ch == '>') {
            addCharacterToBuffer(State.DOUBLE_GREATER, ch);
        } else if (ch == '=') {
            // >=
            addCharacterToBuffer(State.INITIAL, ch);
            addToken(TokenType.OPERATOR);
        } else {
            // >
            addToken(TokenType.OPERATOR);
            this.position--;
            state = State.INITIAL;
        }
    }

    private void stateDoubleGreater(char ch) {
        // >>>
        if (ch == '>') {
            addCharacterToBuffer(State.SOME_OPERATOR_BEFORE_EQUAL, ch);
        } else if (ch == '=') {
            // >>=
            addCharacterToBuffer(State.INITIAL, ch);
            addToken(TokenType.OPERATOR);
        } else {
            // >>
            addToken(TokenType.OPERATOR);
            this.position--;
            state = State.INITIAL;
        }
    }

    private void stateSingleColon(char ch) {
        // ::
        if (ch == ':') {
            addCharacterToBuffer(State.INITIAL, ch);
            addToken(TokenType.PUNCTUATION);
        } else {
            // :
            addToken(TokenType.OPERATOR);
            this.position--;
            state = State.INITIAL;
        }
    }

    private void stateSingleAmpersand(char ch) {
        // && or &=
        if (ch == '&' || ch == '=') {
            addCharacterToBuffer(State.INITIAL, ch);
            addToken(TokenType.OPERATOR);
        } else {
            // &
            addToken(TokenType.OPERATOR);
            this.position--;
            state = State.INITIAL;
        }
    }

    private void stateSingleVerticalBar(char ch) {
        // || or |=
        if (ch == '|' || ch == '=') {
            addCharacterToBuffer(State.INITIAL, ch);
            addToken(TokenType.OPERATOR);
        } else {
            // |
            addToken(TokenType.OPERATOR);
            this.position--;
            state = State.INITIAL;
        }
    }

    private void stateSingleDot(char ch) {
        // ..
        if (ch == '.') {
            addCharacterToBuffer(State.DOUBLE_DOT, ch);
        } else if (Character.isDigit(ch)) {
            addCharacterToBuffer(State.FLOATING_POINT_NUMBER, ch);
        } else {
            // .
            addToken(TokenType.PUNCTUATION);
            this.position--;
            state = State.INITIAL;
        }
    }

    private void stateDoubleDot(char ch) {
        // ...
        if (ch == '.') {
            addCharacterToBuffer(State.INITIAL, ch);
            addToken(TokenType.PUNCTUATION);
        } else {
            // ..
            addToken(TokenType.ERROR);
            this.position--;
            state = State.INITIAL;
        }
    }

    private void stateIdentifier(char ch) {
        if (Character.isDigit(ch) || Character.isLetter(ch) || ch == '_' || ch == '$') {
            addCharacterToBuffer(ch);
        } else if (SymbolType.isKeyword(buffer.toString())) {
            addToken(TokenType.KEYWORD);
            this.position--;
            state = State.INITIAL;
        } else if (buffer.toString().equals("true") || buffer.toString().equals("false")) {
            addToken(TokenType.BOOLEAN_LITERAL);
            this.position--;
            state = State.INITIAL;
        } else if (buffer.toString().equals("null")) {
            addToken(TokenType.NULL_LITERAL);
            this.position--;
            state = State.INITIAL;
        } else {
            addToken(TokenType.IDENTIFIER);
            this.position--;
            state = State.INITIAL;
        }
    }

    private void stateDecimalNumber(char ch) {
        if (Character.isDigit(ch) || ch == '_' || ch == 'e' || ch == 'E') {
            addCharacterToBuffer(ch);
        } else if (ch == '.') {
            addCharacterToBuffer(State.FLOATING_POINT_NUMBER, ch);
        } else if (ch == 'l' || ch == 'L' || ch == 'd' || ch == 'D' || ch == 'f' || ch == 'F') {
            addCharacterToBuffer(State.INITIAL, ch);
            addToken(TokenType.NUMERIC);
        } else {
            addToken(TokenType.NUMERIC);
            this.position--;
            state = State.INITIAL;
        }
    }

    private void stateOctalNumber(char ch) {
        if (ch >= '0' && ch <= '7' || ch == '_') {
            addCharacterToBuffer(ch);
        } else if (ch == 'l' || ch == 'L') {
            addCharacterToBuffer(State.INITIAL, ch);
            addToken(TokenType.NUMERIC);
        } else {
            addToken(TokenType.NUMERIC);
            this.position--;
            state = State.INITIAL;
        }
    }

    private void stateHexNumber(char ch) {
        if (Character.isDigit(ch) || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F') || ch == '_') {
            addCharacterToBuffer(ch);
        } else {
            addToken(TokenType.NUMERIC);
            this.position--;
            state = State.INITIAL;
        }
    }

    private void stateFloatingPointNumber(char ch) {
        if (Character.isDigit(ch) || ch == '_' || ch == 'e' || ch == 'E') {
            addCharacterToBuffer(ch);
        } else if (ch == 'd' || ch == 'D' || ch == 'f' || ch == 'F') {
            addCharacterToBuffer(State.INITIAL, ch);
            addToken(TokenType.NUMERIC);
        } else {
            addToken(TokenType.NUMERIC);
            this.position--;
            state = State.INITIAL;
        }
    }

    private void stateSingleZero(char ch) {
        if (ch == '.') {
            addCharacterToBuffer(State.FLOATING_POINT_NUMBER, ch);
        } else if (ch == 'x' || ch == 'X') {
            addCharacterToBuffer(State.HEX_NUMBER, ch);
        } else if (ch >= '0' && ch <= '7' || ch == '_') {
            addCharacterToBuffer(State.OCTAL_NUMBER, ch);
        } else if (ch == 'b' || ch == 'B') {
            addCharacterToBuffer(State.BINARY_NUMBER, ch);
        } else {
            addToken(TokenType.NUMERIC);
            this.position--;
            state = State.INITIAL;
        }
    }

    private void stateBinaryNumber(char ch) {
        if (ch == '0' || ch == '1' || ch == '_') {
            addCharacterToBuffer(ch);
        } else {
            addToken(TokenType.NUMERIC);
            this.position--;
            state = State.INITIAL;
        }
    }

    private void stateSymbolicConstant(char ch) {
        if (ch == '\\') {
            addCharacterToBuffer(State.BACKSLASH_IN_SYMBOLIC_CONSTANT, ch);
        } else if (ch == '\n') {
            addToken(TokenType.ERROR);
            addToken(TokenType.WHITE_SPACE, String.valueOf(ch));
            state = State.INITIAL;
        } else {
            addCharacterToBuffer(State.END_OF_SYMBOLIC_CONSTANT, ch);
        }
    }

    private void stateBackslashInSymbolicConstant(char ch) {
        if (ch >= '0' && ch <= '7') {
            addCharacterToBuffer(State.ONE_OCTAL_DIGIT_AFTER_BACKSLASH_IN_SYMBOLIC_CONSTANT, ch);
        } else if (SymbolType.isEscapeSequence(ch)) {
            addCharacterToBuffer(State.END_OF_SYMBOLIC_CONSTANT, ch);
        } else {
            addToken(TokenType.ERROR);
            this.position--;
            state = State.INITIAL;
        }
    }

    private void stateOneOctalDigitAfterBackslashInSymbolicConstant(char ch) {
        if (ch >= '0' && ch <= '7') {
            addCharacterToBuffer(State.TWO_OCTAL_DIGIT_AFTER_BACKSLASH_IN_SYMBOLIC_CONSTANT, ch);
        } else {
            this.position--;
            state = State.END_OF_SYMBOLIC_CONSTANT;
        }
    }

    private void stateTwoOctalDigitAfterBackslashInSymbolicConstant(char ch) {
        if (ch >= '0' && ch <= '7') {
            addCharacterToBuffer(State.END_OF_SYMBOLIC_CONSTANT, ch);
        } else {
            this.position--;
            state = State.END_OF_SYMBOLIC_CONSTANT;
        }
    }

    private void stateEndOfSymbolicConstant(char ch) {
        if (ch == '\'') {
            addCharacterToBuffer(State.INITIAL, ch);
            addToken(TokenType.SYMBOLIC);
        } else if (ch == '\n') {
            addToken(TokenType.ERROR);
            addToken(TokenType.WHITE_SPACE, String.valueOf(ch));
            state = State.INITIAL;
        } else {
            addCharacterToBuffer(State.ERROR_READ_SYMBOLIC_CONSTANT, ch);
        }
    }

    private void stateErrorReadSymbolicConstant(char ch) {
        if (ch == '\n') {
            addToken(TokenType.ERROR);
            addToken(TokenType.WHITE_SPACE, String.valueOf(ch));
            state = State.INITIAL;
        } else if (ch == '\'') {
            addCharacterToBuffer(State.INITIAL, ch);
            addToken(TokenType.ERROR);
        } else if (ch == '\\') {
            addCharacterToBuffer(State.BACKSLASH_INSIDE_ERROR_READ_SYMBOLIC_CONSTANT, ch);
        } else {
            addCharacterToBuffer(ch);
        }
    }

    private void stateBackslashInsideErrorReadSymbolicConstant(char ch) {
        addCharacterToBuffer(State.ERROR_READ_SYMBOLIC_CONSTANT, ch);
    }

    private void stateLiteralConstant(char ch) {
        if (ch == '\\') {
            addCharacterToBuffer(State.BACKSLASH_IN_LITERAL_CONSTANT, ch);
        } else if (ch == '\"') {
            addCharacterToBuffer(State.INITIAL, ch);
            addToken(TokenType.LITERAL);
        } else if (ch == '\n') {
            addToken(TokenType.ERROR);
            addToken(TokenType.WHITE_SPACE, String.valueOf(ch));
            state = State.INITIAL;
        } else {
            addCharacterToBuffer(ch);
        }
    }

    private void stateBackslashInLiteralConstant(char ch) {
        if (SymbolType.isEscapeSequence(ch) || (ch >= '0' && ch <= '7')) {
            addCharacterToBuffer(State.LITERAL_CONSTANT, ch);
        } else {
            addCharacterToBuffer(State.ERROR_READ_LITERAL_CONSTANT, ch);
        }
    }

    private void stateErrorReadLiteralConstant(char ch) {
        if (ch == '\n') {
            addToken(TokenType.ERROR);
            addToken(TokenType.WHITE_SPACE, String.valueOf(ch));
            state = State.INITIAL;
        } else if (ch == '\"') {
            addCharacterToBuffer(State.INITIAL, ch);
            addToken(TokenType.ERROR);
        } else {
            addCharacterToBuffer(ch);
        }
    }
}
