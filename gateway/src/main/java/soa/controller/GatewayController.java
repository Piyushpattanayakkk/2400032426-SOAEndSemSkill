package soa.controller;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import soa.service.GatewayService;

@RestController
@RequestMapping("/")
public class GatewayController {

    GatewayService GS;

    public GatewayController(GatewayService GS) {
        this.GS = GS;
    }

    // POST /{service}/{endpoint}
    @PostMapping("{service}/{endpoint}")
    public Object postService(
            @PathVariable String service,
            @PathVariable String endpoint,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @RequestBody Map<String, Object> data) {

        return GS.invokeInstance(
                HttpMethod.POST,
                service,
                endpoint,
                token,
                data
        );
    }

    // POST /{service}/{endpoint}/{subEndpoint}
    @PostMapping("{service}/{endpoint}/{subEndpoint}")
    public Object postServiceWithSubEndpoint(
            @PathVariable String service,
            @PathVariable String endpoint,
            @PathVariable String subEndpoint,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @RequestBody Map<String, Object> data) {

        return GS.invokeInstance(
                HttpMethod.POST,
                service,
                endpoint + "/" + subEndpoint,
                token,
                data
        );
    }

    // GET /{service}/{endpoint}
    @GetMapping("{service}/{endpoint}")
    public Object getService(
            @PathVariable String service,
            @PathVariable String endpoint,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {

        return GS.invokeInstance(
                HttpMethod.GET,
                service,
                endpoint,
                token,
                null
        );
    }

    // GET /{service}/{endpoint}/{id}
    @GetMapping("{service}/{endpoint}/{id}")
    public Object getServiceById(
            @PathVariable String service,
            @PathVariable String endpoint,
            @PathVariable Long id,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {

        return GS.invokeInstance(
                HttpMethod.GET,
                service,
                endpoint + "/" + id,
                token,
                null
        );
    }

    // PUT /{service}/{endpoint}/{id}
    @PutMapping("{service}/{endpoint}/{id}")
    public Object putService(
            @PathVariable String service,
            @PathVariable String endpoint,
            @PathVariable Long id,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @RequestBody Map<String, Object> data) {

        return GS.invokeInstance(
                HttpMethod.PUT,
                service,
                endpoint + "/" + id,
                token,
                data
        );
    }

    // DELETE /{service}/{endpoint}/{id}
    @DeleteMapping("{service}/{endpoint}/{id}")
    public Object deleteService(
            @PathVariable String service,
            @PathVariable String endpoint,
            @PathVariable Long id,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {

        return GS.invokeInstance(
                HttpMethod.DELETE,
                service,
                endpoint + "/" + id,
                token,
                null
        );
    }
}