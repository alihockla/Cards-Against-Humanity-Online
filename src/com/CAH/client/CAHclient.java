package com.CAH.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;


public class CAHclient extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public volatile static String winner = "";

//    private Thread thread;
	
	private boolean receiveCards = false;
	private boolean beginRound = false;
	private boolean ImJudge = false;
	private boolean ImPlayer = false;
	private boolean judgesDecision = false;
	
//	private volatile String winner;
	private int outputInt = 0;
	private int playerScore = 0;
	private String sentenceStr;
	
	private Map<Integer, String> playerCards;
	ArrayList<Integer> keyLst = new ArrayList<Integer>();
    
	private JFrame frame;
	private Container panel;
	private JButton connectBtn;
	private JTextArea ServerContent;
	private JTextArea SentenceArea;
	private JTextArea ServerWelcome;
	private JButton card1;
	private JButton card2;
	private JButton card3;
	private JButton card4;
	private JButton card5;
	private JTextArea cardLbl1;
	private JTextArea cardLbl2;
	private JTextArea cardLbl3;
	private JTextArea cardLbl4;
	private JTextArea cardLbl5;
	
	public CAHclient()
	{
		frame = new JFrame("Cards Against Humanity Online!");

		connectBtn = new JButton("Connect to Server");
		connectBtn.addActionListener(this);
		
		Font sentenceFont = new Font("Helvetica", Font.ITALIC + Font.BOLD, 15);
		Insets sentenceInset = new Insets(15, 30, 0, 0);
		
		ServerWelcome = new JTextArea(0, 30);
		ServerContent = new JTextArea(10, 60);
		ServerContent.setEditable(false);
		ServerContent.setLineWrap(true);
		
		SentenceArea = new JTextArea(7, 60);
		SentenceArea.setEditable(false);
		SentenceArea.setLineWrap(true);
//		SentenceArea.setBackground(Color.BLACK);
//		SentenceArea.setForeground(Color.WHITE);
		SentenceArea.setFont(sentenceFont);
		SentenceArea.setMargin(sentenceInset);
		
	    JScrollPane spane = new JScrollPane(ServerContent);
	    JScrollPane spane2 = new JScrollPane(SentenceArea);

	    Dimension cardDim = new Dimension(165, 180);
	    Font cardLblFont = new Font("Helvetica", Font.BOLD, 16);
	    Font cardFont = new Font("Helvetica", Font.BOLD, 20);
		Insets cardInset = new Insets(15, 15, 0, 0);
		Color bkgrndColor = new Color(238, 238, 238);

	    card1 = new JButton("1");
	    card1.setFont(cardFont);
	    card1.setVerticalAlignment(SwingConstants.BOTTOM);
	    card1.setLayout(new BorderLayout());
	    cardLbl1 = new JTextArea();
	    cardLbl1.setLineWrap(true);
	    cardLbl1.setWrapStyleWord(true);
	    cardLbl1.setFont(cardLblFont);
//	    cardLbl1.setMargin(cardInset);
	    cardLbl1.setEditable(false);
	    cardLbl1.setBackground(bkgrndColor);
	    card1.setPreferredSize(cardDim);
	    card1.add(BorderLayout.NORTH,cardLbl1);
	    card1.setEnabled(false);

	    card2 = new JButton("2");
	    card2.setFont(cardFont);
	    card2.setVerticalAlignment(SwingConstants.BOTTOM);
	    card2.setLayout(new BorderLayout());
	    cardLbl2 = new JTextArea();
	    cardLbl2.setLineWrap(true);
	    cardLbl2.setWrapStyleWord(true);
	    cardLbl2.setFont(cardLblFont);
//	    cardLbl2.setMargin(cardInset);
	    cardLbl2.setEditable(false);
	    cardLbl2.setBackground(bkgrndColor);
	    card2.setPreferredSize(cardDim);
	    card2.add(BorderLayout.NORTH,cardLbl2);
	    card2.setEnabled(false);

	    card3 = new JButton("3");
	    card3.setFont(cardFont);
	    card3.setVerticalAlignment(SwingConstants.BOTTOM);
	    card3.setLayout(new BorderLayout());
	    cardLbl3 = new JTextArea();
	    cardLbl3.setLineWrap(true);
	    cardLbl3.setWrapStyleWord(true);
	    cardLbl3.setFont(cardLblFont);
//	    cardLbl3.setMargin(cardInset);
	    cardLbl3.setEditable(false);
	    cardLbl3.setBackground(bkgrndColor);
	    card3.setPreferredSize(cardDim);
	    card3.add(BorderLayout.NORTH,cardLbl3);
	    card3.setEnabled(false);

	    card4 = new JButton("4");
	    card4.setFont(cardFont);
	    card4.setVerticalAlignment(SwingConstants.BOTTOM);
	    card4.setLayout(new BorderLayout());
	    cardLbl4 = new JTextArea();
	    cardLbl4.setLineWrap(true);
	    cardLbl4.setWrapStyleWord(true);
	    cardLbl4.setFont(cardLblFont);
//	    cardLbl4.setMargin(cardInset);
	    cardLbl4.setEditable(false);
	    cardLbl4.setBackground(bkgrndColor);
	    card4.setPreferredSize(cardDim);
	    card4.add(BorderLayout.NORTH,cardLbl4);
	    card4.setEnabled(false);

	    card5 = new JButton("5");
	    card5.setFont(cardFont);
	    card5.setVerticalAlignment(SwingConstants.BOTTOM);
	    card5.setLayout(new BorderLayout());
	    cardLbl5 = new JTextArea();
	    cardLbl5.setLineWrap(true);
	    cardLbl5.setWrapStyleWord(true);
	    cardLbl5.setFont(cardLblFont);
//	    cardLbl5.setMargin(cardInset);
	    cardLbl5.setEditable(false);
	    cardLbl5.setBackground(bkgrndColor);
	    card5.setPreferredSize(cardDim);
	    card5.add(BorderLayout.NORTH,cardLbl5);
	    card5.setEnabled(false);

	    card1.addActionListener(this);
	    card2.addActionListener(this);
	    card3.addActionListener(this);
	    card4.addActionListener(this);
	    card5.addActionListener(this);

		panel = getContentPane();
		panel.setLayout(new FlowLayout());
		
		panel.add(connectBtn);
		panel.add(ServerWelcome);
		panel.add(spane);
		panel.add(SentenceArea);
		panel.add(spane2);
		panel.add(card1);
		panel.add(card2);
		panel.add(card3);
		panel.add(card4);
		panel.add(card5);
		
		frame.add(panel);
		frame.setSize(875, 550);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}
	
	public void actionPerformed(ActionEvent e) 
	{
	    Object source = e.getSource();
	   	
	    if (source == connectBtn) 
	    {	
	    	try {
	    		connectBtn.setEnabled(false);
	    		
	    		ClientThread t1 = new ClientThread();
	    		
                new Thread(t1).start();
                
//				System.out.println("here: " + Thread.activeCount());
	    		
	    	} catch (Exception ex) 
	    	{
	    		System.out.println("ERROR: " + ex.getMessage());
	    	}
	    }
	    
	    if (source == card1)
	    {
	    	if (ImJudge)
	    	{
	    		outputInt = 1;
	    	}
	    	else
	    	{
	    		outputInt = keyLst.get(0);
	    	}
	    }
	    if (source == card2)
	    {
	    	if (ImJudge)
	    	{
	    		outputInt = 2;
	    	}
	    	else
	    	{
	    		outputInt = keyLst.get(1);
	    	}
	    }
	    if (source == card3)
	    {
	    	outputInt = keyLst.get(2);
	    }
	    if (source == card4)
	    {
	    	outputInt = keyLst.get(3);
	    }
	    if (source == card5)
	    {
	    	outputInt = keyLst.get(4);
	    }

	}
	
	
    //Here we create the ClientThread inner class and have it implement Runnable
    //This means that it can be used as a thread
    class ClientThread implements Runnable 
    {
    	private ObjectOutputStream output;
    	private ObjectInputStream input;
    	private Socket socket;
    	    	
		@SuppressWarnings("unchecked")
		public void run() {

            try {
                socket = new Socket("75.102.241.22",5050); // sohaibs ip 75.102.241.22
                output = new ObjectOutputStream(socket.getOutputStream());
                input = new ObjectInputStream(socket.getInputStream());
                //This will wait for the server to send the string to the client saying a connection
                //has been made.
                
                String inputString = input.readUTF();
                ServerWelcome.setText(inputString);
                
                String inputString1;
				while (!beginRound) //(inputString1 = input.readUTF()) != null) //(inputString1 = input.readUTF()) != null)
				{
					inputString1 = input.readUTF();
					if (inputString1.contains("Judge"))
					{
						ImJudge = true;
						card1.setEnabled(false);
						card2.setEnabled(false);
						card3.setEnabled(false);
						card4.setEnabled(false);
						card5.setEnabled(false);
						ServerContent.append("You're the judge!");
						frame.setTitle("Cards Against Humanity - " + inputString1);
						break;
					}
					else if (inputString1.contains("Player"))
					{
						ImPlayer = true;
						frame.setTitle("Cards Against Humanity - " + inputString1);
					}
					if (inputString1.contains("round"))
					{
						beginRound = true;
					}
					ServerContent.append("\n" + inputString1);
				}
				
				while (beginRound && !receiveCards && !ImJudge)
				{
					sentenceStr = input.readUTF();
					SentenceArea.setBackground(Color.BLACK);
					SentenceArea.setForeground(Color.WHITE);
					SentenceArea.setText(sentenceStr);
					inputString1 = input.readUTF();
					if (inputString1.contains("cards"))
					{
						receiveCards = true;
					}
				}
				

				System.out.println("here1");
				String inputString2; int x = 0;
				if (ImJudge)
				{
					System.out.println("Here 1.5 -> ");
			        while ((inputString2 = input.readUTF()) != null) 
			        {
			        	if (inputString2.contains("cards"))
			        	{
							ServerContent.setText("\n" + inputString2);
							SentenceArea.setText("Which Sentence wins?\n");
			        	}
			        	else if (inputString2.contains("1"))
			        	{
//			        		inputString2 = input.readUTF();
			        		SentenceArea.append("\n" + inputString2);
							card1.setEnabled(true);
							card1.setText("Player 1");
			        		x++;
			        	}
			        	else if (inputString2.contains("2"))
			        	{
//			        		inputString2 = input.readUTF();
			        		SentenceArea.append("\n" + inputString2);
			        		card2.setEnabled(true);
							card2.setText("Player 2");
			        		x++;
			        	}
			        	if (x == 2)
			        	{
			        		break;
			        	}
			        	System.out.println(x);
			        }
			        
					System.out.println("x is now: " + x + " --> Here 1.6 <-- ");
					while (outputInt == 0)
					{
						ServerContent.append("\nPick the winner..");
						Thread.sleep(3500);
					}
					System.out.println("Sending " + outputInt);
					output.writeUTF(String.valueOf(outputInt));
					output.flush();
				}
				
				System.out.println("here2");
				
				if (ImPlayer && receiveCards)
				{
					System.out.println("ImPlayer && receiveCards");
					playerCards = (Map<Integer, String>) input.readObject();
					ServerContent.append("\n" + playerCards.entrySet());
					keyLst.addAll(playerCards.keySet());
					
//        				card1.setText(keyLst.get(0).toString());
					cardLbl1.setText(playerCards.get(keyLst.get(0)));
//        				card2.setText(keyLst.get(1).toString());
					cardLbl2.setText(playerCards.get(keyLst.get(1)));
//        				card3.setText(keyLst.get(2).toString());
					cardLbl3.setText(playerCards.get(keyLst.get(2)));
//        				card4.setText(keyLst.get(3).toString());
					cardLbl4.setText(playerCards.get(keyLst.get(3)));
//        				card5.setText(keyLst.get(4).toString());
					cardLbl5.setText(playerCards.get(keyLst.get(4)));
//					connectBtn.setEnabled(true);

					card1.setEnabled(true);
					card2.setEnabled(true);
					card3.setEnabled(true);
					card4.setEnabled(true);
					card5.setEnabled(true);

					while (outputInt == 0)
					{
						ServerContent.append("\nPick a card..");
						Thread.sleep(3500);
					}
					System.out.println("Sending " + outputInt);
					output.writeUTF(String.valueOf(outputInt));
					output.flush();
				
				}
				
				System.out.println("Heere3! ;akjfalkdjf;a");
				
				ServerContent.setText("The moment of truth");
				SentenceArea.setBackground(Color.WHITE);
				SentenceArea.setForeground(Color.BLACK);
				
				card1.setEnabled(false);
				card2.setEnabled(false);
				card3.setEnabled(false);
				card4.setEnabled(false);
				card5.setEnabled(false);
				
				SentenceArea.setText("");

				while (input.available() == 0)
				{
					Thread.sleep(4000);
					SentenceArea.append("Waiting for judge to decide...\n");
				}
				SentenceArea.setText(input.readUTF() + "WINS!");
				
				System.out.println("Heere4. DONE>.");

            } catch (EOFException eof)
            {
            	System.out.println("EOF: " + eof.getMessage());
            	
            } catch (IOException exception) {
            	
            	ServerWelcome.setText("Error: " + exception.getMessage());
                System.out.println(exception.getMessage());
            	connectBtn.setEnabled(true);
            	
            } catch (ClassNotFoundException e) {
            	
                System.out.println("CNFE " + e.getMessage());
                
			} catch (InterruptedException e) {
				
				System.out.println(e.getMessage());
			}
            
			System.out.println(winner);

    	}
				
    } // end class
	

	
	public static void main(String[] args) 
	{
		new CAHclient();
	} // end main 
	
} // end class 
