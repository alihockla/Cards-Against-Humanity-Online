package com.CAH.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CAHserver implements Runnable {
	
	private static boolean assignRolesAndDeal = false;

    private Thread thread;
    private String threadName;
    private Socket threadSocket;
	private ObjectOutputStream output;
	private ObjectInputStream input;

    static List<CAHserver> connections = new ArrayList<CAHserver>();
    
    private String thisRoundsentence;
    private String p1Sentence;
    private String p2Sentence;
    
    private int p1Score;
    private int p2Score;
    
    private static String winner = "Player ";
    
    private static int sentenceNum;

    public CAHserver(Socket socket, String name) 
	{
		threadName = name;		// thread/player # + ip address
        threadSocket = socket;
        
	}
    
	public void run() 
	{
        try {
        	//Create the streams
        	output = new ObjectOutputStream(threadSocket.getOutputStream());
        	input = new ObjectInputStream(threadSocket.getInputStream());

            //Tell the client that he/she has connected
            output.writeUTF("Welcome to Server 5050!  :  " + new Date());
            output.flush();
            
            while (!assignRolesAndDeal)
            {
            	try {
            		output.writeUTF("Please stand by for more players . . . ");
            		output.flush();
					Thread.sleep(2500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
            
            // assign player/judge roles
            if (threadName.contains("Player 1"))
            {
                connections.get(0).output.writeUTF("Player 1");
                System.out.println("Assigning player 1 role ");
            }
            else if (threadName.contains("Player 2"))
            {
           		connections.get(1).output.writeUTF("Player 2");
                System.out.println("Assigning player 2 role ");
            }
            else
            {
    			connections.get(2).output.writeUTF("Judge");
                System.out.println("Assigning judge role ");
            }
            output.flush();
            
            // begin dealing cards
            output.writeUTF("\nBegin round ");
            output.flush();
            
			// send a sentence
            thisRoundsentence = sentencesMap.get(sentenceNum);
			output.writeUTF(thisRoundsentence);
			output.flush();

            // begin dealing cards
            output.writeUTF("\nDealing cards . . ");
            output.flush();

            
            if (threadName.contains("Player 1"))
            {
                connections.get(0).output.writeObject(dealCards("player 1"));
                System.out.println("Dealing player 1 cards");
            }
            else if (threadName.contains("Player 2"))
            {
           		connections.get(1).output.writeObject(dealCards("player 2"));
                System.out.println("Dealing player 2 cards");
            }
            
			output.flush();
			
			
			// wait for player to select card
          String chatInput = "";
          while ((chatInput = input.readUTF()) != null) 
          {
          	//This will wait until a line of text has been sent
              System.out.println(threadName + " says: " + chatInput);
              if (threadName.contains("Player 1"))
              {
            	  p1Sentence = rebuildSentence("Player 1", Integer.valueOf(chatInput));
            	  System.out.println(p1Sentence);
            	  connections.get(2).output.writeUTF("Player 1 " + p1Sentence);
                  connections.get(2).output.flush();
            	  break;
              }
              else if (threadName.contains("Player 2"))
              {
            	  p2Sentence = rebuildSentence("Player 2", Integer.valueOf(chatInput));
            	  System.out.println(p2Sentence);
            	  connections.get(2).output.writeUTF("Player 2 " + p2Sentence);
                  connections.get(2).output.flush();
            	  break;
              }
              else
              {
            	  winner = winner + chatInput;
                  if (winner.contains("1"))
                  {
                  	p1Score += 1;
                  	//connections.get(0).output.writeUTF("Player 1 wins!");
                  	output.writeUTF("Player 1 ");
                  	output.flush();
                  	break;
                  }
                  else if (winner.contains("2"))
                  {
                  	p2Score += 1;
                  	//connections.get(1).output.writeUTF("Player 2 wins!");
                  	output.writeUTF("Player 2 ");
                  	output.flush();
                  	break;
                  }
//            	  break;
              }
          }
//            System.out.println("Sent to judge.");
//            
// 
//            System.out.println("Player1 = " + p1Score + "\nPlayer2 = " + p2Score);
////            System.out.println("Winner has been declared, round over.");
//            System.out.println("Here.... done.");

            thread.sleep(15000);
        	output.writeUTF(winner + " ");
        	output.flush();
                        

            } catch(IOException exception) {
                System.out.println("Uh oh, error: " + exception);
            } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
		
	public String rebuildSentence(String pName, int hashKey)
	{
		if (pName.contains("1"))
		{
			thisRoundsentence = thisRoundsentence + " " + P1cards.get(hashKey);
		}
		else
		{
			thisRoundsentence = thisRoundsentence + " " + P2cards.get(hashKey);
		}
		return thisRoundsentence;
	}
	
	public static void main(String args[])
	{
        try {
            ServerSocket serverSocket = new ServerSocket(5050);
            
            System.out.println("Server 5050 started at: " + new Date());
             
            InetAddress ip = null;
            int x = 1;
            // loop that accepts and starts thread for each connection
            while(connections.size() < 3) {
            	
                //Wait for a client to connect, then accept that connection
                Socket remote_client = serverSocket.accept();
                ip = remote_client.getInetAddress();
                System.out.println(ip + " has connected!");

                //Create a new custom thread to handle the connection
                CAHserver clientThread = new CAHserver(remote_client, ("Player " + x + " (connected at: " + ip + ")"));
                x++;
                
                //Start the thread for that remote client
                clientThread.start();
                
                connections.add(clientThread);
            }
            Thread.sleep(1500);
            createDecks();
            System.out.println("3 clients connected ...");
            assignRolesAndDeal = true;
            
            sentenceNum = rand.nextInt(11);
            
            
        } catch(IOException exception) {
            System.out.println("Error: " + exception);
        } catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	} // end main
	
	private void start() 
	{
		System.out.println("Starting " +  threadName );
		if (thread == null)
		{
			try 
			{
				thread = new Thread (this, threadName);
				thread.start();
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}

	
	// createDeck variables
	private static HashMap<Integer, String> wordsMap;
	private static HashMap<Integer, String> sentencesMap;
	
	private static HashMap<Integer, String> P1cards;
	private static HashMap<Integer, String> P2cards;
	static Random rand = new Random();
	
	
	public HashMap<Integer, String> dealCard(String pname)
	{
		for (int i = 0; i < 9; i++) {
			if (i %2 != 0) { 
				P1cards.put(i, wordsMap.get(i));}
			else {
				P2cards.put(i, wordsMap.get(i));}
		}
		
		if (pname.contains("1"))
		{
			return P1cards;
		}
		else //if (pname.contains("2"))
		{
			return P2cards;
		}
		
	}

	public HashMap<Integer, String> dealCards(String pName) // could pass an int to specify size of decks
	{
		ArrayList<Integer> randNumList = new ArrayList<Integer>();
		int randKey; String randVal; // init variables

		if (pName.contains("1"))
		{
			while (P1cards.size() != 5) 
			{
				randNumList.addAll(wordsMap.keySet()); 
				randKey = randNumList.get(rand.nextInt(wordsMap.size()));
				randVal = wordsMap.get(randKey);
				
				if (randVal != null)
				{
					P1cards.put(randKey, randVal);
					wordsMap.remove(randKey);
				}
				randNumList.clear();
			}
			return P1cards;
		}
		else
		{
			while (P2cards.size() != 5) 
			{
				randNumList.addAll(wordsMap.keySet()); 
				randKey = randNumList.get(rand.nextInt(wordsMap.size()));
				randVal = wordsMap.get(randKey);
				
				if (randVal != null)
				{
					P2cards.put(randKey, randVal);
					wordsMap.remove(randKey);
				}
				randNumList.clear();
			}
			return P2cards;
		}
	}
	
	public static void createDecks() // could pass an int to specify size of arrays
	{
		// Create new HashMap.
		wordsMap = new HashMap<>();
		sentencesMap = new HashMap<>();
		
		P1cards = new HashMap<>(5);
		P2cards = new HashMap<>(5);

		// Put keys with values for cards
		wordsMap.put(1, "A brain tumor."); wordsMap.put(2, "Wifely duties."); wordsMap.put(3, "A micropenis"); wordsMap.put(4, "Asians who aren't good at math."); 
		wordsMap.put(5, "Friendly fire."); wordsMap.put(6, "Waterboarding."); wordsMap.put(7, "A sausage festival."); wordsMap.put(8, "The terrorists."); wordsMap.put(9, "The Amish.");
		wordsMap.put(10, "When you fart and a little bit comes out."); wordsMap.put(11, "Flying sex snakes");
		
//		wordsMap.put(0, "Shapeshifters"); wordsMap.put(1, "Cockfights");wordsMap.put(2, "A Gypsy curse");
//        wordsMap.put(3,"The milk man");wordsMap.put(4, "Dead parents");wordsMap.put(5, "The milk man.");
//        wordsMap.put(6, "A moment of silence");wordsMap.put(7, "Object permanence");wordsMap.put(8, "Ronald Reagan");
//        wordsMap.put(9, "Loose lips.");wordsMap.put(10, "Opposable thumbs"); 
//        wordsMap.put(11, "A disappointing birthday party");wordsMap.put(12, "An honest cop with nothing left to lose");
//        wordsMap.put(13, "Racially-biased SAT questions");wordsMap.put(14, "A sassy black woman");
//        wordsMap.put(15, "Famine");wordsMap.put(16, "Jibber-jabber");wordsMap.put(17, "Mathletes");
//        wordsMap.put(18, "Flesh-eating bacteria");wordsMap.put(19, "Chainsaws for hands");wordsMap.put(20, "A tiny horse");
//        ;wordsMap.put(22, "Nicolas Cage");
//        wordsMap.put(23, "William Shatner");wordsMap.put(24, "Not giving a shit about the Third World");
//        wordsMap.put(25, "Child beauty pageants");wordsMap.put(26, "Riding off into the sunset");
//        wordsMap.put(27, "Sexting");wordsMap.put(28, "Explosions");
//        wordsMap.put(29, "Sniffing glue");wordsMap.put(30, "Being a dick to children");
        
		sentencesMap.put(0,"What's the next Happy Meal (r) toy?"); sentencesMap.put(1, "What's my anti-drug?");sentencesMap.put(2,"Why can't I sleep at night?");
		sentencesMap.put(3,"What's that smell?"); sentencesMap.put(4,"Who stole the cookies from the cookie jar? "); 
		sentencesMap.put(6,"Anthropologists have recently discovered a primitive tribe that worships");
        sentencesMap.put(7,"It's a pity that kids these days are all getting involved with");sentencesMap.put(8,"During Picasso's often-overlooked Brown Period, he produced hundreds of paintings of");
        sentencesMap.put(9,"Alternative medicine is now embracing the curative powers of");sentencesMap.put(10,"What ended my last relationship?");sentencesMap.put(11,"MTV's new reality TV show features eight washed-up celebrities living with");
        sentencesMap.put(12,"I drink to forget");sentencesMap.put(13,"I'm sorry, I couldn't complete my homework because of");sentencesMap.put(14,"What is Batman's guilty pleasure?");sentencesMap.put(15,"What's a girl's best friend?");
        sentencesMap.put(16,"What does Dick Cheney prefer?");sentencesMap.put(17,"Instead of coal, Santa now gives the bad children ");sentencesMap.put(18,"A romantic, candlelit dinner would be incomplete without");
        sentencesMap.put(19,"What are my parents hiding from me");sentencesMap.put(20,"What will always get you laid?");sentencesMap.put(21,"What did I bring back from Mexico?");
        sentencesMap.put(22,"Due to a PR fiasco, Walmart no longer offers");sentencesMap.put(23,"How am I maintaining my relationship status?");sentencesMap.put(24,"But before I kill you Mr. Bond, I must show you");
        sentencesMap.put(25,"What gives me uncontrollable gas?");sentencesMap.put(26,"What do old people smell like?");sentencesMap.put(27,"The class field trip was completely ruined by");
        sentencesMap.put(28,"What's my secret power?");sentencesMap.put(29,"Why am I sticky? ");sentencesMap.put(30,"What gets better with age?");
	}
		
} // end class