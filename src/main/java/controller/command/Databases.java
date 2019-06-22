package controller.command;

import model.DBManager;
import view.View;

import java.util.Set;

public class Databases implements Command {
    private DBManager dbManager;
    private View view;

    public Databases(DBManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("databases");
    }

    @Override
    public void process(String command) {
        Set<String> set = dbManager.getDatabases();
        if (!set.isEmpty()) {
            String bases = set.toString();
            String result = bases.substring(1, bases.length() - 1);
            view.write(String.format("Существующие базы данных: %s", result));
        } else {
            view.write("Базы данных отсутствуют.");
        }
    }

    @Override
    public String formatCommand() {
        return "databases";
    }

    @Override
    public String describeCommand() {
        return "Получение списка баз данных.";
    }
}
