package com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.clients;

import com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.dtos.PayUTokenResponseDTO;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "PayUAuthClient", url = "${payU.auth_url}")
interface PayUAuthClient {

    @PostMapping("/pl/standard/user/oauth/authorize")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    PayUTokenResponseDTO getAccessToken(@RequestParam("grant_type") String grantType,
                                        @RequestParam("client_id") String clientId,
                                        @RequestParam("client_secret") String clientSecret);

}

