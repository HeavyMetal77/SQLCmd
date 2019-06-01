package controller.command;

import model.DBManager;
import view.View;

import java.sql.SQLException;

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
        return "Удаление записи в таблице";
    }
}

/*
Команда удаляет одну или несколько записей для которых соблюдается условие column = value
Формат: delete | tableName | column | value
где: tableName - имя таблицы
Column - имя столбца записи которое проверяется
value - значение которому должен соответствовать столбец column1 для удаляемой записи
Формат вывода: табличный, как при find со старыми значениями удаляемых записей.
 */