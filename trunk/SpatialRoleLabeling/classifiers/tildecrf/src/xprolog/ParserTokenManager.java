package xprolog;

/* Generated By:JavaCC: Do not edit this line. ParserTokenManager.java */

public class ParserTokenManager implements ParserConstants {
    public java.io.PrintStream debugStream = System.out;

    public void setDebugStream(java.io.PrintStream ds) {
        debugStream = ds;
    }

    private final int jjStopStringLiteralDfa_0(int pos, long active0) {
        switch (pos) {
        case 0:
            if ((active0 & 0x80L) != 0L)
                return 13;
            if ((active0 & 0x2000000L) != 0L) {
                jjmatchedKind = 8;
                return -1;
            }
            return -1;
        default:
            return -1;
        }
    }

    private final int jjStartNfa_0(int pos, long active0) {
        return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
    }

    private final int jjStopAtPos(int pos, int kind) {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        return pos + 1;
    }

    private final int jjStartNfaWithStates_0(int pos, int kind, int state) {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            return pos + 1;
        }
        return jjMoveNfa_0(state, pos + 1);
    }

    private final int jjMoveStringLiteralDfa0_0() {
        switch (curChar) {
        case 33:
            return jjStartNfaWithStates_0(0, 7, 13);
        case 40:
            return jjStopAtPos(0, 23);
        case 41:
            return jjStopAtPos(0, 24);
        case 44:
            return jjStopAtPos(0, 22);
        case 45:
            return jjMoveStringLiteralDfa1_0(0x2000000L);
        case 46:
            return jjStopAtPos(0, 20);
        case 58:
            return jjMoveStringLiteralDfa1_0(0x280000L);
        case 59:
            return jjStopAtPos(0, 26);
        case 91:
            return jjStopAtPos(0, 27);
        case 93:
            return jjStopAtPos(0, 28);
        case 124:
            return jjStopAtPos(0, 29);
        default:
            return jjMoveNfa_0(0, 0);
        }
    }

    private final int jjMoveStringLiteralDfa1_0(long active0) {
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(0, active0);
            return 1;
        }
        switch (curChar) {
        case 45:
            if ((active0 & 0x80000L) != 0L)
                return jjStopAtPos(1, 19);
            break;
        case 61:
            if ((active0 & 0x200000L) != 0L)
                return jjStopAtPos(1, 21);
            break;
        case 62:
            if ((active0 & 0x2000000L) != 0L)
                return jjStopAtPos(1, 25);
            break;
        default:
            break;
        }
        return jjStartNfa_0(0, active0);
    }

    private final void jjCheckNAdd(int state) {
        if (jjrounds[state] != jjround) {
            jjstateSet[jjnewStateCnt++] = state;
            jjrounds[state] = jjround;
        }
    }

    private final void jjAddStates(int start, int end) {
        do {
            jjstateSet[jjnewStateCnt++] = jjnextStates[start];
        } while (start++ != end);
    }

    private final void jjCheckNAddTwoStates(int state1, int state2) {
        jjCheckNAdd(state1);
        jjCheckNAdd(state2);
    }

    private final void jjCheckNAddStates(int start, int end) {
        do {
            jjCheckNAdd(jjnextStates[start]);
        } while (start++ != end);
    }

    private final void jjCheckNAddStates(int start) {
        jjCheckNAdd(jjnextStates[start]);
        jjCheckNAdd(jjnextStates[start + 1]);
    }

    static final long[] jjbitVec0 = { 0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL };

    private final int jjMoveNfa_0(int startState, int curPos) {
        int[] nextStates;
        int startsAt = 0;
        jjnewStateCnt = 26;
        int i = 1;
        jjstateSet[0] = startState;
        int j, kind = 0x7fffffff;
        for (;;) {
            if (++jjround == 0x7fffffff)
                ReInitRounds();
            if (curChar < 64) {
                long l = 1L << curChar;
                MatchLoop: do {
                    switch (jjstateSet[--i]) {
                    case 0:
                        if ((0x3ff000000000000L & l) != 0L) {
                            if (kind > 11)
                                kind = 11;
                            jjCheckNAdd(18);
                        } else if ((0x7000000000000000L & l) != 0L) {
                            if (kind > 10)
                                kind = 10;
                        } else if ((0x840000000000L & l) != 0L) {
                            if (kind > 9)
                                kind = 9;
                        } else if ((0x280000000000L & l) != 0L) {
                            if (kind > 8)
                                kind = 8;
                        } else if (curChar == 39)
                            jjCheckNAddTwoStates(20, 21);
                        else if (curChar == 33)
                            jjCheckNAdd(13);
                        else if (curChar == 37)
                            jjCheckNAddStates(0, 2);
                        if (curChar == 60)
                            jjCheckNAdd(13);
                        else if (curChar == 62)
                            jjCheckNAdd(13);
                        else if (curChar == 61)
                            jjCheckNAdd(13);
                        break;
                    case 1:
                        if ((0xffffffffffffdbffL & l) != 0L)
                            jjCheckNAddStates(0, 2);
                        break;
                    case 2:
                        if ((0x2400L & l) != 0L && kind > 6)
                            kind = 6;
                        break;
                    case 3:
                        if (curChar == 10 && kind > 6)
                            kind = 6;
                        break;
                    case 4:
                        if (curChar == 13)
                            jjstateSet[jjnewStateCnt++] = 3;
                        break;
                    case 5:
                        if ((0x280000000000L & l) != 0L && kind > 8)
                            kind = 8;
                        break;
                    case 6:
                        if ((0x840000000000L & l) != 0L && kind > 9)
                            kind = 9;
                        break;
                    case 10:
                        if ((0x7000000000000000L & l) != 0L && kind > 10)
                            kind = 10;
                        break;
                    case 13:
                        if (curChar == 61 && kind > 10)
                            kind = 10;
                        break;
                    case 14:
                        if (curChar == 61)
                            jjCheckNAdd(13);
                        break;
                    case 15:
                        if (curChar == 33)
                            jjCheckNAdd(13);
                        break;
                    case 16:
                        if (curChar == 62)
                            jjCheckNAdd(13);
                        break;
                    case 17:
                        if (curChar == 60)
                            jjCheckNAdd(13);
                        break;
                    case 18:
                        if ((0x3ff000000000000L & l) == 0L)
                            break;
                        if (kind > 11)
                            kind = 11;
                        jjCheckNAdd(18);
                        break;
                    case 19:
                        if (curChar == 39)
                            jjCheckNAddTwoStates(20, 21);
                        break;
                    case 20:
                        if ((0xffffff7fffffdbffL & l) != 0L)
                            jjCheckNAddTwoStates(20, 21);
                        break;
                    case 21:
                        if (curChar == 39 && kind > 13)
                            kind = 13;
                        break;
                    case 23:
                        if ((0x3ff000000000000L & l) == 0L)
                            break;
                        if (kind > 14)
                            kind = 14;
                        jjstateSet[jjnewStateCnt++] = 23;
                        break;
                    case 25:
                        if ((0x3ff000000000000L & l) == 0L)
                            break;
                        if (kind > 15)
                            kind = 15;
                        jjstateSet[jjnewStateCnt++] = 25;
                        break;
                    default:
                        break;
                    }
                } while (i != startsAt);
            } else if (curChar < 128) {
                long l = 1L << (curChar & 077);
                MatchLoop: do {
                    switch (jjstateSet[--i]) {
                    case 0:
                        if ((0x87fffffeL & l) != 0L) {
                            if (kind > 14)
                                kind = 14;
                            jjCheckNAdd(23);
                        } else if ((0x7fffffe00000000L & l) != 0L) {
                            if (kind > 15)
                                kind = 15;
                            jjCheckNAdd(25);
                        }
                        if (curChar == 105)
                            jjstateSet[jjnewStateCnt++] = 11;
                        else if (curChar == 109)
                            jjstateSet[jjnewStateCnt++] = 8;
                        break;
                    case 1:
                        jjAddStates(0, 2);
                        break;
                    case 7:
                        if (curChar == 100 && kind > 9)
                            kind = 9;
                        break;
                    case 8:
                        if (curChar == 111)
                            jjstateSet[jjnewStateCnt++] = 7;
                        break;
                    case 9:
                        if (curChar == 109)
                            jjstateSet[jjnewStateCnt++] = 8;
                        break;
                    case 11:
                        if (curChar == 115 && kind > 10)
                            kind = 10;
                        break;
                    case 12:
                        if (curChar == 105)
                            jjstateSet[jjnewStateCnt++] = 11;
                        break;
                    case 20:
                        if ((0xffffffffefffffffL & l) != 0L)
                            jjAddStates(3, 4);
                        break;
                    case 22:
                        if ((0x87fffffeL & l) == 0L)
                            break;
                        if (kind > 14)
                            kind = 14;
                        jjCheckNAdd(23);
                        break;
                    case 23:
                        if ((0x7fffffe87fffffeL & l) == 0L)
                            break;
                        if (kind > 14)
                            kind = 14;
                        jjCheckNAdd(23);
                        break;
                    case 24:
                        if ((0x7fffffe00000000L & l) == 0L)
                            break;
                        if (kind > 15)
                            kind = 15;
                        jjCheckNAdd(25);
                        break;
                    case 25:
                        if ((0x7fffffe87fffffeL & l) == 0L)
                            break;
                        if (kind > 15)
                            kind = 15;
                        jjCheckNAdd(25);
                        break;
                    default:
                        break;
                    }
                } while (i != startsAt);
            } else {
                int i2 = (curChar & 0xff) >> 6;
                long l2 = 1L << (curChar & 077);
                MatchLoop: do {
                    switch (jjstateSet[--i]) {
                    case 1:
                        if ((jjbitVec0[i2] & l2) != 0L)
                            jjAddStates(0, 2);
                        break;
                    case 20:
                        if ((jjbitVec0[i2] & l2) != 0L)
                            jjAddStates(3, 4);
                        break;
                    default:
                        break;
                    }
                } while (i != startsAt);
            }
            if (kind != 0x7fffffff) {
                jjmatchedKind = kind;
                jjmatchedPos = curPos;
                kind = 0x7fffffff;
            }
            ++curPos;
            if ((i = jjnewStateCnt) == (startsAt = 26 - (jjnewStateCnt = startsAt)))
                return curPos;
            try {
                curChar = input_stream.readChar();
            } catch (java.io.IOException e) {
                return curPos;
            }
        }
    }

    static final int[] jjnextStates = { 1, 2, 4, 20, 21, };

    public static final String[] jjstrLiteralImages = { "", null, null, null, null, null, null, "\41", null,
            null, null, null, null, null, null, null, null, null, null, "\72\55", "\56", "\72\75", "\54",
            "\50", "\51", "\55\76", "\73", "\133", "\135", "\174", };

    public static final String[] lexStateNames = { "DEFAULT", };

    static final long[] jjtoToken = { 0x3ff8ef81L, };

    static final long[] jjtoSkip = { 0x7eL, };

    static final long[] jjtoSpecial = { 0x40L, };

    private SimpleCharStream input_stream;

    private final int[] jjrounds = new int[26];

    private final int[] jjstateSet = new int[52];

    protected char curChar;

    public ParserTokenManager(SimpleCharStream stream) {
        if (SimpleCharStream.staticFlag)
            throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
        input_stream = stream;
    }

    public ParserTokenManager(SimpleCharStream stream, int lexState) {
        this(stream);
        SwitchTo(lexState);
    }

    public void ReInit(SimpleCharStream stream) {
        jjmatchedPos = jjnewStateCnt = 0;
        curLexState = defaultLexState;
        input_stream = stream;
        ReInitRounds();
    }

    private final void ReInitRounds() {
        int i;
        jjround = 0x80000001;
        for (i = 26; i-- > 0;)
            jjrounds[i] = 0x80000000;
    }

    public void ReInit(SimpleCharStream stream, int lexState) {
        ReInit(stream);
        SwitchTo(lexState);
    }

    public void SwitchTo(int lexState) {
        if (lexState >= 1 || lexState < 0)
            throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState
                    + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
        else
            curLexState = lexState;
    }

    private final Token jjFillToken() {
        Token t = Token.newToken(jjmatchedKind);
        t.kind = jjmatchedKind;
        String im = jjstrLiteralImages[jjmatchedKind];
        t.image = (im == null) ? input_stream.GetImage() : im;
        t.beginLine = input_stream.getBeginLine();
        t.beginColumn = input_stream.getBeginColumn();
        t.endLine = input_stream.getEndLine();
        t.endColumn = input_stream.getEndColumn();
        return t;
    }

    int curLexState = 0;

    int defaultLexState = 0;

    int jjnewStateCnt;

    int jjround;

    int jjmatchedPos;

    int jjmatchedKind;

    public final Token getNextToken() {
        int kind;
        Token specialToken = null;
        Token matchedToken;
        int curPos = 0;

        EOFLoop: for (;;) {
            try {
                curChar = input_stream.BeginToken();
            } catch (java.io.IOException e) {
                jjmatchedKind = 0;
                matchedToken = jjFillToken();
                matchedToken.specialToken = specialToken;
                return matchedToken;
            }

            try {
                input_stream.backup(0);
                while (curChar <= 32 && (0x100003600L & (1L << curChar)) != 0L)
                    curChar = input_stream.BeginToken();
            } catch (java.io.IOException e1) {
                continue EOFLoop;
            }
            jjmatchedKind = 0x7fffffff;
            jjmatchedPos = 0;
            curPos = jjMoveStringLiteralDfa0_0();
            if (jjmatchedKind != 0x7fffffff) {
                if (jjmatchedPos + 1 < curPos)
                    input_stream.backup(curPos - jjmatchedPos - 1);
                if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L) {
                    matchedToken = jjFillToken();
                    matchedToken.specialToken = specialToken;
                    return matchedToken;
                } else {
                    if ((jjtoSpecial[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L) {
                        matchedToken = jjFillToken();
                        if (specialToken == null)
                            specialToken = matchedToken;
                        else {
                            matchedToken.specialToken = specialToken;
                            specialToken = (specialToken.next = matchedToken);
                        }
                    }
                    continue EOFLoop;
                }
            }
            int error_line = input_stream.getEndLine();
            int error_column = input_stream.getEndColumn();
            String error_after = null;
            boolean EOFSeen = false;
            try {
                input_stream.readChar();
                input_stream.backup(1);
            } catch (java.io.IOException e1) {
                EOFSeen = true;
                error_after = curPos <= 1 ? "" : input_stream.GetImage();
                if (curChar == '\n' || curChar == '\r') {
                    error_line++;
                    error_column = 0;
                } else
                    error_column++;
            }
            if (!EOFSeen) {
                input_stream.backup(1);
                error_after = curPos <= 1 ? "" : input_stream.GetImage();
            }
            throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar,
                    TokenMgrError.LEXICAL_ERROR);
        }
    }

}