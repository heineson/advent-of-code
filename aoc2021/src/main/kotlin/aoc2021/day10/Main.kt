package aoc2021.day10

import java.util.*
import kotlin.math.floor

val chunkLimits = mapOf(
    '(' to ')',
    '[' to ']',
    '{' to '}',
    '<' to '>'
)
val errorScoring = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137
)
val autocompleteScoring = mapOf(
    ')' to 1,
    ']' to 2,
    '}' to 3,
    '>' to 4
)
fun syntaxErrorScore(l: String): Int {
    val stack = LinkedList<Char>()
    l.forEach { c ->
        if (c in chunkLimits.keys) {
            stack.push(c)
        } else {
            val start = stack.pop()
            if (c != chunkLimits[start]) {
                return errorScoring.getValue(c)
            }
        }
    }
    return 0
}

fun autocompleteScore(l: String): Long {
    val stack = LinkedList<Char>()
    l.forEach { c ->
        if (c in chunkLimits.keys) {
            stack.push(c)
        } else {
            stack.pop()
        }
    }
    return stack.fold(0L) { acc, c -> acc*5 + autocompleteScoring.getValue(chunkLimits.getValue(c)) }
}

fun main() {
    println(actualData.sumOf { syntaxErrorScore(it) }) // 167379
    println()
    actualData.let { data ->
        val incomplete = data.filter { syntaxErrorScore(it) == 0 }
        val s = incomplete.map { autocompleteScore(it) }.sorted()[floor(incomplete.size.toDouble() / 2).toInt()]
        println(s) // 2776842859
    }
}

val testData = """
    [({(<(())[]>[[{[]{<()<>>
    [(()[<>])]({[<{<<[]>>(
    {([(<{}[<>[]}>{[]{[(<()>
    (((({<>}<{<{<>}{[]{[]{}
    [[<[([]))<([[{}[[()]]]
    [{[{({}]{}}([{[{{{}}([]
    {<[[]]>}<{[{[{[]{()[[[]
    [<(<(<(<{}))><([]([]()
    <{([([[(<>()){}]>(<<{{
    <{([{{}}[<[[[<>{}]]]>[]]
""".trimIndent().lines()

