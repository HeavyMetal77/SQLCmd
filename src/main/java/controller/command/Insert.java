package controller.command;

import model.DBManager;
import model.DataSet;
import view.View;

import java.sql.SQLException;

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
        if (commandWithParam.length >= 4 && commandWithParam.length % 2 == 0) {
            String nameTable = commandWithParam[1];
            //рассчитываем длинну массива DataSet из полученных параметров (минус 2 элемента - команда и имя таблицы)
            int lengthData = (commandWithParam.length - 2) / 2;
            DataSet dataSet = new DataSet();
            for (int i = 0, j = 2; i < lengthData; i++, j += 2) {
                dataSet.put(commandWithParam[j], commandWithParam[j + 1]);
            }
            String insertRequestSql = getRequest(nameTable, dataSet);
            try {
                dbManager.insert(insertRequestSql, dataSet);
                view.write("Данные успешно вставлены!");
            } catch (SQLException e) {
                throw new RuntimeException("Данные не вставлены!");
            }
        } else {
            throw new RuntimeException("Недостаточно параметров!");
        }
    }

    private String getRequest(String nameTable, DataSet dataSet) {
        //строка запроса, содержащая атрибуты таблицы
        String dataRequestColumn = "";
        //строка запроса, содержащая значения таблицы
        String dataRequestValue = "";
        //получаем размер массива датасет
        int lengthArrData = dataSet.getNames().length;

        //формируем запрос для атрибутов таблицы
        for (int i = 0; i < lengthArrData; i++) {
            dataRequestColumn += dataSet.getNames()[i] + ", ";
        }
        //удаляем последнюю запятую и пробел
        dataRequestColumn = dataRequestColumn.substring(0, dataRequestColumn.length() - 2);

        //формируем запрос для значений кортежей таблицы
        for (int i = 0; i < lengthArrData; i++) {
            dataRequestValue += "'" + dataSet.getValues()[i] + "'" + ", ";
        }
        //удаляем последнюю запятую и пробел
        dataRequestValue = dataRequestValue.substring(0, dataRequestValue.length() - 2);
        //пример запроса INSERT INTO nameTable (column1, column2, ...) VALUES(value1, value2, ...);
        String insertRequestSql = "INSERT INTO " + nameTable + " (" + dataRequestColumn + ")"
                + " VALUES (" + dataRequestValue + ")";
        return insertRequestSql;
    }
}
