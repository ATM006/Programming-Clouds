package org.greatfree.dsf.cluster.scalable.task.child;

import java.io.IOException;
import java.util.Calendar;

import org.greatfree.cluster.ChildTask;
import org.greatfree.data.ServerConfig;
import org.greatfree.dsf.cluster.scalable.message.ScalableApplicationID;
import org.greatfree.exceptions.RemoteReadException;
import org.greatfree.message.multicast.MulticastResponse;
import org.greatfree.message.multicast.container.InterChildrenNotification;
import org.greatfree.message.multicast.container.InterChildrenRequest;
import org.greatfree.message.multicast.container.IntercastNotification;
import org.greatfree.message.multicast.container.IntercastRequest;
import org.greatfree.message.multicast.container.Notification;
import org.greatfree.message.multicast.container.Request;
import org.greatfree.message.multicast.container.Response;

/*
 * The program defines the task of a child of the task cluster to be accomplished. 09/06/2020, Bing Li
 */

// Created: 09/06/2020, Bing Li
class TaskChildTask implements ChildTask
{

	@Override
	public void processNotification(Notification notification)
	{
		switch (notification.getApplicationID())
		{
			case ScalableApplicationID.STOP_TASK_CLUSTER_NOTIFICATION:
				System.out.println("STOP_TASK_CLUSTER_NOTIFICATION received @" + Calendar.getInstance().getTime());
				try
				{
					TaskChild.TASK().stop(ServerConfig.SERVER_SHUTDOWN_TIMEOUT);
				}
				catch (ClassNotFoundException | IOException | InterruptedException | RemoteReadException e)
				{
					e.printStackTrace();
				}
				break;
				
			case ScalableApplicationID.STOP_POOL_CLUSTER_NOTIFICATION:
				System.out.println("STOP_POOL_CLUSTER_NOTIFICATION received @" + Calendar.getInstance().getTime());
				try
				{
					TaskChild.TASK().stop(ServerConfig.SERVER_SHUTDOWN_TIMEOUT);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InterChildrenNotification prepareNotification(IntercastNotification notification)
	{
		// TODO Auto-generated method stub
		return null;
	}

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
