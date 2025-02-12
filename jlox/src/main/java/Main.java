
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import errors.RuntimeError;
import token.Token;
import token.TokenType;

public class Main {

    private static final Interpreter interpreter = new Interpreter();
    static boolean hadError = false;
    static boolean hadRuntimeError = false;
    private static boolean debug = true;

    public static void main(String[] args) throws IOException {
        if (args.length > 2) {
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        } else if (args.length == 1 || args.length == 2) {
            if (args[0].equals("debug")) {
                debug = true;
                runFile(args[1]);
            } else {
                debug = false;
                runFile(args[0]);
            }
        } else {
            runPrompt();
        }
    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
        if (hadError) {
            System.exit(65);
        }
        if (hadRuntimeError) {
            System.exit(70);
        }
    }

    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader rdr = new BufferedReader(input);

        while (true) {
            System.out.print("> ");
            String line = rdr.readLine();
            if (line == null) {
                break;
            }
            if (line.equals("debug")) {
                debug = !debug;
                System.out.println("Debug mode: " + (debug ? "ON" : "OFF"));
                continue;
            }
            run(line);
            hadError = false;
        }
    }

    private static void run(String source) {
        Scanner scnr = new Scanner(source);
        List<Token> tokens = scnr.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> stmts = parser.parse();

        Resolver resolver = new Resolver(interpreter);
        resolver.resolve(stmts);

        if (hadError) {
            return;
        }

        if (debug) {
            System.out.println("AST:");
            System.out.println(new AstPrinter().print(stmts));
        }

        interpreter.interpret(stmts);
    }

    private static void report(int line, String where, String message) {
        String msg = String.format("[line %d] Error %s: %s", line, where, message);
        System.err.println(msg);
        hadError = true;
    }

    static void error(int line, String message) {
        report(line, "", message);
    }

    static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
            report(token.line, "at end", message);
        } else {
            report(token.line, String.format("at '%s'", token.lexeme), message);
        }
    }

    static void runtimeError(RuntimeError err) {
        System.err.println(err.getMessage() + "\n[line " + err.token.line + "]");
        hadRuntimeError = true;
    }
}
