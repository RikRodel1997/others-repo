
import java.util.List;

import token.Token;

abstract class Stmt {

    interface Visitor<R> {

        R visitBlockStmt(Block stmt);

        R visitClassStmt(Class stmt);

        R visitExpressionStmt(Expression stmt);

        R visitFunctionStmt(Function stmt);

        R visitIfStmt(If stmt);

        R visitPrintStmt(Print stmt);

        R visitReturnStmt(Return stmt);

        R visitWhileStmt(While stmt);

        R visitVarStmt(Var stmt);
    }

    abstract <R> R accept(Visitor<R> visitor);

    public static class Block extends Stmt {

        Block(List<Stmt> statements) {
            this.statements = statements;
        }

        final List<Stmt> statements;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBlockStmt(this);
        }

        @Override
        public String toString() {
            return String.format("%s", statements);
        }
    }

    public static class Class extends Stmt {

        Class(Token name, Expr.Variable superclass, List<Stmt.Function> methods) {
            this.name = name;
            this.superclass = superclass;
            this.methods = methods;
        }

        final Token name;
        final Expr.Variable superclass;
        final List<Stmt.Function> methods;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitClassStmt(this);
        }

        @Override
        public String toString() {
            return String.format("%s %s %s", name.lexeme, superclass, methods);
        }
    }

    public static class Expression extends Stmt {

        Expression(Expr expression) {
            this.expression = expression;
        }

        final Expr expression;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitExpressionStmt(this);
        }

        @Override
        public String toString() {
            return String.format("%s", expression);
        }
    }

    public static class Function extends Stmt {

        Function(Token name, List<Token> params, List<Stmt> body) {
            this.name = name;
            this.params = params;
            this.body = body;
        }

        final Token name;
        final List<Token> params;
        final List<Stmt> body;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitFunctionStmt(this);
        }

        @Override
        public String toString() {
            return String.format("%s %s %s", name.lexeme, params, body);
        }
    }

    public static class If extends Stmt {

        If(Expr condition, Stmt thenBranch, Stmt elseBranch) {
            this.condition = condition;
            this.thenBranch = thenBranch;
            this.elseBranch = elseBranch;
        }

        final Expr condition;
        final Stmt thenBranch;
        final Stmt elseBranch;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitIfStmt(this);
        }

        @Override
        public String toString() {
            return String.format("%s %s %s", condition, thenBranch, elseBranch);
        }
    }

    public static class Print extends Stmt {

        Print(Expr expression) {
            this.expression = expression;
        }

        final Expr expression;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitPrintStmt(this);
        }

        @Override
        public String toString() {
            return String.format("%s", expression);
        }
    }

    public static class Return extends Stmt {

        Return(Token keyword, Expr value) {
            this.keyword = keyword;
            this.value = value;
        }

        final Token keyword;
        final Expr value;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitReturnStmt(this);
        }

        @Override
        public String toString() {
            return String.format("%s %s", keyword.lexeme, value);
        }
    }

    public static class While extends Stmt {

        While(Expr condition, Stmt body) {
            this.condition = condition;
            this.body = body;
        }

        final Expr condition;
        final Stmt body;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitWhileStmt(this);
        }

        @Override
        public String toString() {
            return String.format("%s %s", condition, body);
        }
    }

    public static class Var extends Stmt {

        Var(Token name, Expr initializer) {
            this.name = name;
            this.initializer = initializer;
        }

        final Token name;
        final Expr initializer;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitVarStmt(this);
        }

        @Override
        public String toString() {
            return String.format("%s %s", name.lexeme, initializer);
        }
    }
}
