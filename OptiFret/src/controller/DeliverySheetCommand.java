package controller;

public interface DeliverySheetCommand {

    void execute();

    void undo();
}
