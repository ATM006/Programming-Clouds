package org.greatfree.dsf.cluster.original.cs.twonode.client;

import java.io.IOException;
import java.util.Scanner;

import org.greatfree.chat.ClientMenu;
import org.greatfree.chat.MenuOptions;
import org.greatfree.dsf.cs.twonode.client.ChatMaintainer;
import org.greatfree.exceptions.RemoteReadException;

// Created: 10/23/2018, Bing Li
class StartChatClient
{

	public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException, RemoteReadException
	{
		// Initialize the option which represents a user's intents of operations. 09/21/2014, Bing Li
		int option = MenuOptions.NO_OPTION;

		// Initialize a command input console for users to interact with the system. 09/21/2014, Bing Li
		Scanner in = new Scanner(System.in);
		
		// Input the local user name. 05/26/2017, Bing Li
		System.out.println("Tell me your user name: ");
		
		String username = in.nextLine();

		// Input the chatting partner name. 05/26/2017, Bing Li
		System.out.println("Tell me your partner: ");
		
		String partner = in.nextLine();

		// The chatting information maintainer. 05/25/2017, Bing Li
		ChatMaintainer.CS().init(username, partner);

		System.out.println("Client starting up ...");
		
		ChatClient.CLUSTER_FRONT().init();
		
		System.out.println("Client started ...");

		String optionStr;
		// Keep the loop running to interact with users until an end option is selected. 09/21/2014, Bing Li
		while (option != MenuOptions.QUIT)
		{
			// Display the menu to users. 09/21/2014, Bing Li
			ClientUI.CLUSTER().printMenu();
			// Input a string that represents users' intents. 09/21/2014, Bing Li
			optionStr = in.nextLine();
			try
			{
				// Convert the input string to integer. 09/21/2014, Bing Li
				option = Integer.parseInt(optionStr);
				System.out.println("Your choice: " + option);
				
				// Send the option to the polling server. 09/21/2014, Bing Li
				ClientUI.CLUSTER().send(option);
			}
			catch (NumberFormatException | ClassNotFoundException | RemoteReadException e)
			{
				option = MenuOptions.NO_OPTION;
				System.out.println(ClientMenu.WRONG_OPTION);
			}
		}
		
		// Dispose the chatting maintainer. 05/26/2017, Bing Li
		ChatMaintainer.CS().dispose();
		
		ChatClient.CLUSTER_FRONT().dispose();

		in.close();
	}
}
