/********************************************
 * Author: A.S.M.Thasneem 					*
 * Index Number: DSE/21/120					*
 * Title: Address Book						*
 * ******************************************/

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import net.proteanit.sql.DbUtils;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class AddressBook extends JFrame {
	
	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtAddress;
	private JTextField txtEmail;
	private JTextField txtContact;
	private JTextField txtId;
	private JTable table;
	private JComboBox comboBox;


	Connection con;	
	PreparedStatement pst;
	ResultSet rs;
	
	private JTextField txtDelete;
	private JTextField txtEdit;
	private JComboBox comboBox_1;
	private JTextField txtNewValue;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddressBook frame = new AddressBook();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	public void connect() 
	{
		/* Variables */
		final String JdbcURL = "jdbc:mysql://localhost/studenttest"; //+ "autoReconnect=true&useSSL=false";
		final String user = "root";
		final String password = "thasneem";
		
		try 
		{
			// register MySQL thin driver with DriverManager service
			// It is optional for JDBC4.x version
			Class.forName("com.mysql.jdbc.Driver");
			
			 // Establish the Connection
			con = DriverManager.getConnection(JdbcURL,user,password);

		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	public void save() 
	{
		try 
		{
			if(!(txtName.getText().isEmpty() || txtAddress.getText().isEmpty() || txtEmail.getText().isEmpty() || txtContact.getText().isEmpty()))
			{
				String name = txtName.getText();
				String address = txtAddress.getText();
				String email = txtEmail.getText();
				String contact_No = txtContact.getText();
				
				pst = con.prepareStatement("insert into AddressBook(Name,Address,Email,Contact_No ) values(?,?,?,?)");
				pst.setString(1, name);
				pst.setString(2, address);
				pst.setString(3, email);
				pst.setString(4, contact_No);
				pst.executeUpdate();
				
				JOptionPane.showMessageDialog(null, "Record Successfully Added into Database");
			
				clearFields();
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Please input All the Details");
			}
			
		}
		catch (SQLException e1) 
		{
			e1.printStackTrace();
		}
	}
	
	
	public void loadTable() 
	{
		try 
		{
			pst = con.prepareStatement("select* from AddressBook");
			rs = pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
		}
		catch (SQLException e1) 
		{
			e1.printStackTrace();
		}
	}
	
	public void clearFields() 
	{
		txtName.setText("");
		txtAddress.setText("");
		txtEmail.setText("");
		txtContact.setText("");
		txtId.setText("");
		txtDelete.setText("");
		txtEdit.setText("");
		txtNewValue.setText("");
		

		/* Clear the Table */
		try 
		{
			pst = con.prepareStatement("select* from AddressBook where ID = -1 ");
			rs = pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
			
		} catch (SQLException e) 
		{
			e.printStackTrace();
		} 
		
	}
	
	public void search()
	{
		try 
		{
			String selection = (String)comboBox.getSelectedItem();
			pst = con.prepareStatement("select* from AddressBook where " + selection + " = ?");
			pst.setString(1, txtId.getText()); 
			rs = pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
		}
		catch (SQLException e1) 
		{
			e1.printStackTrace();
		}
	}
	
	
	public void delete()
	{
		try 
		{
			if(!txtDelete.getText().isEmpty()) 
			{
				String selection = (String)comboBox.getSelectedItem();
				pst = con.prepareStatement("delete from AddressBook where " + selection + " = ?");
				pst.setString(1, txtDelete.getText()); 
				pst.execute();
				pst.close();
				
				JOptionPane.showMessageDialog(null, "Successfully deleted the data");
				clearFields();
			}
			else 
			{
				JOptionPane.showMessageDialog(null, "Please input the vlaue to delete");
			}
			
		}
		catch (SQLException e1) 
		{
			e1.printStackTrace();
		}
	}
	
	
	public void update()
	{
		try 
		{
			if(!(txtEdit.getText().isEmpty() || txtNewValue.getText().isEmpty() )) 
			{
				String selection1 = (String)comboBox.getSelectedItem(); // Where to update
				String selection2 = (String)comboBox_1.getSelectedItem();
				
				pst = con.prepareStatement("update AddressBook set " + selection2 + " = ?" + " where " + selection1 + " = ? ");
				pst.setString(1, txtNewValue.getText()); 
				pst.setString(2, txtEdit.getText());
				pst.execute();
				pst.close();
				
				JOptionPane.showMessageDialog(null, "Successfully Updated the data");
				clearFields();
			}
			else 
			{
				JOptionPane.showMessageDialog(null, "Please input Where to update, New value");
			}
			
		}
		catch (SQLException e1) 
		{
			e1.printStackTrace();
		}
	}
	
	
	public AddressBook() {
		setTitle("Address Book");
		connect();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 964, 758);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Name");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel.setBounds(56, 38, 46, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Address");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel_1.setBounds(56, 91, 67, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Email");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel_2.setBounds(56, 158, 46, 14);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Contact No");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel_3.setBounds(56, 225, 78, 14);
		contentPane.add(lblNewLabel_3);
		
		txtName = new JTextField();
		txtName.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtName.setBounds(215, 35, 316, 28);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		txtAddress = new JTextField();
		txtAddress.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtAddress.setBounds(215, 88, 364, 35);
		contentPane.add(txtAddress);
		txtAddress.setColumns(10);
		
		txtEmail = new JTextField();
		txtEmail.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtEmail.setBounds(215, 155, 316, 35);
		contentPane.add(txtEmail);
		txtEmail.setColumns(10);
		
		txtContact = new JTextField();
		txtContact.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtContact.setBounds(215, 222, 194, 28);
		contentPane.add(txtContact);
		txtContact.setColumns(10);
		
		JButton btnNewButton = new JButton("Save");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				save();
			}
		});
		btnNewButton.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
		btnNewButton.setBounds(670, 38, 89, 35);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Edit");
		btnNewButton_1.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnNewButton_1.setFont(new Font("Times New Roman", Font.BOLD, 16));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});
		btnNewButton_1.setBounds(670, 94, 89, 35);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Delete");
		btnNewButton_2.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnNewButton_2.setFont(new Font("Times New Roman", Font.BOLD, 16));
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delete();
			}
		});
		btnNewButton_2.setBounds(670, 155, 89, 35);
		contentPane.add(btnNewButton_2);
		
		txtId = new JTextField();
		txtId.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				search();
			}
		});
		txtId.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtId.setBorder(new TitledBorder(null, "Search", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		txtId.setBounds(10, 388, 292, 38);
		contentPane.add(txtId);
		txtId.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(312, 324, 626, 384);
		contentPane.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
			}
		));
		
		JButton btnNewButton_3 = new JButton("CLEAR");
		btnNewButton_3.setBackground(Color.GREEN);
		btnNewButton_3.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				clearFields();
			}
		});
		btnNewButton_3.setBounds(487, 254, 112, 45);
		contentPane.add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("View");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadTable();
			}
		});
		btnNewButton_4.setFont(new Font("Times New Roman", Font.BOLD, 16));
		btnNewButton_4.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnNewButton_4.setBounds(670, 225, 89, 35);
		contentPane.add(btnNewButton_4);
		
		comboBox = new JComboBox();
		comboBox.setForeground(new Color(0, 0, 255));
		comboBox.setFont(new Font("Tahoma", Font.BOLD, 15));
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"ID", "Name", "Address", "Email", "Contact_No"}));
		comboBox.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		comboBox.setBounds(40, 305, 155, 24);
		contentPane.add(comboBox);
		
		txtDelete = new JTextField();
		txtDelete.setBorder(new TitledBorder(null, "Delete", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		txtDelete.setBounds(10, 467, 292, 38);
		contentPane.add(txtDelete);
		txtDelete.setColumns(10);
		
		txtEdit = new JTextField();
		txtEdit.setBorder(new TitledBorder(null, "Edit", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		txtEdit.setBounds(10, 535, 292, 38);
		contentPane.add(txtEdit);
		txtEdit.setColumns(10);
		
		comboBox_1 = new JComboBox();
		comboBox_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		comboBox_1.setBackground(new Color(138, 43, 226));
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"ID", "Name", "Address", "Email", "Contact_No"}));
		comboBox_1.setBounds(40, 591, 155, 24);
		contentPane.add(comboBox_1);
		
		txtNewValue = new JTextField();
		txtNewValue.setBorder(new TitledBorder(null, "NewValue", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		txtNewValue.setBounds(10, 640, 292, 38);
		contentPane.add(txtNewValue);
		txtNewValue.setColumns(10);
	}
}
