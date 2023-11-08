package supportkim.shoppingmall.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // exception 에는 인증 실패할 때 생긴 예외가 넘어옴
        String errorMessage = "Please try again later";

        if (exception instanceof UsernameNotFoundException || exception instanceof BadCredentialsException){
            errorMessage = "Please check your username or password again";
        }
        setDefaultFailureUrl("/login?error=true&exception="+errorMessage);

        // 부모에게 위임
        super.onAuthenticationFailure(request, response, exception);
    }
}
