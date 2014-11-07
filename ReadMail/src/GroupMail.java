

import java.util.LinkedList;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.UIDFolder;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;



public class GroupMail {

//	private static Logger logger= Logger.getLogger(GroupMail.class);	
	static String toFolder = "Spam";
	
/*	private DBManagerInboxSent db;

	public GroupMail(DBManagerInboxSent db)
	{
		this.db = db;
	}
    */

	public static void main(String args[]){
		traverseInboxForContactUpdate();
	}
    public static void traverseInboxForContactUpdate()
    {
        Store store = mailConnection("niranjan@taramt.com", "niranjandarling123", "imap.gmail.com");
        if(store!=null){
        Message messages[];
        LinkedList<String> contactsList = new LinkedList<String>();
        long lastMailId =0;
        String newSender = "";
        try 
        {        
	        Folder FromFolder = store.getFolder("INBOX");
	        Folder ToFolder = store.getFolder(toFolder);
	        FromFolder.open(Folder.READ_WRITE);

	        UIDFolder uf = (UIDFolder)FromFolder;
	        ToFolder.open(Folder.READ_WRITE);
	        
	    //    lastMailId = folder.getLastMsgId();
	        	        
	        long tempId = lastMailId;
	//        messages = uf.getMessagesByUID(lastMailId, UIDFolder.LASTUID);
	        messages = FromFolder.getMessages();
//	        FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
//	        messages = FromFolder.search(ft);
	        String senderMailID;
	      //  Hashtable<String, Integer> contactlist = db.getContactList("niranjan@taramt.com");
	     //   ContactPOJO ContactObj;
	        
	    //    logger.info("Fetching mails of "+user.getEmailid()+" in folder - "+folder.getFolderName());
	        int flag = 0;
	        for(Message message : messages) 
	        {
	        //	ContactObj = new ContactPOJO();
        	   long MessageUid = uf.getUID(message);
        	   
        	   /*if(MessageUid > lastMailId)
        	   {*/
        		   flag++;
        		   lastMailId = MessageUid;        	   
	               Address[] fromAdr = message.getFrom();
	               Address[] toAdr = message.getAllRecipients();
	               senderMailID=getMailID(fromAdr[0].toString());
	               Boolean isMessageSeen = message.isSet(Flags.Flag.SEEN);
	               
	               if(senderMailID!=null && !isMessageSeen /*&& tempId<MessageUid*/)
	               {
	            	   Integer weight = 1/*(Integer) contactlist.get(senderMailID.toLowerCase().trim())*/;
	            	   if(weight != null)
	            	   {
		            	//   logger.info("MessageId: "+MessageUid);            	    
		            	    if(weight<=0)
		                    {	            	    	
		            	    	/*logger.info("sender: "+senderMailID+" -tive score");
		                        movemessage((MimeMessage)message,ToFolder);  
		            //         logger.info("Updating "+senderMailID+"("+weight+") by -1");
		            //	    	updateContactsWeight(senderMailID , -1);
		                        ContactObj.setContact_emailid(senderMailID);
		                        ContactObj.setUpdateWeight(-1);
		                        contactsList.add(ContactObj);*/
		                    }
		                    else
		                    {
		                    	//logger.info("sender: "+senderMailID+" +tive score");
	//	                    	updateContact.setContact_emailid(senderMailID);
	//	                        updateContact.setUpdateWeight(1);
	//	                        contactsList.add(updateContact);
	//	                        contactsList.add(updateContact);
		                    	LinkedList<String> toArrayList = new LinkedList<String>();
		                    	for(Address to : toAdr){
		                    		toArrayList.add(getMailID(to.toString()));
		                    	}
		                    	System.out.println(toArrayList.toString());
		                    	for(String groupMailId : toArrayList){
	                    			System.out.println(groupMailId);		                    					
	                    			 }		 
		                    }
	            	   }
	               }
	        }
    
        }
        catch (MessagingException ex) {
        //	logger.error("Exception in parsing messages: ", ex);
        }
        catch(Exception e)
        {
        //	logger.error("Exception in parsing messages: ", e);
        }
        if(lastMailId !=0 ){        	
        	//logger.error("Faced Exception but updating last mail id,");
        	//db.updateLastMid(folder.getEmailid(), folder.getFolderName(), lastMailId);    		
        }
        }
       
      }
    
     
    private static String getMailID(String DisplayName) {
    	
        try {
    		InternetAddress ia = new InternetAddress(DisplayName);
    		return ia.getAddress();
    	} catch (AddressException e) {
    		// TODO Auto-generated catch block
    		 e.printStackTrace();
    	}
        return null;
	}
    


	private static Store mailConnection(String username, String password, String server) {
    	Store store = null;
        Properties props = System.getProperties();
        Session session = Session.getDefaultInstance(props, null);
        try 
        {
            store = session.getStore("imaps");
            store.connect(server, username, password);
        } catch(javax.mail.AuthenticationFailedException ex){
        	//logger.error("Login Authentication failed: ",ex);
        	store = null;
        }catch (NoSuchProviderException ex) {
        	//logger.error("Generate Contacts", ex);
        	store = null;
        } catch (MessagingException ex) {
        	//logger.error("Generate Contacts", ex);
        	store = null;
        } 
        
        return store;
	}
}
