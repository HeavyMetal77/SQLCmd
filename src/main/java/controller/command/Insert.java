package controller.command;

import model.DBManager;
import model.DataSet;
import model.DataSetImpl;
import view.View;

import java.sql.SQLException;
import java.util.Set;

public class Insert implements Command {
    private DBManager dbManager;
    private View view;

    public Insert(DBManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("insert|");
    }

    @Override
    public void process(String command) {
        //получаем массив параметров команды
        String[] commandWithParam = command.split("[|]");
        //0-й элемент - непосредственно команда
        //1-й элемент - имя таблицы nameTable
        //порверяем достаточно ли параметров в команде
        if (commandWithParam.length < 4 && commandWithParam.length % 2 == 1) {
            view.write("Количество параметров не соответствует шаблону!");
            return;
        }
            String nameTable = commandWithParam[1];
            //рассчитываем длинну массива DataSet из полученных параметров (минус 2 элемента - команда и имя таблицы)
            int lengthData = (commandWithParam.length - 2) / 2;
            DataSet dataSet = new DataSetImpl();
            for (int i = 0, j = 2; i < lengthData; i++, j += 2) {
                dataSet.put(commandWithParam[j], commandWithParam[j + 1]);
            }
            String insertRequestSql = getRequest(nameTable, dataSet);
            try {
                dbManager.insert(insertRequestSql);
                view.write("Данные успешно вставлены!");
            } catch (SQLException e) {
                throw new RuntimeException(String.format("Данные в таблицу %s не вставлены, по причине: %s", nameTable, e.getMessage()));
            }
    }

    @Override
    public String formatCommand() {
        return "insert|tableName|column1|value1|column2|value2|...";
    }

    @Override
    public String describeCommand() {
        return "Вставить данные в таблицу 'tableName': 'column1|value1|column2|value2'...";
    }

    private String getRequest(String nameTable, DataSet dataSet) {
        //строка запроса, содержащая атрибуты таблицы
        String dataRequestColumn = "";
        //строка запроса, содержащая значения таблицы
        String dataRequestValue = "";
        //формируем запрос для атрибутов таблицы и значений кортежей таблицы
        Set<String> columns = dataSet.getNames();
        for (String name : columns) {
            dataRequestColumn += name + ", ";
            dataRequestValue += "'" + dataSet.get(name) + "'" + ", ";
        }
        //удаляем последнюю запятую и пробел
        dataRequestColumn = dataRequestColumn.substring(0, dataRequestColumn.length() - 2);
        //удаляем последнюю запятую и пробел
        dataRequestValue = dataRequestValue.substring(0, dataRequestValue.length() - 2);
        //пример запроса INSERT INTO nameTable (column1, column2, ...) VALUES(value1, value2, ...);
        String insertRequestSql = "INSERT INTO " + nameTable + " (" + dataRequestColumn + ")"
                + " VALUES (" + dataRequestValue + ")";
        return insertRequestSql;
    }
}
