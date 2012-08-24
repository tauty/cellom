package tetz42.cellom;

import static tetz42.test.Auty.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import tetz42.cellom.annotation.EachHeader;
import tetz42.cellom.annotation.Header;
import tetz42.cellom.body.Cell;
import tetz42.cellom.body.CelloMap;
import tetz42.cellom.generator.HtmlGenerator;
import tetz42.cellom.generator.PoiGenerator;

enum Sex {
	MALE, FEMALE
}

class Employee {
	final int age;
	final Sex sex;
	final String section;
	final String title;

	Employee(int age, Sex sex, String section, String title) {
		this.age = age;
		this.sex = sex;
		this.section = section;
		this.title = title;
	}
}

public class SampleTest {

	List<Employee> employees;

	@Before
	public void setUp() {
		employees = new ArrayList<Employee>();

		// 名古屋本社
		employees.add(new Employee(21, Sex.MALE, "名古屋本社", "エンジニア"));
		employees.add(new Employee(23, Sex.FEMALE, "名古屋本社", "エンジニア"));
		employees.add(new Employee(25, Sex.FEMALE, "名古屋本社", "エンジニア"));
		employees.add(new Employee(30, Sex.MALE, "名古屋本社", "エンジニア"));
		employees.add(new Employee(35, Sex.MALE, "名古屋本社", "エンジニア"));
		employees.add(new Employee(40, Sex.MALE, "名古屋本社", "エンジニア"));
		employees.add(new Employee(52, Sex.MALE, "名古屋本社", "エンジニア"));
		employees.add(new Employee(55, Sex.MALE, "名古屋本社", "エンジニア"));
		employees.add(new Employee(34, Sex.FEMALE, "名古屋本社", "事務"));
		employees.add(new Employee(51, Sex.MALE, "名古屋本社", "営業"));

		// 東京支社
		employees.add(new Employee(22, Sex.MALE, "東京支社", "エンジニア"));
		employees.add(new Employee(24, Sex.MALE, "東京支社", "エンジニア"));
		employees.add(new Employee(25, Sex.MALE, "東京支社", "エンジニア"));
		employees.add(new Employee(28, Sex.MALE, "東京支社", "エンジニア"));
		employees.add(new Employee(31, Sex.MALE, "東京支社", "エンジニア"));
		employees.add(new Employee(36, Sex.FEMALE, "東京支社", "エンジニア"));
		employees.add(new Employee(37, Sex.MALE, "東京支社", "エンジニア"));
		employees.add(new Employee(42, Sex.MALE, "東京支社", "エンジニア"));
		employees.add(new Employee(24, Sex.FEMALE, "東京支社", "事務"));
		employees.add(new Employee(28, Sex.MALE, "東京支社", "事務"));
		employees.add(new Employee(30, Sex.MALE, "東京支社", "営業"));

		// 大阪支社
		employees.add(new Employee(25, Sex.MALE, "大阪支社", "エンジニア"));
		employees.add(new Employee(30, Sex.MALE, "大阪支社", "エンジニア"));
		employees.add(new Employee(37, Sex.MALE, "大阪支社", "エンジニア"));
		employees.add(new Employee(21, Sex.FEMALE, "大阪支社", "事務"));
		employees.add(new Employee(30, Sex.FEMALE, "大阪支社", "営業"));
	}

	@Test
	public void normal() {

		TableManager<Table> tm = TableManager.create(Table.class);
		rowSetting(tm, 20, 30, 40, 50);

		for (Employee e : employees) {

			// 社員の世代取得
			String generation = getGeneration(e.age);

			// 世代別行への反映
			updateEmployee(tm.row(generation).get(), e);

			// 総計行への反映
			updateEmployee(tm.footer().get(), e);
		}

		assertEqualsWithFile(new HtmlGenerator().generate(tm), getClass(),
				"normal");
	}

	// 行初期設定
	private void rowSetting(TableManager<Table> tm, int... ages) {

		// 各世代行の生成
		for (int age : ages) {
			String generation = getGeneration(age);
			tm.row(generation).get().generation = generation;
		}

		// 総計行の生成
		tm.footer("総計").get().generation = "総計";
	}

	// 年齢から世代を求める
	private String getGeneration(int age) {
		if (age < 30)
			return "20代";
		if (age < 40)
			return "30代";
		if (age < 50)
			return "40代";
		return "50代";
	}

	// 社員情報のTableオブジェクトへの反映
	private void updateEmployee(Table table, Employee e) {
		Section section = table.sectionMap.get(e.section);

		// 各職種ごとに+1
		section.titleMap.set(e.title, section.titleMap.get(e.title) + 1);

		// 小計に+1
		section.sum++;

		// 合計に+1
		table.sum++;
	}

	@Test
	public void query() {
		TableManager<Table> tm = TableManager.create(Table.class);
		rowSetting(tm, 20, 30, 40, 50);

		for (Employee e : employees) {
			String generation = getGeneration(e.age);

			// 更新対象のセル取得用クエリ
			String query = generation + ",総計|@sum,sectionMap|" + e.section
					+ "|@sum,titleMap|" + e.title;

			// クエリを発行し、更新対象を一気に+1
			for (Cell<Integer> cell : tm.getByQuery(Integer.class, query)) {
				cell.add(1);
			}
		}

		assertEqualsWithFile(new HtmlGenerator().generate(tm), getClass(),
				"query");
	}

	@Test
	public void excel() throws FileNotFoundException, IOException {
		TableManager<Table> tm = TableManager.create(Table.class);
		rowSetting(tm, 20, 30, 40, 50);

		for (Employee e : employees) {
			String generation = getGeneration(e.age);

			// 更新対象のセル取得用クエリ
			String query = generation + ",総計|@sum,sectionMap|" + e.section
					+ "|@sum,titleMap|" + e.title;

			// クエリを発行し、更新対象を一気に+1
			for (Cell<Integer> cell : tm.getByQuery(Integer.class, query)) {
				cell.add(1);
			}
		}

		for(Cell<?> cell : tm.getByQuery("*|generation")) {
			cell.setStyle("BOLD_RIGHT");
		}

		PoiGenerator poiGen = new PoiGenerator().generate(tm);
		poiGen.writeBook(new FileOutputStream(new File("./sample.xls")));
	}

}

class Table {
	@Header(title = "世代", width = 8)
	String generation;

	@EachHeader
	CelloMap<Section> sectionMap = CelloMap.create(Section.class);

	@Header(title = "合計", width = 5)
	int sum;
}

class Section {
	@EachHeader(width = 12)
	CelloMap<Integer> titleMap = CelloMap.create(Integer.class);

	@Header(title = "小計")
	int sum;
}
