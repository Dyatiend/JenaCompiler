package builtins

import builtins.util.lex
import org.apache.jena.graph.Node
import org.apache.jena.reasoner.rulesys.RuleContext
import org.apache.jena.reasoner.rulesys.Util
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin
import kotlin.math.abs

class PartialScaleDistance : BaseBuiltin() {

    /**
     * Return a name for this builtin, normally this will be the name of the
     * functor that will be used to invoke it.
     */
    override fun getName() = "partialScaleDistance"

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

        val lex0 = lex(getArg(0, args, context), context)
        val lex1 = lex(getArg(1, args, context), context)

        return if (lex0.startsWith(lex1) || lex1.startsWith(lex0)) {
            val len0 = lex0.split(".").size
            val len1 = lex1.split(".").size
            val distance = abs(len0 - len1)

            context.env.bind(args[2], Util.makeIntNode(distance))
        } else {
            false
        }
    }
}