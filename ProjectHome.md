# Welcome! #

Cellomは、Java向けの集計表作成ライブラリです。

作成した集計表はPOIを使ってExcelとして出力したり、HTMLのテーブルとして出力したりできます。

# 使い方 #

ここでは、下記のデータを元に･･･


  * 株式会社 日本○○○○の社員構成
| **年齢**| **性別**| **拠点**| **職種**|
|:------|:------|:------|:------|
|21     |男性     |名古屋本社  |エンジニア  |
|23     |女性     |名古屋本社  |エンジニア  |
|25     |女性     |名古屋本社  |エンジニア  |
|30     |男性     |名古屋本社  |エンジニア  |
|35     |男性     |名古屋本社  |エンジニア  |
|40     |男性     |名古屋本社  |エンジニア  |
|52     |男性     |名古屋本社  |エンジニア  |
|55     |男性     |名古屋本社  |エンジニア  |
|34     |女性     |名古屋本社  |事務     |
|51     |男性     |名古屋本社  |営業     |
|22     |男性     |東京支社   |エンジニア  |
|24     |男性     |東京支社   |エンジニア  |
|25     |男性     |東京支社   |エンジニア  |
|28     |男性     |東京支社   |エンジニア  |
|31     |男性     |東京支社   |エンジニア  |
|36     |女性     |東京支社   |エンジニア  |
|37     |男性     |東京支社   |エンジニア  |
|42     |男性     |東京支社   |エンジニア  |
|24     |女性     |東京支社   |事務     |
|28     |男性     |東京支社   |事務     |
|30     |男性     |東京支社   |営業     |
|25     |男性     |大阪支社   |エンジニア  |
|30     |男性     |大阪支社   |エンジニア  |
|37     |男性     |大阪支社   |エンジニア  |
|21     |女性     |大阪支社   |事務     |
|30     |女性     |大阪支社   |営業     |

<br><br>
下記集計表を作成する処理を通して、Cellomの基本的な使い方を学びます。<br>
<ul><li>株式会社 日本○○○○の社員構成<br>
<table border='1'>
</li></ul><blockquote><thead>
<blockquote><tr>
<blockquote><th>世代</th>
<th>名古屋本社</th>
<th>東京支社</th>
<th>大阪支社</th>
<th>合計</th>
</blockquote></tr>
<tr>
<blockquote><th>エンジニア</th>
<th>事務</th>
<th>営業</th>
<th>小計</th>
<th>エンジニア</th>
<th>事務</th>
<th>営業</th>
<th>小計</th>
<th>エンジニア</th>
<th>事務</th>
<th>営業</th>
<th>小計</th>
</blockquote></tr>
</blockquote></thead>
<tbody>
<blockquote><tr>
<blockquote><td>20代</td>
<td>3</td>
<td>0</td>
<td>0</td>
<td>3</td>
<td>4</td>
<td>2</td>
<td>0</td>
<td>6</td>
<td>1</td>
<td>1</td>
<td>0</td>
<td>2</td>
<td>11</td>
</blockquote></tr>
<tr>
<blockquote><td>30代</td>
<td>2</td>
<td>1</td>
<td>0</td>
<td>3</td>
<td>3</td>
<td>0</td>
<td>1</td>
<td>4</td>
<td>2</td>
<td>0</td>
<td>1</td>
<td>3</td>
<td>10</td>
</blockquote></tr>
<tr>
<blockquote><td>40代</td>
<td>1</td>
<td>0</td>
<td>0</td>
<td>1</td>
<td>1</td>
<td>0</td>
<td>0</td>
<td>1</td>
<td>0</td>
<td>0</td>
<td>0</td>
<td>0</td>
<td>2</td>
</blockquote></tr>
<tr>
<blockquote><td>50代</td>
<td>2</td>
<td>0</td>
<td>1</td>
<td>3</td>
<td>0</td>
<td>0</td>
<td>0</td>
<td>0</td>
<td>0</td>
<td>0</td>
<td>0</td>
<td>0</td>
<td>3</td>
</blockquote></tr>
<tr>
<blockquote><td>総計</td>
<td>8</td>
<td>1</td>
<td>1</td>
<td>10</td>
<td>8</td>
<td>2</td>
<td>1</td>
<td>11</td>
<td>3</td>
<td>1</td>
<td>1</td>
<td>5</td>
<td>26</td>
</blockquote></tr>
</blockquote></tbody>
</table></blockquote>

