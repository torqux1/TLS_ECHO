import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.security.Security;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.sun.net.ssl.internal.ssl.Provider;


public class TCPmultiServer_threads implements Runnable
{
    Socket clientSocket;
    private Object socket;
    private Object mySock;

    TCPmultiServer_threads(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public static void main(String args[]) throws IOException {
        //The Port number through which this server will accept client connections
        int port = 3333;
        /*Adding the JSSE (Java Secure Socket Extension) provider which provides SSL and TLS protocols
        and includes functionality for data encryption, server authentication, message integrity,
        and optional client authentication.*/
        Security.addProvider(new Provider());
        //specifing the keystore file which contains the certificate/public key and the private key
        System.setProperty("javax.net.ssl.keyStore","myKeyStore.jks");
        //specifing the password of the keystore file
        System.setProperty("javax.net.ssl.keyStorePassword","123456");
        //SSLServerSocketFactory establishes the ssl context and and creates SSLServerSocket
        SSLServerSocketFactory sslServerSocketfactory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
        //Create SSLServerSocket using SSLServerSocketFactory established ssl context
        SSLServerSocket sslServerSocket = (SSLServerSocket)sslServerSocketfactory.createServerSocket(port);
        System.out.println("Listening");
        while (true) {
            Socket sock = (SSLSocket)sslServerSocket.accept();
            System.out.println("Got connection from " + sock.getInetAddress());
            new Thread(new TCPmultiServer_threads(sock)).start();   //start a new thread for the client,
        }

    }

    public void run() {      //executed by every client thread
        try {
            System.out.println("Got connection from " + clientSocket.getInetAddress());
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);    //autoflush=true
            //autoflush - A value indicating whether the stream should be automatically flushed when it is written to.
            //equivalent to manual flush: out.flush(); -> This forces data to be sent to the server without closing the Socket.
            String query;
            while (true) {
                query = input.readLine();
                if (query == null || query.equals("END")) {  // Short-circuit evaluation
                    break;
                }
                out.println(query);
            }
            System.out.println("Connection with " + clientSocket.getInetAddress() + " closed.");
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(TCPmultiServer_threads.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
