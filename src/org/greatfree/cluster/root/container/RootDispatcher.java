package org.greatfree.cluster.root.container;

import java.util.Calendar;

import org.greatfree.client.OutMessageStream;
import org.greatfree.cluster.message.ClusterMessageType;
import org.greatfree.cluster.message.JoinNotification;
import org.greatfree.cluster.message.LeaveNotification;
import org.greatfree.concurrency.reactive.NotificationDispatcher;
import org.greatfree.concurrency.reactive.RequestDispatcher;
import org.greatfree.data.ServerConfig;
import org.greatfree.message.ServerMessage;
import org.greatfree.message.multicast.MulticastMessageType;
import org.greatfree.message.multicast.container.ChildResponse;
import org.greatfree.message.multicast.container.IntercastNotification;
import org.greatfree.message.multicast.container.IntercastRequest;
import org.greatfree.message.multicast.container.IntercastRequestStream;
import org.greatfree.message.multicast.container.Notification;
import org.greatfree.message.multicast.container.Request;
import org.greatfree.message.multicast.container.RequestStream;
import org.greatfree.message.multicast.container.Response;
import org.greatfree.server.ServerDispatcher;

// Created: 01/13/2019, Bing Li
class RootDispatcher extends ServerDispatcher<ServerMessage>
{
	private NotificationDispatcher<JoinNotification, JoinNotificationThread, JoinNotificationThreadCreator> joinNotificationDispatcher;
	private NotificationDispatcher<LeaveNotification, LeaveNotificationThread, LeaveNotificationThreadCreator> leaveNotificationDispatcher;

	private NotificationDispatcher<Notification, RootNotificationThread, RootNotificationThreadCreator> notificationDispatcher;
	private RequestDispatcher<Request, RequestStream, Response, RootRequestThread, RootRequestThreadCreator> requestDispatcher;

	private NotificationDispatcher<ChildResponse, ChildResponseThread, ChildResponseThreadCreator> multicastResponseDispatcher;
	
