package compiler.operators;

import compiler.Operator;
import org.checkerframework.checker.units.qual.C;
import util.CompilationResult;
import util.DataType;
import util.JenaUtil;
import util.NamingManager;

import java.util.ArrayList;
import java.util.List;

public class GetExtreme extends BaseOperator {

    private final String varName;
    private final String extremeVarName;

    /**
     * Шаблон правила выбора экстремального
     */
    private static final String EXTREME_PATTER = "[<rulePart>->drop(0)]";

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
        String empty3 = NamingManager.genVarName();

        // ---------------- Генерируем правило, помечающее объекты множества --------------

        // Флаг, указывающий на объекты множества
        String flag = NamingManager.genFlagName();
        // Skolem name
        String skolemName = NamingManager.genVarName();

        // Собираем правило, помечающее классы
        String part = compiledArg0.rulePart();

        String rule = JenaUtil.genRule(part, skolemName, skolemName, flag, JenaUtil.genRuleVar(varName));

        // ---------------- Генерируем правило, помечающее потенциальный экстремум --------------

        // Флаг цикла
        String cycleFlagName = NamingManager.genFlagName();
        // Переменная цикла
        String cycleVar = NamingManager.genVarName();

        // Собираем правило, организующее цикл
        String cyclePart = "noValue(" + empty1 + "," + cycleFlagName + ")" +
                JenaUtil.genTriple(empty2, flag, cycleVar);
        String cycleRule = JenaUtil.genRule(cyclePart, skolemName, skolemName, cycleFlagName, cycleVar);

        // ---------------- Генерируем правило, проверяющее экстремум --------------

        // Инициализируем потенциальный экстремум
        String extremeVar = NamingManager.genVarName();
        String filterPart = JenaUtil.genTriple(empty1, cycleFlagName, extremeVar);
        filterPart += "bind(" + extremeVar + "," + JenaUtil.genRuleVar(extremeVarName) + ")";

        // Инициализируем переменную
        String var = NamingManager.genVarName();
        filterPart += JenaUtil.genTriple(empty2, flag, var);
        filterPart += "bind(" + var + "," + JenaUtil.genRuleVar(varName) + ")";

        filterPart += compiledArg1.rulePart();

        String filterRule = EXTREME_PATTER;
        filterRule = filterRule.replace("<rulePart>", filterPart);

        value = NamingManager.genVarName();

        // Добавляем в основное правило
        rulePart = JenaUtil.genTriple(empty1, cycleFlagName, value);

        // Собираем правила
        completedRules = compiledArg0.completedRules() +
                compiledArg1.completedRules() +
                rule + cycleRule + filterRule + JenaUtil.PAUSE_MARK;

        // TODO: проверить количество объектов?

        usedObjects = List.of(compiledArg0.value());

        return new CompilationResult(value, rulePart, completedRules);
    }
}