まずはテストデータの準備を行います。<br>
<br>
※ 実際にはRDBMSやCSVファイルなどから読み込む処理になる箇所ですが、ここではサンプルとして下記のようにJavaで実装しています。<br>
<br>
<pre><code>enum Sex {<br>
	MALE, FEMALE, SUMMARY, ALLSUMMARY<br>
}<br>
<br>
class Employee {<br>
	final int age;<br>
	final Sex sex;<br>
	final String section;<br>
	final String title;<br>
<br>
	Employee(int age, Sex sex, String section, String title) {<br>
		this.age = age;<br>
		this.sex = sex;<br>
		this.section = section;<br>
		this.title = title;<br>
	}<br>
}<br>
<br>
public class SampleTest {<br>
<br>
	List&lt;Employee&gt; employees;<br>
<br>
	@Before<br>
	public void setUp() {<br>
		employees = new ArrayList&lt;Employee&gt;();<br>
<br>
		// 名古屋本社<br>
		employees.add(new Employee(21, Sex.MALE, "名古屋本社", "エンジニア"));<br>
		employees.add(new Employee(23, Sex.FEMALE, "名古屋本社", "エンジニア"));<br>
		employees.add(new Employee(25, Sex.FEMALE, "名古屋本社", "エンジニア"));<br>
		employees.add(new Employee(30, Sex.MALE, "名古屋本社", "エンジニア"));<br>
		employees.add(new Employee(35, Sex.MALE, "名古屋本社", "エンジニア"));<br>
		employees.add(new Employee(40, Sex.MALE, "名古屋本社", "エンジニア"));<br>
		employees.add(new Employee(52, Sex.MALE, "名古屋本社", "エンジニア"));<br>
		employees.add(new Employee(55, Sex.MALE, "名古屋本社", "エンジニア"));<br>
		employees.add(new Employee(34, Sex.FEMALE, "名古屋本社", "事務"));<br>
		employees.add(new Employee(51, Sex.MALE, "名古屋本社", "営業"));<br>
<br>
		// 東京支社<br>
		employees.add(new Employee(22, Sex.MALE, "東京支社", "エンジニア"));<br>
		employees.add(new Employee(24, Sex.MALE, "東京支社", "エンジニア"));<br>
		employees.add(new Employee(25, Sex.MALE, "東京支社", "エンジニア"));<br>
		employees.add(new Employee(28, Sex.MALE, "東京支社", "エンジニア"));<br>
		employees.add(new Employee(31, Sex.MALE, "東京支社", "エンジニア"));<br>
		employees.add(new Employee(36, Sex.FEMALE, "東京支社", "エンジニア"));<br>
		employees.add(new Employee(37, Sex.MALE, "東京支社", "エンジニア"));<br>
		employees.add(new Employee(42, Sex.MALE, "東京支社", "エンジニア"));<br>
		employees.add(new Employee(24, Sex.FEMALE, "東京支社", "事務"));<br>
		employees.add(new Employee(28, Sex.MALE, "東京支社", "事務"));<br>
		employees.add(new Employee(30, Sex.MALE, "東京支社", "営業"));<br>
<br>
		// 大阪支社<br>
		employees.add(new Employee(25, Sex.MALE, "大阪支社", "エンジニア"));<br>
		employees.add(new Employee(30, Sex.MALE, "大阪支社", "エンジニア"));<br>
		employees.add(new Employee(37, Sex.MALE, "大阪支社", "エンジニア"));<br>
		employees.add(new Employee(21, Sex.FEMALE, "大阪支社", "事務"));<br>
		employees.add(new Employee(30, Sex.FEMALE, "大阪支社", "営業"));<br>
	}<br>
<br>
<br>
                           :<br>
                           :<br>
                           :<br>
                           :<br>
}<br>
<br>
</code></pre>

このデータを元に集計表を作成します。<br>
<br>
<h2>集計表の構造を表現するクラスの作成</h2>

上記集計表の構造は、下記のようになっています。<br>
<pre><code>・世代<br>
・名古屋本社<br>
	・エンジニア<br>
	・事務<br>
	・営業<br>
	・小計<br>
・東京支社<br>
	・エンジニア<br>
	・事務<br>
	・営業<br>
	・小計<br>
・大阪支社<br>
	・エンジニア<br>
	・事務<br>
	・営業<br>
	・小計<br>
・合計<br>
</code></pre>

つまり、「世代」「合計」に挟まれる形で、「エンジニア」「事務」「営業」「小計」という子要素を持つ、同じ構造のものが繰り返し登場しています。まずはこれをJavaのクラスとして下記のように表現します。<br>
<br>
<pre><code>class Table {<br>
	@Header(title = "世代")<br>
	String generation;<br>
<br>
	@EachHeader<br>
	CelloMap&lt;Section&gt; sectionMap = CelloMap.create(Section.class);<br>
<br>
	@Header(title = "合計")<br>
	int sum;<br>
}<br>
<br>
class Section {<br>
	@EachHeader<br>
	CelloMap&lt;Integer&gt; titleMap = CelloMap.create(Integer.class);<br>
<br>
	@Header(title = "小計")<br>
	int sum;<br>
}<br>
</code></pre>

