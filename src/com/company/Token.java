package com.company;

public class Token implements Comparable<Token>{
    private final Pair<TokenType, String> token;

    public Token(TokenType tokenType, String value) {
        this.token = new Pair<>(tokenType, value);
    }

    public TokenType getTokenType() {
        return this.token.first;
    }

    public String getValue() {
        return this.token.second;
    }

    @Override
    public int compareTo(Token token) {
        return 0;
    }
}
