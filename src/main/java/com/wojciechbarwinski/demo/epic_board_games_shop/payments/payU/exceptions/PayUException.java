package com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.exceptions;

import com.wojciechbarwinski.demo.epic_board_games_shop.exceptions.ApplicationException;

public class PayUException extends ApplicationException {

    public PayUException(String s) {
        super(s);
    }
}