「同じ構造のものが複数回登場する」というのを、ここでは「CelloMap」というCellomのクラスを用いて表現しています。このクラスはCelloMap.createメソッドに構造を表現するクラスのクラスオブジェクトをパラメタとして渡して生成します。CelloMapは集計表で同じ構造のものが何回登場するのか事前に分からないときなどに便利です。例えば集計表に表示しなければならない支社の数が不明な場合などに使用します。<br>
<br>
今回のケースを、もしCelloMapを使用しないでクラスで表現すると、下記のようになります。<br>
<pre><code>class Table {<br>
	@Header(title = "世代")<br>
	String generation;<br>
<br>
	@Header(title = "名古屋本社")<br>
	Section nagoya;<br>
<br>
	@Header(title = "東京支社")<br>
	Section tokyo;<br>
<br>
	@Header(title = "大阪支社")<br>
	Section osaka;<br>
<br>
	@Header(title = "合計")<br>
	int sum;<br>
}<br>
<br>
class Section {<br>
	@Header(title = "エンジニア")<br>
	int engineer;<br>
<br>
	@Header(title = "事務")<br>
	int jimu;<br>
<br>
	@Header(title = "営業")<br>
	int eigyo;<br>
<br>
	@Header(title = "小計")<br>
	int sum;<br>
}<br>
</code></pre>

事前に集計表に表示される内容が全て判明している場合には、この書き方でも良いでしょう。<br>
<br>
なお、各フィールドにはアノテーションが付与されています。「@Header」は通常のフィールドにヘッダ行の定義を行うアノテーションで、ここではヘッダ行に表示される文字列を定義しています。「@EachHeader」はCelloMapに対するヘッダ行の定義を行うアノテーションですが、ここでは単に集計表に表示される対象であることを明示するために使用しています。<br>
<br>
<h2>集計表作成 ～ 通常編 ～</h2>

集計表は、下記のように作成します。<br>
<br>
<pre><code>	@Test<br>
	public void normal() {<br>
<br>
		TableManager&lt;Table&gt; tm = TableManager.create(Table.class);<br>
		rowSetting(tm, 20, 30, 40, 50);<br>
<br>
		for (Employee e : employees) {<br>
<br>
			// 社員の世代取得<br>
			String generation = getGeneration(e.age);<br>
<br>
			// 世代別行への反映<br>
			updateEmployee(tm.row(generation).get(), e);<br>
<br>
			// 総計行への反映<br>
			updateEmployee(tm.footer().get(), e);<br>
		}<br>
<br>
		assertEqualsWithFile(new HtmlGenerator().generate(tm), getClass(),<br>
				"normal");<br>
	}<br>
<br>
	// 行初期設定<br>
	private void rowSetting(TableManager&lt;Table&gt; tm, int... ages) {<br>
<br>
		// 各世代行の生成<br>
		for (int age : ages) {<br>
			String generation = getGeneration(age);<br>
			tm.row(generation).get().generation = generation;<br>
		}<br>
<br>
		// 総計行の生成<br>
		tm.footer("総計").get().generation = "総計";<br>
	}<br>
<br>
	// 年齢から世代を求める<br>
	private String getGeneration(int age) {<br>
		if (age &lt; 30)<br>
			return "20代";<br>
		if (age &lt; 40)<br>
			return "30代";<br>
		if (age &lt; 50)<br>
			return "40代";<br>
		return "50代";<br>
	}<br>
<br>
	// 社員情報のTableオブジェクトへの反映<br>
	private void updateEmployee(Table table, Employee e) {<br>
		Section section = table.sectionMap.get(e.section);<br>
<br>
		// 各職種ごとに+1<br>
		section.titleMap.set(e.title, section.titleMap.get(e.title) + 1);<br>
<br>
		// 小計に+1<br>
		section.sum++;<br>
<br>
		// 合計に+1<br>
		table.sum++;<br>
	}<br>
</code></pre>

