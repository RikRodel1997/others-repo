
import java.util.List;

interface LoxCallable {

    int arity();

    Object call(Interpreter interepter, List<Object> args);
}
