package ba.steles.service.discovery;

/**
 * Created by ensar on 16/04/17.
 */

import ba.steles.service.Service;
import ba.steles.service.ServiceIdProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Primary
@Component
public class EurekaServiceDiscoveryClient implements ServiceDiscoveryClient {
    private static final Logger logger =
            Logger.getLogger(EurekaServiceDiscoveryClient.class.getName());

    private DiscoveryClient discoveryClient;
    private ServiceIdProvider serviceIdProvider;

    @Autowired
    public EurekaServiceDiscoveryClient(DiscoveryClient discoveryClient, ServiceIdProvider serviceIdProvider) {
        this.discoveryClient = discoveryClient;
        this.serviceIdProvider = serviceIdProvider;
    }

    @Override
    public String getServiceUrl(Service service) {
        String serviceId = serviceIdProvider.getServiceId(service);
        List<ServiceInstance> usersInstances = discoveryClient.getInstances(serviceId);
        if(usersInstances == null || usersInstances.size() == 0) {
            System.err.print(service.toString() + " service not found!");
            throw new RuntimeException();
        }

        ServiceInstance serviceInstance = usersInstances.get(0);
        if(serviceInstance == null) {
            throw new RuntimeException();
        }
        return serviceInstance.getUri().toString();
    }
}
