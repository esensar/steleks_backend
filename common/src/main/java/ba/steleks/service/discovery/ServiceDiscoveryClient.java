package ba.steleks.service.discovery;

import ba.steleks.error.exception.ExternalServiceException;
import ba.steleks.service.Service;

/**
 * Created by ensar on 16/04/17.
 */
public interface ServiceDiscoveryClient {

    String getServiceUrl(Service service) throws ExternalServiceException;
}
