package soa.service;

import java.util.List;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Service
public class GatewayService {

    private final DiscoveryClient DC;

    private int current = 0;

    public GatewayService(DiscoveryClient DC) {
        this.DC = DC;
    }

    private ResponseEntity<String> json(HttpStatusCode status, String body) {

        System.out.println("Response status: " + status.value());

        if (body == null || body.isBlank()) {
            body = "{\"status\":\""
                    + (status.is2xxSuccessful() ? "success" : "error")
                    + "\",\"httpStatus\":" + status.value() + "}";
        }

        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }

    public Object invokeInstance(
            HttpMethod method,
            String service,
            String endpoint,
            String token,
            Object data) {

        List<ServiceInstance> services = DC.getInstances(service);

        if (services == null || services.isEmpty()) {
            return json(HttpStatus.SERVICE_UNAVAILABLE,
                    "{\"status\":\"error\",\"message\":\"No service found: " + service + "\"}");
        }

        ServiceInstance instance;
        synchronized (this) {
            instance = services.get(current % services.size());
            current = (current + 1) % services.size();
        }

        String url = instance.getUri() + "/" + service + "/" + endpoint;

        System.out.println("Forwarding request to: " + url);

        try {
            RestClient.RequestBodySpec spec = RestClient.create()
                    .method(method)
                    .uri(url);

            if (token != null && !token.isBlank()) {
                spec.header(HttpHeaders.AUTHORIZATION, token);
            }

            if (data != null) {
                spec.contentType(MediaType.APPLICATION_JSON).body(data);
            }

            ResponseEntity<String> response = spec.retrieve().toEntity(String.class);

            return json(response.getStatusCode(), response.getBody());

        } catch (RestClientResponseException e) {

            return json(e.getStatusCode(), e.getResponseBodyAsString());

        } catch (Exception e) {

            return json(HttpStatus.BAD_GATEWAY,
                    "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }
}