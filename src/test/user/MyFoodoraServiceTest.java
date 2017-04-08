package test.user;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import initialization.InitialScenario;
import policies.DeliveryPolicy;
import policies.FairOccupationDeliveryPolicy;
import policies.FastestDeliveryPolicy;
import policies.TargetProfit_DeliveryCost;
import restaurant.Dish;
import restaurant.Meal;
import restaurant.Menu;
import restaurant.SpecialMeal;
import system.AddressPoint;
import system.AlaCarteOrder;
import system.Order;
import system.SpecialOffer;
import system.StandardMealOrder;
import user.Courier;
import user.Customer;
import user.MyFoodora;
import user.MyFoodoraService;
import user.MyFoodoraServiceImpl;
import user.Restaurant;
import user.User;

public class MyFoodoraServiceTest {

	MyFoodora myfoodora;
	MyFoodoraService m;
	

	static Date startingdate = null;
	
	@Before
	public void setUpBefore() throws Exception {
		InitialScenario.load("scenario_test_services.ini");	
		
		myfoodora = MyFoodora.getInstance();
		m = new MyFoodoraServiceImpl();
		
		//starting date for computing revenue between 2 dates
		String s = "2016.01.01";
		DateFormat format = new SimpleDateFormat("yyyy.MM.dd");
		startingdate = format.parse(s);

	}

	@Test
	public void testSetServiceFee() {
		System.out.println("-----testSetServiceFee-----");
		m.setServiceFee(1);
		assertTrue(myfoodora.getService_fee()==1);
		
	}

	@Test
	public void testSetMarkUpPercentage() {
		System.out.println("-----testSetMarkUpPercentage-----");
		m.setMarkUpPercentage(0.01);
		assertTrue(myfoodora.getMarkup_percentage()==0.01);
	}

	@Test
	public void testSetDeliveryCost() {
		System.out.println("-----testSetDeliveryCost-----");
		m.setDeliveryCost(0.5);
		assertTrue(myfoodora.getDelivery_cost()==0.5);
	}

	@Test
	public void testParse() {
		System.out.println("-----testParse-----");
		Customer customer = (Customer)m.selectUser("customer_1");
		Restaurant restaurant = (Restaurant)m.selectUser("restaurant_1");
		Meal meal = restaurant.getFullMealMenu().getMeals().get(0);
		Order order = new StandardMealOrder(customer,restaurant,meal);
		
		System.out.println("restaurant position = " + restaurant.getAddress());
		for (Courier c : myfoodora.getAvailableCouriers()){
			System.out.println("Courier <"+c.getName()+"> position ="+c.getPosition()+" has count: " + c.getCount());
		}
		
		//Should give the order to courier_1
		System.out.println("FastDeliveryPolicy:");
		myfoodora.setDeliveryPolicy(new FastestDeliveryPolicy());
		m.parse(order, myfoodora.getAvailableCouriers());
		
		//verify that the courier given by the algorithm of delivery policy (tested in its own test file) received the order
		Courier courier_1 = new FastestDeliveryPolicy().parse(order, myfoodora.getAvailableCouriers());
		assertTrue(courier_1.getWaitingOrders().contains(order));
		
		//Should give the order to courier_2
		System.out.println("FairOccupationDelivery");
		myfoodora.setDeliveryPolicy(new FairOccupationDeliveryPolicy());
		m.parse(order, myfoodora.getAvailableCouriers());
		
		//verify that the courier given by the algorithm of delivery policy (tested in its own test file) received the order
		Courier courier_2 = new FairOccupationDeliveryPolicy().parse(order, myfoodora.getAvailableCouriers());
		assertTrue(courier_2.getWaitingOrders().contains(order));
		
	}

	@Test
	public void testNotifyAll() {
		System.out.println("-----testNotifyAll-----");
		Restaurant r = (Restaurant)m.selectUser("restaurant_1");
		Meal supermeal = new SpecialMeal("supertest",r.getMenu().getStarters().get(0),r.getMenu().getMaindishes().get(0));
		SpecialOffer specialoffer = new SpecialOffer(r,supermeal);
		
		//customer c agrees to be notified
		Customer c = (Customer)m.selectUser("customer_1");
		c.getCustomerService().giveConsensusBeNotifiedSpecialOffers();
		
		m.notifyAll(specialoffer);
		
		//verify that the new special offer appears on the board of customer c
		assertTrue(c.getSpecialoffers().contains(specialoffer));
	}

	@Test
	public void testGetTotalIncome() throws ParseException {
		//Starting date for calculating the income/profit
		System.out.println("-----testGetTotalIncome-----");
		System.out.println(myfoodora.getHistory());
		System.out.println(m.getTotalIncome(startingdate, new Date()));
	}

	@Test
	public void testGetTotalProfit() throws ParseException {
		//Starting date for calculating the income/profit
		System.out.println("-----testGetTotalProfit-----");
		System.out.println(myfoodora.getHistory());
		System.out.println(m.getTotalProfit(startingdate, new Date()));
	}

	@Test
	public void testGetAverageIncomePerCustomer() throws ParseException {
		System.out.println("-----testGetAverageIncomePerCustomer-----");
		System.out.println(myfoodora.getHistory());
		System.out.println(m.getAverageIncomePerCustomer(startingdate, new Date()));
	}

	@Test
	public void testApplyTargetProfitPolicy() {
		System.out.println("-----testApplyTargetProfitPolicy-----");
		
		System.out.println("TargetProfit_DeliveryCost:");
		myfoodora.setTargetprofitpolicy(new TargetProfit_DeliveryCost(myfoodora));
		
		System.out.println("Delivery_Cost=" + myfoodora.getDelivery_cost());
		System.out.println("Service_fee="+myfoodora.getService_fee());
		System.out.println("Markup_percentage="+myfoodora.getMarkup_percentage());

		System.out.println("applying target profit policy");
		m.applyTargetProfitPolicy(5);
		
		System.out.println("Delivery_Cost=" + myfoodora.getDelivery_cost());
		System.out.println("Service_fee="+myfoodora.getService_fee());
		System.out.println("Markup_percentage="+myfoodora.getMarkup_percentage());
		
	}

	@Test
	public void testSelectUser() {
		System.out.println("-----testSelectUser-----");
		Customer c = new Customer("","","test",new AddressPoint(0,0),"","");
		myfoodora.addUser(c);
		User user = m.selectUser("test");
		assertEquals(c,user);
	}

	@Test
	public void testGetUsersOfAssignedType() {
		System.out.println("-----testGetUsersOfAssignedType-----");
		for (User u:m.getUsersOfAssignedType("Restaurant")){
			if (!(u instanceof Restaurant)){
				fail();
			}
		}
	}

	@Test
	public void testAskAgree2customers() {
		System.out.println("-----testAskAgree2customers-----");
		m.askAgree2customers("Do you agree ?");
	}

	@Test
	public void testGetHistory() {
		System.out.println("-----testGetHistory-----");
		System.out.println(m.getHistory());
	}

}
