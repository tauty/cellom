package tetz42.cellom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tetz42.cellom.body.Cell;
import tetz42.cellom.body.Row;
import tetz42.clione.common.exception.InvalidParameterException;

public class RowHolder<T> {

	private final Class<T> clazz;
	private final Context<T> context;

	private final List<Row<T>> rowList = new ArrayList<Row<T>>();
	private final Map<String, Row<T>> rowMap = new HashMap<String, Row<T>>();

	private int index = 0;
	private boolean isBody;

	RowHolder(Class<T> clazz, Context<T> context) {
		this(clazz, context, false);
	}

	RowHolder(Class<T> clazz, Context<T> context, boolean isBody) {
		this.clazz = clazz;
		this.context = context;
		this.isBody = isBody;
	}

	public Row<T> newRow() {
		rowList.add(new Row<T>(clazz, context));
		index = rowList.size() - 1;
		return getRow();
	}

	public Row<T> newRow(String key) {
		newRow();
		setCurrentRowAs(key);
		return getRow(key);
	}

	public Row<T> getRow() {
		if (index >= rowList.size())
			return null;
		return rowList.get(index);
	}

	public Row<T> getRow(String key) {
		return rowMap.get(key);
	}

	public Row<T> row() {
		Row<T> row = getRow();
		if (row == null)
			row = newRow();
		return row;
	}

	public Row<T> row(String key) {
		Row<T> row = getRow(key);
		if (row == null)
			row = newRow(key);
		else {
			// set current row as  the key indicated
			index = rowList.indexOf(row);
		}
		return row;
	}

	public void setCurrentRowAs(String key) {
		rowMap.put(key, getRow());
	}

	public int getCurrentIndex() {
		return index;
	}

	public void setCurrentIndex(int index) {
		this.index = index;
	}

	public int getSize() {
		return rowList.size();
	}

	public List<Row<T>> getRowList() {
		return this.rowList;
	}

	public <E> List<Cell<E>> getByQuery(
			@SuppressWarnings("unused") Class<E> clazz, String query) {
		return getByQuery(query);
	}

	public <E> List<Cell<E>> getByQuery(String query) {
		return getByQuery(Query.parse(query));
	}

	public <E> List<Cell<E>> getByQuery(
			@SuppressWarnings("unused") Class<E> clazz, Query query) {
		return getByQuery(query);
	}

	public <E> List<Cell<E>> getByQuery(Query query) {
		if (query.get(0) == null)
			throw new InvalidParameterException(
					"Query must have 1 more elements");
		List<Cell<E>> list = new ArrayList<Cell<E>>();
		for (String rowName : query.get(0)) {
			if (rowName.equals(Query.CURRENT_ROW)) {
				if (isBody)
					addMatchedCells(row(), query, list);
			} else if (rowName.equals(Query.ANY)) {
				for (Row<T> row : rowList)
					addMatchedCells(row, query, list);
			} else if (rowMap.containsKey(rowName)) {
				addMatchedCells(rowMap.get(rowName), query, list);
			} else if (Query.numPtn.matcher(rowName).matches()) {
				int i = Integer.parseInt(rowName);
				if (i < rowList.size())
					addMatchedCells(rowList.get(i), query, list);
			} else {
				// TODO consider about this case.
				// throw new InvalidParameterException(
				// "Unknown row name has specified. name=" + rowName);
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	private <E> void addMatchedCells(Row<T> row, Query query, List<Cell<E>> list) {
		for (Cell<Object> cell : row.getByQuery(query)) {
			list.add((Cell<E>) cell);
		}
	}

	public void clear() {
		rowMap.clear();
		rowList.clear();
	}

	public Row<T> remove(String key) {
		Row<T> row = rowMap.remove(key);
		rowList.remove(row);
		return row;
	}

	public void addRow(Row<T> row) {
		rowList.add(row);
		index = rowList.size() - 1;
	}

	public void addRows(List<Row<T>> rows) {
		for (Row<T> row : rows) {
			rowList.add(row);
		}
		index = rowList.size() - 1;
	}
}
