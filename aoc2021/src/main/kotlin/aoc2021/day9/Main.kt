package aoc2021.day9

import aoc2021.Coord
import aoc2021.Grid2d
import java.util.*

fun Grid2d<Int>.lowPoints() = getCoords().filter { c ->
    val ns = this.cardinalNeighborsWithinLimits(c)
    ns.all { n -> this.getValue(n) > this.getValue(c) }
}

fun findBasin(lowPoint: Coord, grid: Grid2d<Int>): Set<Coord> {
    val result = mutableSetOf<Coord>()

    val visited = mutableSetOf<Coord>()
    val queue = LinkedList<Coord>()

    visited.add(lowPoint)
    queue.add(lowPoint)

    while (queue.size != 0) {
        val c = queue.poll()
        result.add(c)

        grid.cardinalNeighborsWithinLimits(c).forEach { n ->
            val nVal = grid.getValue(n)
            if (!visited.contains(n) && nVal != 9 && nVal > grid.getValue(c)) {
                visited.add(n)
                queue.add(n)
            }
        }
    }

    return result
}

fun main() {
    actualData.let { data ->
        val grid = Grid2d<Int>()
        data.forEachIndexed { y, line ->
            line.forEachIndexed { x, char -> grid[Coord(x, y)] = char.toString().toInt() }
        }
        println("Risk: ${grid.lowPoints().sumOf { grid.getValue(it) + 1 }}") // 468
    }

    actualData.let { data ->
        val grid = Grid2d<Int>()
        data.forEachIndexed { y, line ->
            line.forEachIndexed { x, char -> grid[Coord(x, y)] = char.toString().toInt() }
        }

        val basins = grid.lowPoints().map { p -> findBasin(p, grid) }
        println(basins.map { it.size }.sortedDescending().take(3).fold(1) { a, s -> a * s }) // 1280496
    }
}

val testData = """
    2199943210
    3987894921
    9856789892
    8767896789
    9899965678
""".trimIndent().lines()


