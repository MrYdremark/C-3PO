package ChatApp;


/**
* ChatApp/ChatPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Chat.idl
* Wednesday, 28 March 2012 16:50:28 o'clock CEST
*/

public abstract class ChatPOA extends org.omg.PortableServer.Servant
 implements ChatApp.ChatOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("join", new java.lang.Integer (0));
    _methods.put ("say", new java.lang.Integer (1));
    _methods.put ("list", new java.lang.Integer (2));
    _methods.put ("leave", new java.lang.Integer (3));
    _methods.put ("othello", new java.lang.Integer (4));
    _methods.put ("move", new java.lang.Integer (5));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {

  // Join
       case 0:  // ChatApp/Chat/join
       {
         ChatApp.ChatCallback objref = ChatApp.ChatCallbackHelper.read (in);
         String user = in.read_string ();
         String $result = null;
         $result = this.join (objref, user);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }


  // Post
       case 1:  // ChatApp/Chat/say
       {
         ChatApp.ChatCallback objref = ChatApp.ChatCallbackHelper.read (in);
         String message = in.read_string ();
         String user = in.read_string ();
         this.say (objref, message, user);
         out = $rh.createReply();
         break;
       }


  // List
       case 2:  // ChatApp/Chat/list
       {
         String $result = null;
         $result = this.list ();
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }


  // Leave
       case 3:  // ChatApp/Chat/leave
       {
         String user = in.read_string ();
         this.leave (user);
         out = $rh.createReply();
         break;
       }


  // Othello
       case 4:  // ChatApp/Chat/othello
       {
         ChatApp.ChatCallback objref = ChatApp.ChatCallbackHelper.read (in);
         String color = in.read_string ();
         String user = in.read_string ();
         this.othello (objref, color, user);
         out = $rh.createReply();
         break;
       }


  // Move Othello
       case 5:  // ChatApp/Chat/move
       {
         String move = in.read_string ();
         String user = in.read_string ();
         this.move (move, user);
         out = $rh.createReply();
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:ChatApp/Chat:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public Chat _this() 
  {
    return ChatHelper.narrow(
    super._this_object());
  }

  public Chat _this(org.omg.CORBA.ORB orb) 
  {
    return ChatHelper.narrow(
    super._this_object(orb));
  }


} // class ChatPOA
