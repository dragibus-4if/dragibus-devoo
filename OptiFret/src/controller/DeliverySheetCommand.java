package controller;

interface DeliverySheetCommand {
    public void execute();
    public void undo();
}
