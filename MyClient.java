import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyClient {

	public static void main(String[] args) throws IOException {
		Socket s = new Socket("localhost", 8783);

		InputStream instream = s.getInputStream();
		OutputStream outstream = s.getOutputStream();

		// scanner reads from input stream
		Scanner in = new Scanner(instream);
		// printwriter prints from output stream
		PrintWriter out = new PrintWriter(outstream);

		Scanner input = new Scanner(System.in);

		System.out.println("Enter BYE to quit");
		System.out.println("Enter a command (SUBMIT, REMOVE) and a token: ");
		String request = input.nextLine();

		// print command to output stream
		out.println(request);

		// empty output stream
		out.flush();

		if (in.hasNextLine()) {
			String response = in.nextLine();
			System.out.println("Receiving: " + response);
		}

		// close streams and socket
		in.close();
		out.close();
		input.close();
		s.close();
	}
}
