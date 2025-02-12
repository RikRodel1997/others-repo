
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import token.Token;
import token.TokenType;

public class Scanner {

    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private static final Map<String, TokenType> keywords = Map.ofEntries(
            Map.entry("and", TokenType.AND),
            Map.entry("class", TokenType.CLASS),
            Map.entry("else", TokenType.ELSE),
            Map.entry("false", TokenType.FALSE),
            Map.entry("for", TokenType.FOR),
            Map.entry("fun", TokenType.FUN),
            Map.entry("if", TokenType.IF),
            Map.entry("nil", TokenType.NIL),
            Map.entry("or", TokenType.OR),
            Map.entry("print", TokenType.PRINT),
            Map.entry("return", TokenType.RETURN),
            Map.entry("super", TokenType.SUPER),
            Map.entry("this", TokenType.THIS),
            Map.entry("true", TokenType.TRUE),
            Map.entry("var", TokenType.VAR),
            Map.entry("while", TokenType.WHILE)
    );

    private int start = 0;
    private int curr = 0;
    private int line = 1;

    Scanner(String source) {
        this.source = source;
    }

    List<Token> scanTokens() {
        while (!this.isEof()) {
            this.start = this.curr;
            scanToken();
        }

        tokens.add(new Token(TokenType.EOF, "", null, this.line));
        return this.tokens;
    }

    private boolean isEof() {
        return this.curr >= source.length();
    }

    private void scanToken() {
        char c = this.advance();
        switch (c) {
            case '(' ->
                this.addToken(TokenType.LEFT_PAREN);
            case ')' ->
                this.addToken(TokenType.RIGHT_PAREN);
            case '{' ->
                this.addToken(TokenType.LEFT_BRACE);
            case '}' ->
                this.addToken(TokenType.RIGHT_BRACE);
            case ',' ->
                addToken(TokenType.COMMA);
            case '.' ->
                this.addToken(TokenType.DOT);
            case '-' ->
                this.addToken(TokenType.MINUS);
            case '+' ->
                this.addToken(TokenType.PLUS);
            case ';' ->
                this.addToken(TokenType.SEMICOLON);
            case '*' -> {
                if (this.peek() == '/') {
                    // Meaning we're at the end of a multi-line comment
                    this.advance();
                } else {
                    this.addToken(TokenType.STAR);
                }
            }
            case '!' ->
                this.addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
            case '=' ->
                this.addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
            case '<' ->
                this.addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
            case '>' ->
                this.addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
            case '/' -> {
                if (this.match('/')) {
                    while (this.peek() != '\n' && !this.isEof()) {
                        this.advance();
                    }
                } else if (this.match('*')) {
                    while (this.peek() != '*' && this.nextPeek() != '/' && !this.isEof()) {
                        this.advance();
                    }
                } else {
                    this.addToken(TokenType.SLASH);
                }
            }
            case ' ', '\r', '\t' -> {
                break;
            }
            case '\n' -> {
                this.line++;
                break;
            }
            case '"' ->
                string();
            default -> {
                if (isDigit(c)) {
                    this.number();
                } else if (this.isAlpha(c)) {
                    this.identifier();
                } else {
                    Main.error(this.line, "Unexpected character.");
                }
            }
        }
    }

    private char advance() {
        return this.source.charAt(this.curr++);
    }

    private boolean match(char expected) {
        if (this.isEof()) {
            return false;
        }
        if (source.charAt(this.curr) != expected) {
            return false;
        }
        this.curr++;
        return true;
    }

    private char peek() {
        if (this.isEof()) {
            return '\0';
        }
        return source.charAt(this.curr);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(this.start, this.curr);
        this.tokens.add(new Token(type, text, literal, this.line));
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    private void identifier() {
        while (this.isAlpha(this.peek()) || this.isDigit(this.peek())) {
            this.advance();
        }

        String idenText = this.source.substring(this.start, this.curr);
        TokenType type = keywords.get(idenText);
        if (type == null) {
            type = TokenType.IDENTIFIER;
        }
        addToken(type);
    }

    private void string() {
        while (this.peek() != '"' && !this.isEof()) {
            if (this.peek() == '\n') {
                this.line++;
            }
            this.advance();
        }

        if (this.isEof()) {
            Main.error(this.line, "Unterminated string.");
            return;
        }

        this.advance(); // This is the closing quote
        String value = this.source.substring(this.start + 1, this.curr - 1); // Get the value without the quotes
        this.addToken(TokenType.STRING, value);
    }

    private void number() {
        while (this.isDigit(this.peek())) {
            this.advance();
        }

        if (this.peek() == '.' && isDigit(this.nextPeek())) {
            this.advance();
            while (this.isDigit(this.peek())) {
                this.advance();
            }
        }

        String doubleValue = this.source.substring(this.start, this.curr);
        this.addToken(TokenType.NUMBER, Double.valueOf(doubleValue));
    }

    private char nextPeek() {
        if (this.curr + 1 >= this.source.length()) {
            return '\0';
        }
        return this.source.charAt(this.curr + 1);
    }

}
