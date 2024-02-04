import java.net.*;
import java.io.*;

class UDPClient1 {

    private static final int SERVICE_PORT = 10000;
    private static final int BUFFER_SIZE = 1024;
	private static final String DEST = "localhost";

    public static void main(String[] args) {
        
    	try {
    		//Create input stream to read from file
			BufferedReader reader = new BufferedReader(new FileReader("in.txt"));
    		
    		//create output stream
			ByteArrayOutputStream out = new ByteArrayOutputStream();
    		
    		//create a print stream - bind to output stream
    		PrintStream prnt = new PrintStream(out);
    	
    		//write data to output stream baos
			String readLine;
			do {
				readLine = reader.readLine();
				if (readLine != null) {
					System.out.println(readLine);
					prnt.println(readLine);
				}
			} while (readLine != null);
			reader.close();
			prnt.flush();
			prnt.close();
			
			System.out.println();
        	System.out.println("Step 1:  Create dset socket");
        	DatagramSocket dset = new DatagramSocket();
			InetAddress iadd = InetAddress.getByName(DEST);
        
        	System.out.println("Step 2:  Create dpack packet");
			byte[] byteOut = out.toByteArray();
			DatagramPacket dpack = new DatagramPacket(byteOut, byteOut.length);
			dpack.setAddress(iadd);
			dpack.setPort(SERVICE_PORT);
			out.close();
        	
			System.out.println("Step 3:  Send packet to server");

			//use dset to send packet
			dset.send(dpack);
			System.out.println("\nSubmitted!!!!\nLets Gooooo\n"); 

        	System.out.println("Step 4:  Wait for dpacket from server");

        	//create a datagram packet to hold response from server
			byte[] byteIn = new byte[BUFFER_SIZE];
			DatagramPacket receive = new DatagramPacket(byteIn, BUFFER_SIZE);
        	
        	//packet receive
			dset.receive(receive);
        	System.out.println("Submission accepted!!!\n");

			dset.close();
        	System.out.println("Step 5:  Read dpacket and output to monitor");

        	//create buffered reader 
        	BufferedReader readerIn = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(receive.getData())));
			String name = readerIn.readLine();
			String tstp = readerIn.readLine();
			readerIn.close();
			
			System.out.println("Submission:  " + name);
			System.out.println("Timestamp:  " + tstp);
			System.out.println("THANK YOU\nEND...");
			System.out.println("\nPrepared by : Mohamad Saiful Nizam Bin Abd Aziz");

    	}
        catch (IOException ioe) {
    		System.err.println("Error - "+ioe);
       	}
       	
    }
}