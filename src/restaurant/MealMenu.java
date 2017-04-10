/*
 * 
 */
package restaurant;

import java.text.DecimalFormat;
import java.util.ArrayList;

import exceptions.MealNotFoundException;
import exceptions.NameAlreadyExistsException;
import user.model.Restaurant;


/**
 * The Class MealMenu.
 * @author He Xiaoan
 * @author Ji Raymond
 */
public class MealMenu {

	/** The meals. */
	private ArrayList<Meal> meals;
	
	private Restaurant restaurant;
	
	/**
	 * Instantiates a new meal menu.
	 *
	 * @param discount_factor the discount factor
	 */
	public MealMenu(Restaurant restaurant) {
		super();
		meals = new ArrayList<Meal>();
		this.restaurant = restaurant;
	}
	
	
	/**
	 * Adds the meal.
	 *
	 * @param meal the meal
	 * @throws NameAlreadyExistsException the name already exists exception
	 */
	public void addMeal(Meal meal) throws NameAlreadyExistsException{
		if (hasMeal(meal.getName())){
			throw new NameAlreadyExistsException(meal.getName());
		}
		meal.setRestaurant(getRestaurant());
		meals.add(meal);
	}
	
	/**
	 * Removes the meal.
	 *
	 * @param mealName the meal name
	 * @throws MealNotFoundException the meal not found exception
	 */
	public void removeMeal(String mealName) throws MealNotFoundException{
		int count = 0;
		for(int i=0; i<meals.size(); i++){
			if( meals.get(i).getName().equalsIgnoreCase(mealName)){
				meals.remove(i);
				count++;
			}
		}
		if (count==0){
			throw new MealNotFoundException(mealName);
		}
	}
	
	/**
	 * Checks for meal.
	 *
	 * @param mealName the meal name
	 * @return true, if successful
	 */
	public boolean hasMeal(String mealName){
		for (Meal m:getMeals()){
			if (m.getName().equalsIgnoreCase(mealName)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the meals.
	 *
	 * @return the meals
	 */
	public ArrayList<Meal> getMeals(){
		return meals;
	}
	
	
	
	public Restaurant getRestaurant() {
		return restaurant;
	}


	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}


	/**
	 * Display.
	 */
	public void display(){
		for (Meal meal:meals){
				System.out.println(meal);
		}
	}
	
}
