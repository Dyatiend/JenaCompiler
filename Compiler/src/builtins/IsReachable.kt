package builtins

import org.apache.jena.graph.Node
import org.apache.jena.reasoner.rulesys.RuleContext
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin

class IsReachable : BaseBuiltin() {

    /**
     * Checked nodes
     */
    private val checked = mutableListOf<Node>()

    /**
     * Return a name for this builtin, normally this will be the name of the
     * functor that will be used to invoke it.
     */
    override fun getName() = "isReachable"

    /**
     * Return the expected number of arguments for this functor or 0 if the number is flexible.
     */
    override fun getArgLength() = 3

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
        checkArgs(length, context)

        val current = getArg(0, args, context)
        val predicate = getArg(1, args, context)
        val end = getArg(2, args, context)

        if (context.contains(current, predicate, end)) return true

        val isStart = checked.isEmpty()

        val res = context.find(current, predicate, null).asSequence().any { triple ->
            val next = triple.getObject()

            if (checked.any { it.sameValueAs(next) }) {
                false
            } else {
                checked.add(next)
                bodyCall(arrayOf(next, predicate, end), argLength, context)
            }
        }

        if (isStart) {
            checked.clear()
        }

        return res
    }
}