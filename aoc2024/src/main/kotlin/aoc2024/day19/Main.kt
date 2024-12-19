package aoc2024.day19

fun main() {
    actualData.let { data ->
        val patterns = data.take(1).map { it.split(", ") }.flatten().toSet()
        val designs = data.drop(2)

        println("Part1: ${part1(patterns, designs)}") // 322
        println("Part2: ${part2(patterns, designs)}") // 715514563508258
    }
}

fun part1(patterns: Set<String>, ds: List<String>): Int {
    fun validateDesign(design: String): Boolean {
        val memo = mutableMapOf<String, Boolean>()

        fun loop(remainingDesign: String): Boolean {
            if (remainingDesign.isEmpty()) return true

            if (memo.containsKey(remainingDesign)) {
                return memo[remainingDesign]!!
            }

            for (pattern in patterns) {
                if (remainingDesign.startsWith(pattern)) {
                    val subDesign = remainingDesign.drop(pattern.length)
                    if (loop(subDesign)) {
                        memo[remainingDesign] = true
                        return true
                    }
                }
            }

            memo[remainingDesign] = false
            return false
        }

        return loop(design)
    }

    return ds.map { validateDesign(it) }.filter { it }.size
}

fun part2(patterns: Set<String>, ds: List<String>): Long {
    fun countCombinations(design: String): Long {
        val sublistCounts = MutableList(design.length + 1) { 0L }
        sublistCounts[0] = 1

        for (i in 1..design.length) {
            for (pattern in patterns) {
                if (i >= pattern.length && design.substring(i - pattern.length, i) == pattern) {
                    sublistCounts[i] = sublistCounts[i] + sublistCounts[i - pattern.length]
                }
            }
        }

        return sublistCounts.last()
    }

    return ds.sumOf { countCombinations(it) }
}

val testData = """
    r, wr, b, g, bwu, rb, gb, br

    brwrr
    bggr
    gbbr
    rrbgbr
    ubwu
    bwurrg
    brgr
    bbrgwb
""".trimIndent().lines()

