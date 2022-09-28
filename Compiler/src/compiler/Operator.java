package compiler;

import dictionaries.RelationshipsDictionary;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import util.DataType;
import util.JenaUtil;
import util.NamingManager;
import util.CompilationResult;

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

        // Добавляем вспомогательные правила, если нужно
        String rules = RelationshipsDictionary.isLinerScaleUsed() ?
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
    static Operator fromXML(String path) {
        try {
            // Создается билдер дерева
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            // Создается дерево DOM документа из файла
            Document document = documentBuilder.parse(path);

            // Получаем корневой элемент
            Node root = document.getDocumentElement();

            System.out.println("List of nodes:");
            System.out.println();
            Operator.build(root);
            // Просматриваем всех детей
            NodeList childNodes = root.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); ++i) {
                Node child = childNodes.item(i);

//                // Если нода не текст, то это книга - заходим внутрь
//                if (child.getNodeType() != Node.TEXT_NODE) {
//                    NodeList bookProps = book.getChildNodes();
//                    for(int j = 0; j < bookProps.getLength(); j++) {
//                        Node bookProp = bookProps.item(j);
//                        // Если нода не текст, то это один из параметров книги - печатаем
//                        if (bookProp.getNodeType() != Node.TEXT_NODE) {
//                            System.out.println(bookProp.getNodeName() + ":" + bookProp.getChildNodes().item(0).getTextContent());
//                        }
//                    }
//                    System.out.println("===========>>>>");
//                }
            }
            return null;
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
        // Просматриваем всех детей
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); ++i) {
            Node child = childNodes.item(i);

            if (child.getNodeName().equals("block")) {
                System.out.println("Type: " + child.getAttributes().getNamedItem("type").getNodeValue());
                System.out.println();
                System.out.println("Children: ");
                Operator.build(child);
            }
            else if (child.getNodeName().equals("field") || child.getNodeName().equals("value"))  {
                System.out.println("Name: " + child.getAttributes().getNamedItem("name").getNodeValue());
                System.out.println();
                System.out.println("Children: ");
                Operator.build(child);
            }
            else {
                System.out.println("Text: " + child.getNodeValue());
                System.out.println();
            }
        }
        return null;
    }
}