val actualData = """
    ((<(<{(<([<<{<[]<>><()>}<([]{})[<><>]>>({<{}<>><()()>})>{[({{}()}{<>()})]}]}>)[(({{<((<><>)([][])
    {{[({{{[[<<{<([]{})({}())>}>([({()<>}{<><>})]{<(<>[])[()()]>{(<><>)(<>)}})><(<[[<>{}]<{}<>>]{<[
    ([{<[[((([([(({}))[<[]()>{[][]}]][<{{}{}}({}{}]><(<>{})([][])>])[<((()<>))>[<<<>{}>({}{})>(
    ({<{<[<<((<({<[]>{{}}}{<<>[]>(<>)}){<{<>()}{[]{}}>[[<>[]][[]{}]]}>[<<[<>()]>>(<<{}()><<>()>>[{{}{}}{()[
    {(<[{[<{[<{{{[{}{}]{[]{}}}[<()()><[]<>>]}[[<()()>{[]<>}}{{{}<>}(<>())}]}{<[<[]()>([][])]({[]{}}
    [<<({{({[[({{<[]()><<>[]>}{<(){}>{[]{}}}}[({<>()}({}[])){[{}{}]<()[]>}])[{<(<>[])>}<([()<>])((())(()[]
    <<<({<[[[({([<()<>>([]{})](<[]()>([]{})))(<[<>[]][[]{}]>([()()]<[]>)]})]{{{<[[()[]](<>())]{[<>{}]{<
    {(<{{{<[{{<<{[[]()][{}<>]}>(({[]}[()<>])([()<>][()<>]))>[{([()][[]<>])}]}}<([([[[][]]{[]<>}]{([][])({}())})]
    [[([<{[(<(<[[([]<>)]{[<>()](<>{})}]{{[{}{}]{{}{}}}}><<[([]{}){<>{}}]{[[]{}]{[]<>}}>(({(){}}){{()[]
    [<((<<[{<[(<[<()()>]<<()<>>([]<>)]>({{()[]}{()[]}}[{{}()}<<>[]>]))([([[]<>]<(){}>)[<{}{}>[[
    [[{{<{({<[([((()[]){{}<>})[<<><>>{{}<>}]](<<{}[]>[[][]]>{{()}<[][]>}))<<(({}<>)([][]))>(<([]
    <[({<(<[(<(([{{}[]}]<[<>()]<()<>>>)({{<>()}{{}[]}}{({})({}[])})){[[{<><>}[{}[])][[[]()][()[]]]][{([]())[()()]
    <(<[{<[{{(<{{[[]]{<>[]}}}(({{}<>}<<>[]>)[<<><>>{()[]}])><([(<>())]<[()[]]{(){}}>)(<[<>[]][<>()]>({[]<>}[
    <([[({[((({(<<()[]>{[][]}>(<()[]>(<>()))){{(<>()){{}{}}}[<{}{}]({}<>)]}}{{<([]){[]{}}>}[{{{}[
    <[{[<(([{[<<((()()){<>()})((<>{}))>(([()[]]([]())){{()[]}[[]<>]})>](([<[[]<>]({}<>)>{(<>){{}()}}]))}(
    <([{[({{(<[[{<<><>>{[]()}}{<{}[]><()[]>}]<(<{}[]>{{}[]})>]>[{<<{[][]}<()()>>>[<(()[])<()[]>>[[()[
    {<[{[{([[[(({{[][]}{[]}}<[()[]]<<>{}>>)<<(<>()){{}{}}><<[]<>>({}())>>)(((<{}[]>[()[]]){([]())((){})})
    [{<[<([{([{(<{[]<>}>(<()[]>{{}{}})){<[[]<>]{()()}>{[[]](()[])}}}<{(({}{})<[]<>>){[()<>]}}>]<([({{}<>}({}[]))]
    {[[{({[({{[[({()()}<<>()>)[[()()]{{}[]}]][([[]<>][()()])<([]())([][]}>]]}})[((([(({}{}))<({}<>)<{}[
    {<[{([[<{{<<({<>{}}<<><>>)<{<>}([][])>>[{([]())}<{{}[]}>]>}}<<{(({(){}}))}><([(<()[]>((){}))({()<>})]
    <(({(<{([((<<<()()>{{}{}}>{[(){}]([][])}><({(){}}[[]()])<<{}>>>)<<(<[][]]([]()))<{[]{}}(<>
    <<<[<([<([(((<{}{}>){<()[]>{[]<>}})({(<>())[{}{}]}))([(<()[]>{<>[]})<[(){}][()()]>])])>]<(<({{(<[]{}){()<
    ({<(<{{(<([(<(()())<{}[]>>[[{}<>][<>]])]{([{[]{}}[[][]]]([<>()]{()})){<([]())[[]<>]>}}){{[{(()<>){
    <{{{[<<(<<{([<()[]>([]())]<{[]{}}<{}<>>>)[[<[]{}>]{[{}<>]{<>{}}}]}<[<[[][]][[][]]>]({<(){}><{}()>}({
    <[[<(([([(<{[({}{}){()[]}]{[{}[]]([]<>)}}[<(())[()]]]>([([()[]][()()])]([(()<>)(<><>)](([]())[{}{}]))))])]))(
    ([<[<{[{[([((([])]({<>[]}{{}[]}))(<{[]{}}{{}()}><{()[]}[{}{}]>)])<[{[(()[])<()<>>]<<{}<>>(()<>)>}{({<>
    ([{{{[<[({(({([][])[()<>]}{{[][]}[()[]]})({{[][]}<[]{}>}{{{}[]}<{}[])})){<<<<>{}>({}{})>{{
    {<{[([{<[({<{{{}{}}}{{[]}{()[]}}>}{(([()[]][{}[]])]})]<<(<[{()<>}([][])][{<>()}]>(({[][]}{<>[]})[[[][]]]))
    [([<(<[{<({<({[]()}<<>{}>)<<()[]>[[]()]>>}[{<[()[]]{{}[]}>}<[[()[]]((){})]<({}{})[[][]]>>])[[[{(()())({}())}(
    {{<([([[{<<[<[[][]){<><>}>]>[<[[{}<>]<()[]>]{{<><>}[{}<>]}>[[<{}[]>{[]{}}](<<>{}>)]]><<{{({}<>)}}
    {{<[<<<[<(([((()[]){<>[]})]{((()[]))<[[][]]([][])>})[({(<>)[()()]}([()<>]{()[]}))])((<[{<>[]}]>))>]>((((
    [<{(((<[{(<[<<<><>><{}()>>]><{{(()[])(()[])}}{[[<>()]<<>[]>]{{<>()}[()[]]]}>)((<[<[]{}><{}[]>]{{[]()}{
    {[<([{(<{([<[{{}[]}<[]()>][<()<>>{<>()}]>(([{}()]([][]))({[]{}}([]())>)])((({[[]{}]<{}[]>})<[<<>()>{[]{}}][{(
    (((((((<[<[[[(<>[]){<>}][[{}()][(){}]]](<[{}<>]{(){}}>[<<>{}><(){}>])]>]{{({{[{}<>][<><>]}}(
    <<(<<<({<[{{[(()<>)[()<>]]}}{<[[<><>][{}]](<<>[]>{{}<>})}<<<{}[]>[{}]>{<[]<>><<>{}>}>}]{<<<<<>[]>[()()]>[
    {{[<[{{[<<{[[<<>{}>[{}()]]][(<<><>>{{}<>}){([]<>)[()<>]}]}>{{[(([]()){<><>})<<[]()>{()[]}>]}{([({}[])[{}()]]
    ({([{{{(((<[(({})[[]{}])(<{}[]>{{}[]})][<{()[]}(<>[])>[[{}[]]<{}[]>]]>([{{[]<>}{<>{}}}<<{}[
    <{<{{{((<<[<<[<>[]]>{[<>]<[][]>}>[<[{}{}][()()]><([]{})((){})>]]>><{{[{<{}{}>[()<>]}(((){})[<>{}])]
    {[((<<<[{<[({{[]()}([])}({{}<>}((){})))<<<[]<>>[()<>]>>]>}]({<<{<[[]<>](<><>)>{{[]<>}[()<>]}}>><{{<<{
    [({[[<(([[[(<([][])<<>{}>>((()())[[]<>]))]<{{<[]()>}[{<>}{{}[]}]}>]])[<<[<(<[]>)[[[]<>]<<>[]>]>{({[]()}<[]>)<
    [{[{({[{({[{{({}[]){()<>)}<[<>{}]{{}()}>}[{[()()][{}{}]}[[<>{}]<[]()>]]]{<{<[]<>>[(){}]}((<>{})[
    <{<[[<<{([{[<[()[]]<<><>>>({()()}(<>()))]{<({}<>)[()<>]><[{}{}]{[][]}>}}{<<{<>()}[[]<>]>{<()[]>[()
    {({<[[(([(<{[<<>[]>]}>[{{{{}()}{{}[]}}({<>{}}([]<>))}{{[[]{}][[][]]}}]){<<[[()()][(){}]][<{}[]>[()[]]]]{[[<
    [{<({([<[{{[({()})<({}<>)<{}()>>]}}{[[[{()[]}<<>[]>](<[]()>{[]<>}}][[(<>{}){<>{}}]{{[][]}[
    [<{([(([([(([<{}<>>]([[]<>]<[]>))(({<><>}<{}[]>){[<><>][<>{}]}))])][<<{[<[(){}](<>[]]>]}[[{[[]()]}(([]
    <({({{{({<{[[{[]()}[()[]]]](([{}{}](<>())){{<>()}{{}[]}})}[(<[{}{}]<<>{}>>({[][]}))<<{{}[]}(()<>)>>
    (<[<<[{({[<<[({}[])[()[]]]<[()[]]<<>{}>>>>][[((<<>[]><()>)(<[]()><<><>>))]]}([[{<<<><>>(())>{{()()}}}{
    {<(({[{([((<[[()][<>]]>[<(<>[]){()<>}>{[<>{}]{<>{}}}]){<([(){}][<>()])<({}<>)[[]{}]>>{(<[]<>><{
    [[({([<<[{<({({}<>)({}())}){{{<>{}}(<><>)}<{{}{}}(()<>)>}}<<<[(){}]<<>>>((<>[])[[]<>])><<{[]<>}[()()]>{{(){}
    {({<([<([(({[({}())[[]()]]<[<>]{{}()}>}{[[{}<>]{<>[]}]<{{}[]}{{}{}}>}))({<([()[]>[[]]){<<>(
    [[{[[{{[{{<(<<[]<>><<><>>>[{[]}({})])<(([]()){()[]})>><[[<(){}>(<>())]][{{<>()}[[]()]}{[<>()]}]>}<{{(<<>{}>
    [{<[{(<({{{<{<(){}>(()<>)}>({[[]()][{}[]]}[[{}[]][()<>]])}{<<[(){}]{{}{}}>[[()<>]<<>()>]><<<[][]]([]<>)
    <<<({<{[<{<[<(<>{}){{}()}><[<>{}]>]>[(<{[]{}}({}())>)[(([]()){()()})<<(){}>{<>[]}>]]}[{({{[]{}
    {[((({[<{(({[([]<>)(<>)](<[]{}><(){}>)>[<{{}<>}([]())><((){}){()[]}>])({((()())(<><>))({()[]}{{}<>})}{<{
    <([(({(<{<<[[<<>{}><[][]>]<[[]<>](<><>)>]{{((){}){<>{}}}[<{}()>([])]}>>}{{<<<(<><>){{}<>}>{{{}[]}}>[{
    {{<{(((({<{{[({}{})][<<>()>[()[]]]}}{[{<[]()>(<>{})}[[{}<>](()<>)]]<[[()[]]]([[]()]<<><>>)>}]})<(<<([<[]{
    (([((<[{(({[[[<>[]]{[]{}}](([]<>))]{{<[]{}][<>[]]}{<{}{}>({}())}}}<(({[]()}{[]{}}){(<>[])}){[<(){}
    {<(({{([{{{<((<><>)({}()))>([<<>()>({}())]{{<>[]}[<>{}]})>}}(({[<<{}<>><[]<>>>[<<>{}>{<>[]}]]({(()<>){<>[]
    <<<{{(((({[((<{}[]>[()<>])((<>[]))){[{[]{}}]{((){})([]())}}]}){([<<[{}()]<[]()>>[[<><>][{}
    [[{({[({[<<{[<()()><<>{}>]{[{}{}][[]()]}}><[{{<>[]}<()[]>}]>>]}<(({{{[<>()]<[][]>}[{[]<>}[[]{
    <{({({{{{[{(([[][]](<>()})<[<>{}]{<>()}>)<(({}())(()<>))>}]}[((<(<{}{}>)({<>{}}{<>()})>{(([][]))<(()[
    ((<(([[<{{<[{<(){}>}{<()()><<><>)}][{{<>}}]>{([(()[])(<>[])]{({}{})(()<>)})<[({}()){{}[]}]>}}({
    <{((([{([(<([[<>[]]([])](([]())[()()])){{{[]()}[<>()]}}>)<{{<<<>{}><()()>>)}(({({}[])<()()>}
    <({[[({[[({<[<[][]>]<[[]<>]([]{})>>{[{{}{}}((){})]}}<[<<[]{}><{}()>>][<({}[]]{[]{}}>{(<>){[]
    <<[<{{[<[<[[[{{}[]}[[]()]]]<({{}<>}(()<>))<[()[]]<[][]>>>](([[<>{}]([]())][<()<>>[{}{}]])<<<[]{}>{{}{}}>>)
    {([{{<([([<[{({}())(<><>)}[({}}<<>()>]]{<<<><>><[][]>>{<()><{}<>>}}>(<{[(){}]<<><>>}[([][])([]())]>(<<(
    (([[[({{{{{[<<{}()>><<{}()>>]{<{(){}}{<><>}>[([]<>)<()[]>]}}{(<{[]{}}[[]<>]>)[([[]<>][[]<>])([()
    {[<<[{[[({[{{{[]}}<[{}[]]({}())>}([((){}){()<>}]{<<>{}>{<><>}})]{[<{()[]}{()<>}]{(<>[])<{}
    <({<[([[({<<{{{}{}}([]{})}[[{}]{<>}]>{{({}[])<()<>>}<[<>{}][[]{}]>}>[<({{}<>})<<{}[]>({}())>>]})]]<<[<[{{[()
    [(<{<{(({[{{<{()()}<[]{}>>{([]())[{}[]]}}{<({}{}){[][]}>{(<><>)[{}{}]}}}[([({}<>)<{}{}>]<[
    <[{(<{<{({[{((<>[]){[]})({{}<>})}]}[{[<{<>[]}<{}()>><[[]<>][[]<>]>]<[<[]()>([]<>)}(([][])(<><
    [{[({([<<[[<<<{}<>><[]{}>>]][<{<<>()>((){})}<[{}()]<<>{}>>>{(<(){}>({}()))(<()()>{()})}]]>[({[([[]{}]{()[]})
    {[({<[(<{(<[{<()()><()()>}(<{}()>{<>[]})]>([(<{}{}>(()<>)){[<>[]][{}()]})[{(()<>)({}[])}<(<>{})(<>(
    <[(({{([[(((<([]{}){()<>}>{[()<>]{[]<>}}){[<[]()><[]>][({}[])[<>()]]})[(<(<><>)(()[])>{{[]()}{[]{}}})[{<<>
    [({<({({(<{{{<(){}>[<><>]}(<[]{}>(()[]))}[[{{}[]}[[]()]]<[{}[]][()[]]>]}[<(<()[]>{{}})[[[]<>]]>
    <<[(<{<((<[<<<(){}><(){}>>({{}<>}([]()))><{{(){}}{()}}<<<>()><{}()>>>](<<(()())[[]]>(<[]()>([]<>))><(<
    {(({([[<{[{<(([][]))>{(((){})[[]<>])[(<>{}){{}[]}]}>[<({[]{}})[(()()){<>()}]>]][{[(((){})<()<>>){<<><>>}]<<<[
    ({[{{<[(<[({<<[]<>>{[]}>(<[]()){()()})}){{{{()()}(<>())}<[()<>](()())>}(({[]<>}([]<>)){[{}[]]})}][{{{[[]<>]{
    (<([[{([{<{{<{()<>}[()()]>[([]())[()<>]]}}<([{(){}}{<>{}}]((<><>))){{{(){}}{{}<>}}}>>}({[(<{[]{}}[()<>]>{[[
    {{(({[{[{[[{[{<>{}}]<[()[]]<{}<>>>}{<(()[])([]{})>({{}{}}<(){}>)}]{<(<<>()>(()[])){<{}()><
    ({<((<<<{{({[[<>]<[][]>]((()[])[<>()])}<([<><>]<<>()>)(([][]))>){(<<{}[]>[(){}]><([])({}()}>){{<[]<>>
    {[<[([(<<(<{[[[]{}]<[][]>][{<>}(()<>)]}<[{()[]}{[]<>}]>>[{[({}[])([]{})]((<>){<><>})}((([]<>}[<>[]]))])({(
    ([{[[[{(((((<(()[])<()<>>>{<[]()>[{}<>]})(({()()}({}<>))<[[][]]<<>{}>>))<<({<>()}([]()))<<[]<>>[<>[]]>>[<{(
    [{{<({[<<{[<<(()())[[]]>{{[]<>}[[]<>]}>{[<<><>}]}](<{[<>[]][{}[]]}<{[]{}}[[]{}]>>)}><<({{(()())([
    ({{{[(([<(((<{<>}(<>())>)[((<>)<[]{}>)[([]())[()[]]]})<(((<>[])[{}]){{[][]}[()()]})<[(<>{})({}())
    <[({[<[[{((({[[]<>][<>{}]})(<[{}[]]{[][]}><{()()}{<>{}}>)){(<({}())[<>()]><{[]()}{{}()}>)})}](
    <{(([<[{<<<{<<[]{}>{<>()}>}({<<>[]><[][]>}(<()<>>))>>{([(([]{}){[]{}})((()[])[[][]])][{<()>}<[(){
    {(<[({{(<{{<[({}{})<<>()>](([]<>)[{}])>[([{}{}]<()<>>)[[{}<>](()[])]]}[(({[]<>}){{{}[]}[[]{}]}){[<{}<>>([]<>
    {(<[{<[<<<{([<[]()>[<>[]]]{{<>{}}([]())})}<[[({}[]>([]())]]<([<>{}]{[]<>})>>>>[[<<<{<>{}}<()()>>([{}])>{<((
    [{<{(([[[{<[(((){})<{}[]>)<{<>{}}>]<<<()<>>{[]{}}><<{}()>>>>}]]<<<[[(<()[]>{[]{}})][<{(){}}[<>{}]><<{}>
""".trimIndent().lines()
