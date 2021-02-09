package skichiku.copyobject.test;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.logging.LoggingFeature.Verbosity;
import com.oracle.bmc.auth.BasicAuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.http.ClientConfigurator;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.model.CopyObjectDetails;
import com.oracle.bmc.objectstorage.requests.CopyObjectRequest;
import com.oracle.bmc.objectstorage.requests.GetNamespaceRequest;
import com.oracle.bmc.objectstorage.responses.GetNamespaceResponse;

public class Main {

  private static final Logger logger = Logger.getLogger(Main.class.getName());

  private static final ClientConfigurator configurator = new ClientConfigurator() {

    @Override
    public void customizeClient(Client client) {}

    @Override
    public void customizeBuilder(ClientBuilder builder) {
      builder.register(new LoggingFeature(logger, Level.INFO, Verbosity.PAYLOAD_TEXT, null));
    }
  };

  private static final String profile = "DEFAULT";

  private static final String bucketName = "test-bucket";

  private static final String sourceObjectName = "test-source";

  private static final String destinationObjectName = "test-destination";

  public static void main(String[] args) throws IOException {
    ConfigFileAuthenticationDetailsProvider provider =
        new ConfigFileAuthenticationDetailsProvider(profile);

    String namespace = getNamespace(provider);
    String destinationRegion = provider.getRegion().getRegionId();

    try (ObjectStorageClient client = new ObjectStorageClient(provider, null, configurator)) {
      CopyObjectDetails details = CopyObjectDetails.builder().sourceObjectName(sourceObjectName)
          .destinationNamespace(namespace).destinationRegion(destinationRegion)
          .destinationBucket(bucketName).destinationObjectName(destinationObjectName).build();

      CopyObjectRequest request = CopyObjectRequest.builder().namespaceName(namespace)
          .bucketName(bucketName).copyObjectDetails(details).build();

      client.copyObject(request);
    }
  }

  private static String getNamespace(BasicAuthenticationDetailsProvider provider) {
    try (ObjectStorageClient client = new ObjectStorageClient(provider)) {
      GetNamespaceRequest request = GetNamespaceRequest.builder().build();
      GetNamespaceResponse response = client.getNamespace(request);
      return response.getValue();
    }
  }

}
