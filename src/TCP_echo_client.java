import java.io.BufferedReader;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.InputStreamReader;
import java.security.Security;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.sun.net.ssl.internal.ssl.Provider;

public class TCP_echo_client
{
    public static final int LISTENING_TCP_PORT = 3333;

    public static void main(String args[]) throws IOException {
        System.out.println("Enter IP address of the server:");
        Scanner in = new Scanner(System.in);
        String ipAddress = in.next(); //"localhost";

        /*Adding the JSSE (Java Secure Socket Extension) provider which provides SSL and TLS protocols
        and includes functionality for data encryption, server authentication, message integrity, 
        and optional client authentication.*/
        Security.addProvider(new Provider());
        //specifing the trustStore file which contains the certificate & public of the server
        System.setProperty("javax.net.ssl.trustStore","myTrustStore.jts");
        //specifing the password of the trustStore file
        System.setProperty("javax.net.ssl.trustStorePassword","123456");
        //SSLSSocketFactory establishes the ssl context and and creates SSLSocket
        SSLSocketFactory sslsocketfactory = (SSLSocketFactory)SSLSocketFactory.getDefault();

        //Create SSLSocket using SSLServerFactory already established ssl context and connect to server
        try(SSLSocket sslSocket = (SSLSocket)sslsocketfactory.createSocket(ipAddress,LISTENING_TCP_PORT)){
            System.out.println("Connected to echo server (" + sslSocket.getInetAddress().getHostAddress() + ")");
            PrintWriter out = new PrintWriter(sslSocket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String sentence = reader.readLine();
            while ( !sentence.equals("END") ) {
                out.println(sentence);
                String response = input.readLine();
                System.out.println(response);
                sentence = reader.readLine();
            }
            out.println(sentence); // send END to the server to notify them to close the cocket
        }
        catch (SocketException se){
            Logger.getLogger(TCP_echo_client.class.getName()).log(Level.SEVERE, null, se);
        }
    }
}