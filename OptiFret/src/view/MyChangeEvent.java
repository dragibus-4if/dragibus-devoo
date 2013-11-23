/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.EventObject;

/**
 *
 * @author Sylvain
 */
public class MyChangeEvent extends EventObject {
  // This event definition is stateless but you could always
  // add other information here.
  public MyChangeEvent(Object source) {
    super(source);
  }
}
