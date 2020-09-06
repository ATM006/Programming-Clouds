package org.greatfree.dsf.cluster.cs.twonode.clusterserver.child;

import java.io.IOException;
import java.util.Calendar;

import org.greatfree.cluster.ChildTask;
import org.greatfree.cluster.message.ClusterApplicationID;
import org.greatfree.data.ServerConfig;
import org.greatfree.dsf.cluster.original.cs.twonode.message.ChatApplicationID;
import org.greatfree.dsf.cluster.original.cs.twonode.message.ChatNotification;
import org.greatfree.dsf.cluster.original.cs.twonode.message.ChatRegistryRequest;
import org.greatfree.dsf.cluster.original.cs.twonode.message.ChatRegistryResponse;
import org.greatfree.dsf.cs.multinode.server.CSAccount;
import org.greatfree.dsf.cs.twonode.server.AccountRegistry;
import org.greatfree.exceptions.RemoteReadException;
import org.greatfree.message.SystemMessageConfig;
import org.greatfree.message.multicast.MulticastResponse;
import org.greatfree.message.multicast.container.InterChildrenNotification;
import org.greatfree.message.multicast.container.InterChildrenRequest;
import org.greatfree.message.multicast.container.IntercastNotification;
import org.greatfree.message.multicast.container.IntercastRequest;
import org.greatfree.message.multicast.container.Notification;
import org.greatfree.message.multicast.container.Request;
import org.greatfree.message.multicast.container.Response;

// Created: 01/13/2019, Bing Li
class ChatTask implements ChildTask
{

	@Override
	public void processNotification(Notification notification)
	{
		switch (notification.getApplicationID())
		{
			case ChatApplicationID.CHAT_NOTIFICATION:
				System.out.println("CHAT_NOTIFICATION received @" + Calendar.getInstance().getTime());
				ChatNotification cn = (ChatNotification)notification;
				System.out.println(cn.getSenderName() + " says " + cn.getMessage());
				break;
				
			case ClusterApplicationID.STOP_CHAT_CLUSTER_NOTIFICATION:
				System.out.println("STOP_CHAT_CLUSTER_NOTIFICATION received @" + Calendar.getInstance().getTime());
				try
				{
					ChatChild.CONTAINER().stop(ServerConfig.SERVER_SHUTDOWN_TIMEOUT);
				}
				catch (ClassNotFoundException | IOException | InterruptedException | RemoteReadException e)
				{
					e.printStackTrace();
				}
				break;
				
			case ClusterApplicationID.STOP_ONE_CHILD_ON_CLUSTER_NOTIFICATION:
				System.out.println("STOP_ONE_CHILD_ON_CLUSTER_NOTIFICATION received @" + Calendar.getInstance().getTime());
				try
				{
					ChatChild.CONTAINER().stop(ServerConfig.SERVER_SHUTDOWN_TIMEOUT);
				}
				catch (ClassNotFoundException | IOException | InterruptedException | RemoteReadException e)
				{
					e.printStackTrace();
				}
				break;
		}
	}

	@Override
	public MulticastResponse processRequest(Request request)
	{
		switch (request.getApplicationID())
		{
			case ChatApplicationID.CHAT_REGISTRY_REQUEST:
				System.out.println("CHAT_REGISTRY_REQUEST received @" + Calendar.getInstance().getTime());
				ChatRegistryRequest crr = (ChatRegistryRequest)request;
				
				System.out.println(crr.getUserKey() + ", " + crr.getUserName() + ", " + crr.getDescription());
				
				AccountRegistry.CS().add(new CSAccount(crr.getUserKey(), crr.getUserName(), crr.getDescription()));
				return new ChatRegistryResponse(request.getCollaboratorKey(), true);
		}
		return SystemMessageConfig.NO_MULTICAST_RESPONSE;
	}

	@Override
	public InterChildrenNotification prepareNotification(IntercastNotification notification)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	@Override
	public void processDestinationNotification(InterChildrenNotification notification)
	{
		// TODO Auto-generated method stub
		
	}
	*/

	@Override
	public InterChildrenRequest prepareRequest(IntercastRequest request)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MulticastResponse processRequest(InterChildrenRequest request)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processResponse(Response response)
	{
		// TODO Auto-generated method stub
		
	}

}