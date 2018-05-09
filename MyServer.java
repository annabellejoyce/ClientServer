import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class MyServer {

	private Socket s;
	private Scanner in;
	private PrintWriter out;
	private ArrayList<String> tokenList = new ArrayList<String>();
	static final int maxSize = 9;

	public static void main(String[] args) throws IOException {

		int portNumber = 8783;
		ServerSocket server = new ServerSocket(portNumber);

		MyServer serverInstance = new MyServer();

		System.out.println("Server running. Waiting for a client to connect...");

		while (true) {
			// server side client socket waits for clients
			serverInstance.s = server.accept();

			System.out.println("Client connected");

			// start a new instance thread
			serverInstance.run();

			System.out.println("Client disconnected. Waiting for a new client to connect...");
		}
	}

	public void run() {
		try {

			try {
				// in reads input stream of server side client socket
				in = new Scanner(s.getInputStream());
				// out prints output stream of server side client socket
				out = new PrintWriter(s.getOutputStream());
				// method to do something
				doService();
			}

			finally {
				// close streams and socket
				in.close();
				out.close();
				s.close();
			}
		}

		catch (IOException e) {
			System.err.println(e);
		}
	}

	public void doService() throws IOException {
		// if there is nothing in the input stream, return
		while (true) {
			if (!in.hasNext()) {
				return;
			}

			// request variable is the next string in input stream
			String request = in.nextLine();

			System.out.println("Request received: " + request);

			handleRequest(request);

		}

	}

	public void handleRequest(String request) {
		String[] instructions = request.split(" ");
		String command = instructions[0];

		synchronized (tokenList) {

			if (command.equals("SUBMIT")) {
				String token = instructions[1];
				// if list contains the token
				if (!tokenList.contains(token)) {
					if (tokenList.size() < maxSize) {
						tokenList.add(token);
						// sort the list alphabetically
						Collections.sort(tokenList);
						System.out.println("Token List: " + tokenList);
					}
					out.println("OK");
				} else {
					out.println("ERROR");
				}

			} else if (command.equals("REMOVE")) {
				String token = instructions[1];
				if (tokenList.contains(token)) {
					tokenList.remove(token);
					out.println("OK"); // server response
				}

			} else if (command.equals("BYE")) {
				return;
			} else {
				System.err.print("Unknown request!");
			}

			out.flush();
		}
	}
}
