package com.company;

public class Token{
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
}











// implements Comparable<Token>
//@Override
//public int compareTo(Token token) {
//        return 0;
//        }