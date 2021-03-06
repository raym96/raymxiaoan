/*
 * 
 */
package system;

import java.util.ArrayList;

import restaurant.Meal;


/**
 * An asynchronous update interface for receiving notifications
 * about SpecialOffer information as the SpecialOffer is constructed.
 * 
 * @author He Xiaoan
 * @author Ji Raymond
 */
public interface SpecialOfferObserver{
	
	/**
	 * This method is called when information about an SpecialOffer
	 * which was previously requested using an asynchronous
	 * interface becomes available.
	 *
	 * @param meal the meal
	 */
	public void updateNewOffer(Meal meal);
}
