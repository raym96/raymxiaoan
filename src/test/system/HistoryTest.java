/*
 * 
 */
package test.system;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import restaurant.FullMeal;
import restaurant.Menu;
import system.AddressPoint;
import system.History;
import system.Order;
import user.model.Courier;
import user.model.Customer;
import user.model.Restaurant;


/**
 * The Class HistoryTest.
 * @author He Xiaoan
 * @author Ji Raymond
 */
public class HistoryTest {

	/** The history. */
	private static History history = null;
	
	/** The menu. */
	private static Menu menu = new Menu();
	
	/** The standard meal order. */
	private static Order Order = null;
	
	/** The special meal order. */
	private static Order specialMealOrder = null;
	
	/** The date 1. */
	private static Date date1 = null;
	
	/** The date 2. */
	private static Date date2 = null;
	
	/** The date 3. */
	private static Date date3 = null;
	
	/**
	 * Test history.
	 */
	@BeforeClass
	public static void testHistory(){
		history = new History();
		assertNotNull(history);
		assertNotNull(history.getOrders());

	}
	
	/**
	 * Inits the.
	 *
	 * @throws InterruptedException the interrupted exception
	 */
	@BeforeClass
	public static void init() throws InterruptedException{
		
		menu.initMenu();
		
		date1 = new Date();
		Thread.sleep(1 * 1000);
		
		Restaurant r = new Restaurant("French Restaurant", "restaurant_1", new AddressPoint(1.0,1.0),"password");
		Customer c = new Customer("Liu", "Bei", "customer_1", new AddressPoint(100.0,100.0), "password");
		FullMeal fm1 = new FullMeal("FM2", menu.getStarters().get(0), menu.getMaindishes().get(0), menu.getDesserts().get(0));
		fm1.setRestaurant(r);
		Order = new Order(c, r, "myorder");
		Courier cr = new Courier("Sanders", "Bernie", "courier_3", new AddressPoint(1.0,3.1), "+33 8 30 10 93 29");
		Order.setCourier(cr);
		
		date2 = new Date();
		Thread.sleep(1 * 1000);
		
		Restaurant r2 = new Restaurant("Chinese Restaurant", "restaurant_2", new AddressPoint(1.0,1.0),"password");
		specialMealOrder = new Order(c, r2, "specialorder");
		specialMealOrder.addItem(fm1);
		specialMealOrder.setCourier(cr);
		
		date3 = new Date();
	}
	
	/**
	 * Test add order.
	 */
	@Test
	public void testAddOrder() {
		
		history.addOrder(Order);
		history.addOrder(specialMealOrder);
		
		assertTrue(history.getOrders().size() > 0);
	}

	/**
	 * Test get order between.
	 *
	 * @throws ParseException the parse exception
	 */
	@Test
	public void testGetOrderBetween() throws ParseException {
		history.addOrder(Order);
		history.addOrder(specialMealOrder);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String date1String = sdf.format(date1);
		String date2String = sdf.format(date2);
		String date3String = sdf.format(date3);
		ArrayList<Order> theOrders1 = history.getOrderBetween(date1String, date2String);
		assertNotNull(theOrders1);
		assertTrue(theOrders1.contains(Order));
		assertFalse(theOrders1.contains(specialMealOrder));
		
		ArrayList<Order> theOrders2 = history.getOrderBetween(date2String, date3String);
		assertNotNull(theOrders2);
		assertTrue(theOrders2.contains(specialMealOrder));
		assertFalse(theOrders2.contains(Order));
	}

	

	/**
	 * Test get orders of.
	 */
	@Test
	public void testGetOrdersOf() {
		history.addOrder(Order);
		history.addOrder(specialMealOrder);
		
		ArrayList<Order> theOrders = history.getOrdersOf("restaurant_1");
		assertNotNull(theOrders);
		assertTrue(theOrders.contains(Order));
		System.out.println("testGetOrdersOf() --- " + theOrders);
	}
	
}