先ほど作成したクラスのクラスオブジェクトをパラメタとして、「TableManager」というクラスのインスタンスを生成します。こちらを使うと、指定された行の取得・生成などを自動的に行うことができます。<br>
<br>
上記ソースは、社員のデータ一人分につき所属する世代の行と総計の行を取得し、それぞれに対し所属する支社(or 本社)・職種のセルと、その小計、合計に対して +1を行う、という処理を行っています。TableManagerのインスタンスを「HtmlGenerator」というクラスを使用すると、下記のように集計表のHTMLソースのコードを出力することができます。<br>
<br>
<pre><code>&lt;table border="1"&gt;<br>
	&lt;thead&gt;<br>
		&lt;tr&gt;<br>
			&lt;th rowspan="2" style="HEADER"&gt;世代&lt;/th&gt;<br>
			&lt;th colspan="4" style="HEADER"&gt;名古屋本社&lt;/th&gt;<br>
			&lt;th colspan="4" style="HEADER"&gt;東京支社&lt;/th&gt;<br>
			&lt;th colspan="4" style="HEADER"&gt;大阪支社&lt;/th&gt;<br>
			&lt;th rowspan="2" style="HEADER"&gt;合計&lt;/th&gt;<br>
		&lt;/tr&gt;<br>
		&lt;tr&gt;<br>
			&lt;th style="HEADER"&gt;エンジニア&lt;/th&gt;<br>
			&lt;th style="HEADER"&gt;事務&lt;/th&gt;<br>
			&lt;th style="HEADER"&gt;営業&lt;/th&gt;<br>
			&lt;th style="HEADER"&gt;小計&lt;/th&gt;<br>
			&lt;th style="HEADER"&gt;エンジニア&lt;/th&gt;<br>
			&lt;th style="HEADER"&gt;事務&lt;/th&gt;<br>
			&lt;th style="HEADER"&gt;営業&lt;/th&gt;<br>
			&lt;th style="HEADER"&gt;小計&lt;/th&gt;<br>
			&lt;th style="HEADER"&gt;エンジニア&lt;/th&gt;<br>
			&lt;th style="HEADER"&gt;事務&lt;/th&gt;<br>
			&lt;th style="HEADER"&gt;営業&lt;/th&gt;<br>
			&lt;th style="HEADER"&gt;小計&lt;/th&gt;<br>
		&lt;/tr&gt;<br>
	&lt;/thead&gt;<br>
	&lt;tbody&gt;<br>
		&lt;tr&gt;<br>
			&lt;td style="BODY"&gt;20代&lt;/td&gt;<br>
			&lt;td style="BODY"&gt;3&lt;/td&gt;<br>
			&lt;td style="BODY"&gt;0&lt;/td&gt;<br>
			&lt;td style="BODY"&gt;0&lt;/td&gt;<br>
			&lt;td style="BODY"&gt;3&lt;/td&gt;<br>
			&lt;td style="BODY"&gt;4&lt;/td&gt;<br>
			&lt;td style="BODY"&gt;2&lt;/td&gt;<br>
			&lt;td style="BODY"&gt;0&lt;/td&gt;<br>
			&lt;td style="BODY"&gt;6&lt;/td&gt;<br>
			&lt;td style="BODY"&gt;1&lt;/td&gt;<br>
			&lt;td style="BODY"&gt;1&lt;/td&gt;<br>
			&lt;td style="BODY"&gt;0&lt;/td&gt;<br>
			&lt;td style="BODY"&gt;2&lt;/td&gt;<br>
			&lt;td style="BODY"&gt;11&lt;/td&gt;<br>
		&lt;/tr&gt;<br>
				:<br>
			　　(中略)<br>
				:<br>
		&lt;tr&gt;<br>
			&lt;td style="BODY"&gt;総計&lt;/td&gt;<br>
			&lt;td style="BODY"&gt;8&lt;/td&gt;<br>
			&lt;td style="BODY"&gt;1&lt;/td&gt;<br>
			&lt;td style="BODY"&gt;1&lt;/td&gt;<br>
			&lt;td style="BODY"&gt;10&lt;/td&gt;<br>
			&lt;td style="BODY"&gt;8&lt;/td&gt;<br>
			&lt;td style="BODY"&gt;2&lt;/td&gt;<br>
			&lt;td style="BODY"&gt;1&lt;/td&gt;<br>
			&lt;td style="BODY"&gt;11&lt;/td&gt;<br>
			&lt;td style="BODY"&gt;3&lt;/td&gt;<br>
			&lt;td style="BODY"&gt;1&lt;/td&gt;<br>
			&lt;td style="BODY"&gt;1&lt;/td&gt;<br>
			&lt;td style="BODY"&gt;5&lt;/td&gt;<br>
			&lt;td style="BODY"&gt;26&lt;/td&gt;<br>
		&lt;/tr&gt;<br>
	&lt;/tbody&gt;<br>
&lt;/table&gt;<br>
</code></pre>

※ ソース内に登場する、「assertEqualsWithFile」というメソッドは<a href='http://code.google.com/p/auty4junit/wiki/discriptionJP'>auty4junit</a>という単体テスト補助ツールのメソッドです。<br>
<br>
<h2>集計表作成 ～ クエリ編 ～</h2>

