package ba.steles.service.discovery;

import ba.steles.service.Service;

/**
 * Created by ensar on 16/04/17.
 */
public interface ServiceDiscoveryClient {

    String getServiceUrl(Service service);
}
