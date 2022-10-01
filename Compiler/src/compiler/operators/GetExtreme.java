package compiler.operators;

import compiler.Operator;
import util.CompilationResult;
import util.DataType;
import util.JenaUtil;
import util.NamingManager;

import java.util.ArrayList;
import java.util.List;

// FIXME: переделать
/**
 * Выбор экстремального объекта
 */
public class GetExtreme extends BaseOperator {

    private final String varName;
    private final String extremeVarName;

    /**
     * Шаблон правила выбора экстремального
     */
    private static final String EXTREME_PATTERN = "[<ruleHead>->drop(0)]";

    /**
     * Конструктор
     * @param args Аргументы
     */
    public GetExtreme(List<Operator> args, String varName, String extremeVarName) {
        super(args);
        this.varName = varName;
        this.extremeVarName = extremeVarName;
    }

    @Override
    public List<List<DataType>> argsDataTypes() {
        List<List<DataType>> result = new ArrayList<>();

        result.add(List.of(DataType.BOOLEAN, DataType.BOOLEAN));

        return result;
    }

    @Override
    public DataType resultDataType() {
        return DataType.OBJECT;
    }

    @Override
    public CompilationResult compile() {
        // Объявляем переменные
        String value = "";
        String rulePart = "";
        String completedRules = "";

        // Получаем аргументы
        Operator arg0 = arg(0);
        Operator arg1 = arg(1);

        // Компилируем аргументы
        CompilationResult compiledArg0 = arg0.compile();
        CompilationResult compiledArg1 = arg1.compile();

        // Пустые переменные
        String empty1 = NamingManager.genVarName();
        String empty2 = NamingManager.genVarName();

        // ---------------- Генерируем правило, помечающее объекты множества --------------

        // Флаг, указывающий на объекты множества
        String flag = NamingManager.genPredName();
        // Skolem name
        String skolemName = NamingManager.genVarName();

        // Собираем правило, помечающее объекты
        String part = compiledArg0.ruleHead();

        String rule = JenaUtil.genRule(part, skolemName, skolemName, flag, JenaUtil.genVar(varName));

        // ---------------- Генерируем правило, помечающее потенциальный экстремум --------------

        // Флаг цикла
        String cycleFlagName = NamingManager.genPredName();
        // Переменная цикла
        String cycleVar = NamingManager.genVarName();

        // Собираем правило, организующее цикл
        String cyclePart = JenaUtil.genNoValuePrim(empty1, cycleFlagName) +
                JenaUtil.genTriple(empty2, flag, cycleVar);
        String cycleRule = JenaUtil.genRule(cyclePart, skolemName, skolemName, cycleFlagName, cycleVar);

        // ---------------- Генерируем правило, проверяющее экстремум --------------

        // Инициализируем потенциальный экстремум
        String extremeVar = NamingManager.genVarName();
        String filterPart = JenaUtil.genTriple(empty1, cycleFlagName, extremeVar);
        filterPart += JenaUtil.genBindPrim(extremeVar, JenaUtil.genVar(extremeVarName));

        // Инициализируем переменную
        String var = NamingManager.genVarName();
        filterPart += JenaUtil.genTriple(empty2, flag, var);
        filterPart += JenaUtil.genBindPrim(var, JenaUtil.genVar(varName));

        filterPart += compiledArg1.ruleHead();

        String filterRule = EXTREME_PATTERN;
        filterRule = filterRule.replace("<ruleHead>", filterPart);

        value = NamingManager.genVarName();

        // Добавляем в основное правило
        rulePart = JenaUtil.genTriple(empty1, cycleFlagName, value);

        // Собираем правила
        completedRules = compiledArg0.completedRules() +
                compiledArg1.completedRules() +
                rule + cycleRule + filterRule + JenaUtil.PAUSE_MARK;

        // TODO: проверить количество объектов

        usedObjects = List.of(value);

        return new CompilationResult(value, rulePart, completedRules);
    }
}
