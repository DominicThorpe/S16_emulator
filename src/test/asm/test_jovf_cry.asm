.data:
.code:
    start:
        movi ax 0xA000
        movi bx 0xA000
        add ax bx

        movi dx @next_one
        jovf dx

        movi dx @fail
        jump dx


    next_one:
        movi ax 0xFEC8
        movi bx 0x8000
        addu ax bx

        movi dx @success
        jcry dx

        movi dx @fail
        jump dx


    fail:
        halt
    

    success:
        movi cx 100
        halt