package com.mediasoft.warehouse.currency;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CurrencyFilter extends OncePerRequestFilter {

    @Autowired
    private CurrencyProvider currencyProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String currencyString = request.getHeader("currency");
        if (currencyString != null) {
            currencyProvider.setCurrentCurrency(Currency.valueOf(currencyString));
        }

        filterChain.doFilter(request, response);
    }
}
