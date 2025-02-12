
import java.util.List;
import java.util.Map;

public class LoxClass implements LoxCallable {

    final String name;
    private final Map<String, LoxFunction> methods;
    final LoxClass superclass;

    LoxClass(String name, LoxClass superclass, Map<String, LoxFunction> methods) {
        this.name = name;
        this.superclass = superclass;
        this.methods = methods;
    }

    LoxFunction findMethod(String methodName) {
        if (this.methods.containsKey(methodName)) {
            return this.methods.get(methodName);
        }

        if (this.superclass != null) {
            return this.superclass.findMethod(methodName);
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> args) {
        LoxInstance instance = new LoxInstance(this);
        LoxFunction initializer = this.findMethod("init");
        if (initializer != null) {
            initializer.bind(instance).call(interpreter, args);
        }
        return instance;
    }

    @Override
    public int arity() {
        LoxFunction initializer = this.findMethod("init");
        if (initializer != null) {
            return initializer.arity();
        }
        return 0;
    }
}
