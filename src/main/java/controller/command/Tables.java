package controller.command;

import model.DBManager;
import view.View;

import java.sql.SQLException;
import java.util.Set;

public class Tables implements Command {

    private View view;
    private DBManager dbManager;

    public Tables(DBManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("tables");
    }

    @Override
    public void process(String command) {
        try {
            Set<String> set = dbManager.getTables();
            if (!set.isEmpty()) {
                String tables = set.toString();
                String result = tables.substring(1, tables.length() - 1);
                view.write(String.format("База данных содержит таблицы: %s", result));
            } else {
                view.write("В базе данных таблицы отсутствуют");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    @Override
    public String formatCommand() {
        return "tables";
    }

    @Override
    public String describeCommand() {
        return "Вывод списка всех таблиц";
    }
}
