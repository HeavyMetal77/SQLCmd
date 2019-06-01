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
        //порверяем достаточно ли параметров в команде
        if (commandWithParam.length >= 6 && commandWithParam.length % 2 == 0) {
            String nameTable = commandWithParam[1];
            String column1 = commandWithParam[2];
            String value1 = commandWithParam[3];

            int lengthData = (commandWithParam.length - 4) / 2;
            DataSet dataSets = new DataSetImpl();
            for (int i = 0, j = 4; i < lengthData; i++, j += 2) {
                dataSets.put(commandWithParam[j], commandWithParam[j + 1]);
            }
            try {
                dbManager.update(nameTable, column1, value1, dataSets);
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

/*
Команда обновит запись, установив значение column2 = value2, для которой соблюдается условие column1 = value1
Формат: update | tableName | column1 | value1 | column2 | value2
где: tableName - имя таблицы
column1 - имя столбца записи которое проверяется
value1 - значение которому должен соответствовать столбец column1 для обновляемой записи
column2 - имя обновляемого столбца записи
value2 - значение обновляемого столбца записи
columnN - имя n-го обновляемого столбца записи
valueN - значение n-го обновляемого столбца записи
Формат вывода: табличный, как при find со старыми значениями обновленных записей.
 */