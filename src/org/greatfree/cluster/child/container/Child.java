package org.greatfree.cluster.child.container;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.greatfree.cluster.message.IsRootOnlineResponse;
import org.greatfree.cluster.message.JoinNotification;
import org.greatfree.cluster.message.LeaveNotification;
import org.greatfree.dip.container.p2p.message.ChatRegistryRequest;
import org.greatfree.dip.container.p2p.message.IsRootOnlineRequest;
import org.greatfree.dip.container.p2p.message.LeaveClusterNotification;
import org.greatfree.dip.p2p.RegistryConfig;
import org.greatfree.exceptions.DistributedNodeFailedException;
import org.greatfree.exceptions.RemoteReadException;
import org.greatfree.message.SystemMessageConfig;
import org.greatfree.message.multicast.MulticastMessage;
import org.greatfree.message.multicast.MulticastMessageType;
import org.greatfree.message.multicast.MulticastRequest;
import org.greatfree.message.multicast.MulticastResponse;
import org.greatfree.message.multicast.container.ChildResponse;
import org.greatfree.message.multicast.container.InterChildrenNotification;
import org.greatfree.message.multicast.container.InterChildrenRequest;
import org.greatfree.message.multicast.container.Notification;
import org.greatfree.message.multicast.container.Request;
import org.greatfree.message.multicast.container.Response;
import org.greatfree.multicast.child.ChildClient;
import org.greatfree.multicast.root.RootClient;
import org.greatfree.server.container.Peer;
import org.greatfree.server.container.PeerProfile;
import org.greatfree.server.container.ServerProfile;
import org.greatfree.server.container.Peer.PeerBuilder;
import org.greatfree.util.IPAddress;
import org.greatfree.util.UtilConfig;

/*
 * The reason to design the class, Child, intends to connect with the registry server which is implemented in the container manner. 01/13/2019, Bing Li
 */

// Created: 01/13/2019, Bing Li
class Child
{
	// The IP address of the cluster root. 06/15/2017, Bing Li
	private String rootKey;
	private IPAddress rootAddress;
	private Peer<ChildDispatcher> child;
	private ChildClient client;

	/*
	 * The class is added to the child. That is an interesting design which supports the client's to perform inter-multicasting. 02/28/2019, Bing Li
	 */
	private RootClient subRootClient;

	private Child()
	{
	}
	
	private static Child instance = new Child();
	
	public static Child CONTAINER()
	{
		if (instance == null)
		{
			instance = new Child();
			return instance;
		}
		else
		{
			return instance;
		}
	}

	public void dispose(long timeout) throws IOException, InterruptedException, ClassNotFoundException, RemoteReadException
	{
		this.child.syncNotify(this.rootAddress.getIP(), this.rootAddress.getPort(), new LeaveNotification(this.child.getPeerID()));
		if (!ServerProfile.CS().isDefault())
		{
			this.child.syncNotify(PeerProfile.P2P().getRegistryServerIP(), PeerProfile.P2P().getRegistryServerPort(), new LeaveClusterNotification(this.rootKey, this.child.getPeerID()));
		}
		else
		{
			this.child.syncNotify(RegistryConfig.PEER_REGISTRY_ADDRESS, RegistryConfig.PEER_REGISTRY_PORT, new LeaveClusterNotification(this.rootKey, this.child.getPeerID()));
		}
		this.child.stop(timeout);
		this.client.close();
		this.subRootClient.close();
	}