Cellomではクエリという機能が用意されており、こちらを使うとより短いコードで集計処理を行うことができます。上記と同じ処理を行っているソースが下記です。<br>
<br>
<pre><code>	@Test<br>
	public void query() {<br>
		TableManager&lt;Table&gt; tm = TableManager.create(Table.class);<br>
		rowSetting(tm, 20, 30, 40, 50);<br>
<br>
		for (Employee e : employees) {<br>
			String generation = getGeneration(e.age);<br>
<br>
			// 更新対象のセル取得用クエリ<br>
			String query = generation + ",総計|@sum,sectionMap|" + e.section<br>
					+ "|@sum,titleMap|" + e.title;<br>
			<br>
			// クエリを発行し、更新対象を一気に+1<br>
			for (Cell&lt;Integer&gt; cell : tm.getByQuery(Integer.class, query)) {<br>
				cell.add(1);<br>
			}<br>
		}<br>
<br>
		assertEqualsWithFile(new HtmlGenerator().generate(tm), getClass(),<br>
				"query");<br>
	}<br>
</code></pre>

出力される結果は上記と全く同じなので、割愛します。<br>
<br>
クエリとは、TableManagerから条件に合致するものをCellオブジェクトとして一気に取得するための仕組みで、複数個所に同一の数を足す必要のある集計表では便利な仕組みです。<br>
Cellオブジェクトとは、集計表を作成したときのフィールド一つ一つと対応するオブジェクトで、値の操作の他にも、スタイルの設定、幅の設定、値の変換設定などを行うことができます。<br>
Cellomのクエリは、下記のような構成になっています。<br>
<br>
[行指定箇所]|[フィールド or CelloMapのkey名指定箇所]|[フィールド or CelloMapのkey名指定箇所]|...<br>
<br>
先頭にくるのは行指定箇所で、ここでは行につけた名前(今回のケースでは「20代」・「30代」・「総計」等)を使ってアクセスしています。行指定には他にも全行指定「<code>*</code>」やカレント行指定の「.」などがあります。「,」で区切ることで、複数行を指定することが可能です。<br>
行指定の後には、クラスのフィールド or CelloMapに登録されたkey名を指定します。クラス・CelloMapの下に更なるクラス・CelloMapが登録されている場合には「|」でつないで何階層でも辿って特定のフィールドを指定することができます。行指定と同様に、「,」で区切ることで複数指定することもできます。なおCellオブジェクトとして返却されるのは一番最後に指定されたフィールドと、「@」が先頭に付与されたフィールドです。<br>
例として、上記のquery生成でもしEmployeeが30代で東京支社のエンジニアだった場合に生成されるクエリーは、<br>
<br>
30代,総計|@sum,sectionMap|東京支社|@sum,titleMap|エンジニア<br>
<br>
となります。これにより、Table#sumフィールド、Table#sectionMap#東京支社#sum, Table#sectionMap#東京支社#titleMap#エンジニアの3フィールドが、「30代」「総計」の2行分取得されるので、合計6フィールド分のCellオブジェクトが取得されます。<br>
<br>
<h2>エクセル出力</h2>

今度は作成した集計表をエクセルとして出力してみましょう。<br>
<br>
<pre><code>	@Test<br>
	public void excel() throws FileNotFoundException, IOException {<br>
		TableManager&lt;Table&gt; tm = TableManager.create(Table.class);<br>
		rowSetting(tm, 20, 30, 40, 50);<br>
<br>
		for (Employee e : employees) {<br>
			String generation = getGeneration(e.age);<br>
<br>
			// 更新対象のセル取得用クエリ<br>
			String query = generation + ",総計|@sum,sectionMap|" + e.section<br>
					+ "|@sum,titleMap|" + e.title;<br>
<br>
			// クエリを発行し、更新対象を一気に+1<br>
			for (Cell&lt;Integer&gt; cell : tm.getByQuery(Integer.class, query)) {<br>
				cell.add(1);<br>
			}<br>
		}<br>
<br>
		PoiGenerator poiGen = new PoiGenerator().generate(tm);<br>
		poiGen.writeBook(new FileOutputStream(new File("./sample.xls")));<br>
<br>
	}<br>
</code></pre>

上記の通りPoiGeneratorのgenerateメソッドを使って、生成したインスタンスの<br>
writeBookメソッドににOutputStreamを渡してやることで、Excelを出力します。<br>
出力したExcelの画像を下記に示します。<br>
<br>
<a href='http://cellom.googlecode.com/files/sampleXls1.PNG'>http://cellom.googlecode.com/files/sampleXls1.PNG</a>

