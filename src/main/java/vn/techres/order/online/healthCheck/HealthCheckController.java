package vn.techres.order.online.healthCheck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.techres.order.online.configuration.ApplicationProperties;
import vn.techres.order.online.v1.response.BaseResponse;
import vn.techres.order.online.v1.response.HealthCheckResponse;

@RestController("HealthCheckController")
@RequestMapping("/api/public/health-check")
public class HealthCheckController {

    @Autowired
    private ApplicationProperties applicationProperties;

    /**
     * 
     * @param wrapper
     * @return
     * @throws Exception
     */
    @GetMapping(value = "", consumes = { MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<BaseResponse<HealthCheckResponse>> get()
            throws Exception {
        BaseResponse<HealthCheckResponse> response = new BaseResponse<>();

        response.setData(new HealthCheckResponse(applicationProperties.getConfigBuildNumber(), applicationProperties.getConfigBuildTime()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