	public void init(PeerBuilder<ChildDispatcher> builder,  int rootBranchCount, int treeBranchCount, long waitTime) throws IOException
	{
		this.child = new Peer<ChildDispatcher>(builder);
		this.client = new ChildClient(this.child.getLocalIPKey(), this.child.getClientPool(), treeBranchCount, this.child.getPool());
		this.subRootClient = new RootClient(this.child.getClientPool(), rootBranchCount, treeBranchCount, waitTime, this.child.getPool());
	}

//	public void start(String rootKey, int treeBranchCount) throws ClassNotFoundException, RemoteReadException, IOException, InterruptedException
	public void start(String rootKey) throws ClassNotFoundException, RemoteReadException, IOException, InterruptedException
	{
		this.rootKey = rootKey;
		this.child.start();
		IsRootOnlineResponse response;
		// Register the peer to the chatting registry. 06/15/2017, Bing Li
		if (!ServerProfile.CS().isDefault())
		{
			this.child.read(PeerProfile.P2P().getRegistryServerIP(), PeerProfile.P2P().getRegistryServerPort(), new ChatRegistryRequest(this.child.getPeerID()));
			response = (IsRootOnlineResponse)this.child.read(PeerProfile.P2P().getRegistryServerIP(), PeerProfile.P2P().getRegistryServerPort(), new IsRootOnlineRequest(rootKey, this.child.getPeerID()));
		}
		else
		{
			this.child.read(RegistryConfig.PEER_REGISTRY_ADDRESS, RegistryConfig.PEER_REGISTRY_PORT, new ChatRegistryRequest(this.child.getPeerID()));
			response = (IsRootOnlineResponse)this.child.read(RegistryConfig.PEER_REGISTRY_ADDRESS, RegistryConfig.PEER_REGISTRY_PORT, new IsRootOnlineRequest(rootKey, this.child.getPeerID()));
		}
		if (response.isOnline())
		{
//			System.out.println("ClusterChild-start(): root address = " + response.getRootAddress().getIP() + ":" + response.getRootAddress().getPort());
			this.setRootIP(response.getRootAddress());
//			this.child.syncNotify(this.rootAddress.getIP(), this.rootAddress.getPort(), new JoinNotification(this.child.getPeerID()));
			this.joinCluster();
			System.out.println("ClusterChild-start(): done!");
		}
		else
		{
			System.out.println("ClusterChild-start(): root is not online! ");
		}
	}

	/*
	public String getRootKey()
	{
		return this.rootKey;
	}
	*/
	
	public String getChildKey()
	{
		return this.child.getPeerID();
	}

	public void joinCluster() throws IOException, InterruptedException
	{
		this.child.syncNotify(this.rootAddress.getIP(), this.rootAddress.getPort(), new JoinNotification(this.child.getPeerID()));
	}

	/*
	 * Keep the root IP address. 05/20/2017, Bing Li
	 */
	public void setRootIP(IPAddress rootAddress)
	{
		this.rootAddress = rootAddress;
	}
	
	/*
	public IPAddress getIPAddress()
	{
		return this.rootAddress;
	}
	*/
	
	public String getChildIP()
	{
		return this.child.getPeerIP();
	}
	
	public int getChildPort()
	{
		return this.child.getPort();
	}
	
	public void addPartnerIP(IPAddress ip)
	{
		this.child.addPartners(ip.getIP(), ip.getPort());
	}
	
	public void addPartnerIPs(Set<IPAddress> ips)
	{
		for (IPAddress entry : ips)
		{
			this.child.addPartners(entry.getIP(), entry.getPort());
		}
	}
	
	public void forward(Notification notification)
	{
		if (notification.getNotificationType() == MulticastMessageType.BROADCAST_NOTIFICATION)
		{
			this.asyncNotify(notification);
		}
	}

	/*
	 * The method is added to forward intercasting notifications. 04/26/2019, Bing Li
	 */
	public void forward(InterChildrenNotification notification)
	{
		if (notification.getIntercastNotification().getIntercastType() == MulticastMessageType.INTER_BROADCAST_NOTIFICATION)
		{
			this.asyncNotify(notification);
		}
	}
	
	public void forward(Request request)
	{
		if (request.getRequestType() == MulticastMessageType.BROADCAST_REQUEST || request.getRequestType() == MulticastMessageType.INTER_BROADCAST_REQUEST)
		{
			this.asyncRead(request);
		}
	}

	/*
	 * The method is added to forward intercasting notifications. 04/26/2019, Bing Li
	 */
	public void forward(InterChildrenRequest request)
	{
		if (request.getIntercastRequest().getIntercastType() == MulticastMessageType.INTER_BROADCAST_REQUEST)
		{
			this.asyncRead(request);
		}
	}

