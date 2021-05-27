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
            }
        }
    }

    private void addCharacterToBuffer(State state, char ch) {
        buffer.append(ch);
        this.state = state;
    }

    private void stateStart(char ch) {
        if (Character.isWhitespace(ch)) {
            this.tokens.add(new Token(TokenType.WHITE_SPACE, String.valueOf(ch)));
        } else if (ch == '0') {
            addCharacterToBuffer(State.SINGLE_ZERO, ch);
        } else if (ch >= '1' && ch <= '9') {
            addCharacterToBuffer(State.DECIMAL_NUMBER, ch);
        }
    }
}