val actualData = """
    9821016789345689876545245989999932987654349769898765104567898765634567899765432123788999891045698701
    7632145894234599987432134567898799999979498956789854323458999854323456789898321094567988789234987612
    6543234789345987654321015778987678987898987545678987634569899843212349899987432989679875678949896543
    7654345678956798765692126789854567896587898621567898545698789765103478999876549878998754599998789654
    8765496789987899976989238898763459965456999533478999768789678973212567899989698767898765989987678967
    9876787898798989899879345997652378954345987644567899878893467895323878989998987658999879878876567998
    6987898987689876789768959876543789993234599785698999989912457895456989878987898767899998767765456899
    5798999996579765878957892987984578989356679876789998692101567896569999765496789878989789856312345788
    4569899987498654567898901499876789678967889987896987543213456998778998954345678989775698543201234567
    3659789986329943458999212345987894569898995498945998764394967899989987893236789998664987656312345678
    2545678965439892347895425457998913456789654329434898765989898901298546789959899899543498765453456789
    1234568896798789456789434569879324599998993210125679899876789312987634567892998798992379879567867899
    0123456797987689967897545698765434987876789321234567989865695459896523498921987657989467988789878978
    1294968999996579898998656789876549976745898934545679876974489598765434789439876545878989499999989767
    2989899998765458789998767897997698865434567896898789995432378999876555678998765434567894345678997656
    9878789987654323678999898956798987654323456987899893496321267899989666789019896325679921234899876545
    7654678998765412567899999543459876543212369898934912985432456789999879894323998437789210345799985632
    6543568999654323456999897632498765432101458799329899876543569899985997995439896558994351456989994321
    9854567898798654567896789321239878543212345679498798997854679999874356789998797667895942349878989990
    8767698999899766878925459210123987654323457789989657998965989398765234567895689978999893598769878789
    9878989896999987989412368991234598765454769898878945899876793219876347698924567899998789699754965679
    7989876765788998994323456789947699876767899987653434678987894101987456999213345978987678987643434678
    6799995454567899987434569896899789989878978998762123789098999919598567895101234569876549876542123689
    5678989323456799876565678935678993497989767895421012392129998798439878963214395678987634984321034599
    4895678910167976987676789124569012976593656789532323679245987656522989954323689989876545695432234569
    3434799321279875498787891013689929876432545678993434568956798743210197895434567899987656987954345778
    2223789534569989329898999124567898954321434567989546979767898654321356789545679989998967899876456989
    1012578945678998910949678935678987895910125789879999899898998765432459997676989878999878956987867891
    2123459656989987921234568945989876789891234598767889789999649899543567898787894567898989347898978932
    3436578987899976899499689656798765676799345987655678678998767988999878939898923456957893236799989893
    4547678998999894988988998767998654545678957898743244569759889876789989923969212367898994345689998789
    5789789349987673976567899879876543234569768997542123458945998765678998799654323459999987658789989656
    6998993299876542987678943989995432123456979876543764567896987654589987678976434598989998767898978942
    9887894986987431099789992199986543434567895998654876678929876543498943569996565987878999878987867891
    8656789875698549129897889249897696545678934569765987989210987654567892456789699876569689989876545990
    7645878954987678998965678956798987858789124579876898999391998765678921239999987943489597898787434689
    5434567893298899987654667897899298767891035899989929998989899877889210198789876542123456789632123599
    2123678942109959876543456989999019888989186789891012987678789988994323987678998753244897896521012678
    1034589543212345987632345678978999999679997898752129876567679999995439876567899894556789965432123789
    2137897654323459876321234789567989656567898999643498765434567999987656985498956965678890986563234599
    3246789765434598765430125897679878643456789998756569876323456789598767976989999876899921987784346789
    4557999876545679898641236998789966532345899899867891987212345895459899899876989987999932398765659897
    5768954987786789989752347939899854321236998789878990195323467932378998789765678998989893459876789956
    6879543098899899879765458949998767432347897678989989996934589549999987678954367899876789598987892345
    7889632129978999769878569998769876543456976456799879889895697698789996589895256989865679987798931234
    8996543234567898754989678999654987656567965345898767679789899789678985456789345679654598765679892349
    9987656745678987643298789998743298777678954234987654545699967998799876789895458989765987654566789598
    9898787898789999732109898999856899988989432129876543236789656899893997897987667899879996532345699987
    8789898989898998743412967899987967799996573236987664127894346789965698976498788921989987621234789876
    7678949678987987658583456999998956667899794345698543234993237899876989899329899990199998710123898765
    7543234567896798867678967898999345556798989456987656786789379912999876788934998789239879823939999654
    7654949698965679878789989987893212445987678998998767897895467902987665677945987698998765439898998743
    8969898989754567989896899876789901239879567899999878998996598929876554566899876567899876598767897632
    9997767879543456798945698765899892398768456789898989879987679939875432345789985458912998987656789541
    9986556568912345987656987654356789987654345698767694767998789898954321234679876345793989876545997679
    8765432457893959898979876543245678998743256789654543459899899767899434348789965457989878976434569898
    7654321348999898769899985432123899987654569896543232498789987856798995679892396769876767994323498987
    9874210156789787656789876543012790198767698989692101987679876543456789989921987898765456789434987876
    1984321347896576545699987654125691239988987678989919976567985532345999897899598999876345699949876545
    2995432348954321034989998876234789545699876563479898765459874321248789766998439998765236789899975434
    9876543456796542129878969984345897676798987432365679876398765210123678955987645989854101896789964323
    6987678567897656298767456798767898987987654321234795988987654321234569543499659876543212345899873210
    5498987678998967987658345679878999898999865672347894399598765432345678932398767997654345456998754321
    6399798789899879876543238899989998789999876989456789212459976576556789543459898998785456767898765452
    7987659896789989998630127678999989679899987896567893101969897677679897659569989129886578898969876599
    9876545965678999987621234569999876567789998999879964239898798988989998998998678998997889949658987987
    9987631274798999876542355679987654345678939989998999398787689899196569896987589897698995432347899876
    9999820123457899876543498789898543234599649878987678987654578789023498785876456797549876545756998765
    8987634799598910987656569898765455125989998769876587898743434679234987654212356789432987659867987654
    7898545678919999998767898999874321039878898654322456987652123678949799765101347894321098792998996543
    6999657889209878999898987598765432398767789863210345698543234589998652976432456985432129891239987432
    5878968994398767892999763459876543987654598754321239789654345678999430987543479876549298910129876521
    4659878965943658991987642398989656798543459865434678998766556789987521987674569989998987621234987632
    3234989879892345789986543987898767929432123998765989659877677894987432398989678999887996545345698783
    2123699998789496892397959876789878912949234899876796546989788943596543459998789898656989656957989894
    1014568987678989989998999765678989109898946798987895432399899912987896578959895697645678969898979965
    2123456798545678979899987854569993298767958897699976541034987893498997989543934985434567899789569876
    3654567899434989656789876543458954987658969989549865432123456794579989895432129876323458987693456987
    4765878998745699967895998652367969876549878978934976543234569896989877789541034965434569876532345698
    5878999987658999878944349763456893989689989569019987894345698999998765678952199876545679986321236789
    7989398798867989989431239854567892199789893459198898765467987898987654567893988998756798765445789899
    9895497659978978998532345967678943239897654598976789876879886767897653678999877899767899876556895978
    8789989431299567897644459878789765456998765987865678987998765458975542768998756789898954987667954567
    7655978990123456789765567989899876769999979896754569899019876769654321658789546678969993298778943458
    6544567789236767899899698998910987878989998765443475698998989989643210145678934589654989109899432346
    5432345678945988912998999987891298989878987654321254567897694498754321234789123498799878912954321234
    4321234989856789923987899876789349998969899863210123458976543239865632355678934599988767899765432345
    5430129899767899899876798765478956997658789654431734567899854129876548766789545989877656778998545458
    4321239788978998798995459854357899876545678966545675678998765934987656878999769878965437567987657867
    5432998657899987656789345965456798765434567899656789789219999896798787989659898767894323456898768978
    9659876545699986547893239879767989854323456998767890998909878789899898997545999656789214356789899989
    8769985434589997656792123989979876543412397899878921457898767679989949876434398769898901234589998991
    9898767124567998767891012995989989872101289998989762345987654568975439877321239898987892345678987890
    9949656012369899889952129894398799965432378987899843569898743678954321965452345987676789657989996789
    9239743223456789996543298765219679878554567896987654698769832367895932987565459976585778967898965678
    8949854337567894987964987674101567987669878965498765987654321456789793498976798765434567898987874567
    7698765445878943299899876543212346798778989874349876799776532368995689569987998654323456999876553656
    8549977656989654998754987894334587899899498763256997899897543479434568978999876543213797898765432347
    7632988767899879879543498985455679945954349854367898965999654567925678989456988654435678999897321055
    6543499878921998765432459876576789656891298768478999654298776778936899994345699766576789998765432123
""".trimIndent().lines()
