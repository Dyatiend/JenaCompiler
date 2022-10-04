package compiler;

import compiler.operators.*;
import compiler.values.*;
import compiler.values.ClassValue;
import dictionaries.RelationshipsDictionary;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Оператор
 */
public interface Operator {

    /**
     * Список аргументов
     * @return Список аргументов
     */
    default List<Operator> args() { return new ArrayList<>(); }

    /**
     * Список объектов, использованных в правиле
     * @return Список объектов, использованных в правиле
     */
    default List<String> objectsUsedInRule() { return new ArrayList<>(); }

    /**
     * Получить аргумент
     * @param index Индекс аргумента
     * @return Аргумент
     */
    default Operator arg(int index) { return args().get(index); }

    // FIXME?: придумать более удобный способ задания нескольких комбинаций типов входных данных?
    /**
     * Список типов данных аргументов
     * @return Список типов данных аргументов
     */
    List<List<DataType>> argsDataTypes();

    /**
     * Является ли количество аргументов бесконечным
     * @return true - если является, иначе - false
     */
    default Boolean isArgsCountUnlimited() { return false; }

    /**
     * Тип данных оператора
     * @return Тип данных оператора
     */
    DataType resultDataType();

    // TODO: валидация переменных, вводимых операторами
    // TODO: таблица переменных, вводимых операторами и их валидация
    // TODO?: оптимизация пауз?
    // TODO?: оптимизация правил? (удаление одинаковых строк)
    /**
     * Скомпилировать выражение
     * @return Правила для вычисления выражения и имя предиката для чтения результата (если есть)
     */
    default CompilationResult compileExpression() {
        // Компилируем оператор
        CompilationResult result = compile();

        // Добавляем пустой триплет в граф, чтобы он никогда небыл пустым
        // FIXME: придумать другой способ
        String tmp = NamingManager.genVarName();
        String rules = JenaUtil.genBooleanRule("", tmp, tmp, NamingManager.genPredName());
        rules += JenaUtil.PAUSE_MARK;

        // Добавляем вспомогательные правила, если нужно
        rules += RelationshipsDictionary.isLinerScaleUsed() ?
                RelationshipsDictionary.auxiliaryLinerScaleRules() + result.completedRules() :
                result.completedRules();

        // Генерируем имена
        String skolemName = NamingManager.genVarName();
        String resPredName = resultDataType() != null ? NamingManager.genPredName() : "";
        String resVarName = result.value();

        // Если есть незаконченное правило
        if(!result.ruleHead().isEmpty() && resultDataType() != null) {
            // Генерируем правило
            String rule;
            if (resultDataType() == DataType.BOOLEAN)
                rule = JenaUtil.genBooleanRule(result.ruleHead(), skolemName, skolemName, resPredName);
            else
                rule = JenaUtil.genRule(result.ruleHead(), skolemName, skolemName, resPredName, resVarName);

            // Добавляем правило к остальным
            rules += rule;
        }

        // Удаляем префикс из результата FIXME: может тогда лучше вообще их не использовать?
        // TODO: добавлять сюда удаление префиксов при добавлении нового префикса не удобно, надо  придумать фикс
        resPredName = resPredName.replace(JenaUtil.POAS_PREF + ":", JenaUtil.POAS_PREF_URL);
        resPredName = resPredName.replace(JenaUtil.XSD_PREF + ":", JenaUtil.XSD_PREF_URL);
        resPredName = resPredName.replace(JenaUtil.RDF_PREF + ":", JenaUtil.RDF_PREF_URL);

        return new CompilationResult(resPredName, "", rules);
    }

    /**
     * Скомпилировать оператор
     * @return Правила для вычисления выражения, часть правила для проверки и имя предиката для чтения результата (если есть)
     */
    CompilationResult compile();

    /**
     * Создает дерево из XML файла
     * @param path Путь к файлу
     * @return Дерево выражения
     */
    static Operator fromXML(String path) throws IllegalAccessException {
        try {
            // Создается билдер дерева
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            // Создается дерево DOM документа из файла
            Document document = documentBuilder.parse(path);

            // Получаем корневой элемент
            Node xml = document.getDocumentElement();

            // Корень выражения
            Node root = null;

            // Ищем корень
            NodeList childNodes = xml.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); ++i) {
                Node child = childNodes.item(i);
                if(child.getNodeType() == Node.ELEMENT_NODE) {
                    if (root == null) {
                        root = child;
                    }
                    else {
                        throw new IllegalAccessException("Выражение должно иметь один корневой узел");
                    }
                }
            }
            if(root == null) {
                throw new IllegalAccessException("Не найден корневой узел выражения");
            }

