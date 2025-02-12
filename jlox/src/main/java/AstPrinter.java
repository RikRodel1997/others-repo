
import java.util.List;
import java.util.function.Supplier;

import token.Token;

public class AstPrinter implements Expr.Visitor<String>, Stmt.Visitor<String> {

    private int indentLevel = 0;

    public String print(List<Stmt> statements) {
        StringBuilder builder = new StringBuilder();
        for (Stmt stmt : statements) {
            builder.append(stmt.accept(this)).append("\n");
        }
        return builder.toString();
    }

    private String indent() {
        return "  ".repeat(indentLevel);
    }

    private String visitWithIndent(Supplier<String> visitAction) {
        indentLevel++;
        String result = visitAction.get();
        indentLevel--;
        return result;
    }

    // --- Statement Visitors ---
    @Override
    public String visitBlockStmt(Stmt.Block stmt) {
        StringBuilder builder = new StringBuilder();
        builder.append(indent()).append("Block:\n");
        builder.append(visitWithIndent(() -> {
            StringBuilder bodyBuilder = new StringBuilder();
            for (Stmt statement : stmt.statements) {
                builder.append(statement.accept(this)).append("\n");
            }
            return bodyBuilder.toString();
        }));
        return builder.toString();
    }

    @Override
    public String visitClassStmt(Stmt.Class stmt) {
        StringBuilder builder = new StringBuilder();
        builder.append(indent()).append("Class ").append(stmt.name.lexeme);
        if (stmt.superclass != null) {
            builder.append(" extends ").append(stmt.superclass.name.lexeme);
        }
        builder.append(":\n");
        builder.append(visitWithIndent(() -> {
            StringBuilder bodyBuilder = new StringBuilder();
            for (Stmt.Function method : stmt.methods) {
                builder.append(method.accept(this)).append("\n");
            }
            return bodyBuilder.toString();
        }));
        return builder.toString();
    }

    @Override
    public String visitExpressionStmt(Stmt.Expression stmt) {
        return indent() + "ExprStmt: " + stmt.expression.accept(this);
    }

    @Override
    public String visitFunctionStmt(Stmt.Function stmt) {
        StringBuilder builder = new StringBuilder();
        builder.append(indent()).append("Function ").append(stmt.name.lexeme).append("(");
        for (Token param : stmt.params) {
            builder.append(param.lexeme).append(" ");
        }
        builder.append("):\n");
        builder.append(visitWithIndent(() -> {
            StringBuilder bodyBuilder = new StringBuilder();
            for (Stmt bodyStmt : stmt.body) {
                bodyBuilder.append(bodyStmt.accept(this)).append("\n");
            }
            return bodyBuilder.toString();
        }));
        return builder.toString();
    }

    @Override
    public String visitIfStmt(Stmt.If stmt) {
        StringBuilder builder = new StringBuilder();
        builder.append(indent()).append("If (").append(stmt.condition.accept(this)).append("):\n");
        visitWithIndent(() -> builder.append(stmt.thenBranch.accept(this)).append("\n").toString());
        if (stmt.elseBranch != null) {
            builder.append(indent()).append("Else:\n");
            visitWithIndent(() -> builder.append(stmt.elseBranch.accept(this)).append("\n").toString());
        }
        return builder.toString();
    }

    @Override
    public String visitPrintStmt(Stmt.Print stmt) {
        return indent() + "Print: " + stmt.expression.accept(this);
    }

    @Override
    public String visitReturnStmt(Stmt.Return stmt) {
        return indent() + "Return: " + (stmt.value != null ? stmt.value.accept(this) : "nil");
    }

    @Override
    public String visitVarStmt(Stmt.Var stmt) {
        return indent() + "Var " + stmt.name.lexeme + " = " + (stmt.initializer != null ? stmt.initializer.accept(this) : "nil");
    }

    @Override
    public String visitWhileStmt(Stmt.While stmt) {
        StringBuilder builder = new StringBuilder();
        builder.append(indent()).append("While (").append(stmt.condition.accept(this)).append("):\n");
        visitWithIndent(() -> builder.append(stmt.body.accept(this)).append("\n").toString());
        return builder.toString();
    }

    // --- Expression Visitors ---
    @Override
    public String visitAssignExpr(Expr.Assign expr) {
        return indent() + "Assign " + expr.name.lexeme + " = " + expr.value.accept(this);
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return indent() + "Binary (" + expr.operator.lexeme + ")\n"
                + visitWithIndent(() -> expr.left.accept(this) + "\n" + expr.right.accept(this));
    }

    @Override
    public String visitCallExpr(Expr.Call expr) {
        return indent() + "Call " + expr.callee.accept(this) + " with " + expr.arguments.size() + " args";
    }

    @Override
    public String visitGetExpr(Expr.Get expr) {
        return indent() + "Get " + expr.name.lexeme + " from " + expr.object.accept(this);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return indent() + "Group:\n" + visitWithIndent(() -> expr.expression.accept(this));
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        return indent() + "Literal " + (expr.value == null ? "nil" : expr.value.toString());
    }

    @Override
    public String visitLogicalExpr(Expr.Logical expr) {
        return indent() + "Logical (" + expr.operator.lexeme + ")\n"
                + visitWithIndent(() -> expr.left.accept(this) + "\n" + expr.right.accept(this));
    }

    @Override
    public String visitSetExpr(Expr.Set expr) {
        return indent() + "Set " + expr.name.lexeme + " = " + expr.value.accept(this);
    }

    @Override
    public String visitSuperExpr(Expr.Super expr) {
        return indent() + "Super " + expr.method.lexeme;
    }

    @Override
    public String visitThisExpr(Expr.This expr) {
        return indent() + "This";
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return indent() + "Unary (" + expr.operator.lexeme + ")\n" + visitWithIndent(() -> expr.right.accept(this));
    }

    @Override
    public String visitVariableExpr(Expr.Variable expr) {
        return indent() + "Variable " + expr.name.lexeme;
    }
}
