package controller.command;

import model.DBManager;
import model.DataSet;
import view.PrintTable;
import view.View;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Delete implements Command {
    private DBManager dbManager;
    private View view;

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
        if (commandWithParam.length != 4) {
            view.write("Количество параметров не соответствует шаблону!");
            return;
        }
        String nameTable = commandWithParam[1];
        String columnName = commandWithParam[2];
        String columnValue = commandWithParam[3];
        try {
            dbManager.delete(nameTable, columnName, columnValue);
            view.write("Record " + nameTable + " was successfully deleted!");
            //список с размерами (шириной) каждого атрибута таблицы
            ArrayList<Integer> arrWidthAttribute = dbManager.getWidthAtribute(nameTable);
            //список датасетов таблицы
            List<DataSet> dataSets = dbManager.getDataSetTable(nameTable);
            //коллекция атрибутов (названий колонок) таблицы
            Set<String> attributes = dbManager.getAtribute(nameTable);
            //вывод всей таблицы
            PrintTable printTable = new PrintTable(view);
            printTable.printTable(arrWidthAttribute, attributes, dataSets);
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Ошибка удаления записи в таблице %s, по причине: %s", nameTable, e.getMessage()));
        }
    }

    @Override
    public String formatCommand() {
        return "delete|tableName|columnName|columnValue";
    }

    @Override
    public String describeCommand() {
        return "Удаление записи в таблице tableName, где columnName = columnValue";
    }
}