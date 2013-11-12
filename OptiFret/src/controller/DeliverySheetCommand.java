/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

/**
 *
 * @author jmcomets
 */
interface DeliverySheetCommand {
    public void execute();
    public void undo();
}
