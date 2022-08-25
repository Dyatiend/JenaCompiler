package util;

public class JenaUtil {
    // Постоянные элементы
    public static final String VAR_LINK = "&var&";
    public static final String PAUSE_MARK = "<pause>";

    // Префиксы
    public static final String POAS_PREF = "poas";
    public static final String XSD_PREF = "xsd";
    public static final String RDF_PREF = "rdf";

    public static final String POAS_PREF_LINK = "http://www.vstu.ru/poas/code#";
    public static final String XSD_PREF_LINK = "http://www.w3.org/2001/XMLSchema#";
    public static final String RDF_PREF_LINK = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

    // Шаблоны правил
    public static final String TRIPLE_PATTERN = "(<subj> <pred> <obj>)";

    public static final String RULE_PATTERN = "[<rulePart>makeSkolem(<skolemArgs>)->(<skolemName> <resFlagName> <resVarName>)]";
    public static final String BOOLEAN_RULE_PATTERN = "[<rulePart>makeSkolem(<skolemArgs>)->(<skolemName> <resFlagName> 1)]";

    // Методы для заполнения шаблонов
    public static String genStingVal(String value) {
        return "\"" + value + "\"^^" + XSD_PREF + ":string";
    }
    public static String genBooleanVal(String value) {
        return "\"" + value + "\"^^" + XSD_PREF + ":boolean";
    }
    public static String genIntegerVal(String value) {
        return "\"" + value + "\"^^" + XSD_PREF + ":integer";
    }
    public static String genDoubleVal(String value) {
        return "\"" + value + "\"^^" + XSD_PREF + ":double";
    }

    public static String genRuleLink(String pref, String obj) { return pref + ":" + obj; }
    public static String genRuleVar(String name) { return "?" + name; }

    public static String genTriple(String subj, String pred, String obj) {
        String res = TRIPLE_PATTERN;
        res = res.replace("<subj>", subj);
        res = res.replace("<pred>", pred);
        res = res.replace("<obj>", obj);
        return res;
    }

    public static String genRule(String rulePart, String skolemArgs, String skolemName, String resFlagName, String resVarName) {
        String rule = RULE_PATTERN;

        rule = rule.replace("<rulePart>", rulePart);
        rule = rule.replace("<skolemArgs>", skolemArgs);
        rule = rule.replace("<skolemName>", skolemName);
        rule = rule.replace("<resFlagName>", resFlagName);
        rule = rule.replace("<resVarName>", resVarName);

        return rule;
    }

    public static String genBooleanRule(String rulePart, String skolemArgs, String skolemName, String resFlagName) {
        String rule = BOOLEAN_RULE_PATTERN;

        rule = rule.replace("<rulePart>", rulePart);
        rule = rule.replace("<skolemArgs>", skolemArgs);
        rule = rule.replace("<skolemName>", skolemName);
        rule = rule.replace("<resFlagName>", resFlagName);

        return rule;
    }
}
