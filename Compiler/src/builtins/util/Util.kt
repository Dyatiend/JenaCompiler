package builtins.util

import builtins.*
import org.apache.jena.reasoner.rulesys.BuiltinRegistry

/**
 * Регистрирует все кастомные builtin
 */
fun registerAllCustomBuiltin() {
    BuiltinRegistry.theRegistry.register(AbsoluteValue())
    BuiltinRegistry.theRegistry.register(Bind())
    BuiltinRegistry.theRegistry.register(CountValues())
    BuiltinRegistry.theRegistry.register(MakeUniqueID())
    BuiltinRegistry.theRegistry.register(PartialScaleDistance())
    BuiltinRegistry.theRegistry.register(StartsWith())
}