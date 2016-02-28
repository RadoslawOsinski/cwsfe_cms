1. Create keystore for frontend application with private key:

rmo@rmo ~/I/c/keystore> keytool -genkeypair -alias cwsfeSigningKey -keyalg RSA -keysize 4096 -dname "CN=cwsfe.pl, O=CWSFE, C=PL" -storetype JKS -keystore cwsfeKeystore.jks
Enter keystore password:
Re-enter new password:
Enter key password for <cwsfeSigningKey>
	(RETURN if same as keystore password):
Re-enter new password:

2. Export public certficiate for frontend application
rmo@rmo ~/I/c/keystore> keytool -exportcert -alias cwsfeSigningKey -file cwsfeSelfSignedCert.crt -keystore cwsfeKeystore.jks
Enter keystore password:
Certificate stored in file <cwsfeSelfSignedCert.crt>

3. Create keystore for backend application with private key:
rmo@rmo ~/I/c/keystore> keytool -genkeypair -alias cwsfeCmsSigningKey -keyalg RSA -keysize 4096 -dname "CN=cwsfe.pl, O=CWSFE_CMS, C=PL" -storetype JKS -keystore cwsfeCmsKeystore.jks
Enter keystore password:
Re-enter new password:
Enter key password for <cwsfeCmsSigningKey>
	(RETURN if same as keystore password):
Re-enter new password:

4. Import public certificate to backend application keystore (trust store):
keytool -importcert -alias cwsfeSigningKey -file cwsfeSelfSignedCert.crt -trustcacerts -keystore cwsfeCmsKeystore.jks
Enter keystore password:
......