スタイルの調整は、定義クラスのアノテーションで行うことができます。下の例は幅を調整して、全て右寄せにして表示させる例です。<br>
<br>
<pre><code>class Table {<br>
	@Header(title = "世代", width = 8)<br>
	@Body(style = "RIGHT")<br>
	String generation;<br>
<br>
	@EachHeader<br>
	CelloMap&lt;Section&gt; sectionMap = CelloMap.create(Section.class);<br>
<br>
	@Header(title = "合計", width = 5)<br>
	@Body(style = "RIGHT")<br>
	int sum;<br>
}<br>
<br>
class Section {<br>
	@EachHeader(width = 12)<br>
	@EachBody(style = "RIGHT")<br>
	CelloMap&lt;Integer&gt; titleMap = CelloMap.create(Integer.class);<br>
<br>
	@Header(title = "小計")<br>
	@Body(style = "RIGHT")<br>
	int sum;<br>
}<br>
</code></pre>

実際に出力されたExcelの画像は下記です。<br>
<br>
<a href='http://cellom.googlecode.com/files/sampleXls2.PNG'>http://cellom.googlecode.com/files/sampleXls2.PNG</a>

またコード内でクエリーでCellオブジェクトにアクセスしてスタイルを設定することもできます。<br>
下記は、世代の列の文字列を太字にし、右寄せにして枠線を除去する例です。<br>
<br>
<pre><code>	@Test<br>
	public void excel() throws FileNotFoundException, IOException {<br>
		TableManager&lt;Table&gt; tm = TableManager.create(Table.class);<br>
		rowSetting(tm, 20, 30, 40, 50);<br>
<br>
		for (Employee e : employees) {<br>
			String generation = getGeneration(e.age);<br>
<br>
			// 更新対象のセル取得用クエリ<br>
			String query = generation + ",総計|@sum,sectionMap|" + e.section<br>
					+ "|@sum,titleMap|" + e.title;<br>
<br>
			// クエリを発行し、更新対象を一気に+1<br>
			for (Cell&lt;Integer&gt; cell : tm.getByQuery(Integer.class, query)) {<br>
				cell.add(1);<br>
			}<br>
		}<br>
<br>
		for(Cell&lt;?&gt; cell : tm.getByQuery("*|generation")) {<br>
			cell.setStyle("BOLD_RIGHT_NOLINE_TBLR");<br>
		}<br>
<br>
		PoiGenerator poiGen = new PoiGenerator().generate(tm);<br>
		poiGen.writeBook(new FileOutputStream(new File("./sample.xls")));<br>
	}<br>
</code></pre>


出力結果は下記です。<br>
<br>
<a href='http://cellom.googlecode.com/files/sampleXls3.PNG'>http://cellom.googlecode.com/files/sampleXls3.PNG</a>


<h2>集計表作成 ～ 応用編 ～</h2>
それでは、より汎用的な集計表の作り方を学びましょう。元とするデータは今までと同じものを使い、下記のように少々複雑な集計表を作成します。<br>
<br>
<a href='http://cellom.googlecode.com/files/sampleXls4.PNG'>http://cellom.googlecode.com/files/sampleXls4.PNG</a>

集計表を生成するソースは下記の通りです。<br>
<br>
<ul><li>集計表と対応するクラスの定義<br>
<pre><code>class Table2 {<br>
	@Header(title = "世代", width = 8)<br>
	String generation;<br>
<br>
	@Header(title = "性別")<br>
	@Body(convert = true, convertSchema = "sex")<br>
	String sex;<br>
<br>
	@EachHeader<br>
	CelloMap&lt;Section&gt; sectionMap = CelloMap.create(Section.class);<br>
<br>
	@Header(title = "合計", width = 5)<br>
	int sum;<br>
}<br>
</code></pre></li></ul>

