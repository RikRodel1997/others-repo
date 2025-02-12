
import java.util.HashMap;
import java.util.Map;

import errors.RuntimeError;
import token.Token;

public class Environment {

    final Environment enclosing;
    private final Map<String, Object> values = new HashMap<>();

    Environment() {
        this.enclosing = null;
    }

    Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    Object get(Token name) {
        if (this.values.containsKey(name.lexeme)) {
            return this.values.get(name.lexeme);
        }

        if (this.enclosing != null) {
            return this.enclosing.get(name);
        }

        throw new RuntimeError(name, String.format("Undefined variable '%s'.", name.lexeme));
    }

    void define(String name, Object value) {
        this.values.put(name, value);
    }

    Environment ancestor(int distance) {
        Environment environment = this;
        for (int i = 0; i < distance; i++) {
            environment = environment.enclosing;
        }
        return environment;
    }

    Object getAt(int distance, String name) {
        return this.ancestor(distance).values.get(name);
    }

    void assignAt(int distance, Token name, Object value) {
        this.ancestor(distance).values.put(name.lexeme, value);
    }

    void assign(Token name, Object value) {
        if (this.values.containsKey(name.lexeme)) {
            values.put(name.lexeme, value);
            return;
        }

        if (this.enclosing != null) {
            this.enclosing.assign(name, value);
            return;
        }

        throw new RuntimeError(name, String.format("Undefined variable '%s'.", name.lexeme));
    }
}
