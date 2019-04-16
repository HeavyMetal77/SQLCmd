package controller.command;

import model.DBManager;
import view.View;

public class Connect implements Command {
    private final String COMMAND_SAMPLE = "connect|sqlcmd|sqlcmd|sqlcmd";
    private DBManager dbManager;
    private View view;

    public Connect(DBManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("connect|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("[|]");
        int countCommandParameters = COMMAND_SAMPLE.split("\\|").length;
        if (data.length != countCommandParameters) {
            view.write(String.format("Неверное количество параметров: " +
                    "ожидается: %s, введено: %s", countCommandParameters, data.length));
            return;
        }
        String database = data[1];
        String user = data[2];
        String password = data[3];

        try {
            dbManager.connect(database, user, password);
            view.write("Подключение к базе выполнено успешно!");
        } catch (Exception e) {
            printError(e);
        }

    }

    private void printError(Exception e) {
        String massage = e.getMessage();
        if (e.getCause() != null) {
            massage += " " + e.getCause().getMessage();
        }
        view.write("Ошибка! Причина: " + massage);
        view.write("Повтори попытку!");
    }
}
