package controller.command;

import model.DBManager;
import model.DataSet;
import model.DataSetImpl;
import view.View;

import java.sql.SQLException;

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
        //0-й элемент - непосредственно команда
        //1-й элемент - имя таблицы nameTable
        //порверяем достаточно ли параметров в команде
        if (commandWithParam.length >= 4 && commandWithParam.length % 2 == 0) {
            String nameTable = commandWithParam[1];
            //рассчитываем длинну массива DataSet из полученных параметров
            //минус 2 элемента - команда и имя таблицы)
            int lengthData = (commandWithParam.length - 2) / 2;
            DataSet dataSets = new DataSetImpl();
            for (int i = 0, j = 2; i < lengthData; i++, j += 2) {
                dataSets.put(commandWithParam[j], commandWithParam[j + 1]);
            }
            try {
                dbManager.update(nameTable, dataSets);
                view.write("Данные успешно обновлены!");
            } catch (SQLException e) {
                throw new RuntimeException("Данные не обновлены!");
            }
        } else {
            throw new RuntimeException("Недостаточно параметров!");
        }
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