	/*
	 * The method is necessary? 04/26/2019, Bing Li
	 */
	/*
	public void forward(IntercastNotification notification)
	{
		if (notification.getIntercastType() == MulticastMessageType.INTER_BROADCAST_NOTIFICATION)
		{

		}
	}
	*/

	public void notify(MulticastMessage notification) throws InstantiationException, IllegalAccessException, IOException, InterruptedException, DistributedNodeFailedException
	{
		this.client.notify(notification);
	}
	
	public void asyncNotify(MulticastMessage notification)
	{
		this.client.asynNotify(notification);
	}
	
	public void read(MulticastRequest request) throws InstantiationException, IllegalAccessException, IOException, InterruptedException, DistributedNodeFailedException
	{
		this.client.read(request);
	}
	
	public void asyncRead(MulticastRequest request)
	{
		this.client.asyncRead(request);
	}

	/*
	 * The method notifies the root for the ordinary responses upon one multicasting request. 03/02/2019, Bing Li
	 */
	public void notifyRoot(MulticastResponse response) throws IOException, InterruptedException
	{
		/*
		 * Using the ChildResponse rather than MulticastResponse aims to identify the response from children such that those messages can be collected by the rendezvous point. 03/04/2019, Bing Li
		 */
		this.child.syncNotify(this.rootAddress.getIP(), this.rootAddress.getPort(), new ChildResponse(response));
	}
	
	/*
	 * The method notifies the root for the intercasting responses. 03/02/2019, Bing Li
	 */
	public void notifyRoot(Response response) throws IOException, InterruptedException
	{
		this.child.syncNotify(this.rootAddress.getIP(), this.rootAddress.getPort(), response);
	}
	
	/*
	public void notifyRoot(InterChildrenNotification notification) throws IOException, InterruptedException
	{
		this.child.syncNotify(this.rootAddress.getIP(), this.rootAddress.getPort(), notification);
	}
	
	public void notifyRoot(InterChildrenRequest request) throws IOException, InterruptedException
	{
		this.child.syncNotify(this.rootAddress.getIP(), this.rootAddress.getPort(), request);
	}
	*/
	
	/*
	 * Now I need to implement the root based intercasting. So the method is not necessary temporarily. I will implement the children-based intercasing later. 02/15/2019, Bing Li 
	 */
	/*
	public void notifyChild(IntercastNotification notification) throws IOException, InterruptedException
	{
		this.child.syncNotify(notification.getDestinationIP().getIP(), notification.getDestinationIP().getPort(), notification);
	}
	*/
	
	/*
	 * Now I need to implement the root based intercasting. So the method is not necessary temporarily. I will implement the children-based intercasing later. 02/15/2019, Bing Li 
	 */
	/*
	public ServerMessage readChild(IntercastRequest request) throws ClassNotFoundException, RemoteReadException, IOException
	{
		return this.child.read(request.getDestinationIP().getIP(), request.getDestinationIP().getPort(), request);
	}
	*/
	
	/*
	 * ------------------------------ The below methods are designed for intercasting. 03/04/2019, Bing Li ------------------------------ 
	 */

	/*
	 * The below methods, notifySubRoot() & saveResponse(), are counterparts to attain the goal of multicasting-reading within a cluster. 03/14/2019, Bing Li
	 */
	public void notifySubRoot(String ip, int port, MulticastResponse response) throws IOException, InterruptedException
	{
		/*
		 * Using the ChildResponse rather than MulticastResponse aims to identify the response from children such that those messages can be collected by the rendezvous point. The MulticastResponse itself has no parameters to do the same thing. 03/04/2019, Bing Li
		 */
		this.child.syncNotify(ip, port, new ChildResponse(response));
	}

	public void saveResponse(ChildResponse response) throws InterruptedException
	{
		this.subRootClient.getRP().saveResponse(response.getResponse());
	}

