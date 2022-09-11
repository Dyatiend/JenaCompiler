package builtins;

import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

public class AbsoluteValue extends BaseBuiltin {

    /**
     * Return a name for this builtin, normally this will be the name of the
     * functor that will be used to invoke it.
     */
    @Override
    public String getName() {
        return "absoluteValue";
    }

    /**
     * Return the expected number of arguments for this functor or 0 if the number is flexible.
     */
    @Override
    public int getArgLength() {
        return 2;
    }

    /**
     * This method is invoked when the builtin is called in a rule body.
     * @param args the array of argument values for the builtin, this is an array
     * of Nodes, some of which may be Node_RuleVariables.
     * @param context an execution context giving access to other relevant data
     * @param length the length of the argument list, may be less than the length of the args array
     * for some rule engines
     * @return return true if the builtin predicate is deemed to have succeeded in
     * the current environment
     */
    @Override
    public boolean bodyCall(Node[] args, int length, RuleContext context) {
        checkArgs(length, context);
        BindingEnvironment env = context.getEnv();
        boolean ok = false;
        Node a0 = getArg(0, args, context);
        if (Util.isNumeric(a0)) {
            try {
                int val = Integer.parseInt(a0.getLiteralValue().toString());
                Node newVal = Util.makeIntNode(Math.abs(val));
                ok = env.bind(args[1], newVal);
            }
            catch (NumberFormatException e1) {
                try {
                    double val = Double.parseDouble(a0.getLiteralValue().toString());
                    Node newVal = Util.makeDoubleNode(Math.abs(val));
                    ok = env.bind(args[1], newVal);
                }
                catch (NumberFormatException e2) {
                    return false;
                }
            }
        }
        return ok;
    }
}
