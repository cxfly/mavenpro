import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class PortMap {
	private static boolean alive = true;
	private static String recvIP = "127.0.0.1";
	private static int recvPort = 8080;
	private static String fwdIP = "127.0.0.1";
	private static int fwdPort = 9999;

	public static void main(String[] args) {
		try {
			Thread.sleep(1000*10);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		if (args != null && args.length == 2) {
			String[] recAddrs = args[0].split(":");
			recvIP = recAddrs[0];
			recvPort = Integer.parseInt(recAddrs[1]);
			String[] destAddrs = args[1].split(":");
			fwdIP = destAddrs[0];
			fwdPort = Integer.parseInt(destAddrs[1]);
		}
		PortMap portMap = new PortMap();
		try {
			portMap.startup();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void startup() throws IOException {
		ServerSocket serverSocket = new ServerSocket();
		serverSocket.bind(new InetSocketAddress(recvIP, recvPort));
		serverSocket.setSoTimeout(5000);
		do {
			try {
				Socket originalSocket = serverSocket.accept();
				// srcSocket.setSoTimeout(2000);
				Socket forwardSocket = new Socket();
				// outSocket.setSoTimeout(2000);
				try {
					forwardSocket.connect(new InetSocketAddress(fwdIP, fwdPort));
//					originalSocket.setTcpNoDelay(true);
					forwardSocket.setTcpNoDelay(true);
					process(originalSocket, forwardSocket);
				} catch (Exception e) {
					e.printStackTrace();
					originalSocket.close();
				}
			} catch (InterruptedIOException e) {
			} catch (Exception e) {
				e.printStackTrace();
			}
		} while (alive);
		serverSocket.close();
	}

	void process(Socket originalSocket, Socket forwardSocket) {
		try {
			new NetForwardThread(originalSocket, forwardSocket, true).start();
			new NetForwardThread(originalSocket, forwardSocket, false).start();
		} catch (IOException e) {
			e.printStackTrace();
			try {
				if (!originalSocket.isClosed()) {
					originalSocket.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				if (!forwardSocket.isClosed()) {
					forwardSocket.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	class NetForwardThread extends Thread {
		private static final int BUFFER_SIZE = 1024;
		boolean isRequest;
		Socket originalSocket;
		Socket forwardSocket;
		InputStream in;
		OutputStream out;
		byte[] response;
		byte[] request;

		public NetForwardThread(Socket originalSocket, Socket forwardSocket,
				boolean isRequest) throws IOException {
			this.originalSocket = originalSocket;
			this.forwardSocket = forwardSocket;
			if (isRequest) {
				this.in = originalSocket.getInputStream();
				this.out = forwardSocket.getOutputStream();
				this.setName("Request-"
						+ originalSocket.getInetAddress().getHostAddress()
						+ ":" + originalSocket.getPort());
			} else {
				this.in = forwardSocket.getInputStream();
				this.out = originalSocket.getOutputStream();
				this.setName("Reponse-"
						+ originalSocket.getInetAddress().getHostAddress()
						+ ":" + originalSocket.getPort());
			}
			this.isRequest = isRequest;
			setPriority(NORM_PRIORITY + 1);
			setDaemon(true);
		}

		@Override
		public void run() {
			process();
		}

		private void process() {
			try {
				byte[] b = new byte[BUFFER_SIZE];
				int n = this.in.read(b);
				while (n > 0) {
					this.out.write(b, 0, n);
					if ((b != null) && (n > 0)) {
						byte[] x = null;
						if (n == BUFFER_SIZE) {
							x = b;
						} else {
							x = new byte[n];
							System.arraycopy(b, 0, x, 0, n);
						}
					}
					n = this.in.read(b);
					Thread.yield();
				}
				this.out.flush();
			} catch (IOException ex) {
				ex.printStackTrace();
				try {
					if (!originalSocket.isClosed()) {
						originalSocket.close();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					if (!forwardSocket.isClosed()) {
						forwardSocket.close();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} finally {
				// if ((this.isRequest))
				this.close();
			}
		}

		public void close() {
			try {
				if (!originalSocket.isOutputShutdown()) {
					originalSocket.getOutputStream().flush();
				}
				if (!originalSocket.isInputShutdown()) {
					originalSocket.shutdownInput();
				}
				if (!originalSocket.isOutputShutdown()) {
					originalSocket.shutdownOutput();
				}
				if (!originalSocket.isClosed()) {
					originalSocket.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				try {
					if (!originalSocket.isClosed()) {
						originalSocket.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try {
				if (!forwardSocket.isOutputShutdown()) {
					forwardSocket.getOutputStream().flush();
				}
				if (!forwardSocket.isInputShutdown()) {
					forwardSocket.shutdownInput();
				}
				if (!forwardSocket.isOutputShutdown()) {
					forwardSocket.shutdownOutput();
				}
				if (!forwardSocket.isClosed()) {
					forwardSocket.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				try {
					if (!forwardSocket.isClosed()) {
						forwardSocket.close();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
