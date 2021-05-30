package com.company;

import java.util.List;

public class Output {
    private static String blue(String str){ return (char) 27 + "[34m" + str + (char) 27 + "[0m"; }
    private static String red(String str){ return (char) 27 + "[31m" + str + (char) 27 + "[0m"; }
    private static String green(String str){ return (char) 27 + "[32m" + str + (char) 27 + "[0m"; }

    public static void showTokens(List<Token> tokens) {
        System.out.println("<--------- TOKENS --------->");
        for (Token token : tokens) {
            if (token.getTokenType() == TokenType.WHITE_SPACE)
                System.out.println(blue("token") + ": " + token.getTokenType());
            else
                System.out.println(blue("token") + ": " + token.getTokenType() + " [" + green("value") + "= " + token.getValue() + "]");
        }
    }
}
