package com.greatfree.remote;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.greatfree.concurrency.Collaborator;
import com.greatfree.util.FreeObject;
import com.greatfree.util.Tools;

/*
 * The class encloses all the IO required stuffs to receive and respond a client's requests. 07/30/2014, Bing Li
 */

// Created: 07/30/2014, Bing Li
abstract public class ServerIO extends FreeObject implements Runnable
{
	// 
	private String clientKey;
	// The remote client/server socket. 07/30/2014, Bing Li
	private Socket clientSocket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private boolean isShutdown;
	private String ip;
	private Lock lock;
	private Collaborator collaborator;

	public ServerIO(Socket clientSocket, Collaborator collaborator)
	{
		// Set the object key for the parent object, FreeObject. 07/30/2014, Bing Li
		super(Tools.getClientIPPortKey(clientSocket));
		this.clientSocket = clientSocket;
		
		// Get the IP address of the client. Usually, the IP is used to connect the remote server for an eventing server. 07/30/2014, Bing Li
		this.ip = Tools.getClientIPAddress(clientSocket);
		// Create the key for the class upon the connecting client socket. The key is used to manage ServerIO conveniently in a hash map. 07/30/2014, Bing Li
		this.clientKey = Tools.getKeyOfFreeClient(Tools.getClientIPAddress(clientSocket), Tools.getClientIPPort(clientSocket));
		try
		{
			// Create the input stream to receive requests from the client. 07/30/2014, Bing Li
			this.in = new ObjectInputStream(this.clientSocket.getInputStream());
			// Create the output stream to respond the client. 07/30/2014, Bing Li
			this.out = new ObjectOutputStream(this.clientSocket.getOutputStream());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		// Set the shutdown flag. 07/30/2014, Bing Li
		this.isShutdown = false;

		this.lock = new ReentrantLock();
		this.collaborator = collaborator;
	}

	/*
	 * Dispose all of the resources contained in the class. 07/30/2014, Bing Li
	 */
	public synchronized void shutdown() throws IOException
	{
		// The server listener for a client might be blocked for the limited size of ServerIO. Therefore, when removing one, it is necessary to notify the listener to accept more clients. 08/04/2014, Bing Li
		this.collaborator.signal();
		if (this.isShutdown)
		{
			this.isShutdown = true;
		}
		this.in.close();
		this.out.close();
		this.clientSocket.close();
	}

	/*
	 * Get the key of the class. 07/30/2014, Bing Li
	 */
	public String getClientKey()
	{
		return this.clientKey;
	}
	
	/*
	 * Expose the lock that is used to keep the operations to respond clients in an atomic manner. 07/30/2014, Bing Li
	 */
	public Lock getLock()
	{
		return this.lock;
	}

	/*
	 * Since the resources of ServerIP are limited, it must limit the count of connected clients. It is performed by the collaborator's method of holdOn(). Therefore, when one Server is shutdown, the method can notify the listener to allow more clients to enter. 07/30/2014, Bing Li
	 */
	public void signal()
	{
		this.collaborator.signal();
	}
	
	/*
	 * Get the IP address of the remote client/server. 07/30/2014, Bing Li
	 */
	public String getIP()
	{
		return this.ip;
	}

	/*
	 * Check whether the class is shutdown or not. 07/30/2014, Bing Li
	 */
	public boolean isShutdown()
	{
		return this.isShutdown;
	}

	/*
	 * Set the flag of shutdown to true. 07/30/2014, Bing Li
	 */
	public synchronized void setShutdown()
	{
		this.isShutdown = true;
	}

	/*
	 * Wait for requests from a remote client. 07/30/2014, Bing Li
	 */
	public Object read() throws IOException, ClassNotFoundException, SocketException
	{
		return this.in.readObject();
	}

	/*
	 * Get the output stream that is used to respond the remote client's requests. 07/30/2014, Bing Li
	 */
	public ObjectOutputStream getOutStream()
	{
		return this.out;
	}

	/*
	 * The concurrency method that must be implemented. 08/10/2014, Bing Li
	 */
	@Override
	public void run()
	{
	}
}