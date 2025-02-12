
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import token.Token;
import token.TokenType;

public class Parser {

    private static class ParseError extends RuntimeException {
    }

    private final List<Token> tokens;
    private int curr = 0;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();
        while (!this.isEof()) {
            statements.add(this.declaration());
        }
        return statements;
    }

    // declaration -> varDecl | statement ;
    private Stmt declaration() {
        try {
            if (this.match(TokenType.CLASS)) {
                return this.classDeclaration();
            }
            if (this.match(TokenType.FUN)) {
                return this.function("function");
            }
            if (this.match(TokenType.VAR)) {
                return this.varDeclaration();
            }
            return this.statement();
        } catch (ParseError error) {
            this.synchronize();
            return null;
        }
    }

    // classDecl   -> "class" IDENTIFIER ( "<" IDENTIFIER )? "{" function* "}" ;
    private Stmt classDeclaration() {
        Token name = this.consume(TokenType.IDENTIFIER, "Expect class name.");
        Expr.Variable superclass = null;
        if (this.match(TokenType.LESS)) {
            this.consume(TokenType.IDENTIFIER, "Expect superclass name.");
            superclass = new Expr.Variable(this.previous());
        }
        this.consume(TokenType.LEFT_BRACE, "Expect '{' before class body.");

        List<Stmt.Function> methods = new ArrayList<>();
        while (!this.check(TokenType.RIGHT_BRACE) && !this.isEof()) {
            methods.add(this.function("method"));
        }

        this.consume(TokenType.RIGHT_BRACE, "Expect '}' after class body.");
        return new Stmt.Class(name, superclass, methods);
    }

    // statement   -> exprStmt | ifStmt | printStmt | block ;
    private Stmt statement() {
        if (this.match(TokenType.FOR)) {
            return this.forStatement();
        }
        if (this.match(TokenType.IF)) {
            return this.ifStatement();
        }
        if (this.match(TokenType.PRINT)) {
            return this.printStatement();
        }
        if (this.match(TokenType.RETURN)) {
            return this.returnStatement();
        }
        if (this.match(TokenType.WHILE)) {
            return this.whileStatement();
        }
        if (this.match(TokenType.LEFT_BRACE)) {
            return new Stmt.Block(this.block());
        }
        return this.expressionStatement();
    }

    // forStmt     -> "for" "(" ( varDecl | exprStmt | ";" ) expression? ";" expression? ")" statement ;
    private Stmt forStatement() {
        this.consume(TokenType.LEFT_PAREN, "Expect '(' after 'for'.");

        Stmt initializer;
        if (this.match(TokenType.SEMICOLON)) {
            initializer = null;
        } else if (this.match(TokenType.VAR)) {
            initializer = this.varDeclaration();
        } else {
            initializer = this.expressionStatement();
        }

        Expr condition = null;
        if (!this.check(TokenType.SEMICOLON)) {
            condition = this.expression();
        }
        this.consume(TokenType.SEMICOLON, "Expect ';' after loop condition.");

        Expr increment = null;
        if (!this.check(TokenType.RIGHT_PAREN)) {
            increment = this.expression();
        }
        this.consume(TokenType.RIGHT_PAREN, "Expect ')' after for clauses.");

        Stmt body = this.statement();

        if (increment != null) {
            body = new Stmt.Block(Arrays.asList(body, new Stmt.Expression(increment)));
        }

        if (condition == null) {
            condition = new Expr.Literal(true);
        }

        body = new Stmt.While(condition, body);

        if (initializer != null) {
            body = new Stmt.Block(Arrays.asList(initializer, body));
        }

        return body;
    }

    // ifStmt      -> "if" "(" expression ")" statement ( "else" statement )? ;
    private Stmt ifStatement() {
        this.consume(TokenType.LEFT_PAREN, "Expect '(' after 'if'.");
        Expr condition = this.expression();
        this.consume(TokenType.RIGHT_PAREN, "Expect ')' after if condition.");
        Stmt thenBranch = this.statement();
        Stmt elseBranch = null;
        if (this.match(TokenType.ELSE)) {
            elseBranch = this.statement();
        }
        return new Stmt.If(condition, thenBranch, elseBranch);
    }

    // printStmt   -> "print" expression ";" ;
    private Stmt printStatement() {
        Expr value = this.expression();
        this.consume(TokenType.SEMICOLON, "Expect ';' after value.");
        return new Stmt.Print(value);
    }

    // returnStmt  -> "return" expression? ";" ;
    private Stmt returnStatement() {
        Token keyword = this.previous();
        Expr value = null;
        if (!this.check(TokenType.SEMICOLON)) {
            value = this.expression();
        }
        this.consume(TokenType.SEMICOLON, "Expect ';' after return value.");
        return new Stmt.Return(keyword, value);
    }

    // varDecl     -> "var" IDENTIFIER ( "=" expression )? ";" ;
    private Stmt varDeclaration() {
        Token name = this.consume(TokenType.IDENTIFIER, "Expect variable name.");
        Expr initializer = null;
        if (this.match(TokenType.EQUAL)) {
            initializer = this.expression();
        }
        this.consume(TokenType.SEMICOLON, "Expect ';' after variable declaration.");
        return new Stmt.Var(name, initializer);
    }

    // Rule: 
    // whileStmt   -> "while" "(" expression ")" statement ;
    private Stmt whileStatement() {
        this.consume(TokenType.LEFT_PAREN, "Expect '(' after 'while'.");
        Expr condition = this.expression();
        this.consume(TokenType.RIGHT_PAREN, "Expect ')' after while condition.");
        Stmt body = this.statement();
        return new Stmt.While(condition, body);
    }

    // exprStmt    -> expression ";" ;
    private Stmt expressionStatement() {
        Expr value = this.expression();
        this.consume(TokenType.SEMICOLON, "Expect ';' after expression.");
        return new Stmt.Expression(value);
    }

    // function    -> "fun" IDENTIFIER "(" parameters? ")" block ;
    private Stmt.Function function(String kind) {
        Token name = this.consume(TokenType.IDENTIFIER, String.format("Expect %s name.", kind));
        this.consume(TokenType.LEFT_PAREN, String.format("Expect '(' after %s name.", kind));

        List<Token> params = new ArrayList<>();

        if (!this.check(TokenType.RIGHT_PAREN)) {
            do {
                if (params.size() >= 255) {
                    this.error(this.peek(), "Cannot have more than 255 parameters.");
                }
                params.add(this.consume(TokenType.IDENTIFIER, "Expect parameter name."));
            } while (this.match(TokenType.COMMA));
        }

        this.consume(TokenType.RIGHT_PAREN, "Expect ')' after parameters.");
        this.consume(TokenType.LEFT_BRACE, "Expect '{' before " + kind + " body.");
        List<Stmt> body = this.block();

        return new Stmt.Function(name, params, body);
    }

    // block       -> "{" declaration* "}" ;
    private List<Stmt> block() {
        List<Stmt> statements = new ArrayList<>();
        while (!this.check(TokenType.RIGHT_BRACE) && !this.isEof()) {
            statements.add(this.declaration());
        }
        this.consume(TokenType.RIGHT_BRACE, "Expect '}' after block.");
        return statements;
    }

    // expression  -> equality ;
    private Expr expression() {
        return this.assignment();
    }

    // assignment  -> IDENTIFIER "=" assignment | equality ;
    private Expr assignment() {
        Expr expr = this.or();
        if (this.match(TokenType.EQUAL)) {
            Token equals = this.previous();
            Expr value = this.assignment();

            switch (expr) {
                case Expr.Variable variable -> {
                    Token name = variable.name;
                    return new Expr.Assign(name, value);
                }
                case Expr.Get get -> {
                    return new Expr.Set(get.object, get.name, value);
                }
                default -> {
                }
            }

            this.error(equals, "Invalid assignment target.");
        }
        return expr;
    }

    // logic_or    -> logic_and ( "or" logic_and )* ;
    private Expr or() {
        Expr expr = this.and();
        while (this.match(TokenType.OR)) {
            Token operator = this.previous();
            Expr right = this.and();
            expr = new Expr.Logical(expr, operator, right);
        }
        return expr;
    }

    // logic_and   -> equality ( "and" equality )* ;
    private Expr and() {
        Expr expr = this.equality();
        while (this.match(TokenType.AND)) {
            Token operator = this.previous();
            Expr right = this.equality();
            expr = new Expr.Logical(expr, operator, right);
        }
        return expr;
    }

    // equality     -> comparison ( ( "!=" | "==" ) comparison )* ;
    // The first 'comparison' maps to:
    // Expr expr = this.comparison();
    // ( ... )* maps to the while loop, but not the contents of the while loop.
    // We exit the loop if we find '!=' or '=='. So that is what we check for.
    private Expr equality() {
        Expr expr = this.comparison();
        while (this.match(TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL)) {
            Token operator = this.previous();
            Expr right = this.comparison();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    // comparison  -> term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
    // We exit the loop if we find '>', '>=', '<' or '<='. So that is what we check for.
    private Expr comparison() {
        Expr expr = this.term();

        while (this.match(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            Token operator = this.previous();
            Expr right = this.term();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    // term        -> factor ( ( "-" | "+" ) factor )* ;
    // We exit the loop if we find '-' or '+'. So that is what we check for.
    private Expr term() {
        Expr expr = this.factor();

        while (this.match(TokenType.MINUS, TokenType.PLUS)) {
            Token operator = this.previous();
            Expr right = this.factor();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    // factor      -> unary ( ( "/" | "*" ) unary )* ;
    // We exit the loop if we find '/' or '*'. So that is what we check for.   
    private Expr factor() {
        Expr expr = this.unary();

        while (this.match(TokenType.SLASH, TokenType.STAR)) {
            Token operator = this.previous();
            Expr right = this.unary();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    // unary       -> ( "!" | "-" ) unary | primary ;
    private Expr unary() {
        if (this.match(TokenType.BANG, TokenType.MINUS)) {
            Token operator = this.previous();
            Expr right = this.unary();
            return new Expr.Unary(operator, right);
        }
        return this.call();
    }

    private Expr finishCall(Expr callee) {
        List<Expr> args = new ArrayList<>();
        if (!this.check(TokenType.RIGHT_PAREN)) {
            do {
                if (args.size() >= 255) {
                    this.error(this.peek(), "Cannot have more than 255 arguments.");
                }
                args.add(this.expression());
            } while (this.match(TokenType.COMMA));
        }

        Token paren = this.consume(TokenType.RIGHT_PAREN, "Expect ')' after arguments.");

        return new Expr.Call(callee, paren, args);
    }

    // call        -> primary ( "(" arguments? ")" | "." IDENTIFIER )* ;
    private Expr call() {
        Expr expr = this.primary();

        while (true) {
            if (this.match(TokenType.LEFT_PAREN)) {
                expr = this.finishCall(expr);
            } else if (this.match(TokenType.DOT)) {
                Token name = this.consume(TokenType.IDENTIFIER, "Expect property name after '.'.");
                expr = new Expr.Get(expr, name);
            } else {
                break;
            }
        }

        return expr;
    }

    // primary     -> NUMBER | STRING | "true" | "false" | "nil" | "(" expression ")" | IDENTIFIER | "super" "." IDENTIFIER ;
    private Expr primary() throws ParseError {
        if (this.match(TokenType.FALSE)) {
            return new Expr.Literal(false);
        }
        if (this.match(TokenType.TRUE)) {
            return new Expr.Literal(true);
        }
        if (this.match(TokenType.NIL)) {
            return new Expr.Literal(null);
        }
        if (this.match(TokenType.NUMBER, TokenType.STRING)) {
            return new Expr.Literal(this.previous().literal);
        }
        if (this.match(TokenType.SUPER)) {
            Token keyword = this.previous();
            this.consume(TokenType.DOT, "Expect '.' after 'super'.");
            Token method = this.consume(TokenType.IDENTIFIER, "Expect superclass method name.");
            return new Expr.Super(keyword, method);
        }
        if (this.match(TokenType.THIS)) {
            return new Expr.This(this.previous());
        }
        if (this.match(TokenType.IDENTIFIER)) {
            return new Expr.Variable(this.previous());
        }
        if (this.match(TokenType.LEFT_PAREN)) {
            Expr expr = this.expression();
            this.consume(TokenType.RIGHT_PAREN, "Expect ')' after expression.");
            return new Expr.Grouping(expr);
        }
        // If no match, throw an error to ensure the parser doesn't proceed unexpectedly.
        throw error(this.peek(), String.format("Unexpected token in primary expression: %s", this.peek().lexeme));
    }

    // Different helper methods for the parser.
    // Checks if the next token is of one of a collection of given types and consumes it.
    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (this.check(type)) {
                this.advance();
                return true;
            }
        }
        return false;
    }

    // Consumes the next token if it is of the given type, otherwise throws an error.
    private Token consume(TokenType type, String message) {
        if (this.check(type)) {
            return this.advance();
        }

        throw error(this.peek(), message);
    }

    // Checks if the next token is of the given type.
    private boolean check(TokenType type) {
        if (this.isEof()) {
            return false;
        }
        return this.peek().type == type;
    }

    // Consumes the next token and returns it.
    private Token advance() {
        if (!this.isEof()) {
            this.curr++;
        }
        return this.previous();
    }

    // Check if we have reached the end of the token list.
    private boolean isEof() {
        return this.peek().type == TokenType.EOF;
    }

    // Takes the next token without consuming it.
    private Token peek() {
        return this.tokens.get(this.curr);
    }

    // Takes the previous token without consuming it.
    private Token previous() {
        return this.tokens.get(this.curr - 1);
    }

    private ParseError error(Token token, String message) {
        Main.error(token, message);
        return new ParseError();
    }

    private void synchronize() {
        this.advance();
        while (!this.isEof()) {
            if (this.previous().type == TokenType.SEMICOLON) {
                return;
            }

            switch (this.peek().type) {
                case CLASS, FUN, VAR, FOR, IF, WHILE, PRINT, RETURN -> {
                    return;
                }
                default -> {
                }
            }
        }

        this.advance();
    }
}
