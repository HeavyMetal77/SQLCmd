package controller.command;

import model.DBManager;
import model.DataSet;
import view.PrintTable;
import view.View;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Find implements Command {
    private DBManager dbManager;
    private View view;

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
        if (commandWithParam.length != 2) {
            view.write("Количество параметров не соответствует шаблону!");
            return;
        }
        String nameTable = commandWithParam[1];
        try {
            //список с размерами (шириной) каждого атрибута таблицы
            ArrayList<Integer> arrWidthAttribute = dbManager.getWidthAtribute(nameTable);
            //количество кортежей таблицы //TODO потом посмотреть - возможно достаточно датасетов
            int tableSize = dbManager.getSize(nameTable);
            //список датасетов таблицы
            List<DataSet> dataSets = dbManager.getDataSetTable(nameTable);
            //коллекция атрибутов (названий колонок) таблицы
            Set<String> attributes = dbManager.getAtribute(nameTable);

            //вывод всей таблицы
            PrintTable printTable = new PrintTable(view);
            printTable.printTable(arrWidthAttribute, tableSize, attributes, dataSets);

        } catch (SQLException e) {
            view.write(String.format(e.getMessage()));
        }
    }
}
