
import java.util.List;

import token.Token;

abstract class Expr {

    interface Visitor<R> {

        R visitAssignExpr(Assign expr);

        R visitBinaryExpr(Binary expr);

        R visitCallExpr(Call expr);

        R visitGetExpr(Get expr);

        R visitGroupingExpr(Grouping expr);

        R visitLiteralExpr(Literal expr);

        R visitLogicalExpr(Logical expr);

        R visitSetExpr(Set expr);

        R visitSuperExpr(Super expr);

        R visitThisExpr(This expr);

        R visitUnaryExpr(Unary expr);

        R visitVariableExpr(Variable expr);
    }

    abstract <R> R accept(Visitor<R> visitor);

    public static class Assign extends Expr {

        Assign(Token name, Expr value) {
            this.name = name;
            this.value = value;
        }

        final Token name;
        final Expr value;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitAssignExpr(this);
        }

        @Override
        public String toString() {
            return String.format("%s %s", name.lexeme, value);
        }
    }

    public static class Binary extends Expr {

        Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        final Expr left;
        final Token operator;
        final Expr right;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }

        @Override
        public String toString() {
            return String.format("%s %s %s", left, operator.lexeme, right);
        }
    }

    public static class Call extends Expr {

        Call(Expr callee, Token paren, List<Expr> arguments) {
            this.callee = callee;
            this.paren = paren;
            this.arguments = arguments;
        }

        final Expr callee;
        final Token paren;
        final List<Expr> arguments;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitCallExpr(this);
        }

        @Override
        public String toString() {
            return String.format("%s %s %s", callee, paren.lexeme, arguments);
        }
    }

    public static class Get extends Expr {

        Get(Expr object, Token name) {
            this.object = object;
            this.name = name;
        }

        final Expr object;
        final Token name;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGetExpr(this);
        }

        @Override
        public String toString() {
            return String.format("%s %s", object, name.lexeme);
        }
    }

    public static class Grouping extends Expr {

        Grouping(Expr expression) {
            this.expression = expression;
        }

        final Expr expression;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        }

        @Override
        public String toString() {
            return String.format("%s", expression);
        }
    }

    public static class Literal extends Expr {

        Literal(Object value) {
            this.value = value;
        }

        final Object value;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        }

        @Override
        public String toString() {
            return String.format("%s", value);
        }
    }

    public static class Logical extends Expr {

        Logical(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        final Expr left;
        final Token operator;
        final Expr right;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLogicalExpr(this);
        }

        @Override
        public String toString() {
            return String.format("%s %s %s", left, operator.lexeme, right);
        }
    }

    public static class Set extends Expr {

        Set(Expr object, Token name, Expr value) {
            this.object = object;
            this.name = name;
            this.value = value;
        }

        final Expr object;
        final Token name;
        final Expr value;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitSetExpr(this);
        }

        @Override
        public String toString() {
            return String.format("%s %s %s", object, name.lexeme, value);
        }
    }

    public static class Super extends Expr {

        Super(Token keyword, Token method) {
            this.keyword = keyword;
            this.method = method;
        }

        final Token keyword;
        final Token method;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitSuperExpr(this);
        }

        @Override
        public String toString() {
            return String.format("%s %s", keyword.lexeme, method.lexeme);
        }
    }

    public static class This extends Expr {

        This(Token keyword) {
            this.keyword = keyword;
        }

        final Token keyword;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitThisExpr(this);
        }

        @Override
        public String toString() {
            return String.format("%s", keyword.lexeme);
        }
    }

    public static class Unary extends Expr {

        Unary(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

        final Token operator;
        final Expr right;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }

        @Override
        public String toString() {
            return String.format("%s %s", operator.lexeme, right);
        }
    }

    public static class Variable extends Expr {

        Variable(Token name) {
            this.name = name;
        }

        final Token name;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitVariableExpr(this);
        }

        @Override
        public String toString() {
            return String.format("%s", name.lexeme);
        }
    }
}
