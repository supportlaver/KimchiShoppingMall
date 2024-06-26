package supportkim.shoppingmall.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private ObjectMapper om = new ObjectMapper();
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // exception 에는 인증 실패할 때 생긴 예외가 넘어옴
        String errorMessage = "Invalid Username or Password";

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);


        if(exception instanceof BadCredentialsException) {
            errorMessage = "Invalid Username or Password";
        } else if(exception instanceof DisabledException) {
            errorMessage = "Locked";
        } else if(exception instanceof CredentialsExpiredException) {
            errorMessage = "Expired password";
        }

        om.writeValue(response.getWriter() , errorMessage);
    }
}
