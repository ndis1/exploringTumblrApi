package ndis.tumblrapi;

public interface CredentialStore {

  String[] read();
  void write(String[]response);
  void clearCredentials();
}