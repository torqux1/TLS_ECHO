# Installation

  - Create new Java project
  - Import all files from **src (***TCP_echo_client, TCPmultiServer_threads***)** into src folder of the created project
  - Import ***mykey.cert, myKeyStore.jsk, myTrustStore.jts*** into project directory
  - Run ***TCPmultiServer_threads.java***
  - Run ***TCP_echo_client.java***

# Configuration
 - To specify the port of running server, edit ***TCPmultiServer_threads.java -> port field*** 
 - To specify the connection port of the client edit ***TCP_echo_client.java -> LISTENING_TCP_PORT field***
- To generate custom server Certificate and public/private key and store it in keystore file, execute the following command
```sh
keytool -genkey -keyalg RSA -keysize 2048 -validity 360 -alias mykey -keystore myKeyStore.jks
```
- To export the certficate and the public key that should be send to the client, execute the following command
```sh
keytool -export -alias mykey -keystore myKeyStore.jks -file mykey.cert
```
- To add the key at the client side to a TrustedStore to trust the server, execute the following command
```sh
keytool -export -alias mykey -keystore myKeyStore.jks -file mykey.cert
```