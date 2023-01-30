package builtins.util

import org.apache.jena.graph.Node
import org.apache.jena.reasoner.rulesys.Builtin
import org.apache.jena.reasoner.rulesys.BuiltinException
import org.apache.jena.reasoner.rulesys.RuleContext

/**
 * Return the appropriate lexical form of a node
 */
fun Builtin.lex(node: Node, context: RuleContext): String {
    return when {
        node.isBlank -> node.blankNodeLabel
        node.isURI -> node.uri
        node.isLiteral -> node.literalLexicalForm
        else -> throw BuiltinException(this, context, "Illegal node type: $node")
    }
}