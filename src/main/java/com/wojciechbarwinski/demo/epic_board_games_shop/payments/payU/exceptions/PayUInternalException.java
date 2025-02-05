package com.wojciechbarwinski.demo.epic_board_games_shop.payments.payU.exceptions;

public class PayUInternalException extends PayUException{

    public PayUInternalException(String s) {
        super("Internal error during processing PayU request." + s);
    }
}