            // Строим дерево
            return Operator.build(root);
        } catch (ParserConfigurationException | IOException | SAXException ex) {
            ex.printStackTrace(System.out);
        }
        return null;
    }

    /**
     * Создает оператор из узла XML
     * @param node XML узел
     * @return Оператор
     */
    private static Operator build(Node node) {
        Operator operator;

        if(node.getNodeName().equals("block")) {
            switch (node.getAttributes().getNamedItem("type").getNodeValue()) {
                case "object" -> {
                    String name = node.getFirstChild().getTextContent();
                    return new ObjectValue(name);
                }
                case "class" -> {
                    String name = node.getFirstChild().getTextContent();
                    return new ClassValue(name);
                }
                case "property" -> {
                    String name = node.getFirstChild().getTextContent();
                    return new PropertyValue(name);
                }
                case "relationship" -> {
                    String name = node.getFirstChild().getTextContent();
                    return new RelationshipValue(name);
                }
                case "boolean" -> {
                    String val = node.getFirstChild().getTextContent();
                    return new BooleanValue(val.equals("TRUE"));
                }
                case "integer" -> {
                    String val = node.getFirstChild().getTextContent();
                    return new IntegerValue(Integer.valueOf(val));
                }
                case "double" -> {
                    String val = node.getFirstChild().getTextContent();
                    return new DoubleValue(Double.valueOf(val));
                }
                case "comparison_result" -> {
                    String val = node.getFirstChild().getTextContent();
                    return new ComparisonResultValue(ComparisonResult.valueOf(val.toUpperCase()));
                }
                case "ref_to_decision_tree_var" -> {
                    String name = node.getFirstChild().getTextContent();
                    return new DecisionTreeVarValue(name);
                }
                case "get_class" -> {
                    return new GetClass(List.of(build(node.getFirstChild().getFirstChild())));
                }
                case "get_property_value" -> {
                    return new GetPropertyValue(List.of(
                            build(node.getFirstChild().getFirstChild()),
                            build(node.getLastChild().getFirstChild())
                    ));
                }
                case "get_relationship_object" -> {
                    return new GetByRelationship(List.of(
                            build(node.getFirstChild().getFirstChild()),
                            build(node.getLastChild().getFirstChild())
                    ));
                }
                case "get_condition_object" -> {
                    return new GetByCondition(List.of(
                            build(node.getLastChild().getFirstChild())),
                            node.getFirstChild().getTextContent()
                    );
                }
                case "get_extr_object_condition_and_relation" -> {
                    // TODO
                }
                case "assign_value_to_property" -> {
                    return new Assign(List.of(
                            build(node.getChildNodes().item(0).getFirstChild()),
                            build(node.getChildNodes().item(1).getFirstChild()),
                            build(node.getChildNodes().item(2).getFirstChild())
                    ));
                }
                case "assign_value_to_variable_decision_tree" -> {
                    return new Assign(List.of(
                            build(node.getFirstChild().getFirstChild()),
                            build(node.getLastChild().getFirstChild())
                    ));
                }
                case "check_object_class" -> {
                    return new CheckClass(List.of(
                            build(node.getFirstChild().getFirstChild()),
                            build(node.getLastChild().getFirstChild())
                    ));
                }
                case "check_value_of_property" -> {
                    return new CheckPropertyValue(List.of(
                            build(node.getChildNodes().item(0).getFirstChild()),
                            build(node.getChildNodes().item(1).getFirstChild()),
                            build(node.getChildNodes().item(2).getFirstChild())
                    ));
                }
                case "check_relationship" -> {
                    ArrayList<Operator> args = new ArrayList<>();
                    NodeList childNodes = node.getChildNodes();
                    for (int i = 0; i < childNodes.getLength(); ++i) {
                        Node child = childNodes.item(i);
                        if(child.getNodeType() == Node.ELEMENT_NODE) {
                            args.add(build(child.getFirstChild()));
                        }
                    }
                    return new CheckRelationship(args);
                }
                case "and" -> {
                    return new LogicalAnd(List.of(
                            build(node.getFirstChild().getFirstChild()),
                            build(node.getLastChild().getFirstChild())
                    ));
                }
                case "or" -> {
                    return new LogicalOr(List.of(
                            build(node.getFirstChild().getFirstChild()),
                            build(node.getLastChild().getFirstChild())
                    ));
                }
                case "not" -> {
                    return new LogicalNot(List.of(
                            build(node.getFirstChild().getFirstChild())
                    ));
                }
                case "comparison" -> {
                    return new CompareWithComparisonOperator(List.of(
                            build(node.getChildNodes().item(1).getFirstChild()),
                            build(node.getChildNodes().item(2).getFirstChild())),
                            CompareWithComparisonOperator.ComparisonOperator.valueOf(node.getChildNodes().item(0).getTextContent())
                    );
                }
                case "three_digit_comparison" -> {
                    return new Compare(List.of(
                            build(node.getFirstChild().getFirstChild()),
                            build(node.getLastChild().getFirstChild())
                    ));
                }
                case "quantifier_of_existence" -> {
                    return new ExistenceQuantifier(List.of(
                            build(node.getLastChild().getFirstChild())),
                            node.getFirstChild().getTextContent()
                    );
                }
                case "quantifier_of_generality" -> {
                    return new ForAllQuantifier(List.of(
                            build(node.getChildNodes().item(1).getFirstChild()),
                            build(node.getChildNodes().item(2).getFirstChild())),
                            node.getChildNodes().item(0).getTextContent()
                    );
                }
            }
        }

        return null;
    }
}
