package com.zerobase.commerce.user.config.filter;

import com.zerobase.commerce.domain.config.JwtAuthenticationProvider;
import com.zerobase.commerce.domain.domain.common.UserVo;
import com.zerobase.commerce.user.service.seller.SellerService;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@WebFilter(urlPatterns = "/seller/*")
@RequiredArgsConstructor
public class SellerFilter implements Filter {

	private final JwtAuthenticationProvider jwtAuthenticationProvider;
	private final SellerService sellerService;


	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String token = req.getHeader("X_AUTH_TOKEN");
		if (!jwtAuthenticationProvider.validateTokens(token)) {
			throw new ServletException("Invalid Access");
		}
		UserVo userVo = jwtAuthenticationProvider.getUserVo(token);
		sellerService.findByIdAndEmail(userVo.getId(), userVo.getEmail());
		chain.doFilter(request, response);
	}
}
