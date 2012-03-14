import ChatApp.*;          // The package containing our stubs. 
import org.omg.CosNaming.*; // HelloServer will use the naming service. 
import org.omg.CosNaming.NamingContextPackage.*; // ..for exceptions. 
import org.omg.CORBA.*;     // All CORBA applications need these classes. 
import org.omg.PortableServer.*;   
import org.omg.PortableServer.POA;
import java.util.*;
 
class ChatImpl extends ChatPOA
{
  private ORB orb;
  private ArrayList userlist = new ArrayList();
  private ChatCallback[] objlist = new ChatCallback[34];
  private int objindex = 0;
  
  public void setORB(ORB orb_val) {
    orb = orb_val;
  }
  
  public void say(ChatCallback callobj, String msg, String usr)
  {
    for(int i = 0; i < objindex; i++) {
      objlist[i].callback(usr + " says: " + msg);
    }
  }
  public String join(ChatCallback callobj, String user)
  {
    for(int i = 0; i < userlist.size(); i++) {
      if(user.equals(userlist.get(i))) {
        return("");
      }
    }
    for(int i = 0; i < objindex; i++) {
      objlist[i].callback("* " + user + " joined the chat.");
    }
    userlist.add(user);
    objlist[objindex] = callobj;
    objindex += 1;
    return(user);
    //System.out.println("Content = " + userlist);
  }
  public String list() {
    String users = "List of registered users:\n";
    for(int i = 0; i < userlist.size(); i++) {
      users += (userlist.get(i) + "\n");
    }
    return users;
  }
}

  public class ChatServer 
  {
    public static void main(String args[]) 
    {
      try { 
        // create and initialize the ORB
        ORB orb = ORB.init(args, null); 

        // create servant (impl) and register it with the ORB
        ChatImpl chatImpl = new ChatImpl();
        chatImpl.setORB(orb); 

        // get reference to rootpoa & activate the POAManager
        POA rootpoa = 
          POAHelper.narrow(orb.resolve_initial_references("RootPOA"));  
        rootpoa.the_POAManager().activate(); 

        // get the root naming context
        org.omg.CORBA.Object objRef = 
          orb.resolve_initial_references("NameService");
        NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

        // obtain object reference from the servant (impl)
        org.omg.CORBA.Object ref = 
          rootpoa.servant_to_reference(chatImpl);
        Chat cref = ChatHelper.narrow(ref);

        // bind the object reference in naming
        String name = "Chat";
        NameComponent path[] = ncRef.to_name(name);
        ncRef.rebind(path, cref);

        // Application code goes below
        System.out.println("ChatServer ready and waiting ...");
	
        // wait for invocations from clients
        orb.run();
      }
	    
      catch(Exception e) {
        System.err.println("ERROR : " + e);
        e.printStackTrace(System.out);
      }

      System.out.println("ChatServer Exiting ...");
    }

  }
