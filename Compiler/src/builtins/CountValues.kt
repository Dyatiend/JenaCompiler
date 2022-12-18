package builtins

import org.apache.jena.graph.Node
import org.apache.jena.reasoner.rulesys.RuleContext
import org.apache.jena.reasoner.rulesys.Util
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin

class CountValues : BaseBuiltin() {
    /**
     * Return a name for this builtin, normally this will be the name of the
     * functor that will be used to invoke it.
     */
    override fun getName(): String {
        return "countValues"
    }

    /**
     * Return the expected number of arguments for this functor or 0 if the number is flexible.
     */
    override fun getArgLength(): Int {
        return 3
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
    override fun bodyCall(args: Array<Node>, length: Int, context: RuleContext): Boolean {
        val values: MutableList<Node> = ArrayList()

        val a0 = getArg(0, args, context)
        val a1 = getArg(1, args, context)

        val iterator = context.find(a0, a1, null)

        while (iterator.hasNext()) {
            val currentObject = iterator.next().getObject()

            var equals = false
            for (value in values) {
                if (value.matches(currentObject)) {
                    equals = true
                    break
                }
            }

            if (!equals) {
                values.add(currentObject)
            }
        }

        return context.env.bind(args[2], Util.makeIntNode(values.size))
    }
}