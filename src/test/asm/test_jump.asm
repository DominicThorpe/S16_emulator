.data:
.code:
    movi bx 100

    movi ax @skip
    jump ax
    movi cx 300

skip:
    movi dx 400
    halt