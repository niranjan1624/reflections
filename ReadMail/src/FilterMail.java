import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FlagTerm;



public class Readmail {

	static String username="niranjan@taramt.com";
		static String password="niranjandarling123";
	private static String imapServer = "imap.gmail.com";
	private static boolean textIsHtml = false;


	public static void main(String args[]){
		TraverseInbox();
	}
	
	public static void TraverseInbox()
    {
   	System.out.println("traverse method..");
        Store store = null;
        Message messages[] = null;
        Properties props = System.getProperties();
        Session session = Session.getDefaultInstance(props, null);
      
        try 
        {
        	System.out.println("inTry");
            store = session.getStore("imaps");
            store.connect(imapServer, username, password);
        
	        Folder FromFolder = store.getFolder("INBOX");        
	        FromFolder.open(Folder.READ_WRITE);
	        //ToFolder.open(Folder.READ_WRITE);
	        FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
	        messages = FromFolder.getMessages();
	        
	        for(int i=0;i<messages.length;i++)
		        {
	        		Message message  =messages[messages.length-i-1];
	        	   /* System.out.println("Sender: "+message.getFrom()[0].toString());
	        	    System.out.println("Subject: "+message.getSubject());
	        		System.out.println("body\t"+message.getContent().toString());*/
		               Address[] toAdr = message.getAllRecipients();
		               LinkedList<String> toArrayList = new LinkedList<String>();
                   	for(Address to : toAdr){
                   		toArrayList.add(getMailID1(to.toString()));
                   	}
                		/*for(String groupMailId : toArrayList){
            			System.out.println(groupMailId);		                    					
            			 }	
*/	        	
                System.out.println("Email number:"+(i+1));		
				System.out.println("\tdate:"+message.getSentDate());
				boolean contains = isPresent("googlegroups.com]",toArrayList.toString()) ;
				System.out.print("\tContains:  "+contains+"\n");
				System.out.println("\tSubject:"+message.getSubject());
				System.out.println("\tFrom:"+getMailID(message.getFrom()[0].toString()));

				System.out.println("\tRecepients:"+toArrayList.toString());
				System.out.println("\tBody:"+getMailContent(message));
			
	        	//	System.out.println(mm.receiver);
	        		
	        		
	        	//	message.setFlag(Flag.SEEN, true);
	        		
		        }
	        store.close();
	        
           
        }
        catch (MessagingException ex) {
        	
        	ex.printStackTrace();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
		
    }
	
	private static String getMailContent(Message message) throws IOException, MessagingException
    {
        
         String description = ""; 
         String contentType = "";      
           Object ob = message.getContent();  
            /* Check if content is pure text/html or in parts */   
          // System.out.println(message.getContent().toString());
        //  System.out.println(ob.toString());
           if (ob instanceof Multipart) {
               Multipart multipart = (Multipart) ob;
               int count=0;
               try {
                for (int j = 0; j < multipart.getCount(); j++) {
                     BodyPart bodyPart = multipart.getBodyPart(j);
                     String disposition = bodyPart.getDisposition();
                     if(j!=1){
                         contentType += bodyPart.getContentType() + " | ";
                         if(getText(bodyPart)!=null){
                        	 System.out.println("in getmailcontent in gettext");
	                         description = getText(bodyPart);
	                         //description = Jsoup.parse(description).text();
                         }
                     }                           
               }
               } catch (MessagingException ex) {
               } 
            }
          else        
          {
             try {
                 description= message.getContent().toString();
             } catch (IOException ex) {
            // logger.error("Exception in parsing description: ", ex);
             } catch (MessagingException ex) {
            //	 logger.error("Exception in parsing description: ", ex);
             }
          }
        return description;
    }
	
    private static String getText(Part p) throws IOException {
    	 
        try {
        if (p.isMimeType("text/*")) {
        String s = (String)p.getContent();
        textIsHtml = p.isMimeType("text/html");
        return s;
        }
        if (p.isMimeType("multipart/alternative")) 
        {
                // prefer html text over plain text
                Multipart mp = (Multipart)p.getContent();
                String text = null;
                for (int i = 0; i < mp.getCount(); i++) {
                    Part bp = mp.getBodyPart(i);
                    if (bp.isMimeType("text/plain")) {
                        if (text == null)
                            text = getText(bp);
                        continue;
                    } else if (bp.isMimeType("text/html")) {
                        String s = getText(bp);
                        if (s != null)
                            return s;
                    } else {
                        return getText(bp);
                    }
                }
                System.out.println("message is , "+text);
                return text;
               
        } 
        else if (p.isMimeType("multipart/*")) 
        {
                Multipart mp = (Multipart)p.getContent();
                for (int i = 0; i < mp.getCount(); i++) {
                    String s = getText(mp.getBodyPart(i));
                    if (s != null)
                        return s;
                }
        }
        return null;
        } catch (MessagingException e) {
        	//logger.error("Exception in getting Text fron Content: ", e);
                return null;
        }
	 }

    private static String getMailID(String DisplayName)
	{
	    String s=null;
	    int startpoint=DisplayName.indexOf("<");
	    int endpoint=DisplayName.indexOf(">");
	    if(startpoint!=-1)
	        s=DisplayName.substring(startpoint+1,endpoint);
	    else
	        s=DisplayName;
	    return s;
	}
    private static String getMailID1(String DisplayName) {
    	
        try {
    		InternetAddress ia = new InternetAddress(DisplayName);
    		return ia.getAddress();
    	} catch (AddressException e) {
    		// TODO Auto-generated catch block
    		 e.printStackTrace();
    	}
        return null;
	}
    static boolean isPresent(String query, String s) {    
        String [] deli = s.split("@");
        if(deli.length==2){
        	String dummy = deli[1];
        	String[] deli1 = dummy.split("groups");
        	if(deli1.length==2){
        		return true;
        	}
        }
                  
        return false;    
    }
	
}

