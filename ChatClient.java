import ChatApp.*;          // The package containing our stubs
import org.omg.CosNaming.*; // HelloClient will use the naming service.
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;     // All CORBA applications need these classes.
import org.omg.PortableServer.*;   
import org.omg.PortableServer.POA;
import java.io.*;

 
class ChatCallbackImpl extends ChatCallbackPOA
{
    private ORB orb;

    public void setORB(ORB orb_val) {
        orb = orb_val;
    }

    public void callback(String notification)
    {
        System.out.println(notification);
    }
}

public class ChatClient
{
    static Chat chatImpl;
    
    public static void main(String args[])
    {
	try {
	    // create and initialize the ORB
	    ORB orb = ORB.init(args, null);

	    // create servant (impl) and register it with the ORB
	    ChatCallbackImpl chatCallbackImpl = new ChatCallbackImpl();
	    chatCallbackImpl.setORB(orb);

	    // get reference to RootPOA and activate the POAManager
	    POA rootpoa = 
		POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();
	    
	    // get the root naming context 
	    org.omg.CORBA.Object objRef = 
		orb.resolve_initial_references("NameService");
	    NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
	    
	    // resolve the object reference in naming
	    String name = "Chat";
	    chatImpl = ChatHelper.narrow(ncRef.resolve_str(name));
	    
	    // obtain callback reference for registration w/ server
	    org.omg.CORBA.Object ref = 
		rootpoa.servant_to_reference(chatCallbackImpl);
	    ChatCallback cref = ChatCallbackHelper.narrow(ref);
	    
	    // Application code goes below
            String usr = "";
            while (true) {
              BufferedReader stdin = new BufferedReader
                (new InputStreamReader (System.in));
              
              String msg = stdin.readLine();
              String cmd = msg.split(" ")[0];
              String newmsg[] = msg.split(" ");
              newmsg[0] = "";
              String message = "";
              for(int i = 1; i < newmsg.length - 1; i++) {
                message += (newmsg[i] + " ");
                //System.out.println(newmsg[i]);
              }
              message += newmsg[newmsg.length - 1];
              
              //System.out.println(cmd.equals("join"));
              if(cmd.equals("join")) {
                if(usr == "") {
                  usr = (chatImpl.join(cref, newmsg[1]));
                  if(usr.equals("")) {
                    System.out.println("Username in use!");
                  }
                  else {
                    System.out.println("Welcome " + usr);
                  }
                }
                else {
                  System.out.println("You are already joined");
                }
              }
              else if(cmd.equals("post")) {
                if(usr != "")
                  chatImpl.say(cref, message, usr);
                else
                  System.out.println("You have to join before you can chat");
              }
              else if(cmd.equals("list")) {
                System.out.println(chatImpl.list());
              }
            }
	    
	} catch(Exception e){
	    System.out.println("ERROR : " + e);
	    e.printStackTrace(System.out);
	}
    }
}
