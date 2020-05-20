package watertank.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.filter.GenericFilterBean;
import watertank.dtos.ErrorDTO;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

public class AuthenticationFilter extends GenericFilterBean {

    String[] deviceIds;

    public AuthenticationFilter(final String deviceIds){
        this.deviceIds = deviceIds.split(";");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String deviceId = req.getHeader("Device-ID");

        if (deviceId == null || !Arrays.stream(deviceIds).anyMatch(deviceId::equals)) {
            int status = HttpStatus.UNAUTHORIZED.value();
            String json = new ObjectMapper()
                    .writeValueAsString(new ErrorDTO(
                            status,
                            "Unauthorized",
                            Set.of(new ObjectError("DeviceID", "Invalid device id"))));

            res.setStatus(status);
            res.getWriter().write(json);
            res.flushBuffer();
            return;
        }

        chain.doFilter(request, response);
    }
}
