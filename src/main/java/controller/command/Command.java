package controller.command;

public interface Command {
    //проверка можна ли обработать строку
    boolean canProcess(String command);

    //обработать строку
    void process(String command);

    //формат команды
    default String formatCommand() {
        return null;
    }

    //описание команды
    default String describeCommand() {
        return null;
    }
}
