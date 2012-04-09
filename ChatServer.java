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
  private Othello oth = new Othello();
  private ArrayList userlist = new ArrayList();
  private ChatCallback[] objlist = new ChatCallback[34];
  private ChatCallback[] othlist = new ChatCallback[34];
  private HashMap<String,String> usercolor = new HashMap<String,String>();
  private int objindex = 0;
  private int othindex = 0;
  
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
    if(userlist.size() != 0) {
      String users = "* List of registered users:\n";
      for(int i = 0; i < userlist.size()-1; i++) {
        users += (userlist.get(i) + "\n");
      }
      users += (userlist.get(userlist.size()-1));
      return users;
    }
    else {
      return "* No users active";
    }
  }
  public void leave(String user) {
    for(int i = 0; i < userlist.size(); i++) {
      if(user.equals(userlist.get(i))) {
        userlist.remove(i);
        List<ChatCallback> list = new ArrayList<ChatCallback>(Arrays.asList(objlist));
        list.remove(objlist[i]);
        objlist = list.toArray(new ChatCallback[34]);
        objindex--;
      }
    }
    for(int i = 0; i < objindex; i++) {
      objlist[i].callback("* " + user + " left");
    }
  }
  public void othello(ChatCallback callobj, String color, String user) {
    othlist[othindex] = callobj;
    othindex += 1;
    usercolor.put(user, color);
    print(callobj, user);
    // Show active users that client is playing Othello
    for(int i = 0; i < objindex; i++) {
      objlist[i].callback("* " + user + " is now playing Othello.");
    }
  }
  public void move(String move, String user) {
      if(oth.gameOver() != 0) {
	  System.out.println("Game over!");
	  return;
      }
      if(oth.makeMove(move, usercolor.get(user), true)) {
      print(null, "");
    }
    else {
      for(int i = 0; i < othindex; i++) {
        othlist[i].callback("* " + user + " Could not make a valid move.");
      }
    }
  }
  public void print(ChatCallback callobj, String arg) {
    String print = "";
    HashMap<String,String[]> board = oth.giveme();
    print += "    1   2   3   4   5   6   7   8\n";
    print += "  ---------------------------------";
    for(int i = 0; i < 8; i++) {
      char letter = (char)(65+i);
      String[] a = board.get(Character.toString(letter));
      print += "\n" + Character.toString(letter) + " |";
      for(int j = 0; j < 8; j++) {
        if(a[j] == null) {
          print += "   |";
        }
        else {
          print += " " + a[j] + " |";
        }
      }
      print += "\n  ---------------------------------"; 
    }
    if (oth.gameOver() == 1)
	print += "\n Game over! X won!";
    else if (oth.gameOver() == 2)
	print += "\n Game over! O won!";
    if(arg.equals("")) {
      for(int i = 0; i < othindex; i++) {
        othlist[i].callback(print);
      }
    }
    else {
      callobj.callback(print);
    }
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
