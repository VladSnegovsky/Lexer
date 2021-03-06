package com.company;

public enum State {
    INITIAL,
    SINGLE_ZERO,
    DECIMAL_NUMBER,
    SINGLE_PLUS,
    SINGLE_MINUS,
    SOME_OPERATOR_BEFORE_EQUAL,
    SINGLE_LESS,
    SINGLE_GREATER,
    SINGLE_SLASH,
    SINGLE_AMPERSAND,
    SINGLE_VERTICAL_BAR,
    SINGLE_COLON,
    IDENTIFIER,
    SINGLE_DOT,
    SYMBOLIC_CONSTANT,
    LITERAL_CONSTANT,
    MULTI_LINE_COMMENT,
    SINGLE_LINE_COMMENT,
    MULTI_LINE_COMMENT_AND_STAR,
    DOUBLE_LESS,
    DOUBLE_GREATER,
    DOUBLE_DOT,
    FLOATING_POINT_NUMBER,
    OCTAL_NUMBER,
    HEX_NUMBER,
    BINARY_NUMBER,
    END_OF_SYMBOLIC_CONSTANT,
    BACKSLASH_IN_SYMBOLIC_CONSTANT,
    ONE_OCTAL_DIGIT_AFTER_BACKSLASH_IN_SYMBOLIC_CONSTANT,
    TWO_OCTAL_DIGIT_AFTER_BACKSLASH_IN_SYMBOLIC_CONSTANT,
    ERROR_READ_SYMBOLIC_CONSTANT,
    BACKSLASH_INSIDE_ERROR_READ_SYMBOLIC_CONSTANT,
    BACKSLASH_IN_LITERAL_CONSTANT,
    ERROR_READ_LITERAL_CONSTANT
}