val actualData = """
    guwgbuug, bubbb, grw, wwrbbgug, wrruwb, wgw, wgubbrwu, rrgb, gbrr, gbbgb, rrr, grbrgrur, bgrrub, uubrbwwr, ubwug, rgggg, gwggb, rruwbbbw, uubr, gugwwru, rwru, bgg, rrbgug, brgrw, brwwgrg, ubrrb, wugb, ggrg, buu, bwu, urru, rguw, uuwu, wbr, wuw, rwubww, uwgggb, gbb, guubur, ruw, bwwgrbur, bbbrbbbw, wbrbg, rwgrw, bwgb, uwubwug, bur, bbuwgbug, uurw, rwwbrr, ggur, ugr, wwu, uwbwbu, brg, bw, urg, ubwgguu, wwbuwug, bbgbw, ubrgu, wub, rruur, ggg, rgwgr, wgrrg, wwbr, gbgbru, uuugru, ggbwrbw, rbbrg, bgwrb, rgrgubb, wwbug, wrrrwbwr, b, gurb, rurwg, wuwb, rrw, rbub, gwr, bwr, wuwuwbw, uugb, ugggu, gw, ugubbw, wurwwr, ubw, bbw, brugru, wuwuu, buruw, rwrru, wwurubr, gwu, ugrr, wuu, wgbgg, ugrw, bwgbwu, rwuu, uwbb, bgggr, wwgbu, uwgwwuwr, bwb, gwrwg, wbrb, bgbuug, gwwwgb, uwu, ggr, ubuguu, gbuurb, ugbuu, wrbgww, rrbugug, ruru, rruwr, guug, rwb, gbwurg, uuurgwwg, ruwrb, gbg, guur, gwb, gb, uwwbrb, gww, ggbwrg, grr, uubu, urbb, ugu, ggwb, buurw, bgr, rbw, uwr, urw, bgugrrg, rguuwb, wrggg, brw, urgbbw, wwrb, bgrw, ugug, brrbbb, wbwwuu, bbbu, bww, wrg, bwbgu, rgu, urrwr, gugwu, rrub, buw, ubb, rrgu, ubu, grrug, ubugbrb, uruub, gwwrg, ugg, wuuuurr, bubwwu, gbr, rwgurw, wrw, uruw, rbr, grgb, wwg, rr, ugrb, rgb, ggrw, wg, wrgwrw, rgwb, rbb, rgw, bwgw, gggg, ruu, grww, rgur, gwuu, rwwg, bwgwr, rg, bgrgu, bu, uru, ubg, gur, ubbur, rb, ggwuugw, gg, wruu, wbb, gwrrgr, bgb, bug, ruuub, ug, brr, uwg, urgub, rwu, wubr, bbbbur, bg, rub, wgu, bbwbbgw, uur, bbb, rwr, g, wbwb, urgrurgr, wgg, wbgug, ugbbb, bbr, uug, rgg, gguub, bgwg, rwurubgw, wwuwu, ubgr, bbu, bgww, uuu, bgugugg, wbbruwug, rwgg, uuurb, bwubw, bb, urrbb, grbbbb, buru, gbbg, wruuw, wwbwug, urwbrbwg, gwrb, rgug, bru, wrrbwrg, www, rur, gggw, wwgwww, grb, rgwg, wgur, wwrwrr, gru, grwug, bwuw, rru, wuruw, guw, uu, brrbgg, ggrr, ww, bwbbrw, ururgrwb, wu, rwbg, gbw, wgr, rgru, gbrg, wbub, ggb, brww, ggu, brwgu, bbwbrrg, gbwgrub, rgr, gubwrb, rbgg, rrbu, rbrw, guuuub, bwwwb, uwggww, rrg, wwwub, rrwwu, rbbr, ur, rubrurg, brb, rrwubww, rrwru, gbru, u, wr, ruwu, uruuggg, gu, uubwg, bggr, wurggw, uuwwurb, rurbuuu, wbu, gug, bbuw, urrubb, bugwrwr, urb, bggbb, ugrrrg, ugwwgg, rwg, wwrwbb, wbg, rrbrg, uwuw, rug, ugrwgbw, gwbgu, rgubbw, wbw, gwww, brruu, gwgw, uugbrub, gwwruwr, rbg, gwwwg, ru, grg, wwurg, uubbb, ugw, gruwr, uubg, gubw, wru, rwwwg, urrgwg, ugb, wbbbuug, urgrrur, ugwwguw, w, uww, urrwwu, bwgr, ugbbbwg, uub, urbww, uwwb, gubrub, uwrg, urr, gwru, uugg, uwb, uuwb, burbbu, wb, uurr, gubuu, uwuu, rbwu, wrr, wwrub, bbg, brgrb, ggw, rbu, wwbburur, wrggbwww, gbu, ugrbrr, ruurrwgu, grwrwb, uwruru, uuw, guu, gbur, ub, gbwwg, wguu, gr, ubuubr, uwgu, bub, rbwgruu, wbwrgrr, ururu, bwg, rbggg, gbubrw, wrb, buwbr, grrr, gwgbu, buug, rbrbrwg, bruurrb, brrw, ubbb, wwr, rw, bwbw, grrb, wrwbuu, ruuugru, wgwwgwg, wwb, urbw, rbrwbb, uwub, gbugg, gwbr, rwrrb, urwruuw, buuwg, rww, guburw, bggubg, wug, urggbr, rrwwg, bgw, gwg, ubrgwur, gub

    ubwwwrggbwwwburgrwbugggubwrgwwrwuwwgrbrgwuwwurwrggrbggubr
    ruwbwwgrgrwwwgwrgrrbrrurrwggrgrbrururgwruw
    gbguggwwuggbubugbugurwbuwbuggwgrbrrbugrburggubr
    uwgrrbggugrurrruugrgwrwgubrwrgggwrrrbbbruwb
    gwruuugwgbbgbwrwgubbbuguwwgrruubwbrgugbgwwbwbbbrgwbubr
    gggwbggwgurugbwbgwwwggbwuggbwgbbgbuwrururwwbuwwbgg
    uwwburbrburwrrbwbrwwubrggggbbwwggbbugwbbwbggrrgbwgggu
    gwruruguwuuburuugrgguuuwgwrgwubbgruguwrbwubrrubr
    bgrbwbbbgwwwbrbbrbbbuugbwugbburuuwbbbbrgwwgrrgg
    ugguuugbrubwrwbuwbgwwgbbbgurggwwuwwwbwgubuwruurbr
    rurbwbrrbwrwrruurgubuubuubbgwrrwwbbugrwggwurrrbggwrbgwbrug
    bbggburbbuggbrrwgugubwguugwbguuurguwrrwgwrgrg
    ugbuuwgwgwwurrwbggugwburbgwubuwwbgugruwrubr
    rbwurbrggbbrwggbbrrrgbgrurrwggubwgwwwrrgwrrguurug
    rgwwrgwguubwruuuurwgwbwrgbubrubwwugbggugrbugggrwwgwbrw
    buwrgbbrrgbwbrgwbubrgugwbgbgbwbwbwwrgrwbwgurbuwu
    rubbwbrgwggbbgwgburrbrbrwbggbgugbgurbbwbuwbbbbubg
    rgggbubwggubgrbgwuugburbubbguwuurwububwubuwrbugbuuugwgr
    wuurgugwrwwwgwuwugrugrwbbburbwrruwbwgggguwgbruwbg
    bggurbrwbuggugwbubguuwwguwguwwubwwrwrubgggug
    rrbgbrgwwgrururwbgbbubururruubggrrurwuurbbgubr
    wugrgbbuwuwgruruwwbgwuguurgbwugrwbbuwwgbuwugugbubwgwgr
    brwwwguwrubgwgrrguwrrgrbbguuwbrruwwbgbwuwbgwurgwbgb
    grggugugrwrwurbwrggrurrbubbrwgwwrurwgrbrgrurbwrgubr
    wrrurrwubbgbbbuurbruggrrwgguuwbuurgruwbuugwrwgwuuugurgru
    grrbbbwwuwurgwrbrwugubbrrguwbguggwugurbbrrrb
    wugbguugbgwuubrgguuubbubbbbgburrwgrurbrbbu
    wwuurwwwgbbwrwgbgrrgrwrgbubbuubgwwurwgbwbubwrwrbbbgbub
    wggbwurgrurgrrwuugbwuwwuuwgbwwrgruwrgwuwwbububr
    gwuurguwbrrbwgbwrbbgubbggrwwrbbuwuuburrwgugbububr
    ugbrubwgugwwrugbrgubbbuurguuggwwubgggbrbwbwbwwgb
    gbbuuwbrrggbbrugrwbbbbgrurgbbguurggwurwbrwuguwwwgrbubuw
    wubbrwgbbgwguwggwrbgwbrgbrbwwugbbbrrwrbubr
    bggwbggrbrggrurrgrbbuwrwggrwbuuurguubrwbwuwrwuwr
    bururguuuuwwrgwububrrugwuuwwwwwugwwwgrugbggwrgrgrurgubrwug
    gwbgrguwurgrbgrwuwgwwuwruurrbbugrwwbbwbwgwuggbbgbgwugww
    grggbbwurgrrbugbwbgrrruubwgbugggrwwgwuuwwuwrgugwwwgg
    wrwbrgbwbrwbuwwwrrgggbbguuuuubuwurgwguuwurrggbwwrwugwgbrg
    gbuwgurgrwwbgbwuwgbwrruuwrrgrgbguguwbrgwrgrgrr
    uuurugbgrwrrubwbwbgugguurrguguuurrgrrwbbgwurwwwrbgwgw
    rgwrugrugrgbwwburwwwbggwwwwbwbwgrgbrgurbguubbguguubrb
    uggrwguwurubwbwrugguwggbbrrrrwwbruugwbrrgurubrgwbgwggr
    wbwggwwrbgbubgrwbubggwbwbbuggrrbgggbgrrurwggwwbbrrubr
    rrrbguburwgubrbubuwwwrgwwugbrwwwbuwuwrgrburbw
    rgrguwrgruuuwrgggbrgwgwwwgwrgwwurubuwurruurrwgubgwbwbgubr
    bwwgrwwrwrrgrbuuurrugrugurgwrurwrubgwwwurwuug
    rgwrggbrrbbgguwbbwwubggrbggugwrwbrgrruwuurgbwwu
    wwuurwugrwuguuubuwwurrgrrggggurrbbuwwbgrbrwwgurgrbggrruwg
    ugugwwwrggbgguwgwgwwgbggbwbgbgurrugugubr
    brrubwwgugwrrugugurruwrwbuuuruubrbbguwggurbrwuwg
    wgbbuwbgbgwuurwbbuuwgbuurbuggrwwugbgwgurwu
    gwwgguuugugruubrwbwbbwwgurrgrrgrwrbbuguruub
    wbuuuwrbbbbbbbwgwubuugggrubwubgurbwugrgrubwrw
    gurwrruwbguwrurwrbwrbggbbrwwgrbwrbrbururbubwbrurwwbubu
    rrggbbbrrrrwbbwgwrbbwwwwururgrwbgbguubgbwruru
    bgrwggurgwuubbrbgwgwguubrbwwwbrggwgbgrbwrrgwwubrgug
    bgwwgwgugrgrgwwbbgubwgugbuugrgrruugwwggwbr
    rbrugrgurbwwrburbgrugugrubbgrgrrugwggwgubbrru
    urwwbwgbuguurbbugrwrgugwwwgwuwgbuwwugbgbgrrrwbgrggrwbwu
    wrwubbwwrgrbrurwrgwrgwuwbubrwuugwggbwbbrrugrgbwwbubu
    rgbrubuurubgbwgbrgwrurgbggwgruruwwruurwugwwwurbbgwburguur
    gbrruwrgbwwubrbwgruuwuurbwugwruuubgruggwubugrbrrbrgwburgbb
    bggbwbgbwrgggrrggrbgubgwbbuwwrrwwggrwruwrbbbb
    brwbwrgggbbuwwrrgrgwguwbbwuburwwrgwggrggwbrgwububrrbr
    burburuwgwbguuwwugububgugbrbbbwwbuwggguwgwuwwbwugubr
    uwbrwwgbwwgbbrguubbrurbwgwwwgwugggruugrwrbwrugbw
    ggururbbrbbuubwbgbwurggggurrwubwurrrrrrgrbwgubgwrwbbgrggw
    wgwubuguwuuggwuwwgugbgugbwbgguggwugugguuurwbgwwwbururgwwru
    rgbrwgurwbwwgbguuuubuuuwuurrrguwwbwwgbruwbubu
    brwgbwwrubguwwbbruugbbugurbrwgbrgbrrwguubwuuuubgggwwgr
    gwrwburubgwwwgggubrbrguburbuuuggbwrrubugrgbguuwgubrgbr
    uwuwgwugwuwurgrrgwwurbwubbgbubgwwwggruguwbgwgbuggrrrbuwggr
    wbwwuurbbbubbbbubwuwrwgwuuguwwgwgrrwrrbruwbrw
    brrbrbbbuwbwrrbrrwurubrrgrwwbwuwbrwrwuururwgr
    wwbwubgbbugwgubbuwrugbwrurwwuruwbruuuguggbwrurgruuugbwwgww
    gbwbruuurgwwgwrbgrbrubgwbbruwurwrgurbugwubbuubuwguu
    gwwwugurbrburbbbbugrwwguwgbuugrgubgburuururwrrubwugurr
    bwggwurggguubbbggwrbbuwgwbrwgugburbrbwwrwu
    gurrrrgburbwwguuwburbwgwgwrwgwwwrrrbrgrgrwwbgg
    wbubbwwrggrururgrbwuwuwrurggrwbbwwrgwrbbguuwwwbrrububbrubr
    ruuwbrguwwguwwrwrrrbwrrgruggguurgrbwruugrrgwrwwbwrrbrbwbubr
    grgwubggbbgwwggubgbwwrbbuugwrbwuruuwubggwuuw
    bwwwurbburrgubbggbbugubwbgruguwugrrbggrurbwwru
    uruuugbbbuwrrwbugbggrwwurbbrbrwbwuwbuwuugwuwrb
    ubrggrgrrbbbbrbbbwwuubbrwrwugbrbbrggggbgrrwrgurbbbgb
    grwugbbbwrburgwguwrrbbgwgrwwbubrbbrrgbbgruwrwurbrgugubrwbr
    uburrrrwugububrbwugbugwggggwbgwugugbbrbwbwwggbbg
    ruwwbbuwwwrrbwbugbrurbrbbrrbguwrrrrwrwbwbubgggbbwrurwrbw
    gguugbuburbrggwuburwrrwggbbrwgguwrrrwbugwbwwuubrwggbubr
    gwrbgwuwrrbuguuuuuururgrwbbbrurgbgbuuubbrbububgggbrubr
    bbgggubgwgrwgguubuurbbugruwbwgbrbuuugggbwbburwwruuwwrwg
    uburbwrwguuruuwrbuwrbwruuwrgggwrgugurbbgrrgrbguu
    rgubwbbbbgwugburbrrugruwruwgwururbugrrwrguu
    rbgbgrgwggwbguggwuuwbuwruguguwwbuwwuurbbgrruwbugwwgbrg
    gwwbggwwrubwurbrubgurgrrwbgrwuwgbwuwrbgbwbrwrgbugwrgwrgub
    uubwwrwrbuubbwguuwwrurrurwbgurgwuuwbgbbrgubggr
    gwrwgwwbwrwugrbwgrgbbwrrrwbwrwgwuwbrbwrugrwuruuwrbbuuurubr
    brwuwbrgbuubwbwwbgbwrwugbgwgbbubrbwgruuwbwubgururbuggggur
    gwwbburwgwwrrgrwbwurrwbugrgwwwbgbgbwwwbrgbrwugbrrub
    wrbubbrgwwwgbgbuurgrwurrgwgrubwubrggrwuubbrwrbbrwgwrggbwww
    buuuwgruggwwwgggwbggwwrbruuubruubbbrwggggrgbbrwrwr
    ggbbwgwurrwuuwubrbwuguggbrugrgggwwrgubbuuwrburrwgr
    rwbwurbrwbrbuugbuggubbgggwwbrgbrrggugruurbr
    bggrgwuuguuwbruuurrwwbgwbgrgrbugbbrurbbwgurwwrwrrgr
    uwwuwbwuuwbwbburburbrwbrggbggurruurwrrwuwuubguuwbrbrgubr
    rrwuwuwwruwwbgwrgbbbrbrrbguguurubbwuurguugubbr
    wrwrbwbrurwwwbggwrbwrrrgrubgrurrwubwbugrrwrbgguwubruu
    rwuuubrruuwuggrruubrrgguwwwgbwbrgwguuwubbguru
    rbrubburggbrwbgrwrgbruggrbggrurrrwrbuurbubbuw
    rbbbwwrurrrgubbrwrrgrrwuwgrrwbbuuubgrwubgbbuug
    rrwgwbggggbrrrgubbbrbbbwbwwbbbwwgububwugwbubwbrbwubwwgubr
    urggbwurwbwwuubrwuurbuubbgbwuwwugrubrubwbwwrwrrubr
    uuwrgrgbrwuurgrgrrbbbwrbubgwgbwgwbuuwgbrurburbrbrruurub
    rurrrgbwwbbrubgrbwbbggrwubrwurubgwgwwuwrwbrbwgugugbubgbg
    rwurbrruwuruburgugwwbggrurugwggbgwuwgbwwurbuub
    ggrwbbgbuburwguuuruwbbbrububuwbburugwgrgwbbb
    wbgbrugurgbugwwruwggubrrbgruguwrbrgbwgrwuburrrurw
    gbwugugbbgburgurbrugbwrgbwwwgwurbbrwwgugrubwgrwwbuugrubr
    wrbuuwbrubbbrwwwwrgrrwubgrrrwwuwubrrruugbrurrrgugg
    bbwwbwuubbgbgbrrwruwruwbbwggrwubguruwwwrguggwwwgrugrgwww
    wwwrbwrgrbugbrrrbrrrwburuwguruwwwrgrbrugurrwbug
    grgbbwbrggrbrwrbuuururrwgruggwbrwwgbwbrwgwgruggbggwrwbbggg
    wwurgbbgwrrwuubguuugguubwbrgubggwrwggwbwwrgrugbwbg
    uwugrgubrwwwgugwrwgbggwbggbwbuuurguwwggbrrgwgwuggrgruuu
    wuwrububwgrgbbuubrwbrguwbwubbwuwwuubggwwwuggbw
    rgrbgwubgrrwbbbuwrwrrbbwwburubbbguwbgrbgwgrubggubr
    wgbwwgbrwurwrwrrrburbwgurwbwbwbbugrwrbguwwgubwbrugruuu
    uwrrbbubbggbgbwuruggrugwbwrrgrubwbrgbwbwguurrubrb
    wbggwgggbrbwurwbubuwwggrwugbgrgruugrurrrwuuwwrbugubr
    rbgwrrurrurgrrgwburbrgbugbwurwbbgurubuwruuwbbwbwgr
    ruburgwrgugurgbwwbugrbrruwrugugwuwwbguwurbugguubrwugubr
    rwwuwgbwgbggburgwwugrrbbbgwbwgrbgrrwgugbgrr
    ugbgbggbwwuwggrgwrwwwwuurwrbwbwrggbwwrwwrrrbwwuwbrgbggurb
    uuwrwrwuwrguwuwwbbwbggugguwrrbbuuurugrubr
    gggggbuwbggbugbrrwubrrbwuuwbbrugbbwrrgbwrugwgubr
    bbrgwrwwbggbbwbwrrwwbbwubgguwgugrrgbrwgbrgurggwwbuwuwwu
    grgwgrbugurburbrggwugrrbwwguwbwgrrrwurwbbbggwbbbggu
    rwbbgwuwwrwbwbgrbgrrguuwbbrubbgrbgbbuwwrbggubggrugur
    ubwrgrwwgbwgwbgwbbuwbuurgwgruwrrurubwbgbuww
    rggrwrbbwwrgbbgggurugugugbwbuurrrgrguugwwwubgwugubr
    grrgruuwurgwrbwbrubwugrggwgubruuubgwbrrwggrbrwr
    gubbruwubwuwwbrbwgbguuwbguwwgwwbbwubwwbwururbbrwurru
    gwwbbuuwwrggruguubbbwurwwgwwbrbrrubrrbwwrrrbwrrgburwgwg
    ubrugrgurrgugwrubwrgbbuwwgggugwuugruwbbbbu
    uwgwuuuguugbruugwbrgwugburwbrbggbbwgbgwbrgwwbrbbwbbww
    wwbrguurbrrrwbwurrwrgwbrrwurrgwwuwbbbwwwgbrbguubgbguwgu
    ugggrgbwwwrwubbbgurrwuwuuwwwwwgrugwubbbggugugrrbubr
    wuwbguugwbrgrgugwuwbgrgrgugurgbbwrrubruguwrwbburrwgbuwwr
    gwburgbgrwwwwrrubgwubruggrubgubwruwwrwruggubrgrwrubb
    gbrwbwgwwwbbggwubwggggwbwbuwuurwggwurbwwgburuw
    uurgurgwwwgwbrgbgbwwwrrgugrbwwuguwbwbwwuwurb
    urrrgrrguruububwugwrwgurggwbbrbbrguuwbrbrwbwburgrwb
    ruurwgbwbwrbwbgrwrbbwggwrbbgrubrbwwgbrwgbwuuuwbuwrwrwwg
    gbuuuwuggrwuwgwubbububbuwgwgwrrwgguwwgwgurrwubbb
    urrubgbrbugrgwguwbubugwbrwwrurrrrwgrwuwuurrru
    gbbrwbggwbbbwguwwrwwwwgbrbrgururubgrbbuugbwggrwgurbrbuuub
    wgwgrugbrgbbwuuggbugbgugurbgruuugrgwwwuugwr
    guurbbbruububgrurrgbwuwwrbugbrguwrwubwbwgbwurrrguwuuuwrugr
    ggrrrrwwgwwbruurbrgguguwuuurgbggubrbrwguurubr
    brgbbbwwruwrrwbbbbwwwrrbwbrbwbwubwbrrbwgur
    rrruwbwbgruugbugurgrgwgwbrwgwgrrrgbbgruugbwuuggrrubu
    wggrbuwbuurbwububwrurgrugwgggwruburbrwrrgwu
    rubrbuurggrwwrggrrguruwwbugbgugrbrbbuubugrggurw
    rbwrrrwwugwuruuugwbwbwwgrburgruubbrurwwrubr
    gbgwurwurrubuubbbbrbubwuruurbwwubgrrwbuuburgrgbwwbuug
    gbgwbrrwurwrrubggwwbbuwgbuggubwrubbggwbuuwwburgbrbgrgrwrrw
    ugwbggbuwbgwrwugrbwbggrrggwbgugruruwrgbguwrwuubgur
    urwggbwggrgggwgubgrbururgwwbrgbubbbgbrugubgggwrbr
    wrwbwuguwgbrwuuwwbwbbgwuwwgbuuwbubuggbwwbgwwurrbrgwr
    buwubwgbguwuggruurrurgrgrugrwggrgwrrrwbwrwburugwgbgwwg
    gruwwugwrrurbggrrbbbbbuguuwurwbgurgwgggrbubggbgrwrwwwbr
    uwubwubgwwuwuubbuburruwwruwbubugruurrbrburwgwbbwuwwrgwr
    wuwrugruggrbgggwugwwubuuuwgbbbbrrgbuubrururbrrurwwruurbggr
    wrrwwrbugurgbuwggbguuuwbbruwugwugugwruruubbrgbgubr
    bggwuggbbbgurguruubgwbuwuurgrrwurwwwwrguwwggbrgbrwgbuwuub
    rwugugrbrgbwrbrugbwwrrbrrwbgrwguwrgubgubwgbrgubr
    bwubgwwugbwgurgwgbwrbwugbrgwrgwgwwbbubugrwbggwbgwubg
    gbwbuubbubbbwbgbwuburbbwubbwwwubuwbrbbrurwwbggwuwgrugwbwrubr
    rbbbrrgbugugwgbuwwbrrbwwuurwbwwbgbgwuububr
    wwbubggrubrbbwugurwrwbuuurgrgwuggbbgbubr
    wrwrubbuuurgbbwgrgrugugbwuwwurbgwwrbbrbwgugrurwuruguwbbr
    buwgwgggubuwrrruwgwggrrurwuwrbwgugbrrgrruuwrrugugw
    rgwbrubrwwwwugugbbgrrruwgrggubwgrwwrbbgugbrugwwguwr
    gurbruguwwgrwrwubbgbwwgbuubbruuuuwgwwuwrrgubgbrubrrbr
    ubrbuuugwgguwugwgbgwwburwurrggrggubrurggugwwbbrrgbr
    ruububuuwbggrbugrwbrbrwrrwbruugubrurrrgugguwbr
    rbguguwbbbuwwwruruwbgrugugwbuwbuwwrbugwrrrrubr
    wwgbwuwwgrgurguwbrrrwbgwwggbggggbuuwwrbwwgrwrgurgwrwrwrwww
    wbgrgubguwrbwbgggbwbrwwrgubbrruuugwgbbrwggwwbwru
    uugbwrgbrwrbruwguurggggugggbbrrwbwbruwgrubwgr
    uwwbbuwguggrurgguurbuuuwbrguurbbbrrbruurgubr
    gbguwruubrurwrgbrwgrgwugbgbbgugbbrgbrwurggb
    ugbuggubbwgwrruubwugwwugbuwrbrrugrwgrbrbrggbwwuwu
    grrwgwbubuwwuugrwgubbrwurwrbguwrrwbwwwrwbggg
    bgguggwbwrbwrbubbbubgwgwbuwrwggwggrbuuwguubbrbguugrurrwu
    rbgrguugrururggwwwurwgwggrrbrrwguwubgurwubbbubrrgwu
    ubwgururrugggbwuuwwbruggubuuruwwbruurrwuwuwrwrwwuuw
    wuuwrwburwgwubrbwurbbgrbrbwuurgbwbbugrbgrgugbrbrbubgguwr
    uugbbrbburrwwwgwugwwrbgrbbuburuggbwrrugrbbrbugwgwgwrbg
    rubuwgwburbggbugbggbbuggurwbgrwgwubggbgwwgwwr
    bubbrwgwwwbrwggrrbbrgrbgugggwbwugbwbbrrgwbgggbbuwrbgr
    bggbwgubgwrgrbbggwgwwwwgrrwrwwbbugwurugwwgubrbwgbgbb
    wgrgrrrgubwgrwbguwrgbwuggwuruwggbuggwuwugrwwugguwgwburrrg
    bbwuwwbuuwbbgwuubbrrrbgubrubrrrrruuuguurguuwbuwguubu
    uugwruwubbugguubgubgubwrguwgrrwbbbbruwbwbwub
    wbrwwgubugrwubbuubgrwwwrubgrwubrwgrrbgguburrubbwuggguu
    bbrwggwrrrggggrwwruggwrguubwrggrwbugwwuguw
    wruruubrbugbgbrwwrrgurrbuugbuubuwguuwgugru
    gurbwbbrububuwwgwuurwwbwuuuwbuuugbbbuururrwuubbwwgb
    brrrrwurbrrbbbbbuwbrwgrwubwwwbbguwbbrubgugugwbburrbburguu
    uguwggwubbgurwwrrrgwbuwgwbwwwgubggruuurgrurgrwrr
    bgbbgwbbbwgbbwrrrgurbuwugbwuwbgwuurgbbrrrrguwwuw
    wrrruwuwwgrwbrwbgguuubwubuggbrbrwwgwbwrbgwrwbrwrburw
    wwgurwrwbbwgubburwgrguububbguuwbrubrrgubbbr
    rwbgwwbbrugrgbbgguruwrruwbbwuuuuuubgrgrgbuurrrurrrggbu
    rugbuuuuugurwgrgubggbgrgwggwbuwgwrwbubrgwgrwwgubbrw
    wgbwbbrbbbbbwggbuuggguwwguwruuuwwguugubr
    grwuwbwbgbrwgwggbburwbrurbuwurwbwwugwwwbwwwgggwwrrbgu
    wwbrurbgbugrggrrwgubggubwrbbbgbrbwrwwuggbgruububbwuwgu
    bwuburbwbuguuuuwrwrguuwrbbuuwbrrwgrrgugrrbbuwbgwugwgg
    wbwguubwbugruwgburruuwrbbrwbwbubrbwwrgrbwbuw
    rgwurubrrbgrbburwbgugrrrbgbrbgwruwubbuuwrg
    wbgwwwbrgurbbrwuuwbggwwgugwrbbgwugubbgrwbgrrgwgg
    ugugggrggrwwbubrbrbgbgwgrgbuburbgggbugbwbu
    bgguwuwbbwbrrwburggggbbwrrwubwuuuuuurubr
    wgbwbrwggbuguwwuwrwgwgruwrrgwbgwwguburwrubububrguuguw
    wbwrugrgbwrurbguwggbguwrurrgrwwwrrbubuwwwrwbwwwrgrgbrr
    wuguuubrbgwguwwrgbwwwgbubuurrrgwrgrgbuugugrrbwbugrwurgg
    rgrrgwuwugurgbwrrwrubwbguwgwbguwugggwwgrrbrwrgwburbrwu
    bwbwwguwubrrgbrrbwrwwgbuuuwbrrgwbwrrgbggwuwrugrwwbwbburu
    uggwuwruugwbbruwuggurgwburwrrgbuurwbbgrwuwrgugguw
    wrggrbuuwbbbwugbbuugbgbwwbgbwuwrurwbugbrgurbbubgurrgg
    guwbbgbwbugubgrbwgrrugwbbgggwwbbbrwrwuguurrurwbrbwgubb
    brbrurwburugwbwgwgrwbrrwggbbbbubrrubwrbbbbwgurbbbwwbrbubr
    wbwrubgruuubgbrurguuwwbubbwuuwgrbugbguwrbruruwgrug
    bgwuugrgbuwgwgbuubbrruubwwwgrrububggggwrrrbgrugwguburrrbrr
    urgbgwwgwgwubbugbbugwbrrguubbgrgrwugurgugrrububrwgwbwub
    rwwrwbggwbbwwgwgrbgrbgrgugruubgwrrwuggugugbgu
    gruurrwgubbggububbrbrrubgrguwwuwbgburrwrrw
    grguugwrrrurbwgguurbuwrbuwrwuuubruggbbwbubuubgwbbg
    ugwuurwggrwwwggbugwgbwurrrbbgwwgbuuwwburrbbw
    ggwwuugrgwwrbgburggrrgbbbggrrugruwbgrgbwgbwbuwbuurgrbru
    urwrrbrrggububguuwbbrbrbruwbbrugrgwubuggggrgwgbubbwburuwr
    bwwwgrguurgwgurururgwbrwrurrgbrrrurwrguuuwgrbrwwubuwubrug
    wrwbbwgugubbwbwgbwggrruuwrbrbggurubbrrrgbw
    bururrwrbbgruwuggwgrwgrwwgwwuuwbbbgwuwgrwuwwbubgbgwrrrbw
    brrrbrwgrrrgrwrrugbugubruububrwwwgrwwgwubbbrrggrugwwbbgww
    rbrbuubuurugggubugwrgbggurugwbruwurwuuwgurbbbrwgw
    ugbugrrbwwbwrwrrwrurwwwgurgwgubwgwwrrwgrbbrbgwbbu
    uurbruwwwbrugbwwgrubrwbruuuggrbwwggbwbugurwgwubbrbbgwbww
    uruwugrubuwguwurwwwubruwwrgwrwuubrggbrguuwrrbggru
    wbbrwrrruwbbbwwwurrubuuggwgrwrgggbwubgruubwrruwgubr
    gwwwgrwwgururguwwbrwgwuuwrubgbwwrbwubbbwwbbr
    guwbrbwrwggugggwuwgurbrruubrbbwrrbwuguubwubuurbgrbuu
    bgguubbwgbwuwwwrgwgwbuurwgrbrwwbguuwwruwgrr
    bbrbuwrgurrburuwrwrwurugrwwwbruuuwrrrbbubr
    burrurbbwurrrbbgrubgwrbbrguwrwwwugggbruuwurgw
    rgguruuwgbgbrwwwurugugugurrugbbrrugwgbuwguwgbuugwbubbbbrubr
    ugbwubrwgbggbugwuubwruurwgbrrwgurwggbrwrurubbgwrru
    wbgugubuwbwrugwwwguburguugwrwgrrwwgrwwrwwrggbbgubr
    rbwwrgwguubbgrbguuwggrbbgbugbgwurrrrwbwwbr
    grgbugbwugbbuggwgwwurguwbwuwwurbwrurrrwgwwwuguurbwgwub
    ubwbrurrwguwguburrubrrrwwwggwgbbbwrurubwbuugbbrbgwuruwbubr
    rgrgwrgbguuugwgrrbgrubwwwbrbwuguggbgbuwuwuu
    wugguubgwbbbwbgwgbwrubuugbbbgwrruwwugrbuubbuubr
    bbrbgbwurubugurbuuruwuuuwbwrgbubggbwbuwwbwbwuwbrbwrbg
    brbubwrgugwwgurbbburbrrbrgwgrbugubrguuuurgwwgrugbgubr
    ggrggwguwwuuggurugbwwwgrbbgrburrwguubrwrbg
    rrbwgurrwruurwrbugbubggbggugwurbwgbgwbwgbgwrrbrgubr
    wwgwruugubbwwuwbwuuwbrgbbbbwwuburwwugwrrwrwugwrrgubr
    wrgwurwuubruwbuuwbwuwbrggrbgrgbugguwrbbwbu
    rgbbrgugggwurwurggbrugwuwggrbbwwrgugbburuguwrggrwguw
    wrbbrwbgrbwbrwgrrwrwurgwggbbbrgubwwgubbrwuwbgwbrubr
    uwurbgbwrrbwbrbrubuurgubwuwrrbrbbgwggwrubbgwbwrgburrrggr
    brugruwbrburugbbgwuguurrrgbgubrbgwwurubruw
    grguggwrgbgbwbuurwbuwbgbrwruurbburrwggbrubgrwurbwbgb
    ubugwrgbgrwgwwgwguugugrbuubbrguggbgbwuurbwbburwurgwbugrubr
    bgrwbuwruwrwgwwggrugbwrwrwggugbbuwgbugwrwwgurrgwwbbubr
    gwbuubrrwugrrbuuurggurruugwbwwbrrbbrbwbbwbbbr
    rbguwgbwrrgbububgwbrrwbgwwrgbbrbuguwwggwbu
    ubwubwrwbgrggggrbrgbruggbuubwgwuwwbrgrgggubggugbgg
    wuruubbuugbbrwubguwgrgwbbgrbuuugwrwbgwgwgwbbwrgrbgrbw
    rbbuwuwwrwgbwwwurgwrurggwrrrbrruuwbwwgwgwggrbrrrbbwgrwr
    wrgbgbgrggbrurrbrwrbbruwbrwbrrggurrrguwuubwuwuwb
    bbrrubwbwguruggwrwrbrggrwwgrgbwrwwgbwuurubrurgugwrbgbubr
    rbwwurugwuuwwbbwgbbugrbbggbrguuuguwrbuuruwu
    ggrrwwruwrbugwgurgrggwwbugrgurwbwuuwwburwbubub
    rrruuuurbwbwbwguurgubrrrgwbbwrrububbrrbbbbrugwwwg
    uguwwwbrwurruuugrbrguuwbgwububugbgwruguuuurbruwrrrgwugb
    rggrbuuurrrwbbgwbwruuburbgggrurguwbgbrrruuguuww
    bwbrgububugbrwgggurrbubbubbgggwwburgbrubr
    rrurgrruwbbbwuwgrurwurgbbggbuwbbgrwggrwbrrrgwuubr
    rrururbgbuwrrwuggubwwwgbrurgubgwgwbwwguuwgubr
    uuguwbwrgbugrbgwguuwrgbrrwuggrwwburrgwguwbrrwubru
    wbrbgbwwbbbrbguugbrwgbbuuuugwwrururrrgrwgw
    ggwurrgrburbbrrrguwgurgwwguuuubggrubbwguwbru
    rwruwrwbbbwwbbwgwgwgruuwbbguuuggwbrbrurrgrgrgwuw
    ggwwwrrrbrrwgbugrggbbwrrrbggwuwgwrrwbgwuwbuguwuur
    urgrgbrgugugbbwggbugwwgrubgrgwbwurggrwwrbbbub
    rgbrwgurbbwbgbbguwwrrbbguwburuwrgrwubrrgguwur
    rggubwuuwgugubuggwbwrbggrgubbbubwurwwwurwguggbbgubguu
    wuubgrrrugrgbubuwuuruuurrwbuuuwburrubuggruguwurwbbb
    wrbbuuggrbrbwbbubugrubwurgrggwgrwbwgrugbubbbgbbrgwu
    gwuwbrwrwurubgwgbrwruguugurrugwrgbbubgrurgbrgbrwbwgugbubr
    gwuggguuwwwuburwggruwwbubrgwugrububrgurrgu
    buwgwwgbwuwrwugubwgggbbwuwrurbgugwrgwrugbwgbrgu
    bgbuguurbrugubuggbgrrubbguurgbbbbburubwgwbrgbubrw
    bwbrwrguburrguwrubwrwguuugguwbbgubbrbrburugbuugrrrrw
    wruuwbwbgggwrwwuwwugbbuuburbwurgbruwbggwgu
    bbruwrwugwurgwwwrbwgbbuuugbrgrwggwbubugwwgwwubwrbbrrguggru
    wugrrugwwbggrbggrrwwwgbrbgrgbuguggwuwuwwubwbruurbgwrbbwrbg
    grgwuwruwbgwbugbuuwgrurbwwwwguuwuwwuurwbuubg
    urbwuurggbgwgrgburbbrubwgrbrrgrbbuggbrwwuuuuuuuwbu
    gwwbgbubbwuwrwwwuwwrwrgubwwgggubrugwbgrwwbbggububr
    wwwugwgbwwbwrrrbgrggrugugwruwbwbwwwubbbrwuguwuwgurrwuwrr
    wubrgwwbgwbrwurugrbrgrururrugrbrggwgbwbugbrugguggurgwbwugb
    rbbrgwurbbruubgbbgwwwbwwwurbrruugwubrwuubbuwgbguwwg
    rurrwgrwbbwrububrruwrubuwugwrbrgbrrwuuwrguwgwururw
    ubbgrrgrrrwbwgurugrrgrwrugwgrwgwruurrgwguwuwubbugggurbu
    rrbbrrbuwbrbwwwbbuwgwwgrwbugrwbuwgggbbbwguuwgbburbb
    wgbrbrrbbugbrwbbugwrbwurbwugrguwwrrurrbwbrguuwrurwwwbguggubr
    bwguwgrrurbbwuugbbwgwwurgrrrwwrurggwrggrubuu
    ugubrwrwgbuwwgbgugwgrrubuwruwgbgrgbubwwgwwwurr
    rurrwgrwrbwwgrbuwrwguuubbbbguurbgrugbgrwurwurrwbwgrwbbuw
    uwwuugrwubuwubuuubrwuwbbuwwrwubrrbgwwgrbuwgggubr
    gbruwrugbbubggburwgrrbgubbuwbuwbbgguwrggwrrrgur
    gbuwwgrubbwwgugwwbgbwrbwwgrburrgruggugrwwwrggurwggbwbgub
    ubguuruuwwruubrrwbwuwggbburgwrbwggbguburrrb
    bwrrwggubgbgbrrwgruwburrubbrrbrugrbwwggbrurwgub
    rruruuwgguugrugwguuwwgguggrwuuuwrbgbbuwwruwrwrrrbubgurg
    rbugrrbwrrbrbrruwbbubrubwbrbgggwrgbbuurgrgbwbb
    rggwbbuwrwwuwubbbrwrgwruugbwgwgwgwwuwuwbggwbubr
    ggrubugurrbugwgrgbbrrwrwgrwbwgubwgwubwuwrrrgb
    grrgbubuwwgruwbuburgbwgbgbgrbwubgwbwggbugwwuwbrguwrg
    wgwwrwrgbrwugbuwuubgrggrwrurwwgurbrwuurbwgwgbgggubr
    bgbwbrrurwggbwwugrrgrwwrwurgwruuuuuwwggrgggwugbrrwwrwgg
    wwrwrgugwbwbuguwguugwubwrwwbwggggwgrubrgggbu
    wrwgrubrrrrbgwbbwbwuuwrrwruurgbbbwbwbwgwuwggubr
    wrrruuwggrwwwwrgugbwbrwwbwuurwguugugbbgrbb
    guwguguwugrwrugbrwwrwuugwgrwbuubuwbbuwrbrbbbbgbwwrbrggbwb
    ugrbwbgwubruwwgwruwwgbgubgbguggwrrgbguwbrbgwuwg
    wrwbubrwrruburubwrugbwwwuuggbrbbbwrrbwgbugurbuguwugrw
    rgrurwbrbwgwwrbwubgwrwbubgbgbrubbwbuuubbgrbgbwbrubrwg
    wrbbgbrwrrwurbuwruuuugwrgrwgrguwwubwrugbwrwwgwurw
    uuwuugbrwgugbgbrrbwrgwbwubggbrrwwwggwbubuubrbwg
    ggwruwrwgurwggggruurugbgrrbbwbrugguuugrbbbwg
    gugwruwrrubgrubrgburwrbbwgbrrbuuwggbuuwrubrbbgbgbbrg
    gurguuwbbbgbubrwgwuuwwwgbwgrwwrrubgurugwwrbwwubrw
    wwubuuurbgwbwubggbbrbugbgwugwwuwbgruwwbbgubbguuwbwwwwbbubr
    wugwuurbbwgrwrrrbrwbgwrrrwbwrwguwruuuwwuwrgrbgrbru
    wgrbububurugwwrbrwrrwbwbggrurwwwbrwgrbuuurwwwr
    ubburgububwrrbuurugwuuubgrrbggwrrgwwrgrurubrwgrrwr
    burrwgbwggwrrbgwrgwrrgbbbbrgwubugwgrrugbrugr
    gwggrguuwgbubrugurrrwrwuwrugwuguuggbbbwrwg
    ggrbwuurwbrwrbgbbubwbgwurggrgguurwrbuugbguuwwrrb
    rurrgrwuwguuururgurbgwgruuwwwbbururrrubbruugbbbubr
    urgurbwrrgurgwwbbwwruguwgbwrubwwbwbbrwuwugrrwuw
    ugwrurrwgrubuuuwwwrrrruwubugwwwrgbwrwwubbggrrwgwuwbwwwruu
    wuwguuwwbrgubgwbgruwwrbbgugubgbrgbbbwurgrbwbuwrbbubr
    rwrguuwbuwbrgrgbbrrbbwubrggrggwbguuwurgggwuguubb
    guubrbwwrrgbgrwwrgbrgubgwbggrubuubgubruubr
    brubwwgruguwbwwugrwubugrwgbbuwwuwbgrrgbgrrgw
    rgrggwbwurwbwrwwubggwrbbbrgbgbwbgrurgrurbgurrgwgburbgbbgbu
    rrrrggbrwuggbruwwwgwggubwwuugbbubbuurgrbgrgwrwrubrg
    ruuburbwrruwwrgwuwbwguggwguurgburwurugubgrrgbw
    urgrbuggugrgubbwuwwwuwuuuguugbrurgwuugrwbgggub
    rbrgrgrrwuuwubbbgwbuuwbrbgbrgwuburwrbuuwgbgrug
    gbrwubuggwruuwbwwburrurggububurbguugbuwrgggbgubwrugwurwbbr
    buwwbrrugwgbgurwbgrwbubwrwbruubwgrrwrwrrrr
    ruwuburrrgrgbbwggguwbwwruruurgugwbrwgubwguuwwburg
    uururubbrgrugrbwuurrgugrruwrgugrbuuuurwrwwbbgubr
    brwrbugruwbgrwurbbbbubbbbbbgwwuggrwrgwrbrruubrbwwrwuurr
    rrgbggururubuggwrurwrwwrububruguuurgugbbgbugwggguurw
    uwgbwwwrwubgbwbrrgwgbruuwrurrgrwgburwbrbwgbrrwrrubr
    ubwruwuuuuubwwrwubguuwrwrrwbuguwurwrbuurbrwruwuurwubgww
    gbbbwggrgwrgubgrrrwuggbwwgbwbubrwbgurrgbrrrguuw
    gwbwugbwgbubwwbwbuuuuwwggrbgwuggurgwgubr
    wgbwugrguwuubgrburbwuwguurwrggrbrgburubr
    ruwwgbbuubgrwubgwwbgugwbbrugwuugurrgrrwbgubwgwwrrwbgbgrbu
    wurrrguwggwbwuuuurwbbruwbuubbbugwgwgrwbgurrrrburrwuwbwrbb
    bgbbggwubgbgwwguwwbgrrgrbrgbbbgbgwgbggrrbwbuubrgguwububr
    wwbrwggbbuggruurrbuugwbgbwrugguuurguugwgwbwguug
    bgbrbrrrbwbuuurgwuwbugugubbgbruuuwbwwgruguggbg
    rwbuuburgbuwuuubgwrbguugbwrbgubwbwbwrrgwurgbwg
    wbggrwugbggrrbwwwbgwurgbbuwbwbrrrbrwbwrrugbwgggrbw
    bbwbgugurbrwgbrrgugugwruubbwbgguwrggrbgbgwrbbgrbuguwrubr
    uwgrwbwgrggwwwuuuubwwgbbgrbrbwggrbbwwbwrrrbwgbwrurgurrrrubr
    guwurbbwgrwurwuggwwgwgwbwbuuwgubuguggburbruggbrgrrrw
    ggbrubgwgubbbrgruuwggwbrggrgbuguwbwwwurbggwrwbruuubrww
    uwruwrrwwurwggwruurwbubrrbgwrbwruuwwwrgggwuuur
    grbgwbrbbwbgbruwwbbbrwgurbrgbgbbwurgugwbgbwgbwurw
    uwbrrggwugrgwrrgbbgggbggrugrbggwbgbrwgwuwgwubgurwugruwgur
    uubggbwwwggubbrurubbgbgrwuwuuurbrgubwbwrbuwwgwgbr
    gbwwbwwbuguwugwrurrbbbwurwurggurguguguruwuwgb
    uurububbgruwguwurbrgrruuuurruwwuburwrrbbgrgugrgggwrwrrg
    rbgruwuugbgbrgrgugrgrrrwruuggubbwrbruubbbr
    rrrrwugrrrrrubrbrgwgrrgwrubgwuuuburburburwrrgbwu
    ruwgugbguwuwgrwbrbgwruwurbugwwgbwwugbrrbwguur
    ugrrwbbwwbwwbbururwguuwwurwrguggggugrwbugwwgwgg
    gbwurruuubuurrwurbuwwwubbwbwbbrgrbrwbbwrrrbruwrwubbrbgwbwb
""".trimIndent().lines()
