
import java.util.List;

public class LoxFunction implements LoxCallable {

    private final Stmt.Function decl;
    private final Environment closure;
    private final boolean isIinitializer;

    LoxFunction(Stmt.Function decl, Environment closure, boolean isIinitializer) {
        this.decl = decl;
        this.closure = closure;
        this.isIinitializer = isIinitializer;
    }

    LoxFunction bind(LoxInstance instance) {
        Environment env = new Environment(this.closure);
        env.define("this", instance);
        return new LoxFunction(this.decl, env, this.isIinitializer);
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> args) {
        Environment env = new Environment(this.closure);
        for (int i = 0; i < this.decl.params.size(); i++) {
            env.define(this.decl.params.get(i).lexeme, args.get(i));
        }

        try {
            interpreter.executeBlock(this.decl.body, env);
        } catch (Return returnValue) {
            if (this.isIinitializer) {
                return this.closure.getAt(0, "this");
            }
            return returnValue.value;
        }

        if (this.isIinitializer) {
            return this.closure.getAt(0, "this");
        }
        return null;
    }

    @Override
    public int arity() {
        return this.decl.params.size();
    }

    @Override
    public String toString() {
        return "<fn " + this.decl.name.lexeme + ">";
    }
}
