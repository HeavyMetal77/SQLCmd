package controller.command;

import model.DBManager;
import model.DataSet;
import view.PrintTable;
import view.View;

import java.util.List;
import java.util.Set;

public class Find implements Command {
    private DBManager dbManager;
    private View view;
    private static final int LENGTH_PARAM = 2;

    public Find(DBManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("find|");
    }

    @Override
    public void process(String command) {
        String[] commandWithParam = command.split("[|]");
        if (commandWithParam.length != LENGTH_PARAM) {
            view.write("Количество параметров не соответствует шаблону!");
            return;
        }
        String nameTable = commandWithParam[1];
        //список с размерами (шириной) каждого атрибута таблицы
        List<Integer> arrWidthAttribute = dbManager.getWidthAtribute(nameTable);
        //список датасетов таблицы
        List<DataSet> dataSets = dbManager.getDataSetTable(nameTable);
        //коллекция атрибутов (названий колонок) таблицы
        Set<String> attributes = dbManager.getAtribute(nameTable);
        //вывод всей таблицы
        PrintTable printTable = new PrintTable(view);
        printTable.printTable(arrWidthAttribute, attributes, dataSets);
    }

    @Override
    public String formatCommand() {
        return "find|tableName";
    }

    @Override
    public String describeCommand() {
        return "Вывод содержимого таблицы 'tableName'";
    }
}
