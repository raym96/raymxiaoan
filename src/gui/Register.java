package gui;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import exceptions.NameNotFoundException;
import gui.model.BasicButton;
import gui.model.BasicFrameSimplePage;
import gui.model.MyRadioButton;
import gui.model.PsdFieldWithLabel;
import gui.model.TextFieldWithLabel;
import policies.LotteryCard;
import policies.PointCard;
import policies.StandardCard;
import system.AddressPoint;
import user.model.Courier;
import user.model.Customer;
import user.model.MyFoodora;
import user.model.Restaurant;
import user.model.User;

public class Register extends BasicFrameSimplePage implements ItemListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MyRadioButton rbtn_userType;
	private MyRadioButton rbtn_contact;
	private MyRadioButton rbtn_notify;
	private MyRadioButton rbtn_duty;
	private MyRadioButton rbtn_fidelitycard;
	private TextFieldWithLabel usernameField;
	private PsdFieldWithLabel passwordField;
	private PsdFieldWithLabel confirm_passwordField;
	private TextFieldWithLabel firstnameField;
	private TextFieldWithLabel lastnameField;
	private TextFieldWithLabel nameField;
	private TextFieldWithLabel position;
	private TextFieldWithLabel contactField;
	
	private JButton OkButton;
	private JButton CancelButton;
	
	private JPanel subPanel_name;
	private JPanel subPanel_option;
	private JPanel loginInPanel;
	
	private User user;
	private boolean notified = false;
	private boolean duty = false;
	private String cardType;

	public Register() {
		super("Register");

		headerLabel.setText(null);
		statusLabel.setText(null);
		
		placeComponents();
	}
	
	@Override
	public void placeComponents() {

		loginInPanel = new JPanel();
//		loginInPanel.setBounds(controlPanel.getWidth()/6, controlPanel.getHeight()/6, controlPanel.getWidth()*1/3, controlPanel.getHeight()*2/3);
		controlPanel.add(loginInPanel);
		
		loginInPanel.setLayout(new BoxLayout(loginInPanel, BoxLayout.Y_AXIS));
		int gap = this.getHeight()/90;
		
		// 1. choose user type
		rbtn_userType = new MyRadioButton("You want to register an acount of which type ? ", loginInPanel, new String[] {"Customer", "Restaurant", "Courier"});
		rbtn_userType.bindItemListener(this);
		loginInPanel.add(Box.createVerticalStrut(gap));
		
		// 2. username
		usernameField = new TextFieldWithLabel("username: ", "username already exists !", loginInPanel);
		loginInPanel.add(Box.createVerticalStrut(gap));
		
		// 3. password
		passwordField = new PsdFieldWithLabel("password: ", loginInPanel);
		loginInPanel.add(Box.createVerticalStrut(gap));
		
		// 4. confirm password
		confirm_passwordField = new PsdFieldWithLabel("confirm password: ", "two password not match !", loginInPanel);
		loginInPanel.add(Box.createVerticalStrut(gap));
		
		// 5. first/last name or name
		subPanel_name = new JPanel();
		loginInPanel.add(subPanel_name);
		loginInPanel.add(Box.createVerticalStrut(gap));
		
		// 6. address/position
		position = new TextFieldWithLabel("address/position: ", "address format wrong ! e.g 8.9,7.0", loginInPanel);
		loginInPanel.add(Box.createVerticalStrut(gap));
		
		// 7. contact
		rbtn_contact = new MyRadioButton("contact: email or phone ?", loginInPanel, new String[] {"Email", "Phone"});
		rbtn_contact.bindItemListener(this);
		rbtn_contact.add(Box.createVerticalStrut(MyRadioButton.gap));
		contactField = new TextFieldWithLabel("", "contact format wrong !", rbtn_contact);
		loginInPanel.add(Box.createVerticalStrut(gap));
		
		// 8. customer/courier option
		subPanel_option = new JPanel();
		loginInPanel.add(subPanel_option);
		loginInPanel.add(Box.createVerticalStrut(gap));
			
		// 9. buttons
		JPanel subPanel_btn = new JPanel();
		subPanel_btn.setLayout(new BoxLayout(subPanel_btn, BoxLayout.X_AXIS));
		// button for login up
		OkButton = new BasicButton("OK");
		OkButton.addActionListener(this);
		subPanel_btn.add(OkButton);
		subPanel_btn.add(Box.createHorizontalStrut(50));
		// button for login in 
		CancelButton = new BasicButton("cancel");
		CancelButton.addActionListener(this);
		subPanel_btn.add(CancelButton);
		loginInPanel.add(subPanel_btn);

		rbtn_userType.getButton("Customer").doClick();	
		rbtn_contact.getButton("Email").doClick();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==OkButton){
			
			
			
			loginIn();
		}
		else if(e.getSource()==CancelButton){
			System.exit(0);
		}
	}
	
	public void loginIn(){
		if(MyFoodora.getInstance().hasUser(usernameField.getTextFieldContent())){
			usernameField.showIllegal();
			usernameField.setTextFieldContent(null);
			return ;
		}else{
			usernameField.hideIllegal();
		}
		if(!passwordField.getPsdFieldContent().equalsIgnoreCase(confirm_passwordField.getPsdFieldContent())){
			confirm_passwordField.showIllegal();
			confirm_passwordField.setPsdFieldContent(null);
			return ;
		}else{
			confirm_passwordField.hideIllegal();
		}
		AddressPoint address = null;
		try{
			position.hideIllegal();
			address = new AddressPoint(position.getTextFieldContent());
		}catch (Exception e) {
			// TODO: handle exception
			position.showIllegal();
			position.setTextFieldContent(null);
			return;
		}
		
		if(rbtn_userType.getButton("Customer").isSelected()) {
			user = new Customer(firstnameField.getTextFieldContent(), 
					lastnameField.getTextFieldContent(), 
					usernameField.getTextFieldContent(), 
					address, 
					passwordField.getPsdFieldContent());
			((Customer)user).setActivated(notified);
			if(cardType.equalsIgnoreCase("Standard")){
				((Customer)user).setCard(new StandardCard((Customer)user));
			}else if(cardType.equalsIgnoreCase("Lottery")){
				((Customer)user).setCard(new LotteryCard((Customer)user));
			}else if(cardType.equalsIgnoreCase("Point")){
				((Customer)user).setCard(new PointCard((Customer)user));
			}
		}else if(rbtn_userType.getButton("Courier").isSelected()){
			user = new Courier(firstnameField.getTextFieldContent(), 
					lastnameField.getTextFieldContent(), 
					usernameField.getTextFieldContent(),  
					address, 
					passwordField.getPsdFieldContent());
			((Courier)user).setOn_duty(duty);
		}else if(rbtn_userType.getButton("Restaurant").isSelected()){
			user = new Restaurant(nameField.getTextFieldContent(), 
					usernameField.getTextFieldContent(), 
					address, 
					passwordField.getPsdFieldContent());
		}
		
		if(rbtn_contact.getButton("Email").isSelected()){
			user.setEmail(contactField.getTextFieldContent());
		}else if(rbtn_contact.getButton("Phone").isSelected()){
			user.setPhone(contactField.getTextFieldContent());
		}
		
		if(user!=null){
			MyFoodora.getInstance().addUser(user);
			JOptionPane.showMessageDialog(this, "account successfully registered", "Information", JOptionPane.INFORMATION_MESSAGE);
			this.dispose();
			new Login();
		}else{
			JOptionPane.showMessageDialog(this, "Information incomplete", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource()==rbtn_userType.getButton("Customer")){
			subPanel_name.removeAll();
			subPanel_option.removeAll();
			// first/last name 
			firstnameField = new TextFieldWithLabel("firstname: ", subPanel_name);
			lastnameField = new TextFieldWithLabel("lastname: ", subPanel_name);
			
			subPanel_option.setLayout(new GridLayout(2,1));
			
			// agree/refuse to be notified
			rbtn_notify = new MyRadioButton("Do you agree to be notified about the special offer ? It's no by default.", subPanel_option, new String[] {"yes", "no"});
			rbtn_notify.bindItemListener(this);
			rbtn_notify.getButton("no").doClick();
			
			// fidelitycard
			rbtn_fidelitycard = new MyRadioButton("Choose a type of fidelity card", subPanel_option, new String[] {"Standard Card", "Lottery Card", "Point Card"});
			rbtn_fidelitycard.bindItemListener(this);
			rbtn_fidelitycard.getButton("Standard Card").doClick();
			
		}else if(e.getSource()==rbtn_userType.getButton("Courier")){
			subPanel_name.removeAll();
			subPanel_option.removeAll();
			// first/last name 
			firstnameField = new TextFieldWithLabel("firstname: ", subPanel_name);
			lastnameField = new TextFieldWithLabel("lastname: ", subPanel_name);
			
			// agree/refuse to be on duty
			rbtn_duty = new MyRadioButton("Do you want to set your current duty status as on-duty ? By default it's off-duty.", subPanel_option, new String[] {"on", "off"});
			rbtn_duty.bindItemListener(this);
			rbtn_duty.getButton("off").doClick();

		}else if(e.getSource()==rbtn_userType.getButton("Restaurant")){
			subPanel_name.removeAll();
			subPanel_option.removeAll();
			// name
			nameField = new TextFieldWithLabel("name: ", subPanel_name);
			
		}else if(e.getSource()==rbtn_contact.getButton("Email")){
			contactField.getLabel().setText("Email: ");
		}else if(e.getSource()==rbtn_contact.getButton("Phone")){
			contactField.getLabel().setText("Phone: ");
		}else if(rbtn_notify != null && e.getSource()==rbtn_notify.getButton("yes")){
			notified = true;		
		}else if(rbtn_notify != null && e.getSource()==rbtn_notify.getButton("no")){
			notified = false;	
		}else if(rbtn_notify != null && rbtn_duty != null && e.getSource()==rbtn_duty.getButton("on")){
			duty = true;		
		}else if(rbtn_duty != null && e.getSource()==rbtn_duty.getButton("off")){
			duty = false;		
		}else if(rbtn_fidelitycard != null &&  e.getSource()==rbtn_fidelitycard.getButton("Standard Card")){
			cardType = "Standard";		
		}else if(rbtn_fidelitycard != null &&  e.getSource()==rbtn_fidelitycard.getButton("Lottery Card")){
			cardType = "Lottery";		
		}else if(rbtn_fidelitycard != null &&  e.getSource()==rbtn_fidelitycard.getButton("Point Card")){
			cardType = "Point";			
		}
		
		controlPanel.validate();
	}	
}