	private NotificationDispatcher<IntercastNotification, IntercastNotificationThread, IntercastNotificationThreadCreator> intercastNotificationDispatcher;
	private RequestDispatcher<IntercastRequest, IntercastRequestStream, Response, IntercastRequestThread, IntercastRequestThreadCreator> intercastRequestDispatcher;

//	public RootDispatcher(int schedulerPoolSize, long schedulerKeepAliveTime)
	public RootDispatcher(int serverThreadPoolSize, long serverThreadKeepAliveTime, int schedulerPoolSize, long schedulerKeepAliveTime)
	{
//		super(schedulerPoolSize, schedulerKeepAliveTime);
		super(serverThreadPoolSize, serverThreadKeepAliveTime, schedulerPoolSize, schedulerKeepAliveTime);

		this.joinNotificationDispatcher = new NotificationDispatcher.NotificationDispatcherBuilder<JoinNotification, JoinNotificationThread, JoinNotificationThreadCreator>()
				.poolSize(ServerConfig.NOTIFICATION_DISPATCHER_POOL_SIZE)
				.threadCreator(new JoinNotificationThreadCreator())
				.notificationQueueSize(ServerConfig.NOTIFICATION_QUEUE_SIZE)
				.dispatcherWaitTime(ServerConfig.NOTIFICATION_DISPATCHER_WAIT_TIME)
				.waitRound(ServerConfig.NOTIFICATION_DISPATCHER_WAIT_ROUND)
				.idleCheckDelay(ServerConfig.NOTIFICATION_DISPATCHER_IDLE_CHECK_DELAY)
				.idleCheckPeriod(ServerConfig.NOTIFICATION_DISPATCHER_IDLE_CHECK_PERIOD)
				.scheduler(super.getScheduler())
				.build();

		this.leaveNotificationDispatcher = new NotificationDispatcher.NotificationDispatcherBuilder<LeaveNotification, LeaveNotificationThread, LeaveNotificationThreadCreator>()
				.poolSize(ServerConfig.NOTIFICATION_DISPATCHER_POOL_SIZE)
				.threadCreator(new LeaveNotificationThreadCreator())
				.notificationQueueSize(ServerConfig.NOTIFICATION_QUEUE_SIZE)
				.dispatcherWaitTime(ServerConfig.NOTIFICATION_DISPATCHER_WAIT_TIME)
				.waitRound(ServerConfig.NOTIFICATION_DISPATCHER_WAIT_ROUND)
				.idleCheckDelay(ServerConfig.NOTIFICATION_DISPATCHER_IDLE_CHECK_DELAY)
				.idleCheckPeriod(ServerConfig.NOTIFICATION_DISPATCHER_IDLE_CHECK_PERIOD)
				.scheduler(super.getScheduler())
				.build();

		this.notificationDispatcher = new NotificationDispatcher.NotificationDispatcherBuilder<Notification, RootNotificationThread, RootNotificationThreadCreator>()
				.poolSize(ServerConfig.NOTIFICATION_DISPATCHER_POOL_SIZE)
				.threadCreator(new RootNotificationThreadCreator())
				.notificationQueueSize(ServerConfig.NOTIFICATION_QUEUE_SIZE)
				.dispatcherWaitTime(ServerConfig.NOTIFICATION_DISPATCHER_WAIT_TIME)
				.waitRound(ServerConfig.NOTIFICATION_DISPATCHER_WAIT_ROUND)
				.idleCheckDelay(ServerConfig.NOTIFICATION_DISPATCHER_IDLE_CHECK_DELAY)
				.idleCheckPeriod(ServerConfig.NOTIFICATION_DISPATCHER_IDLE_CHECK_PERIOD)
				.scheduler(super.getScheduler())
				.build();

		this.requestDispatcher = new RequestDispatcher.RequestDispatcherBuilder<Request, RequestStream, Response, RootRequestThread, RootRequestThreadCreator>()
				.poolSize(ServerConfig.REQUEST_DISPATCHER_POOL_SIZE)
				.threadCreator(new RootRequestThreadCreator())
				.requestQueueSize(ServerConfig.REQUEST_QUEUE_SIZE)
				.dispatcherWaitTime(ServerConfig.REQUEST_DISPATCHER_WAIT_TIME)
				.waitRound(ServerConfig.REQUEST_DISPATCHER_WAIT_ROUND)
				.idleCheckDelay(ServerConfig.REQUEST_DISPATCHER_IDLE_CHECK_DELAY)
				.idleCheckPeriod(ServerConfig.REQUEST_DISPATCHER_IDLE_CHECK_PERIOD)
				.scheduler(super.getScheduler())
				.build();
		
		this.multicastResponseDispatcher = new NotificationDispatcher.NotificationDispatcherBuilder<ChildResponse, ChildResponseThread, ChildResponseThreadCreator>()
				.poolSize(ServerConfig.NOTIFICATION_DISPATCHER_POOL_SIZE)
				.threadCreator(new ChildResponseThreadCreator())
				.notificationQueueSize(ServerConfig.NOTIFICATION_QUEUE_SIZE)
				.dispatcherWaitTime(ServerConfig.NOTIFICATION_DISPATCHER_WAIT_TIME)
				.waitRound(ServerConfig.NOTIFICATION_DISPATCHER_WAIT_ROUND)
				.idleCheckDelay(ServerConfig.NOTIFICATION_DISPATCHER_IDLE_CHECK_DELAY)
				.idleCheckPeriod(ServerConfig.NOTIFICATION_DISPATCHER_IDLE_CHECK_PERIOD)
				.scheduler(super.getScheduler())
				.build();

		this.intercastNotificationDispatcher = new NotificationDispatcher.NotificationDispatcherBuilder<IntercastNotification, IntercastNotificationThread, IntercastNotificationThreadCreator>()
				.poolSize(ServerConfig.NOTIFICATION_DISPATCHER_POOL_SIZE)
				.threadCreator(new IntercastNotificationThreadCreator())
				.notificationQueueSize(ServerConfig.NOTIFICATION_QUEUE_SIZE)
				.dispatcherWaitTime(ServerConfig.NOTIFICATION_DISPATCHER_WAIT_TIME)
				.waitRound(ServerConfig.NOTIFICATION_DISPATCHER_WAIT_ROUND)
				.idleCheckDelay(ServerConfig.NOTIFICATION_DISPATCHER_IDLE_CHECK_DELAY)
				.idleCheckPeriod(ServerConfig.NOTIFICATION_DISPATCHER_IDLE_CHECK_PERIOD)
				.scheduler(super.getScheduler())
				.build();

		this.intercastRequestDispatcher = new RequestDispatcher.RequestDispatcherBuilder<IntercastRequest, IntercastRequestStream, Response, IntercastRequestThread, IntercastRequestThreadCreator>()
				.poolSize(ServerConfig.REQUEST_DISPATCHER_POOL_SIZE)
				.threadCreator(new IntercastRequestThreadCreator())
				.requestQueueSize(ServerConfig.REQUEST_QUEUE_SIZE)
				.dispatcherWaitTime(ServerConfig.REQUEST_DISPATCHER_WAIT_TIME)
				.waitRound(ServerConfig.REQUEST_DISPATCHER_WAIT_ROUND)
				.idleCheckDelay(ServerConfig.REQUEST_DISPATCHER_IDLE_CHECK_DELAY)
				.idleCheckPeriod(ServerConfig.REQUEST_DISPATCHER_IDLE_CHECK_PERIOD)
				.scheduler(super.getScheduler())
				.build();
	}

