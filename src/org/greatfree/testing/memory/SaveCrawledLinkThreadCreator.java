package org.greatfree.testing.memory;

import org.greatfree.concurrency.reactive.NotificationThreadCreatable;
import org.greatfree.testing.message.AddCrawledLinkNotification;

/*
 * The code here attempts to create instances of SaveCrawledLinkThread. It is used by the notification dispatcher. 11/28/2014, Bing Li
 */

// Created: 11/28/2014, Bing Li
public class SaveCrawledLinkThreadCreator implements NotificationThreadCreatable<AddCrawledLinkNotification, SaveCrawledLinkThread>
{
	@Override
	public SaveCrawledLinkThread createNotificationThreadInstance(int taskSize)
	{
		return new SaveCrawledLinkThread(taskSize);
	}
}