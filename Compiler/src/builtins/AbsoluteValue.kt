package builtins

import org.apache.jena.graph.Node
import org.apache.jena.reasoner.rulesys.RuleContext
import org.apache.jena.reasoner.rulesys.Util
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin
import kotlin.math.abs

class AbsoluteValue : BaseBuiltin() {

    /**
     * Return a name for this builtin, normally this will be the name of the
     * functor that will be used to invoke it.
     */
    override fun getName() = "absoluteValue"

    /**
     * Return the expected number of arguments for this functor or 0 if the number is flexible.
     */
    override fun getArgLength() = 2

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
        val arg = getArg(0, args, context)
        return if (Util.isNumeric(arg)) {
            try {
                val value = arg.literalValue.toString().toInt()
                val newValue = Util.makeIntNode(abs(value))
                context.env.bind(args[1], newValue)
            } catch (_: NumberFormatException) {
                try {
                    val value = arg.literalValue.toString().toDouble()
                    val newValue = Util.makeDoubleNode(abs(value))
                    context.env.bind(args[1], newValue)
                } catch (_: NumberFormatException) {
                    false
                }
            }
        } else {
            false
        }
    }
}