<ul><li>生成するソース<br>
<pre><code>	@Test<br>
	public void complecate() throws FileNotFoundException, IOException {<br>
<br>
		// 世代順・性別順に並び替え<br>
		Collections.sort(employees, new Comparator&lt;Employee&gt;() {<br>
			@Override<br>
			public int compare(Employee e1, Employee e2) {<br>
				int diffInGeneration = getGeneration(e1.age).compareTo(<br>
						getGeneration(e2.age));<br>
				if (diffInGeneration != 0)<br>
					return diffInGeneration;<br>
				return e1.sex.ordinal() - e2.sex.ordinal();<br>
			}<br>
		});<br>
<br>
		TableManager&lt;Table2&gt; tm = TableManager.create(Table2.class);<br>
		Table2 footer = tm.footer("総計").get();<br>
		footer.generation = "総計";<br>
		footer.sex = Sex.ALLSUMMARY.name();<br>
<br>
		// 性別列の変換設定<br>
		tm.putConversion("sex", Sex.MALE.name(), "男");<br>
		tm.putConversion("sex", Sex.FEMALE.name(), "女");<br>
		tm.putConversion("sex", Sex.SUMMARY.name(), "小計");<br>
		tm.putConversion("sex", Sex.ALLSUMMARY.name(), "－");<br>
<br>
		String generation = null;<br>
		Sex sex = null;<br>
<br>
		for (Employee e : employees) {<br>
			if (!getGeneration(e.age).equals(generation)) {<br>
				// 世代が変わったときの処理<br>
<br>
				// 小計行をTableに移動<br>
				if (generation != null)<br>
					tm.moveTmpToBody(generation);<br>
<br>
				// 新しい行の生成<br>
				tm.newRow();<br>
				tm.row().get().generation = generation = getGeneration(e.age);<br>
				tm.row().get().sex = (sex = e.sex).name();<br>
<br>
				// 世代列の枠線の下を消す<br>
				tm.row().getByQuery(".|generation").get(0).setStyle("NOLINE_B");<br>
<br>
				// 新しい小計行の生成<br>
				Table2 tmp = tm.tmp(generation).get();<br>
				tmp.sex = Sex.SUMMARY.name();<br>
<br>
				// 世代列の枠線の上を消す<br>
				tm.tmp(generation).getByQuery(".|generation").get(0).setStyle(<br>
						"NOLINE_T");<br>
			} else if (e.sex != sex) {<br>
				// 性別が変わったときの処理<br>
<br>
				// 新しい行の生成<br>
				tm.newRow();<br>
				tm.row().get().sex = (sex = e.sex).name();<br>
<br>
				// 世代列の枠線の上下を消す<br>
				tm.row().getByQuery(".|generation").get(0)<br>
						.setStyle("NOLINE_TB");<br>
			}<br>
<br>
			// 更新対象のセル取得用クエリ<br>
			String query = generation + ",.,総計|@sum,sectionMap|" + e.section<br>
					+ "|@sum,titleMap|" + e.title;<br>
<br>
			// クエリを発行し、更新対象を一気に+1<br>
			for (Cell&lt;Integer&gt; cell : tm.getByQuery(Integer.class, query)) {<br>
				cell.add(1);<br>
			}<br>
		}<br>
<br>
		PoiGenerator poiGen = new PoiGenerator();<br>
		poiGen.setXY(1, 1).writeText("株式会社 日本○○○○の社員構成");<br>
		poiGen.setXY(1, 2).generate(tm).setGridlineOff();<br>
		poiGen.writeBook(new FileOutputStream(new File("./complecate.xls")));<br>
	}<br>
</code></pre></li></ul>

それでは、順を追って説明します。<br>
<br>
<h3>データのソート</h3>

これまでは事前に集計表にどんな行が表示されるのか分かっている前提で、最初の時点で行を用意していました。<br>
しかし、実際には集計表を作成するときに必要となる行が決まっているとは限りません。ここでは事前に集計行の内容が判明していない前提のソースとなっています。<br>
<br>
集計行の内容が予め分かっていない場合には、<br>
<ul><li>集計行の並び順に合わせて、事前にデータをソート<br>
</li><li>データを一件ずつ処理するときに、直前のソートのキーを覚えておき、キーが変わったタイミングで前の行の後処理と、新しい行の生成処理を行う<br>
という方法が有効です。</li></ul>

今回のケースでは集計表が世代順を第一キーに、性別を第二キーにソートすれば良いことがわかります。その処理を行っている箇所が、<br>
<br>
<pre><code>		// 世代順・性別順に並び替え<br>
		Collections.sort(employees, new Comparator&lt;Employee&gt;() {<br>
			@Override<br>
			public int compare(Employee e1, Employee e2) {<br>
				int diffInGeneration = getGeneration(e1.age).compareTo(<br>
						getGeneration(e2.age));<br>
				if (diffInGeneration != 0)<br>
					return diffInGeneration;<br>
				return e1.sex.ordinal() - e2.sex.ordinal();<br>
			}<br>
		});<br>
</code></pre>

です。<br>
<br>
<h3>変換処理の設定</h3>

Cellomではデータ中のコード値などを、集計表に出力するときに分かりやすい値に自動的に変換する方法を提供しています。ここでは性別列でその処理を行っています。<br>
<br>
集計表に対応するクラスでは、性別列に対応する部分は下記のように書かれています。<br>
<pre><code>class Table2 {<br>
		：<br>
	@Header(title = "性別")<br>
	@Body(convert = true, convertSchema = "sex")<br>
	String sex;<br>
		：<br>
}<br>
</code></pre>

