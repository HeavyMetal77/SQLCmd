package controller.command;

import model.DBManager;
import view.View;

public class Connect implements Command {
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
        while (true) {
            try {
                String[] data = command.split("[|]");
                if (data.length != 4) {
                    throw new IllegalArgumentException ("Неверное количество параметров: ожидается 4, введено " + data.length);
                }
                String database = data[1];
                String user = data[2];
                String password = data[3];

                dbManager.connect(database, user, password);
                view.write("Подключение к базе выполнено успешно!");
                break;
            } catch (Exception e) {
                printError(e);
            }
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
