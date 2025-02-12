package tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(64);
        }

        String outputDir = args[0];
        List<String> exprClasses = Arrays.asList(
                "Assign   : Token name, Expr value",
                "Binary   : Expr left, Token operator, Expr right",
                "Call     : Expr callee, Token paren, List<Expr> arguments",
                "Get      : Expr object, Token name",
                "Grouping : Expr expression",
                "Literal  : Object value",
                "Logical  : Expr left, Token operator, Expr right",
                "Set      : Expr object, Token name, Expr value",
                "Super    : Token keyword, Token method",
                "This     : Token keyword",
                "Unary    : Token operator, Expr right",
                "Variable : Token name"
        );
        defineAst(outputDir, "Expr", exprClasses);

        List<String> stmtClasses = Arrays.asList(
                "Block     : List<Stmt> statements",
                "Class      : Token name, Expr.Variable superclass, List<Stmt.Function> methods",
                "Expression : Expr expression",
                "Function   : Token name, List<Token> params, List<Stmt> body",
                "If         : Expr condition, Stmt thenBranch, Stmt elseBranch",
                "Print      : Expr expression",
                "Return     : Token keyword, Expr value",
                "While      : Expr condition, Stmt body",
                "Var        : Token name, Expr initializer"
        );
        defineAst(outputDir, "Stmt", stmtClasses);
    }

    private static void defineAst(String outputDir, String baseName, List<String> types) throws IOException {
        String outputPath = String.format("%s/%s.java", outputDir, baseName);
        try (PrintWriter writer = new PrintWriter(outputPath, "UTF-8")) {
            writer.println("import java.util.List;");
            writer.println("import token.Token;");
            writer.println(String.format("abstract class %s {", baseName));

            defineVisitor(writer, baseName, types);

            // Base accept() method.
            writer.println("  abstract <R> R accept(Visitor<R> visitor);");

            // Makes a new class in the abstract class Expr for each type of expression
            for (String type : types) {
                String className = type.split(":")[0].trim();
                String fields = type.split(":")[1].trim();
                defineType(writer, baseName, className, fields);
            }

            writer.println("}");
        }
    }

    private static void defineType(PrintWriter writer, String baseName, String className, String fieldList) {
        writer.println(String.format("  public static class %s extends %s {", className, baseName));

        // Makes the constructor for className
        writer.println(String.format("    %s (%s) {", className, fieldList));
        String[] fields = fieldList.split(", ");
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println(String.format("      this.%s = %s;", name, name));
        }
        writer.println("    }");

        // Makes the fields for className
        writer.println();
        for (String field : fields) {
            writer.println(String.format("      final %s;", field));
        }

        // Override function for visitor pattern
        writer.println(generateOverrideVisitor(className, baseName));

        // Override function for toString()
        writer.println(generateOverrideToString(fields));
        writer.println("   }");
    }

    private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) {
        writer.println("  interface Visitor<R> {");
        for (String type : types) {
            String typeName = type.split(":")[0].trim();
            writer.println(String.format("    R visit%s%s(%s %s);", typeName, baseName, typeName, baseName.toLowerCase()));
        }
        writer.println("  }");
    }

    private static String generateOverrideVisitor(String className, String baseName) {
        return String.format("""
            @Override
            <R> R accept(Visitor<R> visitor) {
              return visitor.visit%s%s(this);
            }""", className, baseName);
    }

    private static String generateOverrideToString(String[] fields) {
        String stringPlaceholders = "";
        String fieldPlaceholders = "";

        for (String field : fields) {
            String fieldType = field.split(" ")[0];
            String name = field.split(" ")[1];
            stringPlaceholders += " %s";
            if (fieldType.equals("Token")) {
                fieldPlaceholders += String.format(", %s.lexeme", name);
            } else {
                fieldPlaceholders += String.format(", %s", name);
            }
        }

        return String.format("""
            @Override
            public String toString() {
                return String.format(\"%s\" %s);
            }""", stringPlaceholders.trim(), fieldPlaceholders.trim());
    }
}