上記アノテーション中の「convert = true, convertSchema = "sex"」というのが変換を行うための定義です。<br>
実際のデータの変換を定義しているのは、下記箇所になります。<br>
<br>
<pre><code>		// 性別列の変換設定<br>
		tm.putConversion("sex", Sex.MALE.name(), "男");<br>
		tm.putConversion("sex", Sex.FEMALE.name(), "女");<br>
		tm.putConversion("sex", Sex.SUMMARY.name(), "小計");<br>
		tm.putConversion("sex", Sex.ALLSUMMARY.name(), "－");<br>
</code></pre>

これにより、convertSchemaが"sex"で定義されているフィールドでは、<br>
<ul><li>MALE ⇒ 男<br>
</li><li>FEMALE ⇒ 女<br>
</li><li>SUMMARY ⇒ 小計<br>
</li><li>ALLSUMMARY ⇒ －<br>
という変換が行われるようになります。</li></ul>

なお、今回の例ではBodyアノテーションで変換の定義をしておりますが、Headerアノテーションで同様の定義を行えば、ヘッダ行でも同様に自動変換を行うことができます。<br>
<br>
<br>
<h3>一時退避行</h3>

今回の集計表には、各世代の小計を集計するための行が登場します。こういった行はfooterでは対応することができません。そこでCellomでは好きなタイミングで生成でき、また好きなタイミングでBodyに追加することができるものとして、一時退避行という機能を設けています。<br>
<br>
下記にて、「tm.tmp(..」としてアクセスされているのが一時退避行です。<br>
<pre><code>			if (!getGeneration(e.age).equals(generation)) {<br>
				// 世代が変わったときの処理<br>
<br>
				// 小計行をTableに移動<br>
				if (generation != null)<br>
					tm.moveTmpToBody(generation);<br>
<br>
				// 新しい行の生成<br>
				tm.newRow();<br>
				tm.row().get().generation = generation = getGeneration(e.age);<br>
				tm.row().get().sex = (sex = e.sex).name();<br>
<br>
				// 世代列の枠線の下を消す<br>
				tm.row().getByQuery(".|generation").get(0).setStyle("NOLINE_B");<br>
<br>
				// 新しい小計行の生成<br>
				Table2 tmp = tm.tmp(generation).get();<br>
				tmp.sex = Sex.SUMMARY.name();<br>
<br>
				// 世代列の枠線の上を消す<br>
				tm.tmp(generation).getByQuery(".|generation").get(0).setStyle(<br>
						"NOLINE_T");<br>
			} else if (e.sex != sex) {<br>
</code></pre>

世代が切り替わったタイミングで、直前に処理していた世代の一時退避行(小計行)はBodyに移動されています。その後新しい一時退避行(小計行)が作成され、以後の処理ではカレント行・フッタ行とともに集計処理が行われています。<br>
<br>
<h3>世代列の制御</h3>
世代列では、枠線の上下を制御することで、纏まり間を出しています。ここでは<br>
<ul><li>その世代の最初に登場する行では、下の枠線を削除<br>
</li><li>その世代の最後に登場する行では、上の枠線を削除<br>
</li><li>上記以外は、上下の枠線を削除<br>
という制御を行って、対処を行っています。</li></ul>

<blockquote>※ スタイル名「NOLINE_XX」は、PoiGeneratorに組み込まれたスタイルです。「XX」の部分に下記値を入れることで、それぞれの枠線の削除を行います。</blockquote>

T ⇒ TOP(上)、B ⇒ BOTTOM(下)、L ⇒ LEFT(左)、R ⇒ RIGHT(右)<br>
<br>
同時に複数個指定することも可能です。<br>
<br>
<h3>PoiGeneratorその他</h3>
PoiGeneratorではその他に、表を出力する座標を指定したり(setXYメソッド)、テキストを書き込んだり(writeTextメソッド)、グリッド線を消したり(setGridlineOffメソッド)、様々な処理が行えます。これを実際にやっているのが、下記です。<br>
<br>
<pre><code>		PoiGenerator poiGen = new PoiGenerator();<br>
		poiGen.setXY(1, 1).writeText("株式会社 日本○○○○の社員構成");<br>
		poiGen.setXY(1, 2).generate(tm).setGridlineOff();<br>
		poiGen.writeBook(new FileOutputStream(new File("./complecate.xls")));<br>
</code></pre>

応用編の説明は以上です。<br>
まだ説明していない箇所もありますが、これまでの内容から充分理解できるはずです。<br>
上記説明用に作成したサンプルコードは<a href='http://code.google.com/p/cellom/source/browse/cellom/test/tetz42/cellom/SampleTest.java'>こちら</a>です。<br>
<br>
もし使っていて分からない箇所があったり、バグを見つけるなどありましたら、お手数ですがissue登録をお願いいたします。<br>
<br>
それでは。<br>
<br>
Enjoy!