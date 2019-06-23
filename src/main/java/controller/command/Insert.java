package controller.command;

import model.DBManager;
import model.DataSet;
import model.DataSetImpl;
import view.View;

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
        dbManager.insert(nameTable, dataSet);
        view.write("Данные успешно вставлены!");
    }

    @Override
    public String formatCommand() {
        return "insert|tableName|column1|value1|column2|value2|...";
    }

    @Override
    public String describeCommand() {
        return "Вставить данные в таблицу 'tableName': 'column1|value1|column2|value2'...";
    }
}
