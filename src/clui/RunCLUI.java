package clui;

import java.util.Scanner;

import user.model.Courier;
import user.model.MyFoodora;

/**
 * The Class CLUI.
 * @author He Xiaoan
 * @author Ji Raymond
 */
public class RunCLUI {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyFoodora.reset();
		InitialScenario.load("my_foodora.ini");
		
		CommandProcessor cmd = new CommandProcessor();
		cmd.start();
		
	}
}

