package view;

public interface View {
    void write(String massage);

    String read();

    public void printError(Exception e);
}
