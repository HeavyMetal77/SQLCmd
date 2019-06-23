package controller.command;

import model.DBManager;
import model.DataSet;
import model.DataSetImpl;
import view.PrintTable;
import view.View;

import java.util.List;
import java.util.Set;

public class Update implements Command {
    private DBManager dbManager;
    private View view;

    public Update(DBManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("update|");
    }

    @Override
    public void process(String command) {
        //получаем массив параметров команды
        String[] commandWithParam = command.split("[|]");
        //порверяем достаточно ли параметров в команде
        if (commandWithParam.length < 6 || commandWithParam.length % 2 == 1) {
            view.write("Количество параметров не соответствует шаблону!");
            return;
        }
        String nameTable = commandWithParam[1];
        String column1 = commandWithParam[2];
        String value1 = commandWithParam[3];

        int lengthData = (commandWithParam.length - 4) / 2;
        DataSet dataSets = new DataSetImpl();
        for (int i = 0, j = 4; i < lengthData; i++, j += 2) {
            dataSets.put(commandWithParam[j], commandWithParam[j + 1]);
        }
        dbManager.update(nameTable, column1, value1, dataSets);
        view.write("Данные успешно обновлены!");
        //Согласно ТЗ - Формат вывода: табличный
        //список с размерами (шириной) каждого атрибута таблицы
        List<Integer> arrWidthAttribute = dbManager.getWidthAtribute(nameTable);
        //список датасетов таблицы
        List<DataSet> dataSets2 = dbManager.getDataSetTable(nameTable);
        //коллекция атрибутов (названий колонок) таблицы
        Set<String> attributes = dbManager.getAtribute(nameTable);
        //вывод всей таблицы
        PrintTable printTable = new PrintTable(view);
        printTable.printTable(arrWidthAttribute, attributes, dataSets2);
    }

    @Override
    public String formatCommand() {
        return "update|tableName|column1|value1|column2|value2";
    }

    @Override
    public String describeCommand() {
        return "Команда обновит запись, установив значение column2 = value2, " +
                "для которой соблюдается условие column1 = value1";
    }
}