	@Override
	public void dispose(long timeout) throws InterruptedException
	{
		super.shutdown(timeout);
		this.joinNotificationDispatcher.dispose();
		this.leaveNotificationDispatcher.dispose();
		this.notificationDispatcher.dispose();
		this.requestDispatcher.dispose();
		this.multicastResponseDispatcher.dispose();
		this.intercastNotificationDispatcher.dispose();
		this.intercastRequestDispatcher.dispose();
	}

	@Override
	public void process(OutMessageStream<ServerMessage> message)
	{
		switch (message.getMessage().getType())
		{
			case ClusterMessageType.JOIN_NOTIFICATION:
				System.out.println("JOIN_NOTIFICATION received @" + Calendar.getInstance().getTime());
				if (!this.joinNotificationDispatcher.isReady())
				{
					// Execute the notification dispatcher concurrently. 02/15/2016, Bing Li
					super.execute(this.joinNotificationDispatcher);
				}
				// Enqueue the instance of Notification into the dispatcher for concurrent processing. 02/15/2016, Bing Li
				this.joinNotificationDispatcher.enqueue((JoinNotification)message.getMessage());
				break;

			case ClusterMessageType.LEAVE_NOTIFICATION:
				System.out.println("LEAVE_NOTIFICATION received @" + Calendar.getInstance().getTime());
				if (!this.leaveNotificationDispatcher.isReady())
				{
					// Execute the notification dispatcher concurrently. 02/15/2016, Bing Li
					super.execute(this.leaveNotificationDispatcher);
				}
				// Enqueue the instance of Notification into the dispatcher for concurrent processing. 02/15/2016, Bing Li
				this.leaveNotificationDispatcher.enqueue((LeaveNotification)message.getMessage());
				break;

			case MulticastMessageType.NOTIFICATION:
				System.out.println("NOTIFICATION received @" + Calendar.getInstance().getTime());
				if (!this.notificationDispatcher.isReady())
				{
					// Execute the notification dispatcher concurrently. 02/15/2016, Bing Li
					super.execute(this.notificationDispatcher);
				}
				// Enqueue the instance of Notification into the dispatcher for concurrent processing. 02/15/2016, Bing Li
				this.notificationDispatcher.enqueue((Notification)message.getMessage());
				break;

			case MulticastMessageType.REQUEST:
				System.out.println("REQUEST received @" + Calendar.getInstance().getTime());
				// Check whether the shutdown notification dispatcher is ready or not. 02/15/2016, Bing Li
				if (!this.requestDispatcher.isReady())
				{
					// Execute the notification dispatcher concurrently. 02/15/2016, Bing Li
					super.execute(this.requestDispatcher);
				}
				// Enqueue the instance of Request into the dispatcher for concurrent processing. 02/15/2016, Bing Li
				this.requestDispatcher.enqueue(new RequestStream(message.getOutStream(), message.getLock(), (Request)message.getMessage()));
				break;

			case ClusterMessageType.CHILD_RESPONSE:
				System.out.println("CHILD_RESPONSE received @" + Calendar.getInstance().getTime());
				if (!this.multicastResponseDispatcher.isReady())
				{
					// Execute the notification dispatcher concurrently. 02/15/2016, Bing Li
					super.execute(this.multicastResponseDispatcher);
				}
				// Enqueue the instance of Notification into the dispatcher for concurrent processing. 02/15/2016, Bing Li
				this.multicastResponseDispatcher.enqueue((ChildResponse)message.getMessage());
				break;
				
			case MulticastMessageType.INTERCAST_NOTIFICATION:
				System.out.println("INTERCAST_NOTIFICATION received @" + Calendar.getInstance().getTime());
				if (!this.intercastNotificationDispatcher.isReady())
				{
					// Execute the notification dispatcher concurrently. 02/15/2016, Bing Li
					super.execute(this.intercastNotificationDispatcher);
				}
				// Enqueue the instance of Notification into the dispatcher for concurrent processing. 02/15/2016, Bing Li
				this.intercastNotificationDispatcher.enqueue((IntercastNotification)message.getMessage());
				break;
				
			case MulticastMessageType.INTERCAST_REQUEST:
				System.out.println("INTERCAST_REQUEST received @" + Calendar.getInstance().getTime());
				// Check whether the shutdown notification dispatcher is ready or not. 02/15/2016, Bing Li
				if (!this.intercastRequestDispatcher.isReady())
				{
					// Execute the notification dispatcher concurrently. 02/15/2016, Bing Li
					super.execute(this.intercastRequestDispatcher);
				}
				// Enqueue the instance of Request into the dispatcher for concurrent processing. 02/15/2016, Bing Li
				this.intercastRequestDispatcher.enqueue(new IntercastRequestStream(message.getOutStream(), message.getLock(), (IntercastRequest)message.getMessage()));
				break;
		}
	}

}