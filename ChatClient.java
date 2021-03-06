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
  
  public void othcallback(String msg) {
    System.out.print(msg);
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
              // Input from client
              BufferedReader stdin = new BufferedReader
                (new InputStreamReader (System.in));
              
              // Splitting input to see command
              String msg = stdin.readLine();
              String cmd = msg.split(" ")[0];
              String newmsg[] = msg.split(" ");
              newmsg[0] = "";
              String message = "";
              // Putting together the message that was split.
              for(int i = 1; i < newmsg.length - 1; i++) {
                message += (newmsg[i] + " ");
              }
              message += newmsg[newmsg.length - 1];
              // Check if join
              if(cmd.equals("join")) {
                if(usr == "") {
                  usr = (chatImpl.join(cref, newmsg[1]));
                  // Check if still no username, (caused by username in use).
                  if(usr.equals("")) {
                    System.out.println("Username in use!");
                  }
                  // Welcome chatter.
                  else {
                    System.out.println("Welcome " + usr);
                  }
                }
                // If allready joined.
                else {
                  System.out.println("* You are already joined");
                }
              }
              // Check for command post.
              else if(cmd.equals("post")) {
                // user can not be empty. must have joined.
                if(usr != "")
                  chatImpl.say(cref, message, usr);
                else
                  System.out.println("* You have to join before you can chat");
              }
              // Check for list command. no need to have joined.
              else if(cmd.equals("list")) {
                System.out.println(chatImpl.list());
              }
              // Check if othello, must join chat before playing.
              else if(cmd.equals("othello")) {
                if(usr != "")
                  chatImpl.othello(cref, message, usr);
                else
                  System.out.println("* You have to join the chat before you can play");
              }
              else if(cmd.equals("move")) {
                if(usr != "")
                  chatImpl.move(newmsg[1], usr);
              }
              // Check leave command, user must be joined
              else if(cmd.equals("leave")) {
                if(usr.equals("")) {
                  System.out.println("* You have to join before you can leave");
                }
                // Call leave function
                else {
                  chatImpl.leave(usr);
                  usr = "";
                }
              }
              // Check help command, prints help with commands.
              else if(cmd.equals("help")) {
                System.out.println("");
                System.out.println("                      C-3PO Usage");
                System.out.println("");
                System.out.println("join + \"username\"         |        join a chat");
                System.out.println("list                        |        list active chatters");
                System.out.println("post + \"your_message\"     |        post message");
                System.out.println("leave                       |        leave chat");
                System.out.println("quit                        |        kill client");
              }
              // Check if command quit, call function leave first if user is joined.
              else if(cmd.equals("quit")) {
                if(usr != "") {
                  chatImpl.leave(usr);
                }
                // Quitting.
                break;
              }
              // if command is unknown.
              else {
                System.out.println("* Unknown command. Type \"help\" for usage info");
              }
            }
            
	} catch(Exception e){
          System.out.println("ERROR : " + e);
	    e.printStackTrace(System.out);
	}
    }
}
