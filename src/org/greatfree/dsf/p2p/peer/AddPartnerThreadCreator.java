package org.greatfree.dsf.p2p.peer;

import org.greatfree.concurrency.reactive.NotificationThreadCreatable;
import org.greatfree.dsf.p2p.message.AddPartnerNotification;

// Created: 05/02/2017, Bing Li
class AddPartnerThreadCreator implements NotificationThreadCreatable<AddPartnerNotification, AddPartnerThread>
{

	@Override
	public AddPartnerThread createNotificationThreadInstance(int taskSize)
	{
		// TODO Auto-generated method stub
		return new AddPartnerThread(taskSize);
	}

}
