package controller.command;

import model.DBManager;
import model.DataSet;
import view.PrintTable;
import view.View;

import java.util.List;
import java.util.Set;

public class Delete implements Command {
    private DBManager dbManager;
    private View view;
    private static final int LENGTH_PARAM = 4;

    public Delete(DBManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("delete|");
    }

    @Override
    public void process(String command) {
        String[] commandWithParam = command.split("[|]");
        if (commandWithParam.length != LENGTH_PARAM) {
            view.write("Количество параметров не соответствует шаблону!");
            return;
        }
        String nameTable = commandWithParam[1];
        String columnName = commandWithParam[2];
        String columnValue = commandWithParam[3];
        List<DataSet> dataSetsBefore = dbManager.getDataSetTable(nameTable);
        dbManager.delete(nameTable, columnName, columnValue);
        List<DataSet> dataSetsAfter = dbManager.getDataSetTable(nameTable);
        if (dataSetsBefore.size() - 1 == dataSetsAfter.size()) {
            view.write("Запись в таблице " + nameTable + " со значением " +
                    columnName + " = " + columnValue + "  была успешно удалена!");
        } else if (dataSetsBefore.size() == dataSetsAfter.size()) {
            view.write("Удаление не произведено!");
        }
        //список с размерами (шириной) каждого атрибута таблицы
        List<Integer> arrWidthAttribute = dbManager.getWidthAtribute(nameTable);
        //коллекция атрибутов (названий колонок) таблицы
        Set<String> attributes = dbManager.getAtribute(nameTable);
        //вывод всей таблицы
        PrintTable printTable = new PrintTable(view);
        printTable.printTable(arrWidthAttribute, attributes, dataSetsAfter);
    }

    @Override
    public String formatCommand() {
        return "delete|tableName|columnName|columnValue";
    }

    @Override
    public String describeCommand() {
        return "Удаление записи в таблице 'tableName', где columnName = columnValue";
    }
}