	/*
	public ServerMessage read(InterChildrenRequest request) throws ClassNotFoundException, RemoteReadException, IOException
	{
		return this.child.read(this.rootAddress.getIP(), this.rootAddress.getPort(), request);
	}
	*/

	
	/*
	 * The method is written in the aircraft from Zhuhai to Xi'An. 03/02/2019, Bing Li
	 */
	/*
	public ServerMessage interUnicastRead(InterChildrenRequest icr) throws ClassNotFoundException, RemoteReadException, IOException
	{
		return this.child.read(icr.getIntercastRequest().getDestinationIP().getIP(), icr.getIntercastRequest().getDestinationIP().getPort(), icr);
	}
	*/

	/*
	 * The method is written in the aircraft from Zhuhai to Xi'An. 03/02/2019, Bing Li
	 */
//	public void interUnicastNotify(IntercastNotification in) throws IOException, InterruptedException
	public void interUnicastNotify(InterChildrenNotification icn) throws IOException, InterruptedException
	{
		this.child.syncNotify(icn.getIntercastNotification().getDestinationIP().getIP(), icn.getIntercastNotification().getDestinationIP().getPort(), icn);
	}

	/*
	 * The method is written in the aircraft from Zhuhai to Xi'An. 03/02/2019, Bing Li
	 */
	public void interAnycastNotify(InterChildrenNotification icn) throws IOException, DistributedNodeFailedException
	{
		/*
		 * The children keys are not IP keys but user keys generated by application-level IDs. So it should be converted by the root. 02/28/2019, Bing Li
		 */
		this.subRootClient.anycastNotify(icn, icn.getIntercastNotification().getChildDestinations().keySet());
	}
	
	/*
	 * The method is written in the aircraft from Zhuhai to Xi'An. 03/02/2019, Bing Li
	 */
//	public void interBroadcastNotify(IntercastNotification in) throws IOException, DistributedNodeFailedException
	public void interBroadcastNotify(InterChildrenNotification icn) throws IOException, DistributedNodeFailedException
	{
		/*
		 * The children keys are not IP keys but user keys generated by application-level IDs. So it should be converted by the root. 02/28/2019, Bing Li
		 */
		System.out.println("Child-interBroadcastNotify(): destination keys size = " + icn.getIntercastNotification().getChildDestinations().keySet().size());
		this.subRootClient.broadcastNotify(icn, icn.getIntercastNotification().getChildDestinations().keySet());
	}
	
	/*
	public Response interUnicastRead(InterChildrenRequest icr)
	{
		return new Response(this.subRootClient.unicastNearestRead(icr.getIntercastRequest().getDestinationKey(), icr));
	}
	*/
	
	public Response interUnicastRead(InterChildrenRequest icr) throws IOException, DistributedNodeFailedException
	{
		String childIPKey = UtilConfig.EMPTY_STRING;
		for (Map.Entry<String, Set<String>> entry : icr.getIntercastRequest().getChildDestinations().entrySet())
		{
			childIPKey = entry.getKey();
			break;
		}
		if (!childIPKey.equals(UtilConfig.EMPTY_STRING))
		{
//			return new Response(this.subRootClient.unicastRead(icr, childIPKey));
			return new Response(icr.getApplicationID(), this.subRootClient.unicastRead(icr, childIPKey));
		}
		return SystemMessageConfig.NO_RESPONSE;
	}
	
	public Response interBroadcastRead(InterChildrenRequest icr) throws DistributedNodeFailedException, IOException
	{
//		return new Response(this.subRootClient.broadcastRead(icr, icr.getIntercastRequest().getChildDestinations().keySet()));
		return new Response(icr.getApplicationID(), this.subRootClient.broadcastRead(icr, icr.getIntercastRequest().getChildDestinations().keySet()));
	}
	
	public Response interAnycastRead(InterChildrenRequest icr) throws DistributedNodeFailedException, IOException
	{
//		return new Response(this.subRootClient.anycastRead(icr, icr.getIntercastRequest().getChildDestinations().keySet()));
		return new Response(icr.getApplicationID(), this.subRootClient.anycastRead(icr, icr.getIntercastRequest().getChildDestinations().keySet()));
	}
}