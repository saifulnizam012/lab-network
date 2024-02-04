import java.net.*;
import java.sql.Timestamp;
import java.io.*;

class UDPServer1 {

	private static final int SERVICE_PORT = 10000;
	private static final int BUFFER_SIZE = 1024;

	public static void main(String[] args) {

		int numSub = 0;
		while (true) {
			try {

				System.out.println("1.  Create dset socket ");
				DatagramSocket dset = new DatagramSocket(SERVICE_PORT);

				System.out.println("2.  Create DatagramPacket object to receive packet from client");
				DatagramPacket dpac = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);

				System.out.println("3.  Wait for packet from client");
				dset.receive(dpac);

				System.out.println("4.  Read data from client");
				BufferedReader in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(dpac.getData())));

				System.out.println("5.  Get date and time of packet arrival and generate submission number");
				Timestamp time = new Timestamp(System.currentTimeMillis());
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				PrintStream pstm = new PrintStream(out);
				numSub++;
				pstm.println(numSub);
				pstm.println(time.toString());
				
				System.out.println("\n\nReceived Submission: " + numSub);
				if(numSub>1){
					System.out.println("The Latest Submission");
				}
				else{
					System.out.println("New Submission");
				}
				System.out.println("Sent from: " + dpac.getAddress().getHostAddress() + ", PORT " + dpac.getPort());
				System.out.println("On :" + time.toString());
				System.out.println("From,"+"\tName      : " + in.readLine() +"\n\tMatric No.: " + in.readLine() +"\n\tFile Name : " + in.readLine());
				System.out.println("\n...Thank You...\n\n");
				System.out.println("6.  Prepare packet to send to client\n");
				
				byte[] sendout = out.toByteArray();
				DatagramPacket send = new DatagramPacket(sendout, sendout.length);
				send.setAddress(dpac.getAddress());
				send.setPort(dpac.getPort());

				dset.send(send);
				System.out.println("7.  Send the packet to client");

				dset.close();
				System.out.println("\n---END SUBMISSION---\n");

			} catch (SocketException se) {
				System.err.println(se);
			} catch (IOException ioe) {
				System.err.println(ioe);
			}
		}
	}
}