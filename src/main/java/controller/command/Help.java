package controller.command;

import view.View;

public class Help implements Command {
    private View view;

    public Help(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("help");
    }

    @Override
    public void process(String command) {
        view.write("Существующие команды:");

        view.write("\tconnect|database|user|password - ");
        view.write("\t\tсоединение с базой данных");

        view.write("\ttables - ");
        view.write("\t\tвывод списка всех таблиц");

        view.write("\tclear|tableName - ");
        view.write("\t\tочистка содержимого таблицы tableName");

        view.write("\tfind|tableName - ");
        view.write("\t\tвывод содержимого таблицы tableName");

        view.write("\tdrop|tableName - ");
        view.write("\t\tудалить таблицу tableName");

        view.write("\tcreateTable|tableName|column1|column2|...|columnN - ");
        view.write("\t\tсоздать таблицу tableName с колонками column1...columnN ");

        view.write("\tinsert|tableName|column1|value1|column2|value2|... - ");
        view.write("\t\tвставить данные в таблицу tableName: column1|value1 ....");

        view.write("\texit - ");
        view.write("\t\tвыход из программы");
    }
}
