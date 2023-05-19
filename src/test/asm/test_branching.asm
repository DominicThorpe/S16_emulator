.data:
.code:
    start:
        movi ax 100
        movi bx 200
        movi cx 0

        cmp  ax bx
        movi dx @not_here
        jeq  dx
        
        movi dx @here_one
        jne dx

    
    here_one:        
        movi ax 400
        movi bx 400
        cmp  ax bx

        movi dx @not_here
        jne dx

        movi dx @here_two
        jeq  dx
    

    here_two:
        cmp  ax bx
        movi dx @not_here
        jle  dx
        
        movi dx @here_three
        jlte dx
    

    here_three:
        cmp  ax bx
        movi dx @not_here
        jgt  dx

        movi dx @here_four
        jgte dx
    

    here_four:
        movi ax 100
        movi bx 200
        cmp  ax bx
        
        movi dx @not_here
        jgt  dx

        movi dx @here_five
        jle  dx
    

    here_five:
        movi ax 200
        movi bx 100
        cmp  ax bx

        movi dx @not_here
        jle  dx

        movi dx @here_six
        jgt  dx
    

    here_six:
        movi ax 100
        movi bx 200
        cmp  ax bx
        
        movi dx @not_here
        jgte dx

        movi dx @here_seven
        jlte dx
    

    here_seven:
        movi ax 200
        movi bx 100
        cmp  ax bx

        movi dx @not_here
        jlte dx

        movi dx @here_eight
        jgte dx
    

    here_eight:
        movi ax 0

        movi  dx @not_here
        jnzro ax dx

        movi dx @here_nine
        jzro ax dx
    

    here_nine:
        movi ax 100

        movi dx @not_here
        jzro ax dx

        movi dx @end
        jnzro ax dx

        movi dx @not_here
        jump dx


    not_here:
        halt


    end:
        movi cx 100
        halt
